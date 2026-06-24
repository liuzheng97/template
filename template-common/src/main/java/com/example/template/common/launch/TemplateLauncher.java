package com.example.template.common.launch;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.StandardEnvironment;

import java.util.ServiceLoader;

/**
 * 统一启动入口，仿 sfa-center {@code Application.run}。
 */
public final class TemplateLauncher {

    private TemplateLauncher() {
    }

    /**
     * 启动微服务：解析 profile → 注入 Nacos 配置 → 运行 Spring Boot。
     *
     * @param appName  注册到 Nacos 的服务名
     * @param source   启动类
     * @param args     启动参数
     */
    public static void run(String appName, Class<?> source, String... args) {
        String profile = resolveProfile(args);
        System.setProperty(StandardEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);

        SpringApplicationBuilder builder = new SpringApplicationBuilder(source);
        ServiceLoader<LauncherService> loaders = ServiceLoader.load(LauncherService.class);
        for (LauncherService loader : loaders) {
            loader.launcher(builder, appName, profile);
        }
        builder.run(args);
    }

    private static String resolveProfile(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--spring.profiles.active=")) {
                return args[i].substring("--spring.profiles.active=".length()).split(",")[0];
            }
            if ("--spring.profiles.active".equals(args[i]) && i + 1 < args.length) {
                return args[i + 1].split(",")[0];
            }
        }
        String env = System.getProperty(StandardEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
        if (env != null && !env.isBlank()) {
            return env.split(",")[0];
        }
        return "dev";
    }
}
