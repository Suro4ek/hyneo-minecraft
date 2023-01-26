package eu.suro

//import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import eu.suro.grpc.GRPChannel
import eu.suro.messanger.MessangerInit
import eu.suro.messanger.listener.MessageListener
import eu.suro.metadata.Metadata
import eu.suro.redis.RedisEventImpl
import eu.suro.redis.RedisInit
import eu.suro.redis.channel.RedisPacketListener
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
        MessangerInit.init(RedisPacketListener(RedisEventImpl()),  "messenger", "messenger.bukkit")
        server.pluginManager.registerEvents(MessageListener(), this)
        instance = this;
        Metadata.ensureSetup()
    }
}