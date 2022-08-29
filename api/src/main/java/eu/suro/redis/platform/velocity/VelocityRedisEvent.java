package eu.suro.redis.platform.velocity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VelocityRedisEvent {

    private String channel;
    private String message;
}
