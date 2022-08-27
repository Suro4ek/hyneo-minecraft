package eu.suro.metadata.listener;

import eu.suro.metadata.StandardMetadataRegistries;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeMetadataListener implements Listener {

    @EventHandler(priority = 5)
    public void onQuit(PlayerDisconnectEvent e){
        StandardMetadataRegistries.PLAYER.remove(e.getPlayer().getUniqueId());
    }
}
