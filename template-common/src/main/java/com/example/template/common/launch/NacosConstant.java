package com.example.template.common.launch;

/**
 * Nacos 配置常量，仿 ac-core {@code NacosConstant}。
 */
public interface NacosConstant {

    /** 主配置默认 Group */
    String NACOS_CONFIG_GROUP = "DEFAULT_GROUP";

    /** 扩展配置是否动态刷新 */
    String NACOS_CONFIG_REFRESH = "true";

    /**
     * 主配置 DataId，如 {@code template-dev.yaml}（Group=DEFAULT_GROUP）。
     */
    static String profileDataId(String prefix, String profile) {
        return prefix + "-" + profile + ".yaml";
    }

    /**
     * 各服务专属配置 DataId，仿 sfa 的 {@code sfa-system} 命名（无后缀）。
     */
    static String serviceDataId(String prefix, String appName) {
        return prefix + "-" + appName;
    }

    /**
     * Boot 3 {@code spring.config.import} 条目，如 {@code optional:nacos:template?group=DEV}。
     */
    static String nacosImport(String dataId, String group) {
        if (group == null || group.isBlank() || NACOS_CONFIG_GROUP.equals(group)) {
            return "optional:nacos:" + dataId;
        }
        return "optional:nacos:" + dataId + "?group=" + group;
    }
}
