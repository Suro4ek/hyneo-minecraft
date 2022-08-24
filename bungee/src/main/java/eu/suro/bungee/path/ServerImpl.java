package eu.suro.bungee.path;

import eu.suro.api.path.Server;
import eu.suro.bungee.ProxyMain;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Listener;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServerImpl implements Server {


    @Override
    public File getDataFolder() {
        return ProxyMain.getInstance().getDataFolder();
    }

    @Override
    public boolean isProxy() {
        return true;
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
    public void executeAsync(Runnable runnable) {
        ProxyMain.getInstance().getProxy().getScheduler().runAsync(
                ProxyMain.getInstance(), runnable
        );
    }

    @Override
    public void executeAsyncTimer(Runnable runnable, TimeUnit timeUnit, int i, int i1) {
        ProxyMain.getInstance().getProxy().getScheduler().schedule(
                ProxyMain.getInstance(), runnable, i, i1, timeUnit
        );
    }

    @Override
    public void executeAsyncAfter(Runnable runnable, TimeUnit timeUnit, int i) {
        ProxyMain.getInstance().getProxy().getScheduler().schedule(
                ProxyMain.getInstance(), runnable, i, timeUnit
        );
    }

    @Override
    public <T> T getPlayer(String s) {
        return (T) ProxyMain.getInstance().getProxy().getPlayer(s);
    }

    @Override
    public void callEvent(Object o) {
        ProxyMain.getInstance().getProxy().getPluginManager().callEvent((Event) o);
    }

    @Override
    public <T> Map<String, T> getServers() {
        return (Map<String, T>) ProxyMain.getInstance().getProxy().getServers();
    }
}
