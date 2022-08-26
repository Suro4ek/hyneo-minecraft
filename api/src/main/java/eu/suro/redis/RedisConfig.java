package eu.suro.redis;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.Data;
import lombok.Getter;

@Configuration
@Data
public class RedisConfig {

    @Comment("Адрес сервера редиса")
    private String host = "localhost";
    @Comment("Порт сервера редиса")
    private int port = 6379;
    @Comment("Пароль сервера редиса")
    private String password = "";
}
