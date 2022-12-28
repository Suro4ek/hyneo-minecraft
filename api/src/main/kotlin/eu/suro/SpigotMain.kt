package eu.suro

//import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import eu.suro.grpc.GRPChannel
import eu.suro.messanger.MessangerInit
import eu.suro.messanger.listener.BukkitMessageListener
import eu.suro.redis.RedisInit
import eu.suro.redis.channel.RedisPacketListener
import eu.suro.redis.platform.bukkit.BukkitRedisEventImpl
import eu.suro.utils.Log
import kr.entree.spigradle.annotations.SpigotPlugin
import org.bukkit.plugin.java.JavaPlugin

@SpigotPlugin
class SpigotMain: JavaPlugin() {

    companion object{
        @JvmStatic var instance: SpigotMain? = null
        private set;
    }

    override fun onEnable() {
        //TODO edit
//        Log.init(logger)
        GRPChannel.init(dataFolder)
        RedisInit.initRedis(dataFolder)
        MessangerInit.init(RedisPacketListener(BukkitRedisEventImpl()),  "messenger", "messenger.bukkit")
        server.pluginManager.registerEvents(BukkitMessageListener(), this)
        instance = this;
    }
}