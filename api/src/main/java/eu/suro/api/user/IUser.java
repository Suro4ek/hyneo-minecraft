package eu.suro.api.user;

import eu.suro.redis.Redis;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.Stream;

public interface IUser<P> {

    static Stream<IUser> getUsers() {
        return APIUser.getUserManager().getUsers();
    }

    /**
        * Получение пользовтеля по id
        * @param id - id пользователя
        * @return пользователь
    */
    @Nullable
    static <T> IUser<T> getUser(int id) {
        return getUsers().filter(user -> (user.getId() == id)).findAny().orElse(null);
    }

    /**
        * Получение пользовтеля по нику
        * @param name - ник пользователя
        * @return <T> - пользователь
     */
    @Nullable
    static <T> IUser<T> getUser(String name) {
        return getUsers().filter(user -> user.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    /**
        * Получение пользовтеля по игроку
        * @param player - игрок
        * @return пользователь
     */
    static <T> IUser<T> getUser(T player) {
        return APIUser.getUserManager().getUser(player);
    }


    /**
        * Получение строку из redis
        * @param key - ключ
        * @return строка
     */
    @Nullable
    default String getRedisData(String key) {
        String value = getRedisData().get(key);
        return (value != null && value.isEmpty()) ? null : value;
    }

    /**
        * Получение строки из redis
        * @param key - ключ
        * @param def - значение по умолчанию
        * @return строка
     */
    default String getRedisData(String key, String defaultValue) {
        String value = getRedisData(key);
        return (value != null) ? value : defaultValue;
    }

    /**
        * Получение данных из redis
        * @param key - ключ
        * @return данные
     */
    @Nullable
    default <T> T getRedisData(String key, Class<T> clazz) {
        return (T) Redis.parseRedisData(getRedisData(key), clazz);
    }

    /**
        * Получение данных из redis
        * @param key - ключ
        * @param def - значение по умолчанию
        * @return данных
     */
    default <T> T getRedisData(String key, T defaultValue) {
        Object value = getRedisData(key, defaultValue.getClass());
        return (value != null) ? (T)value : defaultValue;
    }

    /**
        * Удаление данных из redis
        * @param key - ключ
        * @return удаленные данные
     */
    @Nullable
    default <T> T removeRedisData(String key) {
        Object object = getRedisData().remove(key);
        Redis.pipeline(pipeline -> {
            String userKey = "user:" + getName().toLowerCase();
            pipeline.hdel(userKey, new String[] { key });
            pipeline.expire(userKey, 30);
        });
        return (T)object;
    }

    /**
        * Установка данных в redis
        * @param key - ключ
        * @param value - значение
        * @return данные
     */
    default <T> T putRedisData(String key, T value) {
        if (value != null) {
            getRedisData().put(key, String.valueOf(value));
        } else {
            getRedisData().remove(key);
        }
        return value;
    }

    /**
        * Перезапис данных в redis
        * @param key - ключ
        * @param value - значение
        * @return данные
     */
    default <T> T setRedisData(String key, T value) {
        putRedisData(key, value);
        Redis.pipeline(pipeline -> {
            String userKey = "user:" + getName().toLowerCase();
            if (value != null) {
                pipeline.hset(userKey, key, String.valueOf(value));
            } else {
                pipeline.hdel(userKey, new String[] { key });
            }
            pipeline.expire(userKey, 30);
        });
        return value;
    }

    /**
        * Получить или записать данных из redis
        * @param key - ключ
        * @param value - значение
        * @return данные
     */

    default <T> T getOrSetRedisData(String key, T value) {
        if (hasRedisData(key))
            return getRedisData(key, value);
        return setRedisData(key, value);
    }

    /**
        * Проверить есть ли данные в redis
        * @param key - ключ
        * @return boolean
     */
    default boolean hasRedisData(String key) {
        return (getRedisData(key) != null);
    }


    /**
        * Получение префикса из redis
        * @return String - prefix
     */
    default String getPrefix() {
        return getRedisData("prefix", "");
    }

    /**
        * Получение полное имя пользователя с префиксом в redis
        * @param String - полное имя пользователя
     */
    default String getDisplayName() {
        return getPrefix() + getName();
    }

    /**
        * Получение игрока
        * @return P - игрок
     */
    default P getPlayer() {
        throw new UnsupportedOperationException();
    }

    Map<String, String> getRedisData();

    void connect(String paramString);

    boolean hasPermission(@Nullable String paramString);

    int getId();

    int localeId();

    String getName();

    void sendMessage(String paramString, String... paramVarArgs);

}
