package Server;
// // TODO: Review and Test DatabaseServer Class


// class DatabaseServer{
//     private String ipAddress;
//     private int currentLoad;

//     public DatabaseServer(String ipAddress, int currentLoad){
//         this.ipAddress = ipAddress;
//         this.currentLoad = currentLoad;
//     }

//     public String getIpAddress(){
//         return ipAddress;
//     }

//     public int getCurrentLoad(){
//         return currentLoad;
//     }

//     public void incrementLoad(){
//         currentLoad++;
//     }

// }
// WE DO A LITTLE COMMENTING

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseServer {

    private Connection connection;
    private String databaseUrl;

    public DatabaseServer(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    // Open the database connection
    public void open() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(databaseUrl);
        }
    }

    // Close the database connection
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // Execute query with parameters (for CRUD operations)
    public ResultSet query(String query, Object... params) throws SQLException {
        var preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeQuery();
    }

    // Execute update with parameters (for insert, update, delete)
    public void execute(String query, Object... params) throws SQLException {
        var preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        preparedStatement.executeUpdate();
    }

    public String getUrl(){
        return databaseUrl;
    }
}
