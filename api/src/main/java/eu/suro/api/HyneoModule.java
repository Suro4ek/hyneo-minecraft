package eu.suro.api;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import eu.suro.api.config.ConfigManager;
import eu.suro.api.path.Server;
import eu.suro.api.plugin.PluginManager;

import java.util.concurrent.*;

public class HyneoModule extends AbstractModule {

    private Server path;

    public HyneoModule(Server path){
        this.path = path;
    }

    @Override
    protected void configure() {
        bind(ConfigManager.class);
        bind(PluginManager.class);
        bind(Server.class).toInstance(path);
    }

    @Provides
    ExecutorService provideExecutorService()
    {
        int poolSize = 2 * Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(poolSize, poolSize,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder().setNameFormat("worker-%d").build());
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
}
