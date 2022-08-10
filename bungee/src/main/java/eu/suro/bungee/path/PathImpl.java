package eu.suro.bungee.path;

import eu.suro.api.path.Path;
import eu.suro.bungee.ProxyMain;
import io.grpc.ManagedChannel;

import java.io.File;

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
}
