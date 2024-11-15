package de.voasis.nebula.Maps;

import de.voasis.nebula.Data.Data;
import java.util.ArrayList;
import java.util.List;

public class HoldServer {

    private final String serverName;
    private final String ip;
    private final String password;
    private int freePort;
    private final String username;

    public HoldServer(String serverName, String ip, String password, int freePort, String username) {
        this.serverName = serverName;
        this.password = password;
        this.freePort = freePort;
        this.username = username;
        this.ip = ip;
    }

    public String getIp() { return ip; }
    public String getServerName() { return serverName; }
    public String getPassword() { return password; }
    public int getFreePort() { return freePort; }
    public String getUsername() { return username; }
    public void setFreePort(int freePort) { this.freePort = freePort; }

    public List<BackendServer> getBackendServers() {
        List<BackendServer> list = new ArrayList<>();
        for(BackendServer backendServer : Data.backendInfoMap) {
            if(backendServer.getHoldServer().equals(this)) {
                list.add(backendServer);
            }
        }
        return list;
    }
}
