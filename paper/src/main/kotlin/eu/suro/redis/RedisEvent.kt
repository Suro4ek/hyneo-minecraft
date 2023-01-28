package eu.suro.redis

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/*
   * Реализация ивента для получения данных из Redis
*/
data class RedisEvent<T> constructor(val channel: String, val obj: T): Event() {
    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }

    companion object {
        private val handlers = HandlerList()
    }
}