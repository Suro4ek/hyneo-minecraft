package eu.suro.messanger.listener

import com.velocitypowered.api.event.Subscribe
import eu.suro.messanger.MessangerInit
import eu.suro.redis.RedisEvent


class MessageListener<T> {
    @Subscribe
    fun on(e: RedisEvent<T>) {
        MessangerInit.onPluginMessageReceived(e.channel, e.obj)
    }
}
