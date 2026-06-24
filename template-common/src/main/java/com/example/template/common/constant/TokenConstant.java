package com.example.template.common.constant;

/**
 * Token 与认证相关常量。
 * <p>
 * 定义请求头、客户端类型、过期时间等，供 auth、system 及各业务服务共用。
 * </p>
 */
public interface TokenConstant {

    /** 请求头中的 Token 字段名，客户端需在 Header 中携带 */
    String TOKEN_HEADER = "token-auth";

    /** Bearer 前缀，支持 "bearer {token}" 格式（不区分大小写） */
    String BEARER_PREFIX = "bearer ";

    /** 客户端类型：Web / 小程序 */
    String CLIENT_TYPE_WEB = "web";

    /** 客户端类型：App */
    String CLIENT_TYPE_APP = "app";

    /** Web 端 Token 有效期（秒），默认 1 天 */
//    long WEB_EXPIRE_SECONDS = 24 * 60 * 60;
    long WEB_EXPIRE_SECONDS =5;

    /** App 端 Token 有效期（秒），默认 7 天 */
    long APP_EXPIRE_SECONDS = 7 * 24 * 60 * 60;

    /** 登录失败锁定时长（分钟） */
    long LOGIN_LOCK_MINUTES = 5;

    /** 验证码有效期（分钟） */
    long CAPTCHA_EXPIRE_MINUTES = 30;

    /**
     * 按客户端类型解析 Token 过期秒数。
     *
     * @param clientType 客户端类型，{@link #CLIENT_TYPE_APP} 为 7 天，其余为 Web 1 天
     * @return 过期秒数
     */
    static long expireSeconds(String clientType) {
        return CLIENT_TYPE_APP.equalsIgnoreCase(clientType) ? APP_EXPIRE_SECONDS : WEB_EXPIRE_SECONDS;
    }
}
