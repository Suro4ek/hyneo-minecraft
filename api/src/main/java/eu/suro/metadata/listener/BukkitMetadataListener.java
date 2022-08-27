package eu.suro.metadata.listener;

import eu.suro.metadata.StandardMetadataRegistries;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitMetadataListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e){
        StandardMetadataRegistries.PLAYER.remove(e.getPlayer().getUniqueId());
    }
}
