package com.example.template.common.context;

import com.example.template.common.model.UserDetail;

/**
 * 当前登录用户上下文（ThreadLocal）。
 * <p>
 * 由 {@link com.example.template.common.filter.BasicTokenFilter} 在 Token 校验通过后写入，
 * 同一次 HTTP 请求线程内的 Controller / Service 可通过静态方法获取登录人信息。
 * 请求结束必须在 {@link #clear()} 中清理，避免线程池复用导致用户串号。
 * </p>
 */
public final class UserContext {

    /** 当前请求登录用户会话 */
    private static final ThreadLocal<UserDetail> HOLDER = new ThreadLocal<>();

    private UserContext() {
    }

    /**
     * 写入当前登录用户（仅 Filter 内部调用）。
     *
     * @param detail 登录会话详情
     */
    public static void set(UserDetail detail) {
        HOLDER.set(detail);
    }

    /**
     * 获取当前登录用户完整信息。
     *
     * @return 登录会话，未登录或白名单接口返回 null
     */
    public static UserDetail get() {
        return HOLDER.get();
    }

    /**
     * 获取当前登录用户 ID。
     *
     * @return 用户 ID，未登录时返回 null
     */
    public static Long getUserId() {
        UserDetail detail = get();
        return detail == null ? null : detail.getUserId();
    }

    /**
     * 获取当前登录账号。
     *
     * @return 账号，未登录时返回 null
     */
    public static String getAccount() {
        UserDetail detail = get();
        return detail == null ? null : detail.getAccount();
    }

    /**
     * 获取当前登录用户姓名。
     *
     * @return 姓名，未登录时返回 null
     */
    public static String getName() {
        UserDetail detail = get();
        return detail == null ? null : detail.getName();
    }

    /**
     * 清理当前线程中的登录用户，防止线程池泄漏。
     */
    public static void clear() {
        HOLDER.remove();
    }
}
