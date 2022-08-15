package eu.suro.api.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ConfigInvocationHandler implements InvocationHandler {

    private static final Object NULL = new Object();

    private final ConfigManager manager;
    private final Cache<Method, Object> cache = CacheBuilder.newBuilder()
            .maximumSize(256)
            .build();

    ConfigInvocationHandler(ConfigManager manager)
    {
        this.manager = manager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(args == null)
        {
            Object cachedValue = cache.getIfPresent(method);
            if(cachedValue != null)
            {
                return cachedValue == NULL ? null : cachedValue;
            }
        }

        Class<?> iface = proxy.getClass().getInterfaces()[0];
        if("toString".equals(method.getName()) && args == null)
        {
            return iface.getSimpleName();
        }

        if("hasCode".equals(method.getName()) && args == null)
        {
            return System.identityHashCode(proxy);
        }

        if("equals".equals(method.getName()) && args != null && args.length == 1)
        {
            return proxy == args[0];
        }

        ConfigGroup group = iface.getAnnotation(ConfigGroup.class);
        ConfigItem item = method.getAnnotation(ConfigItem.class);

        if(group == null)
        {
            //log warn
            return null;
        }

        if(item == null){
            //log warn
            return null;
        }

        if(args == null)
        {
            String value = manager.getConfiguration()
        }
    }
}
