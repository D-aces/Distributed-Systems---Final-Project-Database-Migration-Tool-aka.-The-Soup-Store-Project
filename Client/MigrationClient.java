package Client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import Server.DatabaseMigrationServer;


public class MigrationClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            DatabaseMigrationServer server = (DatabaseMigrationServer) registry.lookup("MigrationService");

            MigrationCallback callback = new MigrationClientCallback();
            registry.rebind("ClientCallback", callback);
            System.out.println("Callback object bound to registry.");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("=== Database Migration CLI Tool ===");
                System.out.println("1. Create a New Migration");
                System.out.println("2. Check Ongoing Migration");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1: // Create a new migration
                        System.out.print("Enter SQLite DB Path: ");
                        String dbPath = scanner.nextLine();
                        String migrationId = server.startMigration(dbPath);
                        System.out.println("Migration started with ID: " + migrationId);
                        break;

                    case 2: // Check ongoing migration
                        System.out.print("Enter Migration ID to check: ");
                        String checkId = scanner.nextLine();
                        String status = server.getMigrationStatus(checkId);
                        System.out.println("Migration Status: " + status);
                        break;

                    case 3: // Exit
                        System.out.println("Exiting CLI tool...");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
