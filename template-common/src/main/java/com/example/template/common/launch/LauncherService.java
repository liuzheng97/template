package com.example.template.common.launch;

import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 启动前 Nacos 配置注入 SPI，仿 sfa {@code LauncherService}。
 */
public interface LauncherService {

    /**
     * 在 Spring Boot 启动前注入 Nacos 连接与扩展配置。
     *
     * @param builder  应用构建器
     * @param appName  服务名，如 auth、gateway
     * @param profile  激活环境，如 dev、prod
     */
    void launcher(SpringApplicationBuilder builder, String appName, String profile);
}
