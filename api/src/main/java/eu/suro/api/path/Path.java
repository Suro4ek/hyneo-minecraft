package eu.suro.api.path;


import java.io.File;

public interface Path {

    File getDataFolder();

    boolean isProxy();

    <T> void RegisterListener(T listener);

    <T> void RegisterCommand(T command);
}
