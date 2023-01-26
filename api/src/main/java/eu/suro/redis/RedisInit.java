package eu.suro.redis;

import de.exlll.configlib.YamlConfigurations;
import eu.suro.utils.Log;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
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
        Config jedisPoolConfig = new Config();
//        jedisPoolConfig.setTransportMode(TransportMode.EPOLL);
        jedisPoolConfig.useSingleServer().setAddress("redis://"+redisConfig.getHost()+":"+redisConfig.getPort());
        jedisPoolConfig.useSingleServer().setPassword(redisConfig.getPassword());
//        RedissonClient jedisPool = new RedissonClient(jedisPoolConfig,
//                redisConfig.getHost(),
//                redisConfig.getPort(),
//                0,
//                redisConfig.getPassword(),
//                false
//        ) {
//        };
        RedissonClient jedisPool = Redisson.create(jedisPoolConfig);
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
