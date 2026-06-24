package com.example.template.common.token;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.template.common.constant.CacheNames;
import com.example.template.common.model.UserDetail;
import com.example.template.common.redis.RedisUtil;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 的 Token 会话存储实现。
 * <p>
 * 维护双向映射：token → UserDetail、userId → token，便于校验与登出清理。
 * </p>
 */
@RequiredArgsConstructor
public class RedisTokenStore implements TokenStore {

    /** Redis 操作工具 */
    private final RedisUtil redisUtil;

    @Override
    public UserDetail getByToken(String token) {
        if (StrUtil.isBlank(token)) {
            return null;
        }
        // 根据 Token 从 Redis 读取用户会话 JSON
        String json = redisUtil.get(CacheNames.TOKEN_BY_VALUE + token);
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSONUtil.toBean(json, UserDetail.class);
    }

    @Override
    public void saveToken(String token, UserDetail detail, long expireSeconds) {
        String json = JSONUtil.toJsonStr(detail);
        // 写入 token → 用户会话
        redisUtil.set(CacheNames.TOKEN_BY_VALUE + token, json, expireSeconds, TimeUnit.SECONDS);
        // 写入 userId → token，便于按用户踢下线
        redisUtil.set(CacheNames.TOKEN_BY_USER + detail.getUserId(), token, expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void removeToken(String token, Long userId) {
        // 删除 token → 用户会话
        redisUtil.del(CacheNames.TOKEN_BY_VALUE + token);
        if (userId != null) {
            // 删除 userId → token 映射
            redisUtil.del(CacheNames.TOKEN_BY_USER + userId);
        }
    }
}
