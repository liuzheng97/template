package com.example.template.system;

import com.example.template.common.config.CommonAutoConfiguration;
import com.example.template.common.constant.AppConstant;
import com.example.template.common.launch.TemplateLauncher;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 系统服务启动类。
 * <p>
 * 职责：系统日志、Token 生成（TokenClient）、Feign 内部接口等。
 * </p>
 */
@SpringBootApplication
@EnableDiscoveryClient // 注册到 Nacos
@EnableFeignClients(basePackages = AppConstant.BASE_PACKAGES) // 扫描 IUserClient 等 Feign 接口
@ImportAutoConfiguration(CommonAutoConfiguration.class) // 按需引入 Redis、Token、OpenAPI 等公共配置
public class SystemApplication {

    public static void main(String[] args) {
        TemplateLauncher.run(AppConstant.APPLICATION_SYSTEM_NAME, SystemApplication.class, args);
    }
}
