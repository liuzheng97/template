package com.example.template.common.launch;

/**
 * 启动与 Nacos 环境常量，仿 sfa-center {@code LauncherConstant}。
 */
public interface LauncherConstant {

    /** Nacos 配置/注册命名空间（开发） */
    String NACOS_NAMESPACE = "template";

    /** Nacos 配置 DataId 前缀 */
    String NACOS_DATA_ID_PREFIX = "template";

    String NACOS_DEV_ADDR = "127.0.0.1:8848";
    String NACOS_DEV_USERNAME = "nacos";
    String NACOS_DEV_PASSWORD = "nacos";

    /**
     * 按 profile 解析 Nacos 地址。
     */
    static String nacosAddr(String profile) {
        return NACOS_DEV_ADDR;
    }

    /**
     * 按 profile 解析 Nacos 用户名。
     */
    static String nacosUsername(String profile) {
        return NACOS_DEV_USERNAME;
    }

    /**
     * 按 profile 解析 Nacos 密码。
     */
    static String nacosPassword(String profile) {
        return NACOS_DEV_PASSWORD;
    }

    /**
     * 配置中心 Group，与 sfa 一致使用 profile 大写（DEV / PROD）。
     */
    static String configGroup(String profile) {
        if (profile == null || profile.isBlank()) {
            return "DEV";
        }
        return profile.toUpperCase();
    }

    /**
     * 注册中心 Group，与 sfa 一致使用 profile 小写（dev / prod）。
     */
    static String discoveryGroup(String profile) {
        if (profile == null || profile.isBlank()) {
            return "dev";
        }
        return profile.toLowerCase();
    }
}
