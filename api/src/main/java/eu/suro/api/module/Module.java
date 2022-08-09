package eu.suro.api.module;

import org.pf4j.ExtensionPoint;

public interface Module extends ExtensionPoint {

    void registerListeners();
    void registerCommands();

}
