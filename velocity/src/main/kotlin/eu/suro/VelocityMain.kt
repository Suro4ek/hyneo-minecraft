package eu.suro

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import eu.suro.grpc.GRPChannel
import eu.suro.messanger.MessangerInit
import eu.suro.messanger.listener.MessageListener
import eu.suro.messanger.listener.VelocityMessage
import eu.suro.metadata.Metadata
import eu.suro.redis.RedisEventImpl
import eu.suro.redis.RedisInit
import eu.suro.redis.channel.RedisPacketListener
import eu.suro.utils.Log
import java.nio.file.Path

//@Plugin(id = "hyneo", name = "HyNeoApi", version = "1.0")
class VelocityMain @Inject constructor(suspendingPluginContainer: SuspendingPluginContainer, @DataDirectory val dataDirectory: Path) {

    @Inject
    lateinit var proxyServer: ProxyServer

    @Inject
    lateinit var pluginContainer: PluginContainer

    companion object{
        @JvmStatic var instance: VelocityMain? = null
            private set
    }

    init {
        suspendingPluginContainer.initialize(this)
        instance = this;

        Log.init(suspendingPluginContainer.logger)
        GRPChannel.init(dataDirectory.toFile())
        RedisInit.initRedis(dataDirectory.toFile())
        MessangerInit.init(RedisPacketListener(RedisEventImpl<VelocityMessage>()), "messenger.proxy", VelocityMessage::class.java)
    }

    @Subscribe
    fun onInit(event: ProxyInitializeEvent){
        proxyServer.eventManager.register(this, MessageListener<VelocityMessage>())
        Metadata.ensureSetup()
    }
}