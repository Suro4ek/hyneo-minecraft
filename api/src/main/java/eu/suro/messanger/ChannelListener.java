package eu.suro.messanger;

import java.io.IOException;

public interface ChannelListener {
    void onPluginMessageReceived(MessageData data) throws IOException, ClassNotFoundException;
}
