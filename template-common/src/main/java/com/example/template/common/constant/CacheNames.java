package com.example.template.common.constant;

/**
 * Redis 缓存 Key 前缀常量。
 * <p>
 * 各服务共用同一套 Key 命名，保证 Token 会话在 Redis 中可跨服务读写。
 * </p>
 */
public interface CacheNames {

    /** Token 值 → 用户会话 JSON，完整 Key：user:token:{token} */
    String TOKEN_BY_VALUE = "user:token:";

    /** 用户 ID → Token 值，完整 Key：user:token:id:{userId} */
    String TOKEN_BY_USER = "user:token:id:";

    /** 图形验证码，完整 Key：captcha:{key} */
    String CAPTCHA_KEY = "captcha:";

    /** 登录失败锁定，完整 Key：login_lock:{account} */
    String LOGIN_LOCK = "login_lock:";
}
