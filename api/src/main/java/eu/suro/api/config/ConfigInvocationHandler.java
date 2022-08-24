package eu.suro.api.config;

import eu.suro.api.util.ReflectUtil;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigInvocationHandler implements InvocationHandler {

    private final ConfigManager manager;
    protected static DumperOptions yamlOptions = new DumperOptions() {{
        setWidth(80 * 2);
        setDefaultFlowStyle(FlowStyle.BLOCK);
    }};

    protected static Yaml yaml = new Yaml(yamlOptions);

    private File file;
    private Map<String, Object> map;
    ConfigInvocationHandler(ConfigManager manager, File file)
    {
        this.manager = manager;
        this.file = file;
        createFile();
        loadFromFile();
    }

    private void loadFromFile() {
        String content = this.loadDataFromFile();
        this.loadFromString(content);
    }
    private String loadDataFromFile() {
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFromString(String data) {
        StringBuilder dataBuild = new StringBuilder();
        for (String line : data.split("\n")) {
            if (!line.isEmpty()) {
                dataBuild.append(line).append("\n");
            }
        }

        this.map = yaml.load(dataBuild.toString());
        if (map == null)
            map = new LinkedHashMap<>();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> iface = proxy.getClass().getInterfaces()[0];
        if("toString".equals(method.getName()) && args == null)
        {
            return iface.getSimpleName();
        }

        if("hashCode".equals(method.getName()) && args == null)
        {
            return System.identityHashCode(proxy);
        }

        if("equals".equals(method.getName()) && args != null && args.length == 1)
        {
            return proxy == args[0];
        }

        if("setIfNotExist".equals(method.getName()) && args != null && args.length == 2 && args[0] instanceof String && args[1] instanceof Object)
        {
            setIfNotExist((String) args[0], args[1]);
            return void.class;
        }

        ConfigGroup group = iface.getAnnotation(ConfigGroup.class);
        ConfigItem item = method.getAnnotation(ConfigItem.class);

        if(group == null)
        {
            System.out.println("group is null");
            //log warn
            return null;
        }

        if(item == null){
            System.out.println("item is null");
            //log warn
            return null;
        }

        if(args == null)
        {
            if(contains(ConfigManager.getWholeKey(null, item.keyName())))
            {
                try {
                    Object objectValue = stringToObject(getString(ConfigManager.getWholeKey(null, item.keyName())), method.getReturnType());
                    return objectValue;
                }catch (Exception e)
                {
                    System.out.println(e);
                    return null;
                }
            } else if(item.value() != null){
                setIfNotExist(ConfigManager.getWholeKey(null, item.keyName()), item.value());
                return item.value();
            }else {
                if (method.isDefault())
                {
                    Object defaultValue = callDefaultMethod(proxy, method, null);
                    return defaultValue;
                }
                return null;
            }
        }
        return null;
    }

    public void setAndSave(String path, Object o) {
        this.set(path, o);
        this.save();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createFile() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static Object callDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable
    {
        Class<?> declaringClass = method.getDeclaringClass();
        return ReflectUtil.privateLookupIn(declaringClass)
                .unreflectSpecial(method, declaringClass)
                .bindTo(proxy)
                .invokeWithArguments(args);
    }

    public void set(String path, Object o) {
        Map<String, Object> current = this.map;
        String[] data = path.split("\\.");
        for (int i = 0; i < data.length - 1; i++) {
            String next = data[i];
            Map<String, Object> old = current;
            current = (Map) current.get(next);
            if (current == null) {
                old.put(next, current = new LinkedHashMap<>());
            }
        }
        if (o != null) {
            current.put(data[data.length - 1], o);
        } else {
            current.remove(data[data.length - 1]);
        }
    }

    public void save() {
        if (file != null) {
            try {
                FileOutputStream stream = new FileOutputStream(file);
                stream.write(this.saveDataToString().getBytes());
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setIfNotExist(String path, Object o) {
        if (!this.contains(path)) {
            this.setAndSave(path, o);
        }
    }
    private String saveDataToString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.saveToString());
        return builder.toString();
    }

    public String saveToString() {
        return yaml.dumpAs(this.map, null, DumperOptions.FlowStyle.BLOCK);
    }

    public boolean contains(String path) {
        try {
            Object current = this.map;
            for (String next : path.split("\\.")) {
                current = ((Map) current).get(next);
            }
            return current != null;
        } catch (NullPointerException | ClassCastException e) {
            return false;
        }
    }

    public Object stringToObject(String str, Type type)
    {
        if (type == boolean.class || type == Boolean.class)
        {
            return Boolean.parseBoolean(str);
        }
        if (type == int.class || type == Integer.class)
        {
            return Integer.parseInt(str);
        }
        if (type == long.class || type == Long.class)
        {
            return Long.parseLong(str);
        }
        if (type == double.class || type == Double.class)
        {
            return Double.parseDouble(str);
        }
        if (type instanceof Class && ((Class<?>) type).isEnum())
        {
            return Enum.valueOf((Class<? extends Enum>) type, str);
        }
        if (type == Instant.class)
        {
            return Instant.parse(str);
        }
        if (type == Duration.class)
        {
            return Duration.ofMillis(Long.parseLong(str));
        }
        if (type == byte[].class)
        {
            return Base64.getUrlDecoder().decode(str);
        }
        if (type instanceof ParameterizedType)
        {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            System.out.println(parameterizedType.getRawType());
        }
        if(type == List.class)
        {
            return getList(str);
        }

        if(type == Section.class)
        {
            return getKeys(str);
        }

        return str;
    }

    public List<String> getStringList(String path) {
        final List<Object> list = this.getList(path);
        return list == null ? null : list.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    public List<Object> getList(String path) {
        return (List<Object>) this.get(path);
    }

    public Set<String> getKeys(String section) {
        final Map<String, Object> map = (Map) this.get(section);
        return map == null ? Collections.emptySet() : new LinkedHashSet<>(map.keySet());
    }

    public String objectToString(Object object)
    {
        if (object instanceof Enum)
        {
            return ((Enum) object).name();
        }
        if (object instanceof Instant)
        {
            return ((Instant) object).toString();
        }
        if (object instanceof Duration)
        {
            return Long.toString(((Duration) object).toMillis());
        }
        if (object instanceof byte[])
        {
            return Base64.getUrlEncoder().encodeToString((byte[]) object);
        }
        return object == null ? null : object.toString();
    }

    public String getString(String path) {
        Object o = this.get(path);
        return o == null ? null : o.toString();
    }

    public Object get(String path) {
        Object current = this.map;
        for (String next : path.split("\\.")) {
            current = ((Map) current).get(next);
            if (current == null) {
                return null;
            }
        }
        return current;
    }
}
