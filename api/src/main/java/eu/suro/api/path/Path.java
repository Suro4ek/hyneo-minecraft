package eu.suro.api.path;

import java.io.File;

public interface Path {

    File getDataFolder();

    boolean isProxy();
}
