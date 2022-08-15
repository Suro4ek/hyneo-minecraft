package eu.suro.api.config;

import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import eu.suro.api.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Singleton
public class ConfigManager {

    private final Map<String, Consumer<? super Plugin>> consumers = new HashMap<>();

    @Inject
    public ConfigManager(){

    }

    public void load(){

    }

    public void loadFromFile(){
        consumers.clear();

    }

    public ConfigDescriptor getConfigDescriptor(Config configurationProxy)
    {
        Class<?> inter = configurationProxy.getClass().getInterfaces()[0];
        ConfigGroup group = inter.getAnnotation(ConfigGroup.class);

        if (group == null)
        {
            throw new IllegalArgumentException("Not config group");
        }

        final List<ConfigSectionDescriptor> sections = getAllDeclaredInterfaceFields(inter)
                .stream().filter(m -> m.isAnnotationPresent(ConfigSection.class) && m.getType() == String.class)
                .map(m ->
                {
                    try {
                        return new ConfigSectionDescriptor(
                                String.valueOf(m.get(inter)),
                                m.getDeclaredAnnotation(ConfigSection.class)
                        );
                    }catch (IllegalAccessException e){
                        //TODO log warn
                        return  null;
                    }
                })
                .filter(Objects::nonNull)
                .sorted((a,b) -> ComparisonChain.start()
                        .compare(a.getSection().position(), b.getSection().position())
                        .compare(a.getSection().name(), b.getSection().name())
                        .result())
                .collect(Collectors.toList());

        final List<ConfigTitleDescriptor> titles = getAllDeclaredInterfaceFields(inter)
                .stream().filter(m -> m.isAnnotationPresent(ConfigTitle.class) && m.getType() == String.class)
                .map(m -> {
                    try {
                        return new ConfigTitleDescriptor(String.valueOf(m.get(inter)),
                                m.getDeclaredAnnotation(ConfigTitle.class));
                    }catch (IllegalAccessException e){
                        //TODO log warn
                        return  null;
                    }
                })
                .filter(Objects::nonNull)
                .sorted((a,b) -> ComparisonChain.start()
                        .compare(a.getTitle().position(), b.getTitle().position())
                        .compare(a.getTitle().name(), b.getTitle().name())
                        .result())
                .collect(Collectors.toList());

        final List<ConfigItemDescriptor> items = Arrays.stream(inter.getMethods())
                .filter(m -> m.getParameterCount() == 0 && m.isAnnotationPresent(ConfigItem.class))
                .map(m -> new ConfigItemDescriptor(
                        m.getDeclaredAnnotation(ConfigItem.class),
                        m.getGenericReturnType(),
                        m.getDeclaredAnnotation(Range.class),
                        m.getDeclaredAnnotation(Alpha.class),
                        m.getDeclaredAnnotation(Units.class)
                ))
                .sorted((a, b) -> ComparisonChain.start()
                        .compare(a.getItem().position(), b.getItem().position())
                        .compare(a.getItem().name(), b.getItem().name())
                        .result())
                .collect(Collectors.toList());
        return new ConfigDescriptor(group, sections, titles, items);
    }

    public void setDefaultConfiguration(Objects proxy, boolean override)
    {
        Class<?> clazz = proxy.getClass().getInterfaces()[0];
        ConfigGroup group = clazz.getAnnotation(ConfigGroup.class);

        if (group == null)
        {
            throw new IllegalArgumentException("Not config group");
        }

        for (Method method : getAllDeclaredInterfaceMethods(clazz))
        {
            ConfigItem item = method.getAnnotation(ConfigItem.class);

            if (item == null || method.getParameterCount() != 0)
            {
                continue;
            }

            if(method.getReturnType().isAssignableFrom(Consumer.class))
            {
                Object defaultValue;
                try {
                    defaultValue =
                }
            }
        }
    }

    public <T> T getConfiguration(String groupName, String key, Type clazz)
    {
        return getConfiguration(groupName, null, key, clazz);
    }

    public <T> T getConfiguration(String groupName, String profile, String key, Type type)
    {
        String value = getConfiguration(groupName, profile, key);
        if (!Strings.isNullOrEmpty(value))
        {
            try
            {
                return (T) stringToObject(value, type);
            }
            catch (Exception e)
            {
                log.warn("Unable to unmarshal {} ", getWholeKey(groupName, profile, key), e);
            }
        }
        return null;
    }

    public String getConfiguration(String groupName, String profile, String key)
    {
        return
    }

    private Collection<Method> getAllDeclaredInterfaceMethods(Class<?> clazz)
    {
        Collection<Method> methods = new HashSet<>();
        Stack<Class<?>> interfaces = new Stack<>();
        interfaces.push(clazz);

        while (!interfaces.isEmpty())
        {
            Class<?> interfaze = interfaces.pop();
            Collections.addAll(methods, interfaze.getDeclaredMethods());
            Collections.addAll(interfaces, interfaze.getInterfaces());
        }

        return methods;
    }


    private Collection<Field> getAllDeclaredInterfaceFields(Class<?> clazz)
    {
        Collection<Field> methods = new HashSet<>();
        Stack<Class<?>> interfaces = new Stack<>();
        interfaces.push(clazz);

        while (!interfaces.isEmpty())
        {
            Class<?> interfaze = interfaces.pop();
            Collections.addAll(methods, interfaze.getDeclaredFields());
            Collections.addAll(interfaces, interfaze.getInterfaces());
        }

        return methods;
    }
}
