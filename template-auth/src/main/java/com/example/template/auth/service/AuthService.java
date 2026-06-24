package com.example.template.auth.service;

import com.example.template.common.model.TokenInfo;

/**
 * 认证业务接口。
 * <p>
 * 定义登录、登出等认证相关操作。
 * </p>
 */
public interface AuthService {

    /**
     * 账号密码登录，返回访问令牌。
     *
     * @param account    账号
     * @param password   密码
     * @param clientType 登陆端类型
     * @param clientIp   客户端 IP，用于 IP 防刷
     * @return Token 及用户信息
     */
    TokenInfo login(String account, String password, String clientType, String clientIp);

    /**
     * 注销指定 Token，清除 Redis 会话。
     *
     * @param token 访问令牌
     */
    void logout(String token);
}
