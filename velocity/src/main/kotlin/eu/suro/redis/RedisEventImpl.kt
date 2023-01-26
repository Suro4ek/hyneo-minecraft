package eu.suro.redis

import eu.suro.VelocityMain.Companion.instance
import eu.suro.redis.channel.RedisEvent

class RedisEventImpl : RedisEvent<eu.suro.redis.RedisEvent> {
    override fun createPubSubEvent(channel: String, message: String): eu.suro.redis.RedisEvent {
        return eu.suro.redis.RedisEvent(channel, message)
    }

    override fun callEvent(event: eu.suro.redis.RedisEvent) {
        instance!!.proxyServer.eventManager.fire(event)
    }
}
