package eu.suro.messanger.listener;

import eu.suro.messanger.MessangerInit;
import eu.suro.redis.platform.bukkit.BukkitRedisEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class BukkitMessageListener implements Listener {

    @EventHandler
    public void onPackageReceived(BukkitRedisEvent event) {
        MessangerInit.onPluginMessageReceived(event.getMessage());
    }
}