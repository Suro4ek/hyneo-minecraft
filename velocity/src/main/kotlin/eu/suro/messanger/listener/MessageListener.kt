package eu.suro.messanger.listener

import com.velocitypowered.api.event.Subscribe
import eu.suro.messanger.MessangerInit
import eu.suro.redis.RedisEvent


class MessageListener {
    @Subscribe
    fun on(e: RedisEvent) {
        MessangerInit.onPluginMessageReceived(e.message)
    }
}
