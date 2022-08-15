package eu.suro.api.plugin;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface PluginDescriptor {

    String name();

    String description() default "";

    String configName() default "";

    String[] conflicts() default {};

    boolean enabledByDefault() default true;

}
