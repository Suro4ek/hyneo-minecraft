package eu.suro.bungee;

import eu.suro.api.config.Config;
import eu.suro.api.config.ConfigGroup;
import eu.suro.api.config.ConfigItem;

@ConfigGroup
public interface BungeeConfig extends Config {

    @ConfigItem(
            keyName = "grpc.host",
            value = "127.0.0.1",
            description = ""
    )
    String grpcHost();

    @ConfigItem(
            keyName = "grpc.port",
            value = "50051",
            description = ""
    )
    String grpcPort();
}
