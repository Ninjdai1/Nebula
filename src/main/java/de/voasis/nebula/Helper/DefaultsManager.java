package de.voasis.nebula.Helper;

import com.velocitypowered.api.proxy.ProxyServer;
import de.voasis.nebula.Data.Data;
import de.voasis.nebula.Maps.BackendServer;
import de.voasis.nebula.Nebula;
import java.util.ArrayList;
import java.util.List;

public class DefaultsManager {

    private final ProxyServer server;
    private final int max;
    private final int min;

    public DefaultsManager(ProxyServer server) {
        this.max = Data.defaultmax;
        this.min = Data.defaultmin;
        this.server = server;
        createDefault();
    }

    public BackendServer getTarget() {
        BackendServer target = getServerWithLowestPlayerCount();
        if(target != null) {
            int count = server.getServer(target.getServerName()).get().getPlayersConnected().size();
            if(count + 1 == min && !isOtherUnderMin(target)) {
                createDefault();
            }
        }
        target = getServerBetweenMinAndMaxPlayers();
        if(target != null) {
            return target;
        }
        return getServerWithLowestPlayerCount();
    }

    private List<BackendServer> getAvailableServers() {
        List<BackendServer> servers = new ArrayList<>();
        for (BackendServer server : Data.backendInfoMap) {
            if (server.getFlags().contains("lobby") && server.isOnline()) {
                servers.add(server);
            }
        }
        return servers;
    }

    private BackendServer getServerWithLowestPlayerCount() {
        if (getAvailableServers().isEmpty()) {
            return null;
        }
        BackendServer serverWithLowestCount = getAvailableServers().getFirst();
        int lowestPlayerCount = server.getServer(serverWithLowestCount.getServerName())
                .get()
                .getPlayersConnected()
                .size();
        for (BackendServer backendServer : getAvailableServers()) {
            int playerCount = server.getServer(backendServer.getServerName())
                    .get()
                    .getPlayersConnected()
                    .size();
            if (playerCount < lowestPlayerCount) {
                serverWithLowestCount = backendServer;
                lowestPlayerCount = playerCount;
            }
        }
        return serverWithLowestCount;
    }

    private boolean isOtherUnderMin(BackendServer other) {
        for (BackendServer backendServer : getAvailableServers()) {
            int playerCount = server.getServer(backendServer.getServerName())
                    .get()
                    .getPlayersConnected()
                    .size();
            if (playerCount < min && !backendServer.equals(other)) {
                return true;
            }
        }
        return false;
    }

    private BackendServer getServerBetweenMinAndMaxPlayers() {
        for (BackendServer backendServer : getAvailableServers()) {
            int playerCount = server.getServer(backendServer.getServerName())
                    .get()
                    .getPlayersConnected()
                    .size();
            if (playerCount >= min && playerCount < max) {
                return backendServer;
            }
        }
        return null;
    }

    public void createDefault() {
        String name = "Lobby-" + Nebula.util.generateUniqueString();
        Nebula.serverManager.createFromTemplate(
                Data.defaultServerTemplate,
                name,
                server.getConsoleCommandSource(),
                "lobby"
        );
    }
}
