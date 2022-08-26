package eu.suro.redis.channel;

public interface RedisEvent<T> {

    T createPubSubEvent(String channel, String message);
    void callEvent(T event);
}
