package eu.suro.api.user

import org.redisson.api.annotation.REntity
import org.redisson.api.annotation.RId
import org.redisson.api.annotation.RIndex
import java.io.Serializable


@REntity
open class UserRedis constructor(
    @RId
    var id: String,
    @RIndex
    var username: String,
    var ip: String,
    var prefix: String,
    var last_server: String,
    var register_ip: String,
    var locale_id: Int,
    var auth: Boolean,
) {

    override fun toString()
    = """{
        |"id":$id,
        |"name":"$username",
        |"ip":$ip,
        |"prefix":$prefix,
        |"last_server":$last_server,
        |"register_ip:$register_ip,
        |"locale_id:$locale_id,
        |"auth":$auth
        |}""".trimMargin()
}