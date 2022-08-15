package eu.suro.api.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConfigItem {

    int position() default -1;

    String keyName();
    String name();
    String description ();

    Class <? extends Enum> enumClass() default Enum.class;
}
