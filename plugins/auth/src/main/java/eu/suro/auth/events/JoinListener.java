package eu.suro.auth.events;

import eu.suro.auth.AuthMain;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

    public AuthMain.AuthModule authModule;

    public JoinListener(AuthMain.AuthModule auth){
        authModule = auth;
    }

    @EventHandler
    public void PreLogin(PreLoginEvent e){
        String name = e.getConnection().getName();
        String ip = e.getConnection().getAddress().getAddress().getHostAddress();
        authModule.getLogger().info("pre loging " + name + " by ip " + ip);
    }


}
