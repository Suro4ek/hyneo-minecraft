package eu.suro.auth;

import eu.suro.api.config.Config;
import eu.suro.api.config.ConfigGroup;
import eu.suro.api.config.ConfigItem;

import java.util.List;

@ConfigGroup()
public interface AuthConfig extends Config {

    @ConfigItem(
            keyName = "auths",
            name = "",
            description = ""
    )
    List<String> authServers();


    @ConfigItem(
            keyName = "lobby",
            name = "",
            description = ""
    )
    List<String> lobbyServers();

}
