package eu.suro.api.module;

import com.electronwill.nightconfig.core.file.FileConfig;
import eu.suro.api.path.Path;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

import java.nio.file.Paths;
import java.util.List;

public class ModuleHandler {

    private PluginManager manager;
    private List<Module> moduleList;

    public ModuleHandler(Path path) {
        manager = new DefaultPluginManager(Paths.get(path.getDataFolder()+"/plugins"));
        manager.loadPlugins();
        manager.startPlugins();
        moduleList = manager.getExtensions(Module.class);
        for(Module module : moduleList){
            java.nio.file.Path plugins = Paths.get(path.getDataFolder()+"/configs/"+module.getName());
            if(!plugins.toFile().exists()){
                plugins.toFile().mkdirs();
            }
            module.setChannel(path.getChannel());

            if(module.configExists()){
                FileConfig config = FileConfig.of(plugins.resolve("config.yml"));
                if(!config.getFile().exists()){
                    config.save();
                }
                config.load();
                module.setConfig(config);
            }
            if(path.isProxy()){
                module.initProxy();
            }else{
                module.initBukkit();
            }
        }
    }

    public PluginManager getManager() {
        return manager;
    }
}
