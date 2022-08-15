package eu.suro.api.plugin;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.inject.Singleton;
import org.pf4j.*;
import org.pf4j.PluginDescriptor;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
@Singleton
public class ModulePluginManager extends DefaultPluginManager {

    private final Set<String> disabledPlugins = new HashSet<>();

    public ModulePluginManager(Path path)
    {
        super(path);
    }


    @Override
    public void loadPlugins() {
        for(Path path : pluginsRoots) {
            if (Files.notExists(path) || !Files.isDirectory(path)) {
                return;
            }
        }

        List<Path> pluginPaths = pluginRepository.getPluginPaths();
        Collections.reverse(pluginPaths);

        if(pluginPaths.isEmpty()){
            return;
        }

        Set<String> duplicatePlugins = new HashSet<>();
        for(Path pluginPath : pluginPaths){
            try {
                loadPluginFromPath(pluginPath);
            }catch (PluginRuntimeException e)
            {
                if(!(e instanceof PluginAlreadyLoadedException))
                {
                    String plugin = pluginPath.toString().substring(pluginsRoots.get(0).toString().length() + 1) ;
                    duplicatePlugins.add(plugin);
                    //TODO log cannot load plugin
                }
            }
        }

        if(!duplicatePlugins.isEmpty()){
            //TODO log duplicate plugin
        }

        try {
            resolvePlugins();
        }
        catch (PluginRuntimeException e)
        {
            if(e instanceof DependencyResolver.DependenciesNotFoundException)
            {
                throw e;
            }
            //TODO log cannot resolve
        }
    }

    @Override
    protected void resolvePlugins() {
        List<PluginDescriptor> descriptors = new ArrayList<>();
        Multimap<String, String > reverseDepMap = MultimapBuilder.hashKeys().hashSetValues().build();
        for (PluginWrapper plugin : plugins.values())
        {
            descriptors.add(plugin.getDescriptor());
            for (org.pf4j.PluginDependency dependency : plugin.getDescriptor().getDependencies())
            {
                reverseDepMap.put(dependency.getPluginId(), plugin.getPluginId());
            }
        }

        for (PluginWrapper plugin : resolvedPlugins)
        {
            descriptors.add(plugin.getDescriptor());
            for (org.pf4j.PluginDependency dependency : plugin.getDescriptor().getDependencies())
            {
                reverseDepMap.put(dependency.getPluginId(), plugin.getPluginId());
            }
        }
        DependencyResolver.Result result = dependencyResolver.resolve(descriptors);
        if (result.hasCyclicDependency())
        {
            throw new DependencyResolver.CyclicDependencyException();
        }

        List<String> notFoundDependencies = result.getNotFoundDependencies();
        if (!notFoundDependencies.isEmpty()){
            throw new MissingDependenciesException(notFoundDependencies, reverseDepMap);
        }

        List<DependencyResolver.WrongDependencyVersion> wrongVersionDependecies = result.getWrongVersionDependencies();
        if (!wrongVersionDependecies.isEmpty())
        {
            throw new DependencyResolver.DependenciesWrongVersionException(wrongVersionDependecies);
        }

        List<String> sortedPlugins = result.getSortedPlugins();

        for (String pluginId : sortedPlugins)
        {
            PluginWrapper pluginWrapper = plugins.get(pluginId);

            if(resolvedPlugins.contains(pluginWrapper))
            {
                continue;
            }

            if(unresolvedPlugins.remove(pluginWrapper))
            {
                PluginState pluginState = pluginWrapper.getPluginState();
                if (pluginState != PluginState.DISABLED)
                {
                    pluginWrapper.setPluginState(PluginState.RESOLVED);
                }

                resolvedPlugins.add(pluginWrapper);

                firePluginStateEvent(new PluginStateEvent(this, pluginWrapper, pluginState));
            }
        }
    }

    @Override
    public RuntimeMode getRuntimeMode() {
        return RuntimeMode.DEPLOYMENT;
    }

