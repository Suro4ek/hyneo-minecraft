package eu.suro

import com.google.common.eventbus.Subscribe
import com.google.inject.Inject
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import eu.suro.grpc.GRPChannel
import eu.suro.messanger.MessangerInit
import eu.suro.messanger.listener.VelocityMessangerListener
import eu.suro.redis.RedisInit
import eu.suro.redis.channel.RedisPacketListener
import eu.suro.redis.platform.velocity.VelocityRedisEventImpl
import eu.suro.utils.Log
import java.nio.file.Path
import java.util.logging.Logger

@Plugin(id = "hyneoapi", name = "HyNeoAPI", version = "1.0", authors = ["Suro"])
class VelocityMain @Inject constructor(
        val proxyServer: ProxyServer,
        logger: Logger,
        @DataDirectory val dataFolder: Path,
){

    companion object{
        @JvmStatic var instance: VelocityMain? = null
        private set;
    }

    init {
        instance = this;
        Log.init(logger)
        GRPChannel.init(dataFolder.toFile())
        RedisInit.initRedis(dataFolder.toFile())
        MessangerInit.init(RedisPacketListener(VelocityRedisEventImpl()), "messenger", "messenger.proxy")
    }

    @Subscribe
    fun onInit(event: ProxyInitializeEvent){
        proxyServer.eventManager.register(this, VelocityMessangerListener())
    }
}