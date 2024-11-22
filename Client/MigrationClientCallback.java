package Client;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class MigrationClientCallback extends UnicastRemoteObject implements MigrationCallback {
    protected MigrationClientCallback() throws RemoteException {
        super();
    }

    @Override
    public void notifyMigrationComplete(String message) throws RemoteException {
        System.out.println("Server Notification: " + message);
    }
}
