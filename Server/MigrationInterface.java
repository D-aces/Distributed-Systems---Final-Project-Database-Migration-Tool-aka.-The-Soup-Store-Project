package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MigrationInterface extends Remote {
   //public byte[] downloadFile(String fileName) throws RemoteException;
   void addCloudDatabase(String cloudDatabase) throws RemoteException;
   void setLocalDatabase(String localDatabase) throws RemoteException;
   // TODO: Find a better return type for this
   String migrationStatus() throws RemoteException;
   String select(String query) throws RemoteException;
   String insert(String query) throws RemoteException;
}   
