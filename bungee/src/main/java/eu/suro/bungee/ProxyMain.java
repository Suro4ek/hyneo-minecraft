package eu.suro.bungee;

import com.electronwill.nightconfig.core.file.FileConfig;
import eu.suro.api.module.ModuleHandler;
import eu.suro.api.path.Path;
import eu.suro.bungee.path.PathImpl;
import io.grpc.ChannelCredentials;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

public class ProxyMain extends Plugin {

    static ProxyMain instance;
    static ManagedChannel channel;
    ModuleHandler handler;
    static FileConfig config;

    @Override
    public void onEnable() {
        instance = this;
        File file = new File(getDataFolder()+"/plugins/");
        if (!file.exists()) {
            file.mkdirs();
        }
        Path path = new PathImpl();
//        startGRPCClient();
        handler =  new ModuleHandler(path);
        config = FileConfig.of(getDataFolder().getPath()+"/config.yml");
        if(!config.getFile().exists()) {
            config.set("grpc.host", "localhost");
            config.set("grpc.port", "50051");
            config.save();
        }
        config.load();
    }

    @Override
    public void onDisable() {
        handler.getManager().stopPlugins();
    }

    public static ManagedChannel getChannel() {
        return channel;
    }

    public void startGRPCClient(){
        ChannelCredentials credentials = InsecureChannelCredentials.create();
        channel = Grpc.newChannelBuilder(config.get("grpc.host")+":"+config.get("grpc.port"),
                        credentials)
                .build();
    }

    public static ProxyMain getInstance() {
        return instance;
    }
}
