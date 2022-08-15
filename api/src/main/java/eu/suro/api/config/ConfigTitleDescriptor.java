package eu.suro.api.config;

public class ConfigTitleDescriptor implements ConfigObject
{
    private final String key;
    private final ConfigTitle title;

    public ConfigTitleDescriptor(String key, ConfigTitle title)
    {
        this.key = key;
        this.title = title;
    }

    @Override
    public String key()
    {
        return key;
    }

    @Override
    public String name()
    {
        return title.name();
    }

    @Override
    public int position()
    {
        return title.position();
    }

    public ConfigTitle getTitle() {
        return title;
    }
}