package eu.suro.redis;

import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.function.Consumer;

/*
    Используется для транзакций с редисом, чтобы запрос точно дошел до редиса
 */
public class Redis {
    private static RedisManager redisManager;

    public static void init(RedisManager redisManager) throws JedisException {
        Redis.redisManager = redisManager;
        sync(stream -> stream.getJedis().info());
    }

    public static RedisManager getManager() {
        return redisManager;
    }

    @Deprecated
    public static void async(RedisConsumer create) {
        getManager().async(create);
    }

    public static void sync(RedisConsumer create) {
        try (Jedis jedis = getManager().getJedisPool().getResource()) {
            create.run(new RedisStream(jedis));
        } catch (JedisException e) {
            e.printStackTrace();
        }
    }

    public static void pipeline(Consumer<Pipeline> pipelineConsumer) {
        try (Jedis jedis = getManager().getJedisPool().getResource()) {
            try (Pipeline pipelined = jedis.pipelined()) {
                pipelineConsumer.accept(pipelined);
            }
        } catch (JedisException e) {
            e.printStackTrace();
        }
    }

    public static void transaction(Consumer<Transaction> transactionConsumer) {
        try (Jedis jedis = getManager().getJedisPool().getResource()) {
            try (Transaction transaction = jedis.multi()) {
                transactionConsumer.accept(transaction);
            }
        } catch (JedisException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public static <T> T parseRedisData(String data, Class<T> clazz) {
        if (data != null && !data.isEmpty())
            try {
                Object object = data;
                if (clazz == Integer.class)
                    object = Integer.parseInt(data);
                if (clazz == Long.class)
                    object = Long.parseLong(data);
                if (clazz == Double.class)
                    object = Double.parseDouble(data);
                if (clazz == Float.class)
                    object = Float.parseFloat(data);
                if (clazz == Boolean.class)
                    object = data.equals("true");
                if (clazz == Timestamp.class)
                    object = new Timestamp(Long.parseLong(data));
                if (clazz == UUID.class)
                    object = UUID.fromString(data);
                return (T)object;
            } catch (Exception ex) {
                return null;
            }
        return null;
    }

}
