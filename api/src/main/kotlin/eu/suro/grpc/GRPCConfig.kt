package eu.suro.grpc

import de.exlll.configlib.Comment
import de.exlll.configlib.Configuration

@Configuration
class GRPCConfig {

    @Comment("Адрес сервера ядра")
    var host: String = "localhost"

    @Comment("Порт сервера ядра")
    var port = 9000;
}