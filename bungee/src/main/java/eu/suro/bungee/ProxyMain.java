package eu.suro.bungee;

import eu.suro.api.module.ModuleHandler;
import eu.suro.api.path.Path;
import eu.suro.bungee.path.PathImpl;
import net.md_5.bungee.api.plugin.Plugin;

public class ProxyMain extends Plugin {

    static ProxyMain instance;

    @Override
    public void onEnable() {
        instance = this;
        Path path = new PathImpl();
        new ModuleHandler(path);
    }

    @Override
    public void onDisable() {

    }

    public static ProxyMain getInstance() {
        return instance;
    }
}
