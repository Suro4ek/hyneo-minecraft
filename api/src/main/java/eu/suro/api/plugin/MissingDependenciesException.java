package eu.suro.api.plugin;

import com.google.common.collect.Multimap;
import org.pf4j.DependencyResolver;

import java.util.List;

public class MissingDependenciesException extends DependencyResolver.DependenciesNotFoundException
{
    Multimap<String, String> reverseDependencyMap;

    public MissingDependenciesException(List<String> dependencies, Multimap<String, String> reverseDependencyMap)
    {
        super(dependencies);
        this.reverseDependencyMap = reverseDependencyMap;
    }
}
