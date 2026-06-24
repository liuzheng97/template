package com.example.template.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Knife4j 全局文档配置。
 * <p>
 * 仅在 Servlet Web 服务中生效；网关为 WebFlux，不加载此类。
 * </p>
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET) // 排除 WebFlux 网关
@ConditionalOnClass(OpenAPI.class) // 依赖 springdoc / knife4j
public class OpenApiConfig {

    /**
     * 定义接口文档基础信息与 Bearer 鉴权方案。
     *
     * @return OpenAPI 全局配置对象
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Template API")
                .description("模版项目接口文档")
                .version("1.0.0")
                .contact(new Contact().name("dev").email("dev@example.com")))
            .components(new Components()
                .addSecuritySchemes("Authorization", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"))); // Swagger 中展示 Bearer Token 输入框
    }
}
