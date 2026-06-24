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

    /** 登录失败锁定，完整 Key：login_lock:{account} */
    String LOGIN_LOCK = "login_lock:";

    /** 登录连续失败次数，完整 Key：login_fail:{account} */
    String LOGIN_FAIL = "login_fail:";

    /** IP 登录失败锁定，完整 Key：login_lock_ip:{ip} */
    String LOGIN_LOCK_IP = "login_lock_ip:";

    /** IP 连续登录失败次数，完整 Key：login_fail_ip:{ip} */
    String LOGIN_FAIL_IP = "login_fail_ip:";
}
