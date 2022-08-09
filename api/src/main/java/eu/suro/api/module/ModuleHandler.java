package eu.suro.api.module;

import eu.suro.api.path.Path;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

import java.nio.file.Paths;
import java.util.List;

public class ModuleHandler {

    private PluginManager manager;
    private List<Module> moduleList;

    public ModuleHandler(Path path) {
        manager = new DefaultPluginManager(Paths.get(path.getDataFolder()+"/modules"));
        manager.loadPlugins();
        manager.startPlugins();
        moduleList = manager.getExtensions(Module.class);
        for(Module module : moduleList){
            module.registerListeners();
            module.registerCommands();
        }
    }
}
