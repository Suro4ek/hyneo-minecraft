package eu.suro.redis;

import de.exlll.configlib.YamlConfigurations;
import eu.suro.utils.Log;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.nio.file.Path;

public class RedisInit {


    /**
     * Создание канала для подключения к серверу
     * @param dataFolder - Папка с конфигом
     */
    public static void initRedis(File dataFolder) {
        File configFile = new File(dataFolder, "redis.yml");
        RedisConfig redisConfig = new RedisConfig();
        if(!configFile.exists())
            YamlConfigurations.save(configFile.toPath(), RedisConfig.class, redisConfig);
        redisConfig = YamlConfigurations.load(configFile.toPath(), RedisConfig.class);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,
                                            redisConfig.getHost(),
                                            redisConfig.getPort(),
                                     0,
                                            redisConfig.getPassword(),
                                            false
        );
        RedisManager redisManager = new RedisManager(jedisPool){
            @Override
            protected void runAsync(Runnable task) {
                getService().execute(task);
            }
        };
        Log.info("[redis] Запускаем редис");
        Redis.init(redisManager);
    }
}
