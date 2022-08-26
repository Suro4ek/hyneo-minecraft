package eu.suro.redis.platform.bukkit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/*
    * Реализация ивента для получения данных из Redis
 */
@AllArgsConstructor
@Getter
public class BukkitRedisEvent extends Event {

    private String channel;
    private String message;

    private static final HandlerList handlers = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
