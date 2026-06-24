package com.example.template.gateway;

import com.example.template.common.constant.AppConstant;
import com.example.template.common.launch.TemplateLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API 网关启动类。
 * <p>
 * 仿 sfa-center：仅扫描本包，不引入 template-common 自动配置，
 * 避免加载 MyBatis/Redis/Servlet 组件导致 WebFlux 网关启动失败。
 * Nacos 配置通过 {@link TemplateLauncher} 注入。
 * </p>
 */
@SpringBootApplication // 仅扫描 com.example.template.gateway
@EnableDiscoveryClient // 注册到 Nacos，服务名 gateway
public class GatewayApplication {

    public static void main(String[] args) {
        TemplateLauncher.run(AppConstant.APPLICATION_GATEWAY_NAME, GatewayApplication.class, args);
    }
}
