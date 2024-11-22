package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MigrationCallback extends Remote {
    void notifyMigrationComplete(String migrationId) throws RemoteException;
}

