package eu.suro.messanger.listener;

import com.velocitypowered.api.event.Subscribe;
import eu.suro.messanger.MessangerInit;
import eu.suro.redis.platform.velocity.VelocityRedisEvent;

public class VelocityMessangerListener {

    @Subscribe
    public void on(VelocityRedisEvent e){
        MessangerInit.onPluginMessageReceived(e.getMessage());
    }
}
