package eu.suro.redis

import eu.suro.redis.channel.RedisEvent
import org.bukkit.Bukkit

/*
* Реализация RedisEvent для получения данных из Redis
*/
class RedisEventImpl<T> : RedisEvent<eu.suro.redis.RedisEvent<T>, T> {
    override fun createPubSubEvent(channel: String, obj: T): eu.suro.redis.RedisEvent<T> {
        return eu.suro.redis.RedisEvent(channel, obj)
    }


    override fun callEvent(event: eu.suro.redis.RedisEvent<T>) {
        Bukkit.getPluginManager().callEvent(event)
    }
}