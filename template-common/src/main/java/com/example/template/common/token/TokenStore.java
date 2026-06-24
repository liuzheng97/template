package com.example.template.common.token;

import com.example.template.common.model.UserDetail;

/**
 * Token 会话存储接口。
 * <p>
 * 由 system 服务在登录时写入，各业务服务通过 {@link com.example.template.common.filter.BasicTokenFilter} 读取校验。
 * </p>
 */
public interface TokenStore {

    /**
     * 根据 Token 获取登录用户会话。
     *
     * @param token 访问令牌
     * @return 用户会话，不存在或已过期时返回 null
     */
    UserDetail getByToken(String token);

    /**
     * 保存 Token 会话到 Redis。
     *
     * @param token          访问令牌
     * @param detail         用户会话详情
     * @param expireSeconds  过期时间（秒）
     */
    void saveToken(String token, UserDetail detail, long expireSeconds);

    /**
     * 移除 Token 会话（登出时调用）。
     *
     * @param token  访问令牌
     * @param userId 用户 ID，用于清理反向索引
     */
    void removeToken(String token, Long userId);
}
