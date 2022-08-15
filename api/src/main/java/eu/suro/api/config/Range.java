package eu.suro.api.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Documented
public @interface Range
{
    int min() default 0;

    int max() default Integer.MAX_VALUE;
}