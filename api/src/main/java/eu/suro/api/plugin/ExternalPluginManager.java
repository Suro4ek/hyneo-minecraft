package eu.suro.api.plugin;

import com.google.common.collect.Lists;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import com.google.inject.*;
import com.google.inject.Module;
import eu.suro.api.HyNeoApi;
import eu.suro.api.config.Config;
import org.pf4j.PluginDependency;
import org.pf4j.PluginManager;
import org.pf4j.PluginRuntimeException;
import org.pf4j.PluginWrapper;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Singleton
public class ExternalPluginManager {

    public static ArrayList<ClassLoader> pluginClassLoaders = new ArrayList<ClassLoader>();

    @Inject
    private eu.suro.api.plugin.PluginManager pluginManager;

    private PluginManager externalPluginManager;

    @Inject
    private ExecutorService executorService;

    private final Map<String, String> pluginsMap = new HashMap<>();

    private final Map<String, Map<String, String>> pluginsInfoMap = new HashMap<>();

    public void setupInstance(Path path)
    {
        initPluginManager(path);
    }

    public void initPluginManager(Path path)
    {
        externalPluginManager = new ModulePluginManager(path);
    }

    public void startExtrenalPluginManager()
    {
        externalPluginManager.loadPlugins();
    }

    public void loadPlugins()
    {
        System.out.println("Loading plugins...");
        externalPluginManager.startPlugins();
        System.out.println("Loaded plugins : " + externalPluginManager.getPlugins().size());
        List<PluginWrapper> startedPlugins = getStartedPlugins();
        List<Plugin> scannedPlugins = new ArrayList<>();
        for (PluginWrapper plugin : startedPlugins)
        {
            checkDepsAndStart(startedPlugins, scannedPlugins, plugin);
        }
        System.out.println("Loaded plugins2 : " + scannedPlugins.size());

        scanAndInstantiate(scannedPlugins, false, false);
    }

    private void scanAndInstantiate(List<Plugin> plugins, boolean init, boolean initConfig)
    {
        MutableGraph<Class<? extends Plugin>> graph = GraphBuilder.directed().build();

        for(Plugin plugin : plugins)
        {
            Class<? extends Plugin> clazz = plugin.getClass();
            PluginDescriptor pluginDescriptor = clazz.getAnnotation(PluginDescriptor.class);

            try {
                if(pluginDescriptor == null)
                {
                    if(Plugin.class.isAssignableFrom(clazz))
                    {
                        //log warn
                    }
                    continue;
                }else if(!Plugin.class.isAssignableFrom(clazz))
                {
                    //log warn
                    continue;
                }
            }catch (EnumConstantNotPresentException e)
            {
                //log warn
                continue;
            }

            Class<Plugin> pluginClass = (Class<Plugin>) clazz;
            graph.addNode(pluginClass);
        }

        for (Class<? extends Plugin> pluginClazz : graph.nodes())
        {
            eu.suro.api.plugin.PluginDependency[] pluginDependencies = pluginClazz.getAnnotationsByType(eu.suro.api.plugin.PluginDependency.class);
            for (eu.suro.api.plugin.PluginDependency pluginDependency : pluginDependencies)
            {
                if(graph.nodes().contains(pluginDependency.value()))
                {
                    graph.putEdge(pluginClazz, pluginDependency.value());
                }
            }
        }

        if (Graphs.hasCycle(graph))
        {
            throw new RuntimeException("Cycle detected in plugin dependencies");
        }

        List<List<Class<? extends Plugin>>> sortedPlugins = eu.suro.api.plugin.PluginManager.topologicalGroupSort(graph);
        sortedPlugins = Lists.reverse(sortedPlugins);
        AtomicInteger loaded = new AtomicInteger();

        final long start = System.currentTimeMillis();

        List<Plugin> scannedPlugins = new CopyOnWriteArrayList<>();

        sortedPlugins.forEach(group -> {
            List<Future<?>> curGroup = new ArrayList<>();
            group.forEach(pluginClazz ->
                    curGroup.add(executorService.submit(() -> {
                        Plugin plugininst;
                        try {
                            System.out.println("Loading plugin " + pluginClazz.getName());
                            plugininst = instantiate(scannedPlugins, (Class<Plugin>) pluginClazz, init, initConfig);
                            if (plugininst == null)
                            {
                                return;
                            }
                            scannedPlugins.add(plugininst);
                        }catch (PluginInstantiationException e)
                        {

                            System.out.println("Failed to load plugin " +e);
                            //log warn
                            return;
                        }
                        loaded.getAndIncrement();
                    })));
            curGroup.forEach(future -> {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    //log warn
                }
            });
        });
        System.out.println("Loaded plugins : " + scannedPlugins.size());

