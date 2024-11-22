package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Migrationserver extends UnicastRemoteObject implements MigrationInterface, DatabaseMigrationServer {
    public volatile Map<String, List<Boolean>> tableTransferStatus = new HashMap<>();
    public volatile List<DatabaseHandler> dbHandlers = new ArrayList<>();

    public Migrationserver() throws RemoteException {
        super();
    }
    public static void main(String[] args) {
        try {
            Migrationserver theServer = new Migrationserver();

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("MigrationService", theServer);
            System.out.println("Migration Server is running...");

            synchronized (Migrationserver.class) {
                Migrationserver.class.wait(); // Keeps the server alive indefinitely
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String migrationStatus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'migrationStatus'");
    }

    @Override
    public String select(String query) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'select'");
    }

    @Override
    public String insert(String query) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
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
    @Override
    public String startMigration(String dbPath) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startMigration'");
    }
    @Override
    public String getMigrationStatus(String migrationId) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMigrationStatus'");
    }
    @Override
    public void registerCallback(String migrationId, MigrationCallback callback) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerCallback'");
    }
}

