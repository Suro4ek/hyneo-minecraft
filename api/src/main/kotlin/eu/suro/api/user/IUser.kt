package eu.suro.api.user

import eu.suro.redis.Redis
import java.util.stream.Stream

interface IUser {

    val id: Long
    val localeId: Int
    val name: String
    val isAuth: Boolean
    var redisData: MutableMap<String, String>
    companion object{
        @JvmStatic fun getUsers(): Stream<IUser> = APIUser.userManager!!.getUsers()
        /**
         * Получение пользовтеля по id
         * @param id - id пользователя
         * @return пользователь
         */
        @JvmStatic fun getUser(id: Long): IUser? =
            getUsers().filter { it.id == id }.findAny().orElse(null)

        /**
         * Получение пользовтеля по нику
         * @param name - ник пользователя
         * @return <T> - пользователь
         */
        @JvmStatic fun getUser(name: String): IUser? =
            getUsers().filter { it.name.equals(name, ignoreCase = true) }.findAny().orElse(null)

        /**
         * Получение пользовтеля по игроку
         * @param player - игрок
         * @return пользователь
         */
        @JvmStatic fun getUser(player: Any): IUser? =
            APIUser.userManager!!.getUser(player)
    }

//    /**
//     * Получение строку из redis
//     * @param key - ключ
//     * @return строка
//     */
//    fun getRedisData(key: String): String? = (
//            if(redisData[key] != null && redisData[key]!!.isEmpty()){
//                null
//            }else{
//                redisData[key]
//            })

//    /**
//     * Получение строки из redis
//     * @param key - ключ
//     * @param defaultValue - значение по умолчанию
//     * @return строка
//     */
//    fun getRedisData(key: String, defaultValue: String): String = getRedisData(key) ?: defaultValue

    /**
     * Получение данных из redis
     * @param key - ключ
     * @return данные
     */
    fun getRedisData(): UserRedis? = Redis.manager.jedisPool.liveObjectService.get(UserRedis::class.java, id.toString())
//    /**
//     * Получение данных из redis
//     * @param key - ключ
//     * @param def - значение по умолчанию
//     * @return данных
//     */
//    fun <T: Any> getRedisData(key: String, defaultValue: T?): T? =
//        defaultValue?.let { getRedisData(key, it::class.java) } ?: defaultValue

//    /**
//     * Удаление данных из redis
//     * @param key - ключ
//     * @return удаленные данные
//     */
//    fun <T> removeRedisData(key: String): String? {
//        val obj = redisData.remove(key)
//        Redis.pipeline{pipeline ->
//            val userKey: String = "user:" + name.lowercase()
//            pipeline.hdel(userKey, key)
//            pipeline.expire(userKey, 30)
//        }
//        return obj
//    }

//    /**
//     * Установка данных в redis
//     * @param key - ключ
//     * @param value - значение
//     * @return данные
//     */
//    fun <T> putRedisData(key: String, value: T): T {
//        if (value != null) {
//            redisData[key] = value.toString()
//        } else {
//            redisData.remove(key)
//        }
//        return value
//    }

//    /**
//     * Перезапис данных в redis
//     * @param key - ключ
//     * @param value - значение
//     * @return данные
//     */
//    fun <T> setRedisData(key: String, value: T?): T? {
//        putRedisData(key, value)
//        Redis.pipeline { pipeline ->
//            val userKey: String = "user:" + name.lowercase()
//            if(value != null){
//                pipeline.hset(userKey, key, value.toString())
//            }else{
//                pipeline.hdel(userKey, key);
//            }
//            pipeline.expire(userKey, 30)
//        }
//        return value
//    }

//    /**
//     * Проверить есть ли данные в redis
//     * @param key - ключ
//     * @return boolean
//     */
//    fun hasRedisData(key: String): Boolean = getRedisData(key) != null

//    /**
//     * Получить или записать данных из redis
//     * @param key - ключ
//     * @param value - значение
//     * @return данные
//     */
//
//    fun <T: Any> getOrSetRedisData(key: String, value: T?): T? = getRedisData(key, value) ?: setRedisData(key, value)

    /**
     * Получение префикса из redis
     * @return String - prefix
     */
    fun prefix(): String = getRedisData()?.prefix ?: ""

    fun displayName(): String = prefix() + name

//    /**
//     * Получение игрока
//     * @return P - игрок
//     */
//    fun <T> player(): T

//    fun connect(paramString: String)

}