    @Override
    public PluginState stopPlugin(String pluginId) {
        if(!plugins.containsKey(pluginId))
        {
            throw new IllegalArgumentException("Plugin with id " + pluginId + " not found");
        }

        PluginWrapper pluginWrapper = getPlugin(pluginId);
        PluginDescriptor pluginDescriptor = pluginWrapper.getDescriptor();
        PluginState pluginState = pluginWrapper.getPluginState();
        if(pluginState == PluginState.STOPPED || pluginState == PluginState.DISABLED)
        {
            return pluginState;
        }

        pluginWrapper.getPlugin().stop();
        pluginWrapper.setPluginState(PluginState.STOPPED);
        firePluginStateEvent(new PluginStateEvent(this, pluginWrapper, pluginState));

        return pluginWrapper.getPluginState();
    }

    @Override
    public boolean unloadPlugin(String pluginId) {
        try
        {
            PluginState pluginState = stopPlugin(pluginId);
            if (PluginState.STARTED == pluginState)
            {
                return false;
            }

            PluginWrapper pluginWrapper = getPlugin(pluginId);

            plugins.remove(pluginId);
            getResolvedPlugins().remove(pluginWrapper);

            firePluginStateEvent(new PluginStateEvent(this, pluginWrapper, pluginState));

            Map<String, ClassLoader> pluginClassLoaders = getPluginClassLoaders();
            if (pluginClassLoaders.containsKey(pluginId))
            {
                ClassLoader classLoader = pluginClassLoaders.remove(pluginId);
                if (classLoader instanceof Closeable)
                {
                    try
                    {
                        ((Closeable) classLoader).close();
                    }
                    catch (IOException e)
                    {
                        throw new PluginRuntimeException(e, "Cannot close classloader");
                    }
                }
            }

            return true;
        }
        catch (IllegalArgumentException e)
        {

        }

        return false;
    }

    @Override
    public boolean deletePlugin(String pluginId)
    {
        if (!plugins.containsKey(pluginId))
        {
            throw new IllegalArgumentException(String.format("Unknown pluginId %s", pluginId));
        }

        PluginWrapper pluginWrapper = getPlugin(pluginId);
        PluginState pluginState = stopPlugin(pluginId);
        if (PluginState.STARTED == pluginState)
        {

            return false;
        }


        org.pf4j.Plugin plugin = pluginWrapper.getPlugin();

        if (!unloadPlugin(pluginId))
        {

            return false;
        }

        plugin.delete();

        Path pluginPath = pluginWrapper.getPluginPath();

        return pluginRepository.deletePluginPath(pluginPath);
    }

    @Override
    protected PluginWrapper loadPluginFromPath(Path pluginPath)
    {
        String pluginId = idForPath(pluginPath);
        if (pluginId != null)
        {
            throw new PluginAlreadyLoadedException(pluginId, pluginPath);
        }


        PluginDescriptorFinder pluginDescriptorFinder = getPluginDescriptorFinder();

        PluginDescriptor pluginDescriptor = pluginDescriptorFinder.find(pluginPath);
        validatePluginDescriptor(pluginDescriptor);


        if (disabledPlugins.contains(pluginDescriptor.getPluginId()))
        {

            return null;
        }

        pluginId = pluginDescriptor.getPluginId();
        if (plugins.containsKey(pluginId))
        {
            PluginWrapper loadedPlugin = getPlugin(pluginId);
            throw new PluginRuntimeException();
        }


        String pluginClassName = pluginDescriptor.getPluginClass();

        ClassLoader pluginClassLoader = getPluginLoader().loadPlugin(pluginPath, pluginDescriptor);

        PluginWrapper pluginWrapper = new PluginWrapper(this, pluginDescriptor, pluginPath, pluginClassLoader);
        pluginWrapper.setPluginFactory(getPluginFactory());

        if (isPluginDisabled(pluginDescriptor.getPluginId()))
        {
            pluginWrapper.setPluginState(PluginState.DISABLED);
        }

        if (!isPluginValid(pluginWrapper))
        {
            pluginWrapper.setPluginState(PluginState.DISABLED);
        }


        pluginId = pluginDescriptor.getPluginId();

        plugins.put(pluginId, pluginWrapper);
        getUnresolvedPlugins().add(pluginWrapper);

        getPluginClassLoaders().put(pluginId, pluginClassLoader);

        return pluginWrapper;
    }

    void disableLoading(String pluginId)
    {
        unloadPlugin(pluginId);
        disabledPlugins.add(pluginId);
    }

    private boolean isPluginEligibleForLoading(Path path)
    {
        return path.toFile().getName().endsWith(".jar");
    }
}
