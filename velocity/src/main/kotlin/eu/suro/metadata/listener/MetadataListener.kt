package eu.suro.metadata.listener

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import eu.suro.metadata.StandardMetadataRegistries

class MetadataListener {
    @Subscribe
    fun on(e: DisconnectEvent) {
        StandardMetadataRegistries.USER_METADATA_REGISTRY.remove(e.player.username.lowercase())
    }
}
