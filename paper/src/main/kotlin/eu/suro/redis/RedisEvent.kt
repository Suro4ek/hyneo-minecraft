package eu.suro.redis

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/*
   * Реализация ивента для получения данных из Redis
*/
data class RedisEvent constructor(val channel: String, val message: String): Event() {
    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }

    companion object {
        private val handlers = HandlerList()
    }
}