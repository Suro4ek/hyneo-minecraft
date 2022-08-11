package eu.suro.auth.events;

import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

    @EventHandler
    public void PreLogin(PreLoginEvent e){
        String name = e.getConnection().getName();
        String ip = e.getConnection().getAddress().getAddress().getHostAddress();
        System.out.println("[Auth] " + name + " is trying to login from " + ip);

    }
}
