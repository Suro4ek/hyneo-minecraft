package eu.suro.api.path;


import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface Server {

    File getDataFolder();

    boolean isProxy();

    <T> void RegisterListener(T listener);

    <T> void RegisterCommand(T command);

    void executeAsync(Runnable runnable);

    void executeAsyncAfter(Runnable runnable, TimeUnit timeUnit, int time);

    <T> T getPlayer(String name);

    <T>  Map<String, T> getServers();
}
