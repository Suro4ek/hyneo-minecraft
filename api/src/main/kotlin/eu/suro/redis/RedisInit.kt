package eu.suro.redis

import de.exlll.configlib.YamlConfigurations
import eu.suro.api.user.IUserRedis
import eu.suro.utils.Log
import org.redisson.Redisson
import org.redisson.config.Config
import java.io.File


object RedisInit {
    /**
     * Создание канала для подключения к серверу
     * @param dataFolder - Папка с конфигом
     */
    fun initRedis(dataFolder: File?) {
        val configFile = File(dataFolder, "redis.yml")
        var redisConfig = RedisConfig()
        if (!configFile.exists()) YamlConfigurations.save(
            configFile.toPath(),
            RedisConfig::class.java, redisConfig
        )
        redisConfig = YamlConfigurations.load(configFile.toPath(), RedisConfig::class.java)
        val jedisPoolConfig = Config()
        //        jedisPoolConfig.setTransportMode(TransportMode.EPOLL);
        jedisPoolConfig.useSingleServer().address = "redis://" + redisConfig.host + ":" + redisConfig.port
        jedisPoolConfig.useSingleServer().password = redisConfig.password
        //        RedissonClient jedisPool = new RedissonClient(jedisPoolConfig,
//                redisConfig.getHost(),
//                redisConfig.getPort(),
//                0,
//                redisConfig.getPassword(),
//                false
//        ) {
//        };
        val jedisPool = Redisson.create(jedisPoolConfig)
        val redisManager: RedisManager = object : RedisManager(jedisPool) {
            override fun runAsync(task: Runnable) {
                service.execute(task)
            }
        }
        Log.info("[redis] Запускаем редис")
        Redis.init(redisManager)
        jedisPool.liveObjectService.registerClass(IUserRedis::class.java)
    }
}
