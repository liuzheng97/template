package com.example.template.auth;

import com.example.template.common.config.CommonAutoConfiguration;
import com.example.template.common.constant.AppConstant;
import com.example.template.common.launch.TemplateLauncher;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证服务启动类。
 * <p>
 * 仿 sfa-center：仅扫描本包 + 按需引入公共配置（Redis/Web/OpenAPI/Token，不含 MyBatis）。
 * </p>
 */
@SpringBootApplication // 仅扫描 com.example.template.auth 包
@EnableDiscoveryClient // 注册到 Nacos，服务名 auth
@EnableFeignClients(basePackages = AppConstant.BASE_PACKAGES) // 扫描 ITokenClient 等 Feign 接口
@ImportAutoConfiguration(CommonAutoConfiguration.class) // 按需加载 Redis、Token 过滤器、OpenAPI 等
public class AuthApplication {

    public static void main(String[] args) {
        TemplateLauncher.run(AppConstant.APPLICATION_AUTH_NAME, AuthApplication.class, args);
    }
}
