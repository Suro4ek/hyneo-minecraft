package eu.suro.api.module;

import com.electronwill.nightconfig.core.file.FileConfig;
import io.grpc.ManagedChannel;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public abstract class Module implements ExtensionPoint {

    protected FileConfig config;

    protected ManagedChannel channel;

    public void setConfig(FileConfig config) {
        this.config = config;
    }

    public FileConfig getConfig() {
        return config;
    }

    public ManagedChannel getChannel() {
        return channel;
    }

    public void setChannel(ManagedChannel channel) {
        this.channel = channel;
    }

    public abstract String getName();
    public boolean configExists() {
        return false;
    }
    public abstract void initBukkit();

    public abstract void initProxy();
}