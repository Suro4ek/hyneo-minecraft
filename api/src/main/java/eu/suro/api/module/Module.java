package eu.suro.api.module;

import com.electronwill.nightconfig.core.file.FileConfig;
import eu.suro.api.path.Path;
import io.grpc.ManagedChannel;
import org.pf4j.ExtensionPoint;
import java.util.logging.Logger;

public abstract class Module implements ExtensionPoint {

    public FileConfig config;

    private Path path;

    protected ManagedChannel channel;

    protected void setPath(Path path) {
        this.path = path;
    }

    public void setConfig(FileConfig config) {
        this.config = config;
    }

    public FileConfig getConfig() {
        return config;
    }

    public ManagedChannel getChannel() {
        return channel;
    }

    public Logger getLogger(){
        return this.path.getLogger();
    }

    public void setChannel(ManagedChannel channel) {
        this.channel = channel;
    }

    public void RegisterCommand(Class<?> command) {
        path.RegisterCommand(command);
    }

    public <T> void RegisterListener(T listener) {
        path.RegisterListener(listener);
    }

    public abstract String getName();
    public boolean configExists() {
        return false;
    }
    public abstract void initBukkit();

    public abstract void initProxy();
}