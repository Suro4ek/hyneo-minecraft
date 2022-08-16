package eu.suro.bungee;

import com.google.inject.Injector;
import dev.rollczi.litecommands.bungee.LiteBungeeFactory;
import eu.suro.api.HyNeoApi;
import eu.suro.api.path.Path;
import eu.suro.bungee.path.PathImpl;
import io.grpc.ChannelCredentials;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import net.md_5.bungee.api.plugin.Plugin;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProxyMain extends Plugin {

    public List<Class<?>> commands = new ArrayList<>();
    static ProxyMain instance;

    static ManagedChannel channel;

    private HyNeoApi hyNeoApi;

    @Inject
    private BungeeConfig bungeeConfig;

    @Override
    public void onEnable() {
        instance = this;
        //create plugins folder if not exists
        File file = new File(getDataFolder()+"/plugins/");
        if (!file.exists()) {
            file.mkdirs();
        }
        Path path = new PathImpl();
//        startGRPCClient();
        //load modules
        hyNeoApi = new HyNeoApi(path);
        Injector injector = HyNeoApi.getInjector().createChildInjector(new BungeeModule());
        injector.injectMembers(ProxyMain.instance);
        loadConfig();
        //register commands
        LiteBungeeFactory.builder(this)
                .command(commands.toArray(new Class<?>[0]))
                .register();
    }

    public void loadConfig(){
        bungeeConfig.setIfNotExist("grpc.host", "localhost");
        bungeeConfig.setIfNotExist("grpc.port", "50051");
        bungeeConfig.save();
    }

    @Override
    public void onDisable() {

    }

    public static ManagedChannel getChannel() {
        return channel;
    }

    public void startGRPCClient(){
        ChannelCredentials credentials = InsecureChannelCredentials.create();
        channel = Grpc.newChannelBuilder(bungeeConfig.grpcHost()+":"+bungeeConfig.grpcPort(),
                        credentials)
                .build();
    }

    public static ProxyMain getInstance() {
        return instance;
    }
}
