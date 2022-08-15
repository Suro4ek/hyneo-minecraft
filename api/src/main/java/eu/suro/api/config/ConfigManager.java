package eu.suro.api.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import eu.suro.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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
}
