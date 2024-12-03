package Server;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Thread;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.ResultSet;

public class MigrationserverTests extends UnicastRemoteObject implements DatabaseMigrationServer {
    private static Boolean[] tableMigrationArray;
    private static Boolean[] tableMigrationArray2;
    private static Map<String, String> migrationsList = new HashMap<>();
    private Map<String, MigrationCallback> callbacks = new HashMap<>();
    private DatabaseHandler dbHandler;
    private static DatabaseServer localDatabase, cloudDatabase;
    private static boolean testingBit = false;

    protected MigrationserverTests(DatabaseHandler dbHandler) throws RemoteException {
        super();
        this.dbHandler = dbHandler;
    }

    private void performMigration(String migrationId) {
        // Create two local databases
        // Change datasource url
        DatabaseServer localDatabase = new DatabaseServer("jdbc:sqlite:LocalSoupStore.db");
        DatabaseServer cloudDatabase = new DatabaseServer("jdbc:sqlite:CloudSoupStore.db");

        // Create the handler --> DatabaseManager (rename?)
        DatabaseHandler dbHandler = new DatabaseHandler(localDatabase, cloudDatabase);

        // Open connections
        dbHandler.openConnection();

                // Clear the cloud database
        // FOR TESTING PURPOSES ONLY
        if (testingBit) {
            System.out.println("\nResetting cloud database for testing...");
            dbHandler.clearCloudDatabase();
        }

        long start1 = System.currentTimeMillis();
        while (System.currentTimeMillis() - start1 < 4000) {
            // prison
        }

        // Get the number of tables in a database. They are stored in: localTableCount
        // and cloudTableCount
        int localTableCount = dbHandler.countTables(localDatabase);
        int cloudTableCount = dbHandler.countTables(cloudDatabase);

        // Simple prints to check the number of tables in each database, they need to be
        // equal
        System.out.println("Number of tables in LocalSoupStore: " + localTableCount);
        System.out.println("Number of tables in CloudSoupStore: " + cloudTableCount);

        // Retrieve the list of table names
        List<String> tableNames = dbHandler.getTableNames(localDatabase);
        if (!tableNames.isEmpty()) {
            System.out.println(tableNames.size());
            tableMigrationArray = new Boolean[tableNames.size()];
            tableMigrationArray2 = new Boolean[tableNames.size()];
            for (int x = 0; x < tableNames.size(); x++) {
                tableMigrationArray[x] = true;  // Initially set to true (needs migration)
                tableMigrationArray2[x] = false;  // Initially set to false (not yet migrated)
            }
        } else {
            System.err.println("Table Size is 0");
        }

        // We now wil loop through each of the tables in the local database and migrate
        // them to the cloud database
        System.out.println(
                "Tables in the database named: localDatabase, that need to be migrated to CloudSoupStore\n");
        long start6 = System.currentTimeMillis();
        while (System.currentTimeMillis() - start6 < 5000) {
            // prison
        }

        int counter = 0;
        for (String tableName : tableNames) {
            
            dbHandler.createTable(cloudDatabase, tableName);
            // tableNames =["chunky_soup", "smooth_soup", "spicy_soup"] this is what is in
            // tableNames
            // This for each loop will loop through each of the tables in the local
            // database, starting with chunky_soup
            System.out.println("\nProcessing table " + tableName);

            // This is to get the number of rows in the table
            int rowCount = dbHandler.getRowCount(localDatabase, tableName);
            System.out.println("Table: " + tableName + " has " + rowCount + " entries.\n");

            long start3 = System.currentTimeMillis();
            while (System.currentTimeMillis() - start3 < 4000) {
                // prison
            }

            // Perform your logic for each entry
            for (int i = 0; i < rowCount; i++) {
                // Use the method to fetch the row at the current offset
                ResultSet resultSet = dbHandler.getRowByOffset(localDatabase, tableName, i);

                try {
                    if (resultSet != null && resultSet.next()) {

                        int id = resultSet.getInt("id"); // Example column: id
                        String name = resultSet.getString("name"); // Example column: name
                        double price = resultSet.getDouble("price"); // Example column: price

                        System.out.println("Processing entry #" + (i + 1) + ": ID=" + id + ", Name=" + name
                                + ", Price=" + price);

                        // Migrate the data to the cloud database
                        dbHandler.insertDataCloud(tableName, id, name, price);

                        System.out.println("Migrated entry #" + (i + 1) + " to cloud database.");
                    }
                } catch (Exception e) {
                    System.err.println(
                            "Error processing row #" + (i + 1) + " in table " + tableName + ": " + e.getMessage());
                }

                long start2 = System.currentTimeMillis();
                while (System.currentTimeMillis() - start2 < 500) {
                    // prison
                }
            }

            // Successful Migration
            System.out.println(counter+1 +  "/" + tableNames.size() + " tables");
            tableMigrationArray2[counter] = true;
            tableMigrationArray[counter] = false;

            // //Thread.sleep(1000);
            // Simulate a delay of approximately 1 second, horrible way to do
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < 1000) {
                // prison
            }
            counter++;
        }

        dbHandler.closeConnection();
        dbHandler.openConnection();


        System.out.println("\nDropping tables from local database...");
        for (int i = 0; i < tableNames.size(); i++) {
            if (tableMigrationArray[i] == false && tableMigrationArray2[i] == true) {
                dbHandler.dropTable(localDatabase, tableNames.get(i)); 
                System.out.println("Dropped table: " + tableNames.get(i));
            }
        }

        // Verify the contents of the cloud database
        System.out.println("\nContents of the cloud database:");
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 4000) {
            // prison
        }

        dbHandler.printAllEntries(cloudDatabase);
        dbHandler.printAllEntries(localDatabase);
        

        // Close connections
        dbHandler.closeConnection();

        // Signal the transfer is complete
        // notifyClient("Migration completed successfully!");
    }

    private static void notifyClient(String message) {
        try {
            // Locate the client callback through the RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            MigrationCallback callback = (MigrationCallback) registry.lookup("ClientCallback");

            // Notify the client
            callback.notifyMigrationComplete(message);
        } catch (Exception e) {
            System.err.println("Failed to notify client: " + e.getMessage());
        }
    }

    @Override
    public String getMigrationStatus(String migrationId) throws RemoteException {
        return null;
        // return migrations.getOrDefault(migrationId, "Not Found");
    }

    @Override
    public void registerCallback(String migrationId, MigrationCallback callback) throws RemoteException {
        callbacks.put(migrationId, callback);
    }

    @Override
    public String startMigration(String dbPath) throws RemoteException {
        String migrationId = "MIG-" + System.currentTimeMillis();
        new Thread(() -> performMigration(migrationId)).start();
        return migrationId;
    }
}