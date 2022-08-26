package eu.suro.redis.platform.bukkit;

import eu.suro.redis.channel.RedisEvent;
import org.bukkit.Bukkit;

/*
 * Реализация RedisEvent для получения данных из Redis
 */
public class BukkitRedisEventImpl implements RedisEvent<BukkitRedisEvent> {
    @Override
    public BukkitRedisEvent createPubSubEvent(String channel, String message) {
        return new BukkitRedisEvent(channel, message);
    }

    @Override
    public void callEvent(BukkitRedisEvent event) {
        Bukkit.getPluginManager().callEvent(event);
    }
}
