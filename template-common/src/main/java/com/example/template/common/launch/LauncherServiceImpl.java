package com.example.template.common.launch;

import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Nacos 配置启动器实现，仿 sfa-center {@code LauncherServiceImpl}。
 * <p>
 * Boot 3 通过 {@code spring.config.import} 显式加载（dataId 不可省略）：
 * <ul>
 *   <li>{@code template-{profile}.yaml} — 主配置（DEFAULT_GROUP）</li>
 *   <li>{@code template} — 公共配置（DEV/PROD）</li>
 *   <li>{@code template-{appName}} — 服务专属配置</li>
 * </ul>
 * </p>
 */
public class LauncherServiceImpl implements LauncherService {

    @Override
    public void launcher(SpringApplicationBuilder builder, String appName, String profile) {
        String namespace = LauncherConstant.NACOS_NAMESPACE;
        String prefix = LauncherConstant.NACOS_DATA_ID_PREFIX;
        String configGroup = LauncherConstant.configGroup(profile);
        String discoveryGroup = LauncherConstant.discoveryGroup(profile);

        Properties props = System.getProperties();
        props.setProperty("spring.application.name", appName);

        // Nacos 配置中心连接
        props.setProperty("spring.cloud.nacos.config.username", LauncherConstant.nacosUsername(profile));
        props.setProperty("spring.cloud.nacos.config.password", LauncherConstant.nacosPassword(profile));
        props.setProperty("spring.cloud.nacos.config.server-addr", LauncherConstant.nacosAddr(profile));
        props.setProperty("spring.cloud.nacos.config.namespace", namespace);
        props.setProperty("spring.cloud.nacos.config.group", NacosConstant.NACOS_CONFIG_GROUP);
        props.setProperty("spring.cloud.nacos.config.prefix", prefix);
        props.setProperty("spring.cloud.nacos.config.file-extension", "yaml");

        // Boot 3：显式 import 各 DataId（optional:nacos:  alone 会报 dataId must be specified）
        props.setProperty("spring.cloud.nacos.config.import-check.enabled", "false");
        props.setProperty("spring.config.import", buildConfigImport(prefix, appName, profile, configGroup));

        // Nacos 服务注册
        props.setProperty("spring.cloud.nacos.discovery.username", LauncherConstant.nacosUsername(profile));
        props.setProperty("spring.cloud.nacos.discovery.password", LauncherConstant.nacosPassword(profile));
        props.setProperty("spring.cloud.nacos.discovery.server-addr", LauncherConstant.nacosAddr(profile));
        props.setProperty("spring.cloud.nacos.discovery.namespace", namespace);
        props.setProperty("spring.cloud.nacos.discovery.group", discoveryGroup);
    }

    /**
     * 组装 spring.config.import，顺序：主配置 → 公共 → 服务专属（→ ES 扩展）。
     */
    private static String buildConfigImport(String prefix, String appName, String profile, String configGroup) {
        List<String> imports = new ArrayList<>();
        imports.add(NacosConstant.nacosImport(NacosConstant.profileDataId(prefix, profile), NacosConstant.NACOS_CONFIG_GROUP));
        imports.add(NacosConstant.nacosImport(prefix, configGroup));
        imports.add(NacosConstant.nacosImport(NacosConstant.serviceDataId(prefix, appName), configGroup));
        if ("system".equals(appName) && profile.toLowerCase().contains("es")) {
            imports.add(NacosConstant.nacosImport(prefix + "-system-es", configGroup));
        }
        return String.join(",", imports);
    }
}
