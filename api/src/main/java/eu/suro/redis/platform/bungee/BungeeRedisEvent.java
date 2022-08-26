package eu.suro.redis.platform.bungee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Event;

/*
 * Реализация ивента для получения данных из Redis
 */
@AllArgsConstructor
public class BungeeRedisEvent extends Event {

    @Getter
    private String channel;
    @Getter
    private String message;
}
