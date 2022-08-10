package eu.suro.api.module;

import com.electronwill.nightconfig.core.file.FileConfig;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public abstract class Module implements ExtensionPoint {

    protected FileConfig config;

    public void setConfig(FileConfig config) {
        this.config = config;
    }

    public FileConfig getConfig() {
        return config;
    }

    public abstract String getName();
    public boolean configExists() {
        return false;
    }
    public abstract void initBukkit();

    public abstract void initProxy();
}