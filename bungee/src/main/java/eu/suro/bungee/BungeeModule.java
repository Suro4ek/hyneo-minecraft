package eu.suro.bungee;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import eu.suro.api.config.ConfigManager;

public class BungeeModule extends AbstractModule {

    @Provides
    BungeeConfig getBungeeConfig(ConfigManager configManager){
        return configManager.getConfig(BungeeConfig.class);
    }
}
