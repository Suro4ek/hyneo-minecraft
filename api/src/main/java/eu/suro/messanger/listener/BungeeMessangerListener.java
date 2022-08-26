package eu.suro.messanger.listener;

import eu.suro.messanger.MessangerInit;
import eu.suro.redis.platform.bungee.BungeeRedisEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


public class BungeeMessangerListener implements Listener {

    @EventHandler
    public void onPackageReceived(BungeeRedisEvent event) {
        MessangerInit.onPluginMessageReceived(event.getMessage());
    }
}
