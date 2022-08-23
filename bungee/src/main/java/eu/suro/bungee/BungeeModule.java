package eu.suro.bungee;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import eu.suro.api.config.ConfigManager;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class BungeeModule extends AbstractModule {

    @Provides
    Plugin getPlugin(){
        return ProxyMain.getInstance();
    }

    @Provides
    BungeeConfig getBungeeConfig(ConfigManager configManager){
        return configManager.getConfig(BungeeConfig.class);
    }

    @Provides
    ManagedChannel getManagedChannel(){
        return ProxyMain.getChannel();
    }
}
