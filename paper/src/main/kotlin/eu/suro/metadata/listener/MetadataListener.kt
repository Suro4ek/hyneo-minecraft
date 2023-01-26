package eu.suro.metadata.listener

import eu.suro.metadata.StandardMetadataRegistries
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class MetadataListener : Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    fun onQuit(e: PlayerQuitEvent) {
        StandardMetadataRegistries.USER_METADATA_REGISTRY.remove(e.player.name.lowercase())
    }
}
