package com.example.template.common.config;

import com.example.template.common.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 业务服务公共自动配置入口。
 * <p>
 * 仿 sfa-center 的 {@code @ImportAutoConfiguration(CommonConfiguration.class)} 模式，
 * 由各微服务按需引入，网关服务不应引入此类。
 * </p>
 */
@Configuration
@Import({
    OpenApiConfig.class,           // Knife4j / OpenAPI 文档配置（仅 Web 服务）
    MybatisAutoConfiguration.class, // MyBatis-Plus 条件配置（无依赖时不加载）
    RedisAutoConfiguration.class,   // RedisUtil Bean 注册
    TokenAutoConfiguration.class,   // Token 存储与鉴权过滤器
    GlobalExceptionHandler.class    // 全局异常统一响应
})
public class CommonAutoConfiguration {
}
