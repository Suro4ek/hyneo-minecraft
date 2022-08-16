package eu.suro.api.config;


public class ConfigSectionDescriptor implements ConfigObject{

    private final String key;
    private final ConfigSection section;

    public ConfigSectionDescriptor(String key, ConfigSection section)
    {
        this.key = key;
        this.section = section;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public String name() {
        return section.name();
    }


    public ConfigSection getSection() {
        return section;
    }
}
