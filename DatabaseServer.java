// TODO: Review and Test DatabaseServer Class

class DatabaseServer{
    private String ipAddress;
    private int currentLoad;

    public DatabaseServer(String ipAddress, int currentLoad){
        this.ipAddress = ipAddress;
        this.currentLoad = currentLoad;
    }

    public String getIpAddress(){
        return ipAddress;
    }

    public int getCurrentLoad(){
        return currentLoad;
    }

    public void incrementLoad(){
        currentLoad++;
    }

}