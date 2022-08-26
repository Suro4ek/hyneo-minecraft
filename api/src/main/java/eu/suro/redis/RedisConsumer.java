package eu.suro.redis;

import redis.clients.jedis.exceptions.JedisException;

public interface RedisConsumer {
    void run(RedisStream paramRedisStream) throws JedisException;
}