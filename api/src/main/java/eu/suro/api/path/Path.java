package eu.suro.api.path;

import io.grpc.ManagedChannel;

import java.io.File;

public interface Path {

    File getDataFolder();

    boolean isProxy();

    ManagedChannel getChannel();
}
