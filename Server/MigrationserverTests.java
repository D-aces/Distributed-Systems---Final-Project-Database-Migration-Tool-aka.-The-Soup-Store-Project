package Server;
// TODO: Create a Mockup of Client Request for data

// TODO: Create a Local Database Mockup
// TODO: Create a Cloud Database Mockup

import java.util.ArrayList;
import java.util.List;
import java.lang.Thread;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.ResultSet;

public class MigrationserverTests extends UnicastRemoteObject implements DatabaseMigrationServer {
    private static Boolean[] tableMigrationArray;
    private static Boolean[] tableMigrationArray2;

    protected MigrationserverTests() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            // Create the RMI registry to handle callbacks
            Registry registry = LocateRegistry.createRegistry(1099);

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
            System.out.println("\nResetting cloud database for testing...");
            dbHandler.clearCloudDatabase();

            long start1 = System.currentTimeMillis(); // TODO: need to fix all of these start variables, dont need to
                                                      // declare them all
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
            if (tableNames.size() <= 0) {
                tableMigrationArray = new Boolean[tableNames.size()];
            } else {
                System.err.println("Table Size is 0");
            }

            // TODO: Use tables from localDatabase to create equivalent tables in cloud
            // TODO: Do a check to ensure the cloud database has all the relevant tables

            // We now wil loop through each of the tables in the local database and migrate
            // them to the cloud database
            System.out.println(
                    "Tables in the database named: localDatabase, that need to be migrated to CloudSoupStore\n");
            long start6 = System.currentTimeMillis();
            while (System.currentTimeMillis() - start6 < 5000) {
                // prison
            }

            int x = 0;
            for (String tableName : tableNames) {
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

                // TODO: Vanquish table (destroyer of databases) 

                // Successful Migration
                // TODO: Add Validation Check
                tableMigrationArray2[x] = true;
                tableMigrationArray[x] = false;

                // //Thread.sleep(1000);
                // Simulate a delay of approximately 1 second, horrible way to do
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < 1000) {
                    // prison
                }
                x++;
            }

            // Verify the contents of the cloud database
            System.out.println("\nContents of the cloud database:");
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < 4000) {
                // prison
            }
            dbHandler.printAllEntries(cloudDatabase);

            // Close connections
            dbHandler.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return migrations.getOrDefault(migrationId, "Not Found");
    }

    @Override
    public void registerCallback(String migrationId, MigrationCallback callback) throws RemoteException {
        callbacks.put(migrationId, callback);
    }

    @Override
    public String startMigration(String dbPath) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startMigration'");
    }
}