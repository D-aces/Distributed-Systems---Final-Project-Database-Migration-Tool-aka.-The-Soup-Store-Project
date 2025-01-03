package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MigrationInterface extends Remote {
   //public byte[] downloadFile(String fileName) throws RemoteException;
   String migrateDatabase(String localDatabase, String cloudDatabase) throws RemoteException;
   String migrationStatus();
}