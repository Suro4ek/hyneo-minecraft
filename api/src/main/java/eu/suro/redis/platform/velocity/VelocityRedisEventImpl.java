package eu.suro.redis.platform.velocity;

import eu.suro.VelocityMain;
import eu.suro.redis.channel.RedisEvent;

public class VelocityRedisEventImpl implements RedisEvent<VelocityRedisEvent> {
    @Override
    public VelocityRedisEvent createPubSubEvent(String channel, String message) {
        return new VelocityRedisEvent(channel, message);
    }

    @Override
    public void callEvent(VelocityRedisEvent event) {
        VelocityMain.getInstance().getProxyServer().getEventManager().fire(event);
    }
}
