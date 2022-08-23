package eu.suro.api.plugin;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import eu.suro.api.path.Bukkit;
import eu.suro.api.path.Bungee;
import eu.suro.api.path.Server;
import org.pf4j.ExtensionPoint;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Plugin implements Module, ExtensionPoint {

    protected Injector injector;

    @Inject
    private Server path;

    @Inject
    private Bungee bungee;

    @Inject
    private Bukkit bukkit;
    @Override
    public void configure(Binder binder) {}

    protected void init() throws Exception {}

    protected void stop() throws Exception {}
    public Injector getInjector() {
        return injector;
    }

    public <T> void RegisterCommand(T command){
        path.RegisterCommand(command);
    }

    public <T> void RegisterListener(T listener){
        path.RegisterListener(listener);
    }

    public void executeAsync(Runnable runnable){
        path.executeAsync(runnable);
    }

    public <T> Map<String, T> getServers(){
        return path.getServers();
    }

    public <T> T getPlayer(String username){
        return path.getPlayer(username);
    }

    public void executeAsyncAfter(Runnable runnable, TimeUnit timeUnit, int time){
        path.executeAsyncAfter(runnable, timeUnit, time);
    }

    public String getName()
    {
        return getClass().getAnnotation(PluginDescriptor.class).name();
    }
}
