package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DatabaseMigrationServer extends Remote {
    String startMigration(String dbPath) throws RemoteException;
    String getMigrationStatus(String migrationId) throws RemoteException;
    void registerCallback(String migrationId, MigrationCallback callback) throws RemoteException;
}