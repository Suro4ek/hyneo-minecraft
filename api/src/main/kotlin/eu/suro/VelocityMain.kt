package eu.suro

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator
import cloud.commandframework.kotlin.coroutines.annotations.installCoroutineSupport
import cloud.commandframework.meta.SimpleCommandMeta
import cloud.commandframework.velocity.CloudInjectionModule
import cloud.commandframework.velocity.VelocityCommandManager
import com.github.shynixn.mccoroutine.velocity.MCCoroutine
import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.github.shynixn.mccoroutine.velocity.registerSuspend
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import eu.suro.command.TestCommand
import eu.suro.grpc.GRPChannel
import eu.suro.messanger.MessangerInit
import eu.suro.messanger.listener.VelocityMessangerListener
import eu.suro.redis.RedisInit
import eu.suro.redis.channel.RedisPacketListener
import eu.suro.redis.platform.velocity.VelocityRedisEventImpl
import eu.suro.utils.Log
import java.nio.file.Path
import java.util.function.Function

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
        MessangerInit.init(RedisPacketListener(VelocityRedisEventImpl()), "messenger", "messenger.proxy")
    }

    @Subscribe
    fun onInit(event: ProxyInitializeEvent){
        proxyServer.eventManager.register(this, VelocityMessangerListener())
    }
}