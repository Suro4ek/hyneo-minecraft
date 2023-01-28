package eu.suro.redis

import eu.suro.VelocityMain.Companion.instance
import eu.suro.redis.channel.RedisEvent

class RedisEventImpl<T> : RedisEvent<eu.suro.redis.RedisEvent<T>, T> {

    override fun createPubSubEvent(channel: String, obj: T): eu.suro.redis.RedisEvent<T> {
        return eu.suro.redis.RedisEvent(channel, obj)
    }

    override fun callEvent(event: eu.suro.redis.RedisEvent<T>) {
        instance!!.proxyServer.eventManager.fire(event)
    }
}
