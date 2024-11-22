package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MigrationInterface extends Remote {
   void addCloudDatabase(String cloudDatabase) throws RemoteException;
   void setLocalDatabase(String localDatabase) throws RemoteException;
   String migrationStatus() throws RemoteException;
   String select(String table, String query, Object... params) throws RemoteException;
   String insert(String table, String query, Object... params) throws RemoteException;
}   
