package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Migrationserver extends UnicastRemoteObject implements MigrationInterface {
    public static volatile Map<String, List<Boolean>> tableTransferStatus = new HashMap<>();
    public static volatile List<DatabaseHandler> dbHandlers = new ArrayList<>();

    public Migrationserver() throws RemoteException {
        super();
    }
    public static void main(String[] args) {
        try {
            Migrationserver theServer = new Migrationserver();

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("MigrationService", theServer);
            System.out.println("Migration Server is running...");

            while (true) {
                for (String table : tableTransferStatus.keySet()) {
                    for (int i = 1; i < dbHandlers.size(); i++) {
                        if (!tableTransferStatus.get(table).get(i)) {
                            dbHandlers.get(0).transferTo(dbHandlers.get(i).localDatabase, table);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String migrationStatus() {
        int total = 0;
        int transferred = 0;
        for (String table : tableTransferStatus.keySet()) {
            total += tableTransferStatus.get(table).size();
            for (Boolean bool : tableTransferStatus.get(table))
                transferred += bool ? 1 : 0;
        }
        return "Replica " + transferred + "/" + total;
    }

    @Override
    public String select(String table, String query, Object... params) throws RemoteException {
        List<Boolean> boolArr = tableTransferStatus.get(table);
        if (boolArr == null)
            return "Table not found!";
        List<DatabaseHandler> valids = new ArrayList<>();
        for (int i = 0; i < boolArr.size(); i++) {
            if (boolArr.get(i))
                valids.add(dbHandlers.get(i));
        }
        DatabaseHandler useingDB = valids.get((int)(Math.random() * valids.size()));
        try {
            return useingDB.localDatabase.query(query, params).toString();
        } catch (SQLException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public String insert(String table, String query, Object... params) throws RemoteException {
        return this.select(table, query, params);
    }

    @Override
    public void addCloudDatabase(String cloudDatabase) throws RemoteException {
        if (dbHandlers.size() == 0 || tableTransferStatus.size() == 0)
            throw new RemoteException("Local Database not set yet!");

        dbHandlers.add(new DatabaseHandler(new DatabaseServer("jdbc:sqlite:" + cloudDatabase)));
        for (String key : tableTransferStatus.keySet()) {
            tableTransferStatus.get(key).add(false);
        }
    }
    @Override
    public void setLocalDatabase(String localDatabase) throws RemoteException {
        if(dbHandlers.size() != 0 || tableTransferStatus.size() != 0)
            throw new RemoteException("Local Table already set!");
        
        DatabaseHandler localdb = new DatabaseHandler(new DatabaseServer("jdbc:sqlite:" + localDatabase));
        localdb.openConnection();
        List<String> tables = localdb.getTableNames();
        for (String table : tables) {
            List<Boolean> booleanArr = new ArrayList<>();
            booleanArr.add(true);
            tableTransferStatus.put(table, booleanArr);
        }
        dbHandlers.add(localdb);

    }
}

