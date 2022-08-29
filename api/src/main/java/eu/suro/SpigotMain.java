package eu.suro;

import eu.suro.grpc.GRPChannel;
import eu.suro.locale.Language;
import eu.suro.locale.LocaleStorage;
import eu.suro.messanger.MessangerInit;
import eu.suro.messanger.listener.BukkitMessageListener;
import eu.suro.redis.RedisInit;
import eu.suro.redis.channel.RedisPacketListener;
import eu.suro.redis.platform.bukkit.BukkitRedisEventImpl;
import eu.suro.utils.Log;
import kr.entree.spigradle.annotations.SpigotPlugin;
import org.bukkit.plugin.java.JavaPlugin;

@SpigotPlugin
public class SpigotMain extends JavaPlugin {

    private static SpigotMain instance;

    @Override
    public void onEnable() {
        instance = this;
        Log.init(getLogger());
        GRPChannel.init(getDataFolder());
        RedisInit.initRedis(getDataFolder());
        MessangerInit.init(new RedisPacketListener<>(new BukkitRedisEventImpl()), "messenger", "messenger.bukkit");
        getServer().getPluginManager().registerEvents(new BukkitMessageListener(), this);
    }

    public static SpigotMain getInstance() {
        return instance;
    }
}
