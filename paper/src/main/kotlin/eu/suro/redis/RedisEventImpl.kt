package eu.suro.redis

import eu.suro.redis.channel.RedisEvent
import org.bukkit.Bukkit

/*
* Реализация RedisEvent для получения данных из Redis
*/
class RedisEventImpl : RedisEvent<eu.suro.redis.RedisEvent> {
    override fun createPubSubEvent(channel: String, message: String): eu.suro.redis.RedisEvent {
        return eu.suro.redis.RedisEvent(channel, message)
    }


    override fun callEvent(event: eu.suro.redis.RedisEvent) {
        Bukkit.getPluginManager().callEvent(event)
    }
}