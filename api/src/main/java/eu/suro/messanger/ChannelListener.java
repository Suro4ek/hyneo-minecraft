package eu.suro.messanger;

import java.io.IOException;

public interface ChannelListener<T> {
    void onPluginMessageReceived(T data) throws IOException, ClassNotFoundException;
}
