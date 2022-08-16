package eu.suro.api.config;

import java.lang.reflect.Type;

public class ConfigItemDescriptor implements ConfigObject
{
    private final ConfigItem item;
    private final Type type;
    private final Range range;
    private final Alpha alpha;
    private final Units units;

    public ConfigItemDescriptor(ConfigItem item, Type type, Range range, Alpha alpha, Units units)
    {
        this.item = item;
        this.type = type;
        this.range = range;
        this.alpha = alpha;
        this.units = units;
    }

    @Override
    public String key()
    {
        return item.keyName();
    }

    @Override
    public String name()
    {
        return item.name();
    }


    public ConfigItem getItem() {
        return item;
    }
}