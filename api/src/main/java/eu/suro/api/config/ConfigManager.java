package eu.suro.api.config;

import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import eu.suro.api.path.Path;
import eu.suro.api.plugin.Plugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Singleton
public class ConfigManager {
    @Inject
    Path path;

    @Inject
    public ConfigManager(){

    }

    public static String getWholeKey(String profile, String key)
    {
        if (profile == null)
        {
            return key;
        }else{
            return  profile + "." + key;
        }
    }


    public <T extends Config> T getConfig(Class<T> clazz)
    {
        if(!Modifier.isPublic(clazz.getModifiers()))
        {
            throw new IllegalArgumentException("Class must be public");
        }

        T t = (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                new ConfigInvocationHandler(this,
                        new File(path.getDataFolder(), "configs/"+generateNameModule(clazz) + ".yml")));
        return t;
    }



    public static String generateNameModule(Class classModule) {
        String name = classModule.getSimpleName();
        if (StringUtils.startsWithIgnoreCase(name, "Main")) {
            name = name.substring(4);
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Нельзя называть класс модуля " + classModule.getName() + ", выберите уникальное имя");
        }
        final char[] chars = name.toCharArray();
        for (int i = 0; i < chars.length && Character.isUpperCase(chars[i]) && (i == 0 || i == chars.length - 1 || Character.isUpperCase(chars[i + 1])); i++) {
            chars[i] = Character.toLowerCase(chars[i]);
        }
        name = new String(chars);
        return name;
    }
}
