package eu.suro.redis

data class RedisEvent constructor(val channel: String, val message: String) {
}
