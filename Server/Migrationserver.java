package Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Migrationserver {
    public static void main(String[] args) {
        try {
            DatabaseHandler dbHandler = new DatabaseHandler(
                new DatabaseServer("jdbc:sqlite:LocalSoupStore.db"),
                new DatabaseServer("jdbc:sqlite:CloudSoupStore.db")
            );

            MigrationserverTests server = new MigrationserverTests(dbHandler);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("MigrationService", server);
            System.out.println("Migration Server is running...");

            synchronized (Migrationserver.class) {
                Migrationserver.class.wait(); // Keeps the server alive indefinitely
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

