package eu.suro.api.user

import org.redisson.api.annotation.REntity
import org.redisson.api.annotation.RId
import org.redisson.api.annotation.RIndex


@REntity
class UserRedis constructor(
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
)