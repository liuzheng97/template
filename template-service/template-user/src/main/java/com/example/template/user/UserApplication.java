package com.example.template.user;

import com.example.template.common.config.CommonAutoConfiguration;
import com.example.template.common.constant.AppConstant;
import com.example.template.common.launch.TemplateLauncher;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 用户服务启动类。
 * <p>
 * 职责：用户 CRUD、内部账号密码校验（供 system TokenClient Feign 调用）。
 * </p>
 */
@SpringBootApplication // 仅扫描 com.example.template.user
@EnableDiscoveryClient // 注册到 Nacos，服务名 user
@MapperScan("com.example.template.user.mapper") // 扫描 MyBatis Mapper 接口
@ImportAutoConfiguration(CommonAutoConfiguration.class) // 按需加载 Redis、Token 过滤器、OpenAPI 等
public class UserApplication {

    public static void main(String[] args) {
        TemplateLauncher.run(AppConstant.APPLICATION_USER_NAME, UserApplication.class, args);
    }
}
