// TODO: Create a Mockup of Client Request for data
// TODO: Create a Local Database Mockup
// TODO: Create a Cloud Database Mockup

public class MigrationserverTests{
    public static void main(String[] args) {
        // Create two local databases
        DatabaseServer localDatabase = new DatabaseServer("jdbc:sqlite:LocalSoupStore.db");
        DatabaseServer cloudDatabase = new DatabaseServer("jdbc:sqlite:CloudSoupStore.db"); // Just another local database

        // Create the handler
        DatabaseHandler dbHandler = new DatabaseHandler(localDatabase, cloudDatabase);

        // Open connections
        dbHandler.openConnection();
        // Perform operations
        // dbHandler.insertData("chunky_soup", 9, "Tomato Soup", 3.99);
        // dbHandler.readData("chunky_soup", 8);
        //dbHandler.readAllData("chunky_soup");


        //Get the number of tables in a database
        //They are stored in: localTableCount and cloudTableCount

        int localTableCount = dbHandler.countTables(localDatabase);
        int cloudTableCount = dbHandler.countTables(cloudDatabase);

        System.out.println("Number of tables in LocalSoupStore: " + localTableCount);
        System.out.println("Number of tables in CloudSoupStore: " + cloudTableCount);


        for (int i = 0; i < localTableCount; i++) {
       
            System.out.println("Processing table #" + (i + 1));

        }







        // Close connections
        dbHandler.closeConnection();


    }
 

}