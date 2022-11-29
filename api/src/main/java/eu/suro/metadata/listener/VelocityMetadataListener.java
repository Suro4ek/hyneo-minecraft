package eu.suro.metadata.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import eu.suro.api.user.IUser;
import eu.suro.metadata.StandardMetadataRegistries;

import java.util.Locale;

public class VelocityMetadataListener {

    @Subscribe
    public void on(DisconnectEvent e){
        StandardMetadataRegistries.USER_METADATA_REGISTRY.remove(e.getPlayer().getUsername().toLowerCase(Locale.ROOT));
    }
}
