package eu.suro.redis.platform.bungee;

import eu.suro.redis.channel.RedisEvent;
import net.md_5.bungee.api.ProxyServer;

/*
 * Реализация RedisEvent для получения данных из Redis
 */

public class BungeeRedisEventImpl implements RedisEvent<BungeeRedisEvent> {
    @Override
    public BungeeRedisEvent createPubSubEvent(String channel, String message) {
        return new BungeeRedisEvent(channel, message);
    }

    @Override
    public void callEvent(BungeeRedisEvent event) {
        ProxyServer.getInstance().getPluginManager().callEvent(event);
    }
}
