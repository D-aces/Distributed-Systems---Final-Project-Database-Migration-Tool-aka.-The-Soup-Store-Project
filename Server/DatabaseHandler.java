package Server;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler {

    public DatabaseServer localDatabase;


    public DatabaseHandler(DatabaseServer localDatabase){
        this.localDatabase = localDatabase;
    }

     // Open both local database connections
     public void openConnection() {
        try {
            localDatabase.open();
        } catch (Exception e) {
            System.err.println("Error opening database connections: " + e.getMessage());
        }
    }

    // Close both local database connections
    public void closeConnection() {
        try {
            localDatabase.close();
        } catch (Exception e) {
            System.err.println("Error closing database connections: " + e.getMessage());
        }
    }

    public void insertData(String table, int id, String name, double price) {
        String query = "INSERT INTO " + table + " (id, name, price) VALUES (?, ?, ?)";
        try {
            // insertDataLocal is used here to execute the query on localDatabase
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

    public void transferTo(DatabaseServer otherdb, String table) {
        String query = "SELECT 'id', 'name', 'price' FROM ?;";
        try {
            ResultSet resultSet = this.localDatabase.query(query, table);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                otherdb.execute("INSERT INTO ? ('id', 'name', 'price') VALUES (?,?,?);", table, id, name, price);
            }
        } catch (Exception e) {
            System.err.println("Error Transferring: " + e.getMessage());
            e.printStackTrace();
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

                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < 500) {
                // prison
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading data: " + e.getMessage());
        }
    }

    // Get the number of tables in a database
    //I want to pass in db name and get the number of tables in that database, not sure if this will work
    public int countTables() {
        String query = "SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name NOT LIKE 'sqlite_%'";
        int tableCount = 0;
    
        try {
            ResultSet resultSet = this.localDatabase.query(query);
            if (resultSet.next()) {
                tableCount = resultSet.getInt(1); // Get the count from the result set
            }
        } catch (Exception e) {
            System.err.println("Error counting tables: " + e.getMessage());
        }
    
        return tableCount;
    }

    //Used to get the table names in a database
    public List<String> getTableNames() {
    String query = "SELECT name FROM sqlite_master WHERE type = 'table' AND name NOT LIKE 'sqlite_%'";
    List<String> tableNames = new ArrayList<>();

    try {
        ResultSet resultSet = this.localDatabase.query(query);
        while (resultSet.next()) {
            tableNames.add(resultSet.getString("name"));
        }
    } catch (Exception e) {
        System.err.println("Error retrieving table names: " + e.getMessage());
    }

    return tableNames;
}
    
// Get the number of rows in a table
public int getRowCount(String tableName) {
    String query = "SELECT COUNT(*) FROM " + tableName;
    int rowCount = 0;

    try {
        ResultSet resultSet = this.localDatabase.query(query);
        if (resultSet.next()) {
            rowCount = resultSet.getInt(1); // Get the count from the first column
        }
    } catch (Exception e) {
        System.err.println("Error getting row count for table " + tableName + ": " + e.getMessage());
    }

    return rowCount;
} 

// This method retrieves a single row from a table based on an offset
public ResultSet getRowByOffset(String tableName, int offset) {
    String query = "SELECT * FROM " + tableName + " LIMIT 1 OFFSET " + offset;
    ResultSet resultSet = null;

    try {
        resultSet = this.localDatabase.query(query);
    } catch (Exception e) {
        System.err.println("Error retrieving row at offset " + offset + " from table " + tableName + ": " + e.getMessage());
    }

    return resultSet;
}

public void printAllEntries(DatabaseServer database) {
    try {
        // Get all table names in the database
        List<String> tableNames = getTableNames();

        // Iterate through each table
        for (String tableName : tableNames) {
            System.out.println("\nTable: " + tableName);

            // Call readAllData for each table
            readAllData(tableName);
            long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < 1000) {
                // prison
                }
        }

        System.out.println("\nAll entries have been printed.");
    } catch (Exception e) {
        System.err.println("Error printing all entries: " + e.getMessage());
    }
}    
}
