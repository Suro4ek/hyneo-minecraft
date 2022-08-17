package eu.suro.api.plugin;

import com.google.common.base.Strings;
import com.google.common.graph.Graph;
import com.google.inject.*;
import com.google.inject.Module;
import eu.suro.api.HyNeoApi;
import eu.suro.api.config.Config;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Singleton
public class PluginManager {

    private final List<Plugin> plugins = new CopyOnWriteArrayList<>();
    private final List<Plugin> activePlugins = new CopyOnWriteArrayList<>();

    @Inject
    PluginManager(){

    }


    public Config getPluginConfigProxy(Plugin plugin)
    {
        try {
            Injector injector = plugin.getInjector();
            if (injector == null) {
                Module pluginModule = (Binder binder) ->
                {
                    binder.bind((Class<Plugin>) plugin.getClass()).toInstance(plugin);
                    binder.install(plugin);
                };
                Injector pluginInjector = HyNeoApi.getInjector().createChildInjector(pluginModule);
                pluginInjector.injectMembers(plugin);
                plugin.injector = pluginInjector;
                injector = pluginInjector;
            }
            for (Key<?> key : injector.getBindings().keySet()) {
                Class<?> type = key.getTypeLiteral().getRawType();
                if (Config.class.isAssignableFrom(type)) {
                    return (Config) injector.getInstance(key);
                }
            }
        }
        catch (ThreadDeath e)
        {
            throw e;
        }
        catch (Throwable e)
        {
            //log error
        }
        return null;
    }

    public static <T> List<List<T>> topologicalGroupSort(Graph<T> graph)
    {
        final Set<T> root = graph.nodes().stream()
                .filter(node -> graph.inDegree(node) == 0)
                .collect(Collectors.toSet());
        final Map<T, Integer> dependencyCount = new HashMap<>();

        root.forEach(n -> dependencyCount.put(n, 0));
        root.forEach(n -> graph.successors(n)
                .forEach(m -> incrementChildren(graph, dependencyCount, m, dependencyCount.get(n) + 1)));

        // create list<list> dependency grouping
        final List<List<T>> dependencyGroups = new ArrayList<>();
        final int[] curGroup = {-1};

        dependencyCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry ->
                {
                    if (entry.getValue() != curGroup[0])
                    {
                        curGroup[0] = entry.getValue();
                        dependencyGroups.add(new ArrayList<>());
                    }
                    dependencyGroups.get(dependencyGroups.size() - 1).add(entry.getKey());
                });

        return dependencyGroups;
    }


    private static <T> void incrementChildren(Graph<T> graph, Map<T, Integer> dependencyCount, T n, int val)
    {
        if (!dependencyCount.containsKey(n) || dependencyCount.get(n) < val)
        {
            dependencyCount.put(n, val);
            graph.successors(n).forEach(m ->
                    incrementChildren(graph, dependencyCount, m, val + 1));
        }
    }

    public void startPlugins()
    {
        List<Plugin> scannedPlugins = new ArrayList<>(plugins);
        int loaded = 0;
        for (Plugin plugin : scannedPlugins)
        {
            try {
                startPlugin(plugin);
            }catch (PluginInstantiationException ex){
                //log error
                plugins.remove(plugin);
            }
            loaded++;
        }
    }


    public boolean startPlugin(Plugin plugin) throws PluginInstantiationException
    {
        List<Plugin> conficts = conflictsForPlugin(plugin);
        for(Plugin conflict : conficts)
        {
            if(activePlugins.contains(conflict)){
                stopPlugin(conflict);
            }
        }

        activePlugins.add(plugin);

        try {
            plugin.init();

            //log plugin init
        }
        catch (ThreadDeath e)
        {
            throw e;
        }
        catch (Throwable ex)
        {
            //log error
            throw new PluginInstantiationException(ex);
        }
        return true;
    }

    public boolean stopPlugin(Plugin plugin) throws PluginInstantiationException
    {
        if(!activePlugins.remove(plugin))
        {
            return false;
        }

        try {
            plugin.stop();
            //log plugin stop
        }
        catch (ThreadDeath e)
        {
            throw e;
        }
        catch (Throwable ex)
        {
            //log error
            throw new PluginInstantiationException(ex);
        }
        return true;
    }

    public boolean isPluginEnabled(Plugin plugin)
    {
        final PluginDescriptor pluginDescriptor = plugin.getClass().getAnnotation(PluginDescriptor.class);
        final String keyName = Strings.isNullOrEmpty(pluginDescriptor.configName()) ? plugin.getClass().getSimpleName() : pluginDescriptor.configName();
        return pluginDescriptor.enabledByDefault();
    }

    public List<Plugin> conflictsForPlugin(Plugin plugin)
    {
        Set<String> conflicts;
        {
            PluginDescriptor desc = plugin.getClass().getAnnotation(PluginDescriptor.class);
            conflicts = new HashSet<>(Arrays.asList(desc.conflicts()));
            conflicts.add(desc.name());
        }

        return plugins.stream()
                .filter(p ->
                {
                    if (p == plugin)
                    {
                        return false;
                    }

                    PluginDescriptor desc = p.getClass().getAnnotation(PluginDescriptor.class);
                    if(conflicts.contains(desc.name()))
                    {
                        return true;
                    }

                    for(String conflict : desc.conflicts())
                    {
                        if(conflict.contains(conflict))
                        {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public void add(Plugin plugin)
    {
        plugins.add(plugin);
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }
}
