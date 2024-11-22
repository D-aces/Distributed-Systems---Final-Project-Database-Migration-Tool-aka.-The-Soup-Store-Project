import java.sql.ResultSet;


public class DatabaseHandler {

    DatabaseServer localDatabase;
    DatabaseServer cloudDatabase;


    public DatabaseHandler(DatabaseServer localDatabase, DatabaseServer cloudDatabase){
        this.localDatabase = localDatabase;
        this.cloudDatabase = cloudDatabase;
    }

     // Open both local database connections
     public void openConnection() {
        try {
            localDatabase.open();
            cloudDatabase.open();
        } catch (Exception e) {
            System.err.println("Error opening database connections: " + e.getMessage());
        }
    }

    // Close both local database connections
    public void closeConnection() {
        try {
            localDatabase.close();
            cloudDatabase.close();
        } catch (Exception e) {
            System.err.println("Error closing database connections: " + e.getMessage());
        }
    }

    public void insertData(String table, int id, String name, double price) {
        String query = "INSERT INTO " + table + " (id, name, price) VALUES (?, ?, ?)";
        try {
            localDatabase.execute(query, id, name, price);
        } catch (Exception e) {
            System.err.println("Error inserting data: " + e.getMessage());
        }
    }

    public void readData(String table, int id) {
        String query = "SELECT * FROM " + table + " WHERE id = ?";
        try {
            ResultSet resultSet = localDatabase.query(query, id);
            while (resultSet.next()) {
                int retrievedId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                System.out.println("ID: " + retrievedId + ", Name: " + name + ", Price: " + price);
            }
        } catch (Exception e) {
            System.err.println("Error reading data: " + e.getMessage());
        }
    }

    public void readAllData(String table) {
        String query = "SELECT * FROM " + table;
        try {
            ResultSet resultSet = localDatabase.query(query);
            while (resultSet.next()) {
                int retrievedId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                System.out.println("ID: " + retrievedId + ", Name: " + name + ", Price: " + price);
            }
        } catch (Exception e) {
            System.err.println("Error reading data: " + e.getMessage());
        }
    }

    // Get the number of tables in a database
    //I want to pass in db name and get the number of tables in that database, not sure if this will work
    public int countTables(DatabaseServer database) {
        String query = "SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name NOT LIKE 'sqlite_%'";
        int tableCount = 0;
    
        try {
            ResultSet resultSet = database.query(query);
            if (resultSet.next()) {
                tableCount = resultSet.getInt(1); // Get the count from the result set
            }
        } catch (Exception e) {
            System.err.println("Error counting tables: " + e.getMessage());
        }
    
        return tableCount;
    }
    
    







    
}
