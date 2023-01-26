package eu.suro.messanger.listener

import eu.suro.messanger.MessangerInit
import eu.suro.redis.RedisEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MessageListener : Listener {
    @EventHandler
    fun onPackageReceived(event: RedisEvent) {
        MessangerInit.onPluginMessageReceived(event.message)
    }
}