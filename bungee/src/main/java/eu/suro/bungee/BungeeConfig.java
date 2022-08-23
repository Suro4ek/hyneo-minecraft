package eu.suro.bungee;

import eu.suro.api.config.Config;
import eu.suro.api.config.ConfigGroup;
import eu.suro.api.config.ConfigItem;

@ConfigGroup
public interface BungeeConfig extends Config {

    @ConfigItem(
            keyName = "grpc.host",
            description = ""
    )
    String grpcHost();

    @ConfigItem(
            keyName = "grpc.port",
            description = ""
    )
    String grpcPort();
}
