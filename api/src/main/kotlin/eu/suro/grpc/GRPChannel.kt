package eu.suro.grpc

import de.exlll.configlib.YamlConfigurations
import io.grpc.Grpc
import io.grpc.InsecureChannelCredentials
import io.grpc.ManagedChannel
import java.io.File

class GRPChannel {
    /**
     * Создание канала для подключения к серверу
     * @param dataFolder - Папка с конфигом
     */
    companion object{
        var channel: ManagedChannel? = null
        private set

        @JvmStatic fun init(dataFolder: File){
            if(channel == null){
                val configFile = File(dataFolder, "config.yml")
                if(!configFile.exists()){
                    YamlConfigurations.save(configFile.toPath(), GRPCConfig::class.java, GRPCConfig())
                }
                val grpcConfig: GRPCConfig = YamlConfigurations.load(configFile.toPath(), GRPCConfig::class.java)
                val credentials = InsecureChannelCredentials.create()
                println(grpcConfig.host+":"+grpcConfig.port)
                channel = Grpc.newChannelBuilder(grpcConfig.host + ":" + grpcConfig.port, credentials).build()
                println(channel)
                println("[GRPC] Запускаем канал связи с ядром")
            }
        }
    }
}