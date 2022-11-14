package eu.suro.metadata.listener;

import eu.suro.api.user.IUser;
import eu.suro.metadata.StandardMetadataRegistries;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Locale;

public class BukkitMetadataListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e){
        StandardMetadataRegistries.USER_METADATA_REGISTRY.remove(e.getPlayer().getName().toLowerCase(Locale.ROOT));
    }
}
