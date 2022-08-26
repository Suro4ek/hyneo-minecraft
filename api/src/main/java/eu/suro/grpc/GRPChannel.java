package eu.suro.grpc;

import de.exlll.configlib.YamlConfigurations;
import eu.suro.utils.Log;
import io.grpc.ChannelCredentials;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;


import java.io.File;
import java.nio.file.Path;

public class GRPChannel {

    private static ManagedChannel channel;

    public static void init(File dataFolder) {
        if(channel == null){
            File configFile = new File(dataFolder, "config.yml");
            GRPCConfig grpcConfig = new GRPCConfig();
            if(!configFile.exists()){
                YamlConfigurations.save(configFile.toPath(), GRPCConfig.class, grpcConfig);
            }
            grpcConfig = YamlConfigurations.load(configFile.toPath(), GRPCConfig.class);
            ChannelCredentials credentials = InsecureChannelCredentials.create();
            GRPChannel.channel = Grpc.newChannelBuilder(grpcConfig.getHost()+":"+grpcConfig.getPort(),credentials)
                    .build();
            Log.info("[GRPC] Запускаем канал связи с ядром");
        }
    }

    public static ManagedChannel getChannel() {
        return channel;
    }
}
