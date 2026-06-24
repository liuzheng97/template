package com.example.template.auth.service;

import com.example.template.common.model.TokenInfo;

/**
 * 认证业务接口。
 * <p>
 * 定义登录、登出、验证码等认证相关操作。
 * </p>
 */
public interface AuthService {

    /**
     * 账号密码登录，返回访问令牌。
     *
     * @param account  账号
     * @param password 密码
     * @param clientType 登陆端类型
     * @return Token 及用户信息
     */
    TokenInfo login(String account, String password,String clientType);

    /**
     * 注销指定 Token，清除 Redis 会话。
     *
     * @param token 访问令牌
     */
    void logout(String token);

    /**
     * 保存验证码到 Redis。
     *
     * @param key  验证码 key（前端回传用）
     * @param code 验证码内容
     */
    void saveCaptcha(String key, String code);
}
