package com.example.template.common.redis;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Redis 字符串操作工具类。
 * <p>
 * 由 {@link com.example.template.common.config.RedisAutoConfiguration} 注册为 Spring Bean，
 * 供 Token 会话、验证码、登录锁、订单缓存等场景使用。
 * </p>
 */
public class RedisUtil {

    /** Spring Data Redis 字符串模板 */
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * @param stringRedisTemplate Redis 字符串操作模板
     */
    public RedisUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 写入字符串缓存（永不过期）。
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 写入带过期时间的字符串缓存。
     *
     * @param key     缓存键
     * @param value   缓存值
     * @param timeout 过期时长
     * @param unit    时间单位
     */
    public void set(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 读取字符串缓存。
     *
     * @param key 缓存键
     * @return 缓存值，不存在时返回 null
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 判断 key 是否存在。
     *
     * @param key 缓存键
     * @return true 表示存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    /**
     * 删除指定缓存。
     *
     * @param key 缓存键
     */
    public void del(String key) {
        stringRedisTemplate.delete(key);
    }
}
