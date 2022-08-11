package eu.suro.bungee.path;

import dev.rollczi.litecommands.bungee.LiteBungeeFactory;
import eu.suro.api.path.Path;
import eu.suro.bungee.ProxyMain;
import io.grpc.ManagedChannel;
import net.md_5.bungee.api.plugin.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PathImpl implements Path {


    @Override
    public File getDataFolder() {
        return ProxyMain.getInstance().getDataFolder();
    }

    @Override
    public boolean isProxy() {
        return true;
    }

    @Override
    public ManagedChannel getChannel() {
        return ProxyMain.getChannel();
    }

    @Override
    public <T> void RegisterListener(T t) {
        if (t instanceof Listener){
            ProxyMain.getInstance().getProxy().getPluginManager().registerListener(ProxyMain.getInstance(),
                    (Listener) t);
        }
    }

    @Override
    public <T> void RegisterCommand(T t) {
       if (t instanceof Class<?>){
           ProxyMain.getInstance().commands.add((Class<?>) t);
       }
    }

    @Override
    public Logger getLogger() {
        return ProxyMain.getInstance().getLogger();
    }
}