        //log info loaded plugin and ms
        System.out.println("Loaded plugins " + (System.currentTimeMillis() - start) + "ms");
    }

    private Plugin instantiate(List<Plugin> scannedPlugins, Class<Plugin> clazz, boolean init, boolean initConfig) throws PluginInstantiationException {
        eu.suro.api.plugin.PluginDependency[] pluginDependencies = clazz.getAnnotationsByType(eu.suro.api.plugin.PluginDependency.class);
        List<Plugin> deps = new ArrayList<>();

        for(eu.suro.api.plugin.PluginDependency pluginDependency : pluginDependencies)
        {
            Optional<Plugin> dependecy = Stream.concat(pluginManager.getPlugins().stream(),
                    scannedPlugins.stream().filter(p -> p.getClass() == pluginDependency.value())).findFirst();
            if(!dependecy.isPresent())
            {
                throw  new PluginRuntimeException(
                        "Plugin " + clazz.getName() + " depends on " + pluginDependency.value().getName() + " but it is not loaded");
            }
            deps.add(dependecy.get());
        }

        //log loading plugin
        Plugin plugin;
        try {
            plugin = clazz.getDeclaredConstructor().newInstance();
        }
        catch (ThreadDeath e)
        {
            throw e;
        }
        catch (Throwable ex)
        {
            throw new PluginInstantiationException(ex);
        }
        System.out.println("LOAD plugin " + clazz.getName());
        try {
            Injector parent = HyNeoApi.getInjector();
            if(deps.size() > 1)
            {
                System.out.println("Loaded plugin " + plugin.getClass().getName());
                List<Module> modules = new ArrayList<>(deps.size());
                for (Plugin p : deps)
                {
                    Module module = (Binder binder) ->
                    {
                        binder.bind((Class<Plugin>) p.getClass()).toInstance(p);
                        binder.install(p);
                    };
                    modules.add(module);
                }

                parent = parent.createChildInjector(modules);
            }else if(!deps.isEmpty())
            {
                System.out.println("Loaded plugin2 " + plugin.getClass().getName());
                parent = deps.get(0).getInjector();
            }
            System.out.println("Loaded plugin3 " + plugin.getClass().getName());
            Module pluginModule = (Binder binder) ->
            {
                binder.bind(clazz).toInstance(plugin);
                binder.install(plugin);
            };
            Injector pluginInjector = parent.createChildInjector(pluginModule);
            pluginInjector.injectMembers(plugin);
            plugin.injector = pluginInjector;

//            if(initConfig)
//            {
//                for (Key<?>key : pluginInjector.getBindings().keySet())
//                {
//                    Class<?> type = key.getTypeLiteral().getRawType();
//                    if(Config.class.isAssignableFrom(type))
//                    {
//                        Config config = (Config) pluginInjector.getInstance(key);
//                        config.save();
//                    }
//                }
//            }

            if(init)
            {
//                try {
//                    pluginManager.
//                }
            }

            pluginManager.add(plugin);
        }
        catch (CreationException ex)
        {
            throw new PluginInstantiationException(ex);
        }
        catch (NoClassDefFoundError | NoSuchFieldError | NoSuchMethodError ex)
        {
            System.out.println("Failed to load plugin " + plugin.getClass().getName());
            //log error
            return null;
        }
        //log loaded plugin {}
        return plugin;
    }

    private void checkDepsAndStart(List<PluginWrapper> startedPlugins, List<Plugin> scannedPlugins, PluginWrapper pluginWrapper)
    {
//        boolean depsLoaded = true;
//        for(PluginDependency dependency : pluginWrapper.getDescriptor().getDependencies())
//        {
//            if(startedPlugins.stream().noneMatch(pl -> pl.getPluginId().equals(dependency.getPluginId())))
//            {
//                System.out.println(startedPlugins.stream().noneMatch(pl -> pl.getPluginId().equals(dependency.getPluginId())));
//                depsLoaded = false;
//            }
//        }
//
//        if (!depsLoaded)
//        {
//            return;
//        }

        scannedPlugins.addAll(loadPlugin(pluginWrapper.getPluginId()));
    }

    private List<Plugin> loadPlugin(String pluginId)
    {
        System.out.println("Load plugin " + pluginId);
        List<Plugin> scannedPlugins = new ArrayList<>();
        try {
            List<Plugin> extensions = externalPluginManager.getExtensions(Plugin.class, pluginId);
            System.out.println("Loaded extensions " + extensions.size());
            for(Plugin plugin : extensions)
            {

                pluginClassLoaders.add(plugin.getClass().getClassLoader());

                pluginsMap.remove(plugin.getClass().getSimpleName());
                pluginsMap.put(plugin.getClass().getSimpleName(), pluginId);

                pluginsInfoMap.remove(plugin.getClass().getSimpleName());

                pluginsInfoMap.put(
                        plugin.getClass().getSimpleName(),
                        new HashMap<String, String>()
                        {{
                            put("version", externalPluginManager.getPlugin(pluginId).getDescriptor().getVersion());
                            put("id", externalPluginManager.getPlugin(pluginId).getDescriptor().getPluginId());
                            put("provider", externalPluginManager.getPlugin(pluginId).getDescriptor().getProvider());
                        }}
                );

                scannedPlugins.add(plugin);
            }
        }
        catch (Throwable ex)
        {
            System.out.println("Error loading plugin " + pluginId);
        }
        return scannedPlugins;
    }

    public List<PluginWrapper> getStartedPlugins(){
        return externalPluginManager.getStartedPlugins();
    }
}
