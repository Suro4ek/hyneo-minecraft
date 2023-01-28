package eu.suro.redis

import de.exlll.configlib.Comment

class RedisConfig {
    @Comment("Адрес сервера редиса")
    var host = "localhost"

    @Comment("Порт сервера редиса")
    var port = 6379

    @Comment("Пароль сервера редиса")
    var password = ""
}