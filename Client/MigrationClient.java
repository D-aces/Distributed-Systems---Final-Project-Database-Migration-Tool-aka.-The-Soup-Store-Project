package Client;
/* TODO: Create CLI tooling that allows developers (our users) to:
    - Input Database Information (setting up access to the database)
    From here the user either:
    1. Initiates a migration
    2. Wants to check on the progress of an ongoing migration
    Which means the user can: 
    - Get an estimate of the database migration progress
        - Aka as the ability to query the Migrationserver for the migration progress (in case the client CLI is closed, the migration should not stop)
    - When the Migrationserver has completed the migration process the user should be notified, if the client CLI is closed it should be reopened (or some other method to notify?)      
 */

 import java.rmi.Remote;
 import java.rmi.RemoteException;

 public class MigrationClient{
    
    
 }
