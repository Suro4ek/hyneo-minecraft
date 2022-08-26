package eu.suro.grpc;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.Data;
import lombok.Getter;

@Configuration
@Data
public class GRPCConfig {

    @Comment("Адрес сервера ядра")
    private String host = "localhost";
    @Comment("Порт сервера ядра")
    private int port = 9000;
}
