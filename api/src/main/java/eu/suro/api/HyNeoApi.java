package eu.suro.api;

import com.google.inject.Inject;
import com.google.inject.Injector;
import eu.suro.api.config.ConfigManager;
import eu.suro.api.path.Path;
import eu.suro.api.plugin.ExternalPluginManager;
import eu.suro.api.plugin.PluginManager;

import java.nio.file.Paths;

public class HyNeoApi {

    private static Injector injector;

    @Inject
    private ExternalPluginManager externalPluginManager;
    @Inject
    private PluginManager pluginManager;
    @Inject
    private ConfigManager configManager;

    public HyNeoApi(){

    }

    public HyNeoApi(Path path, Injector injector1){
        injector = injector1;
        injector.getInstance(HyNeoApi.class).start(path);
    }

    public void start(Path path){
        externalPluginManager.setupInstance(Paths.get(path.getDataFolder()+"/plugins"));
        externalPluginManager.startExtrenalPluginManager();
        externalPluginManager.loadPlugins();
        pluginManager.startPlugins();
    }

    public ExternalPluginManager getExternalPluginManager() {
        return externalPluginManager;
    }

    public static Injector getInjector() {
        return injector;
    }
}
