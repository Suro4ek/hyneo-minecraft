package eu.suro.messanger;

import com.google.gson.Gson;
import eu.suro.redis.Redis;
import eu.suro.redis.channel.RedisPacketListener;
import eu.suro.utils.Log;
import lombok.Getter;
import java.util.HashMap;

public class MessangerInit {

    @Getter
    public static HashMap<String, ChannelListener> channels = new HashMap<>();

    public static void init(RedisPacketListener redisPacketHandler, String... channels) {
        if(Redis.getManager() == null){
            Log.warn("Redis Manager null");
        }
        Redis.getManager().registerMessageListener(redisPacketHandler,  channels);
    }

    public static void registerChannelListener(String channel, ChannelListener listener) {
        channels.put(channel, listener);
    }

    public static void onPluginMessageReceived(String message){
        Gson gson = new Gson();
        try{
            MessageData data = gson.fromJson(message, MessageData.class);
            if(data != null){
                if(channels.containsKey(data.getChannel())){
                    channels.get(data.getChannel()).onPluginMessageReceived(data);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
