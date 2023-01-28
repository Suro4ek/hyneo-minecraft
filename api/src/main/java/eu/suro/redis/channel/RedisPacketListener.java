package eu.suro.redis.channel;

import org.redisson.api.listener.MessageListener;

public class RedisPacketListener<T, V> implements MessageListener<V> {

    private final RedisEvent<T, V> redisEvent;

    public RedisPacketListener(RedisEvent<T, V> redisEvent) {
        this.redisEvent = redisEvent;
    }


    @Override
    public void onMessage(CharSequence channel, V msg) {
        T event = redisEvent.createPubSubEvent((String) channel, msg);
        redisEvent.callEvent(event);
    }
}