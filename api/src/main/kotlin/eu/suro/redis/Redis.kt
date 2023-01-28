package eu.suro.redis

import java.sql.Timestamp
import java.util.*
import java.util.function.Consumer

/*
   Используется для транзакций с редисом, чтобы запрос точно дошел до редиса
*/
object Redis {
    lateinit var manager: RedisManager


    fun init(redisManager: RedisManager) {
        manager = redisManager
//        sync { stream: RedisStream -> stream.jedis.info() }
    }

//    /**
//     * Выполнить транзакцию с редисом async
//     * @param create - Создание транзакции
//     */
//    @Deprecated("")
//    fun async(create: RedisConsumer?) {
//        manager!!.async(create)
//    }

//    /**
//     * Выполнить транзакцию с редисом sync
//     * @param create - Создание транзакции
//     */
//    fun sync(create: RedisConsumer) {
//        try {
//            manager!!.jedisPool.getResource().use { jedis -> create.run(RedisStream(jedis)) }
//        } catch (e: JedisException) {
//            e.printStackTrace()
//        }
//    }
//
//    /**
//     * Выполнить pipeline с редисом sync
//     * @param pipelineConsumer - Создание транзакции
//     */
//    fun pipeline(pipelineConsumer: Consumer<Pipeline?>) {
//        try {
//            manager!!.jedisPool.getResource().use { jedis ->
//                jedis.pipelined().use { pipelined ->
//                    pipelineConsumer.accept(
//                        pipelined
//                    )
//                }
//            }
//        } catch (e: JedisException) {
//            e.printStackTrace()
//        }
//    }
//
//    /**
//     * Выполнить транзакцию с редисом sync
//     * @param transactionConsumer - Создание транзакции
//     */
//    fun transaction(transactionConsumer: Consumer<Transaction?>) {
//        try {
//            manager!!.jedisPool.getResource().use { jedis ->
//                jedis.multi().use { transaction ->
//                    transactionConsumer.accept(
//                        transaction
//                    )
//                }
//            }
//        } catch (e: JedisException) {
//            e.printStackTrace()
//        }
//    }
//
//    /**
//     * Парсинг данных с редиса
//     * @param data - Данные
//     * @param clazz - Класс
//     * @return - Объект
//     */
//    fun <T> parseRedisData(data: String?, clazz: Class<T>): T? {
//        return if (data != null && !data.isEmpty()) try {
//            var `object`: Any = data
//            if (clazz == Int::class.java) `object` = data.toInt()
//            if (clazz == Long::class.java) `object` = data.toLong()
//            if (clazz == Double::class.java) `object` = data.toDouble()
//            if (clazz == Float::class.java) `object` = data.toFloat()
//            if (clazz == Boolean::class.java) `object` = data == "true"
//            if (clazz == Timestamp::class.java) `object` = Timestamp(data.toLong())
//            if (clazz == UUID::class.java) `object` = UUID.fromString(data)
//            `object` as T
//        } catch (ex: Exception) {
//            null
//        } else null
//    }
}
