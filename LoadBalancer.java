// TODO: Test the LoadBalancer Class
// This is a simple load balancer implementation

import java.util.ArrayList;
import java.util.List;

public class LoadBalancer{
    private List<DatabaseServer> dbServers;
    
    public LoadBalancer(){
        dbServers = new ArrayList<>();
    }

    public void addServer(String ipAddress, int currentLoad){
        dbServers.add(new DatabaseServer(ipAddress, currentLoad));
    }

    public String getNextServer(){
        if(dbServers.isEmpty()){
            throw new IllegalStateException("No server available in the load balancer");
        }

        DatabaseServer minLoadServer  = dbServers.get(0);
        for(DatabaseServer dbServer : dbServers){
            if(dbServer.getCurrentLoad() < minLoadServer.getCurrentLoad()){
                minLoadServer = dbServer;
            }
        }

        minLoadServer.incrementLoad();
        return minLoadServer.getIpAddress();
    }

}