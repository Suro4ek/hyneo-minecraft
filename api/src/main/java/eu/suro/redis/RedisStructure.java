package eu.suro.redis;

import eu.suro.redis.types.RedisObject;

import java.util.function.Consumer;

public interface RedisStructure extends RedisObject {
    String getCheckKey();

    void onSave(RedisStream paramRedisStream);

    void onLoad(RedisStream paramRedisStream);

    default void save(RedisManager manager) {
        manager.saveAsync(this);
    }

    default void load(RedisManager manager, Consumer<RedisStructure> create) {
        manager.loadAsync(this, create);
    }
}