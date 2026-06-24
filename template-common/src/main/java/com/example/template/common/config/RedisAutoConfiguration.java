package com.example.template.common.config;

import com.example.template.common.redis.RedisUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis 公共自动配置。
 * <p>
 * 在引入 spring-boot-starter-data-redis 的服务中注册 {@link RedisUtil} Bean。
 * </p>
 */
@Configuration
@ConditionalOnClass(StringRedisTemplate.class) // 仅当 Redis 依赖存在时加载
public class RedisAutoConfiguration {

    /**
     * 注册 Redis 工具 Bean，供 Token、验证码、缓存等业务使用。
     *
     * @param stringRedisTemplate Spring 自动配置的 Redis 模板
     * @return Redis 操作工具
     */
    @Bean
    @ConditionalOnMissingBean // 允许业务模块自定义 RedisUtil
    public RedisUtil redisUtil(StringRedisTemplate stringRedisTemplate) {
        return new RedisUtil(stringRedisTemplate);
    }
}
