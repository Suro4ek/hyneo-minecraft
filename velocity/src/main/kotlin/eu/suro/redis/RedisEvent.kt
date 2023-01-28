package eu.suro.redis

data class RedisEvent<T> constructor(val channel: String, val obj: T) {
}
