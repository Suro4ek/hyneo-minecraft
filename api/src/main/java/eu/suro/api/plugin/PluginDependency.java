package eu.suro.api.plugin;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(PluginDependencies.class)
public @interface PluginDependency
{
    Class<? extends Plugin> value();
}