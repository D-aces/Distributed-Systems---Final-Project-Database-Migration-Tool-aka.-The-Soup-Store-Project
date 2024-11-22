package Server;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


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

    public void insertDataLocal(String table, int id, String name, double price) {
        String query = "INSERT INTO " + table + " (id, name, price) VALUES (?, ?, ?)";
        try {
            // insertDataLocal is used here to execute the query on localDatabase
            localDatabase.execute(query, id, name, price);
        } catch (Exception e) {
            System.err.println("Error inserting data: " + e.getMessage());
        }
    }

    public void insertDataCloud(String table, int id, String name, double price) {
        String query = "INSERT INTO " + table + " (id, name, price) VALUES (?, ?, ?)";
        try {
            // cloudDatabase is used here to execute the query on cloudDatabase
            cloudDatabase.execute(query, id, name, price);
        } catch (Exception e) {
            System.err.println("Error inserting data into table " + table + ": " + e.getMessage());
        }
    }

    public void readData(DatabaseServer db, String table, int id) {
        String query = "SELECT * FROM " + table + " WHERE id = ?";
        try {
            ResultSet resultSet = db.query(query, id);
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

    public void readAllData(DatabaseServer db, String table) {
        String query = "SELECT * FROM " + table;
        try {
            ResultSet resultSet = db.query(query);
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

    //Used to get the table names in a database
    public List<String> getTableNames(DatabaseServer database) {
    String query = "SELECT name FROM sqlite_master WHERE type = 'table' AND name NOT LIKE 'sqlite_%'";
    List<String> tableNames = new ArrayList<>();

    try {
        ResultSet resultSet = database.query(query);
        while (resultSet.next()) {
            tableNames.add(resultSet.getString("name"));
        }
    } catch (Exception e) {
        System.err.println("Error retrieving table names: " + e.getMessage());
    }

    return tableNames;
}
    
// Get the number of rows in a table
public int getRowCount(DatabaseServer database, String tableName) {
    String query = "SELECT COUNT(*) FROM " + tableName;
    int rowCount = 0;

    try {
        ResultSet resultSet = database.query(query);
        if (resultSet.next()) {
            rowCount = resultSet.getInt(1); // Get the count from the first column
        }
    } catch (Exception e) {
        System.err.println("Error getting row count for table " + tableName + ": " + e.getMessage());
    }

    return rowCount;
} 

// This method retrieves a single row from a table based on an offset
public ResultSet getRowByOffset(DatabaseServer database, String tableName, int offset) {
    String query = "SELECT * FROM " + tableName + " LIMIT 1 OFFSET " + offset;
    ResultSet resultSet = null;

    try {
        resultSet = database.query(query);
    } catch (Exception e) {
        System.err.println("Error retrieving row at offset " + offset + " from table " + tableName + ": " + e.getMessage());
    }

    return resultSet;
}

// The purge 
public void clearCloudDatabase() {
    try {
        // Get the list of table names for the cloud database
        List<String> tableNames = getTableNames(cloudDatabase);

        // Loop through each table and delete all rows
        for (String tableName : tableNames) {
            String query = "DELETE FROM " + tableName; // Deletes all rows in the table
            cloudDatabase.execute(query); // Execute the delete query
            System.out.println("Cleared all entries from table: " + tableName);
        }

        System.out.println("All tables in the cloud database have been cleared.\n");
    } catch (Exception e) {
        System.err.println("Error clearing cloud database: " + e.getMessage());
    }
}

public void printAllEntries(DatabaseServer database) {
    try {
        // Get all table names in the database
        List<String> tableNames = getTableNames(database);

        if(tableNames.size() == 0){
            System.out.println("No tables exist in database: " + database.getUrl());
            return;
        }
        // Iterate through each table
        for (String tableName : tableNames) {
            System.out.println("\nTable: " + tableName + " from database" + database.getUrl());

            // Call readAllData for each table
            readAllData(database, tableName);
            long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < 1000) {
                // prison
                }
        }

        System.out.println("\nAll entries have been printed.");
        System.out.println("Number of tables migrated: " + tableNames.size());
    } catch (Exception e) {
        System.err.println("Error printing all entries: " + e.getMessage());
    }
}

public void dropTable(DatabaseServer db, String tableName) {
    String query = "DROP TABLE IF EXISTS " + tableName;
    try {
        db.execute(query);
        System.out.println("Table " + tableName + " has been dropped successfully.");
    } catch (Exception e) {
        System.err.println("Error dropping table " + tableName + ": " + e.getMessage());
    }
}   
}
