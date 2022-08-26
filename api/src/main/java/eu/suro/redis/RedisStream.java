package eu.suro.redis;

import eu.suro.redis.types.ExpireType;
import eu.suro.redis.types.RedisObject;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RedisStream {
    private final Jedis jedis;

    private String domain;

    private long expireTime;

    private ExpireType expireType;

    public RedisStream(Jedis jedis) {
        this(jedis, (String)null);
    }

    public RedisStream(Jedis jedis, String domain) {
        this.jedis = jedis;
        this.domain = domain;
    }

    public RedisStream(Jedis jedis, RedisObject redisObject) {
        this(jedis, (String)null);
        setDomain(redisObject);
    }

    public Jedis getJedis() {
        return this.jedis;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(RedisObject redisObject) {
        this.domain = redisObject.getDomainKey() + ":" + redisObject.getDomainId();
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getFixedWay(String key) {
        if (this.domain == null || this.domain.isEmpty())
            return key;
        return this.domain + ":" + key;
    }

    public boolean exists(String key) {
        return this.jedis.exists(getFixedWay(key));
    }

    public void del(String key) {
        this.jedis.del(getFixedWay(key));
    }

    public void setExpireTime(String key, int seconds) {
        key = getFixedWay(key);
        this.jedis.expire(key, seconds);
    }

    public void setExpireAt(String key, long time) {
        this.jedis.expireAt(getFixedWay(key), time);
    }

    public void setExpire(ExpireType type, long time) {
        this.expireTime = time;
        this.expireType = type;
    }

    public void clearExpire() {
        this.expireType = null;
    }

    private void checkExpire(String key) {
        if (this.expireType != null) {
            if (this.expireType == ExpireType.AFTER_TIME || this.expireType == ExpireType.ALL_AFTER_TIME) {
                setExpireTime(key, (int)this.expireTime);
            } else {
                setExpireAt(key, this.expireTime);
            }
            if (this.expireType == ExpireType.AFTER_TIME || this.expireType == ExpireType.AT_TIME)
                clearExpire();
        }
    }

    public String getAndSetNew(String key, String newValue) {
        checkExpire(key);
        String value = this.jedis.getSet(getFixedWay(key), newValue);
        if (value.equals("null"))
            return null;
        return value;
    }

    public void set(String key, String value) {
        this.jedis.set(getFixedWay(key), value);
        checkExpire(key);
    }

    public void setMap(String key, Map<String, String> map) {
        key = getFixedWay(key);
        map.values().removeIf(Objects::isNull);
        this.jedis.hmset(key, map);
    }

    public void setInteger(String key, int value) {
        set(key, String.valueOf(value));
    }

    public void setLong(String key, long value) {
        set(key, String.valueOf(value));
    }

    public void setFloat(String key, float value) {
        set(key, String.valueOf(value));
    }

    public void setDouble(String key, double value) {
        set(key, String.valueOf(value));
    }

    public void setBoolean(String key, boolean value) {
        set(key, String.valueOf(value));
    }

    public String get(String key) {
        String value = this.jedis.get(getFixedWay(key));
        if (value == null || value.equals("null"))
            return null;
        return value;
    }

    public Map<String, String> getMap(String key) {
        key = getFixedWay(key);
        return this.jedis.hgetAll(key);
    }

    public byte[] getBytes(String key) {
        String value = getFixedWay(key);
        if (value == null)
            return null;
        return this.jedis.get(value.getBytes());
    }

    public void setBytes(String key, byte[] bytes) {
        this.jedis.set(getFixedWay(key).getBytes(), bytes);
        checkExpire(key);
    }

    public Integer getInteger(String key) {
        String value = get(key);
        if (value == null)
            return null;
        return Integer.valueOf(value);
    }

    public Long getLong(String key) {
        String value = get(key);
        if (value == null)
            return null;
        return Long.valueOf(value);
    }

    public Float getFloat(String key) {
        String value = get(key);
        if (value == null)
            return null;
        return Float.valueOf(value);
    }

    public Double getDouble(String key) {
        String value = get(key);
        if (value == null)
            return null;
        return Double.valueOf(value);
    }

    public Boolean getBoolean(String key) {
        String value = get(key);
        if (value == null)
            return null;
        return Boolean.valueOf(value);
    }

    public Set<String> getKeys(String pattern) {
        return this.jedis.keys(getFixedWay(pattern));
    }
}