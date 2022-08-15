package eu.suro.api.config;

import java.util.Collection;

public class ConfigDescriptor {

    private final ConfigGroup group;
    private final Collection<ConfigSectionDescriptor> sections;
    private final Collection<ConfigTitleDescriptor> titles;
    private final Collection<ConfigItemDescriptor> items;

    public ConfigDescriptor(ConfigGroup group, Collection<ConfigSectionDescriptor> sections, Collection<ConfigTitleDescriptor> titles, Collection<ConfigItemDescriptor> items)
    {
        this.group = group;
        this.sections = sections;
        this.titles = titles;
        this.items = items;
    }
}
