package eu.suro;

import eu.suro.grpc.GRPChannel;
import eu.suro.messanger.MessangerInit;
import eu.suro.messanger.listener.BungeeMessangerListener;
import eu.suro.redis.RedisInit;
import eu.suro.redis.channel.RedisPacketListener;
import eu.suro.redis.platform.bungee.BungeeRedisEvent;
import eu.suro.redis.platform.bungee.BungeeRedisEventImpl;
import eu.suro.utils.Log;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import kr.entree.spigradle.annotations.BungeePlugin;
import net.md_5.bungee.api.plugin.Plugin;

@BungeePlugin
public class BungeeMain extends Plugin {

    private static BungeeMain instance;
    @Override
    public void onEnable() {
        Log.init(getLogger());
        GRPChannel.init(getDataFolder());
        RedisInit.initRedis(getDataFolder());
        MessangerInit.init(new RedisPacketListener<>(new BungeeRedisEventImpl()), "messenger", "messenger.bungee");
        getProxy().getPluginManager().registerListener(this, new BungeeMessangerListener());

    }

    public static BungeeMain getInstance() {
        return instance;
    }
}
