package eu.suro;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.suro.grpc.GRPChannel;
import eu.suro.messanger.MessangerInit;
import eu.suro.messanger.listener.VelocityMessangerListener;
import eu.suro.redis.RedisInit;
import eu.suro.redis.channel.RedisPacketListener;
import eu.suro.redis.platform.velocity.VelocityRedisEventImpl;
import eu.suro.utils.Log;

import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(id = "hyneoapi", name = "HyNeoAPI", version = "1.0", authors = {"Suro"})
public class VelocityMain {

    private final ProxyServer proxyServer;
    private final Logger logger;
    private final Path dataFolder;

    private static VelocityMain instance;

    @Inject
    public VelocityMain(ProxyServer proxyServer, Logger logger, @DataDirectory Path dataFolder) {
        instance = this;
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.dataFolder = dataFolder;

        Log.init(logger);
        GRPChannel.init(dataFolder.toFile());
        RedisInit.initRedis(dataFolder.toFile());
        MessangerInit.init(new RedisPacketListener(new VelocityRedisEventImpl()), "messenger", "messenger.proxy");
        proxyServer.getEventManager().register(proxyServer, new VelocityMessangerListener());
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

    public static VelocityMain getInstance() {
        return instance;
    }
}
