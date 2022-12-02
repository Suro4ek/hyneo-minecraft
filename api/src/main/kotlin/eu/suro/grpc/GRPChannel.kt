package eu.suro.grpc

import de.exlll.configlib.YamlConfigurations
import io.grpc.Grpc
import io.grpc.InsecureChannelCredentials
import io.grpc.ManagedChannel
import java.io.File

class GRPChannel {

    companion object{
        var channel: ManagedChannel? = null
        private set

        @JvmStatic fun init(dataFolder: File){
            if(channel == null){
                val configFile = File(dataFolder, "config.yml")
                var grpcConfig = GRPCConfig()
                if(!configFile.exists()){
                    YamlConfigurations.save(configFile.toPath(), grpcConfig.javaClass, grpcConfig)
                }
                grpcConfig = YamlConfigurations.load(configFile.toPath(), grpcConfig.javaClass)
                val credentials = InsecureChannelCredentials.create()
                channel = Grpc.newChannelBuilder(grpcConfig.host + ":" + grpcConfig.port, credentials).build()
                println("[GRPC] Запускаем канал связи с ядром")
            }
        }
    }
}