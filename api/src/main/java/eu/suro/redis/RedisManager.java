package eu.suro.redis;

import eu.suro.redis.channel.RedisPacketListener;
import eu.suro.redis.types.RedisObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

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
    private final JedisPool jedisPool;
    private final ExecutorService service;
    private final Jedis jedis;

    public RedisManager(final JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        this.service = Executors.newCachedThreadPool();
        this.jedis = jedisPool.getResource();
    }

    public void disable() {
        this.jedis.close();
        this.async(stream -> this.service.shutdown());
        try {
            this.service.awaitTermination(15L, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

    public ExecutorService getService() {
        return this.service;
    }

    public Jedis getJedis() {
        return this.jedis;
    }

    /**
     * Сохранить async объект в редис
     * @param structure - Структура объекта
     * @param create - Создание объекта
     * @param <T> - структура
     */
    public <T extends RedisStructure> void saveAsync(final T structure, final Consumer<T> create) {
        this.async(stream -> {
            stream.setDomain(structure);
            structure.onSave(stream);
            create.accept(structure);
        });
    }

    public <T extends RedisStructure> void saveAsync(final T structure) {
        this.saveAsync(structure, saved -> {});
    }

    public <T extends RedisObject> void removeAsync(final T redisObject) {
        async(stream -> {
            stream.setDomain(redisObject);
            Set<String> keys = stream.getKeys("*");
            stream.setDomain("");
            for (String key : keys)
                stream.del(key);
        });
    }

    public <T extends RedisStructure> boolean loadSync(final T structure) {
        try (final Jedis jedis = this.jedisPool.getResource()) {
            final RedisStream stream = new RedisStream(jedis, structure);
            if (stream.exists(structure.getCheckKey())) {
                structure.onLoad(stream);
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T extends RedisStructure> void loadAsync(final T structure, final Consumer<T> create) {
        this.async(stream -> {
            stream.setDomain(structure);
            try {
                if (stream.exists(structure.getCheckKey())) {
                    structure.onLoad(stream);
                    create.accept(structure);
                }
                else {
                    create.accept(null);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Future<?> async(final RedisConsumer create) {
        return this.service.submit(() -> {
            try (Jedis jedis = getJedisPool().getResource()) {
                create.run(new RedisStream(jedis));
            }
        });
    }

    protected abstract void runAsync(final Runnable p0);

    private void registerDataChannel(final JedisPubSub handler, final String... channels) {
        this.runAsync(() -> {
            System.out.println("[Redis] Подписываемся на каналы " + Arrays.toString(channels));
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.subscribe(handler, channels);
            }
        });
    }

    public void registerMessageListener(final RedisPacketListener listener, final String... channels) {
        this.registerDataChannel(listener, channels);
    }

    public void registerPacketHandler(final RedisPacketListener handler, final String... channels) {
        this.registerDataChannel(handler, channels);
    }

    public void getObjectStream(final RedisObject redisObject, final Consumer<RedisStream> consumer) {
        async(s -> {
            s.setDomain(redisObject);
            consumer.accept(s);
        });
    }
}