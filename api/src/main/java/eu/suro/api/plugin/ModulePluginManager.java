package eu.suro.api.plugin;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.pf4j.*;
import org.pf4j.PluginDescriptor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ModulePluginManager extends DefaultPluginManager {

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
            for (PluginDependency dependency : plugin.getDescriptor().getDependencies())
            {
                reverseDepMap.put(dependency.getPluginId(), plugin.getPluginId());
            }
        }

        for (PluginWrapper plugin : resolvedPlugins)
        {
            descriptors.add(plugin.getDescriptor());
            for (PluginDependency dependency : plugin.getDescriptor().getDependencies())
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


}
