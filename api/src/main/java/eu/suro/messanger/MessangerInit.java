package eu.suro.messanger;

import eu.suro.redis.Redis;
import eu.suro.redis.channel.RedisPacketListener;
import eu.suro.utils.Log;
import lombok.Getter;
import java.util.HashMap;

public class MessangerInit {

    @Getter
    public static HashMap<String, ChannelListener> channels = new HashMap<>();

    public static void init(RedisPacketListener redisPacketHandler, String channel, Class type) {
        if(Redis.manager == null){
            Log.warn("Redis Manager null");
        }
        Redis.manager.registerMessageListener(redisPacketHandler, channel, type);
    }

    public static void registerChannelListener(String channel, ChannelListener listener) {
        channels.put(channel, listener);
    }

    public static <T> void onPluginMessageReceived(String channel, T obj){
        try{
            if(channels.containsKey(channel)){
                channels.get(channel).onPluginMessageReceived(obj);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
