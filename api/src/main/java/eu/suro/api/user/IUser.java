package eu.suro.api.user;

import eu.suro.redis.Redis;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.Stream;

public interface IUser<P> {

    static Stream<IUser> getUsers() {
        return APIUser.getUserManager().getUsers();
    }

    @Nullable
    static <T> IUser<T> getUser(int id) {
        return getUsers().filter(user -> (user.getId() == id)).findAny().orElse(null);
    }

    @Nullable
    static <T> IUser<T> getUser(String name) {
        return getUsers().filter(user -> user.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    static <T> IUser<T> getUser(T player) {
        return APIUser.getUserManager().getUser(player);
    }


    @Nullable
    default String getRedisData(String key) {
        String value = getRedisData().get(key);
        return (value != null && value.isEmpty()) ? null : value;
    }

    default String getRedisData(String key, String defaultValue) {
        String value = getRedisData(key);
        return (value != null) ? value : defaultValue;
    }

    @Nullable
    default <T> T getRedisData(String key, Class<T> clazz) {
        return (T) Redis.parseRedisData(getRedisData(key), clazz);
    }

    default <T> T getRedisData(String key, T defaultValue) {
        Object value = getRedisData(key, defaultValue.getClass());
        return (value != null) ? (T)value : defaultValue;
    }

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

    default <T> T putRedisData(String key, T value) {
        if (value != null) {
            getRedisData().put(key, String.valueOf(value));
        } else {
            getRedisData().remove(key);
        }
        return value;
    }

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

    default <T> T getOrSetRedisData(String key, T value) {
        if (hasRedisData(key))
            return getRedisData(key, value);
        return setRedisData(key, value);
    }

    default boolean hasRedisData(String key) {
        return (getRedisData(key) != null);
    }

//    @SuppressWarnings("unchecked")
//    default <T> T getMetadata(String key, Class<T> c) {
//        return getMetadata(key);
//    }
//
//    @SuppressWarnings("unchecked")
//    default <T> T getMetadata(String key, Supplier<T> supplier) {
//        return (T)getMetadata().computeIfAbsent(key, k -> supplier.get());
//    }
//
//    @SuppressWarnings("unchecked")
//    default <T> T removeMetadata(String key) {
//        return (T)getMetadata().remove(key);
//    }
//
//    @SuppressWarnings("unchecked")
//    default <T> T getMetadata(String key) {
//        return (T)getMetadata().get(key);
//    }
//
//    @SuppressWarnings("unchecked")
//    default Object setMetadata(String key, Object object) {
//        return getMetadata().put(key, object);
//    }
//
//    @SuppressWarnings("unchecked")
//    default <T> T removeMetadata(String key, Class<T> c) {
//        return (T)getMetadata().remove(key);
//    }
//
//    @SuppressWarnings("unchecked")
//    default boolean hasMetadata(String key) {
//        return getMetadata().containsKey(key);
//    }
//
//    @SuppressWarnings("unchecked")
//    default <T> void getMetadata(String key, Consumer<T> consumer, Consumer<Consumer<T>> load) {
//        getAsyncLoader().loadOrGet(key, (Consumer) consumer, (Consumer) load);
//    }

    default String getPrefix() {
        return getRedisData("prefix", "");
    }

    default String getDisplayName() {
        return getPrefix() + getName();
    }

    default P getPlayer() {
        throw new UnsupportedOperationException();
    }

    Map<String, String> getRedisData();

    void connect(String paramString);

    void connect(String paramString, int paramInt);

    void connect(int paramInt);

    void connect(int paramInt1, int paramInt2);

    Map<String, Object> getMetadata();

    boolean hasPermission(@Nullable String paramString);

    int getId();

    String getName();

    void sendMessage(String paramString, String... paramVarArgs);

}
