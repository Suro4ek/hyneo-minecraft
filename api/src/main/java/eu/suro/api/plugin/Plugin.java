package eu.suro.api.plugin;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.pf4j.ExtensionPoint;

public class Plugin implements Module, ExtensionPoint {

    protected Injector injector;

    @Override
    public void configure(Binder binder) {

    }

    protected void init() throws Exception
    {

    }

    protected void stop() throws Exception
    {

    }

    public Injector getInjector() {
        return injector;
    }

    public String getName()
    {
        return getClass().getAnnotation(PluginDescriptor.class).name();
    }
}
