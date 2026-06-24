package com.example.template.common.constant;

/**
 * 用户状态相关常量。
 * <p>
 * 与 {@code sys_user.status} 字段对应，供 user、system、auth 服务共用。
 * </p>
 */
public interface UserConstant {

    /** 用户状态：启用 */
    int STATUS_ENABLED = 1;

    /** 用户状态：禁用 */
    int STATUS_DISABLED = 0;

    /** 账号禁用时的登录提示 */
    String MSG_ACCOUNT_DISABLED = "账号已禁用，请联系管理员";
}
