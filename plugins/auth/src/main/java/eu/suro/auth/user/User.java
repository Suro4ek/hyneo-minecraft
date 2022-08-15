package eu.suro.auth.user;

import com.google.protobuf.Timestamp;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import server.AuthOuterClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {


    private String id;
    private String username;
    private String last_server;
    private String ip;
    private String registerIP;
    private ProxiedPlayer player;
    private boolean isAuth;

    private final PendingConnection connection;
    public User(PendingConnection pendingConnection,
                String id, String ip,
                String last_server, String registerIP,
                boolean isAuth){
        this.id = id;
        this.ip = ip;
        this.connection = pendingConnection;
        this.last_server = last_server;
        this.registerIP = registerIP;
        this.isAuth = isAuth;
    }

    public boolean isAuth(){
        return this.isAuth;
    }

    public boolean isActive(){
        return this.connection.isConnected();
    }

    public void setPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

}
