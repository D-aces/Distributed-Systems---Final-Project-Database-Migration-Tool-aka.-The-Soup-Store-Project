// TODO: Create a Mockup of Client Request for data
// TODO: Create a Local Database Mockup
// TODO: Create a Cloud Database Mockup

import java.util.List;
import java.lang.Thread;

public class MigrationserverTests{
    public static void main(String[] args) {

        // Create two local databases
        DatabaseServer localDatabase = new DatabaseServer("jdbc:sqlite:LocalSoupStore.db");
        DatabaseServer cloudDatabase = new DatabaseServer("jdbc:sqlite:CloudSoupStore.db"); // Just another local database

        // Create the handler
        DatabaseHandler dbHandler = new DatabaseHandler(localDatabase, cloudDatabase);

        // Open connections
        dbHandler.openConnection();
      
        //Get the number of tables in a database. They are stored in: localTableCount and cloudTableCount
        int localTableCount = dbHandler.countTables(localDatabase);
        int cloudTableCount = dbHandler.countTables(cloudDatabase);

        //Simple prints to check the number of tables in each database, they need to be equal
        System.out.println("Number of tables in LocalSoupStore: " + localTableCount);
        System.out.println("Number of tables in CloudSoupStore: " + cloudTableCount);

        // Retrieve the list of table names
        List<String> tableNames = dbHandler.getTableNames(localDatabase);

         // We now wil loop through each of the tables in the local database and migrate them to the cloud database
         System.out.println("Tables in the database named: localDatabase\n");
        for (String tableName : tableNames) {
             // tableNames =["chunky_soup", "smooth_soup", "spicy_soup"] this is what is in tableNames
             // This for each loop will loop through each of the tables in the local database, starting with chunky_soup
             System.out.println("\nProcessing table #" + tableName);

             // This is to get the number of rows in the table
             int rowCount = dbHandler.getRowCount(localDatabase, tableName);
             System.out.println("Table: " + tableName + " has " + rowCount + " entries.\n");

             for (int i = 0; i < rowCount; i++) {
                // Perform your logic for each entry
                System.out.println("Processing entry #" + (i + 1));
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < 500) {
                // prison
                }

            }
              
        //Thread.sleep(1000);
        // Simulate a delay of approximately 1 second, horrible way to do
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 1000) {
        // prison
        }
        }

        // Close connections
        dbHandler.closeConnection();
    }
}