package com.example.template.common.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Servlet 请求工具。
 */
public final class ServletUtils {

    private ServletUtils() {
    }

    /**
     * 解析客户端真实 IP（兼容 Nginx X-Forwarded-For / X-Real-IP）。
     *
     * @param request HTTP 请求
     * @return 客户端 IP
     */
    public static String resolveClientIp(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }
}
