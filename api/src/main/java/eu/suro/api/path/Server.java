package eu.suro.api.path;


import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface Server<P> {

    File getDataFolder();

    boolean isProxy();

    <T> void RegisterListener(T listener);

    <T> void RegisterCommand(T command);

    void executeAsync(Runnable runnable);

    void executeAsyncAfter(Runnable runnable, TimeUnit timeUnit, int time);

    P getPlayer(String name);
}
