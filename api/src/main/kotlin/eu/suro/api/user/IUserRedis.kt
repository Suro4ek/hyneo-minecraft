package eu.suro.api.user

import org.redisson.api.annotation.REntity


@REntity
interface IUserRedis {
//    @RId
    var id: String
//    @RIndex
    var username: String
    var ip: String
    var prefix: String
    var last_server: String
    var register_ip: String
    var locale_id: Int
    var auth: Boolean

//    constructor(
//     id: String,
//     username: String,
//     ip: String,
//     prefix: String,
//     last_server: String,
//     register_ip: String,
//     locale_id: Int,
//     auth: Boolean
//    ) : this() {
//        this.id = id
//        this.username = username
//        this.ip = ip
//        this.prefix = prefix
//        this.last_server = last_server
//        this.register_ip = register_ip
//        this.locale_id = locale_id
//        this.auth =auth
//    }
//
//    override fun toString()
//    = """{
//        |"id":$id,
//        |"name":"$username",
//        |"ip":$ip,
//        |"prefix":$prefix,
//        |"last_server":$last_server,
//        |"register_ip:$register_ip,
//        |"locale_id:$locale_id,
//        |"auth":$auth
//        |}""".trimMargin()
}