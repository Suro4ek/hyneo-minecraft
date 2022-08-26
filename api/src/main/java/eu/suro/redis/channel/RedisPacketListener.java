package eu.suro.redis.channel;

import redis.clients.jedis.JedisPubSub;

public class RedisPacketListener<T> extends JedisPubSub {

    private final RedisEvent<T> redisEvent;

    public RedisPacketListener(RedisEvent<T> redisEvent) {
        this.redisEvent = redisEvent;
    }

    @Override
    public void onMessage(String channel, String message) {
        if (message.trim().length() == 0) return;
        T event = redisEvent.createPubSubEvent(channel, message);
        redisEvent.callEvent(event);
    }
}