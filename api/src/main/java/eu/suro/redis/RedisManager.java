package eu.suro.redis;

import eu.suro.redis.channel.RedisPacketListener;
import org.redisson.api.RReliableTopic;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Менеджер для работы с Redis
 * В нем есть методы для работы с каналами и подписки на каналы
 * так же с функциями самого редиса
 */
public abstract class RedisManager
{
    final RedissonClient jedisPool;
    private final ExecutorService service;

    public RedisManager(final RedissonClient jedisPool) {
        this.jedisPool = jedisPool;
        this.service = Executors.newCachedThreadPool();
    }

    public void disable() {
//        this.async(stream -> this.service.shutdown());
        try {
            this.service.awaitTermination(15L, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public ExecutorService getService() {
        return this.service;
    }


    public RedissonClient getJedisPool() {
        return jedisPool;
    }

    protected abstract void runAsync(final Runnable p0);

//    private void registerDataChannel(final JedisPubSub handler, final String topic) {
//
//        this.runAsync(() -> {
//            System.out.println("[Redis] Подписываемся на каналы " + Arrays.toString(channels));
//            jedisPool.getTopic(channels).
//            try (Jedis jedis = this.jedisPool.getResource()) {
//                jedis.subscribe(handler, channels);
//            }
//        });
//    }

    public void registerMessageListener(final RedisPacketListener listener, final String topic, Class type) {
        System.out.println("[Redis] Подписываемся на канал " + topic);
        RReliableTopic topic1 = jedisPool.getReliableTopic(topic);
        topic1.addListenerAsync(type, listener);
//        this.runAsync(() -> {
//
////            try (Jedis jedis = this.jedisPool.getResource()) {
////                jedis.subscribe(handler, channels);
////            }
//        });
    }
//
//    public void registerPacketHandler(final RedisPacketListener handler, final String... channels) {
//        this.registerDataChannel(handler, channels);
//    }
//
//    public void getObjectStream(final RedisObject redisObject, final Consumer<RedisStream> consumer) {
//        async(s -> {
//            s.setDomain(redisObject);
//            consumer.accept(s);
//        });
//    }
}