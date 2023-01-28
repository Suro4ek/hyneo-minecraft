package eu.suro.redis.channel;

public interface RedisEvent<T,V> {

    T createPubSubEvent(String channel, V message);
    void callEvent(T event);
}
