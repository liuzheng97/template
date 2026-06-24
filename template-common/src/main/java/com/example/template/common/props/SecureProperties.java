package com.example.template.common.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全相关配置。
 * <p>
 * 绑定 {@code secure.*} 配置项，供 {@link com.example.template.common.filter.BasicTokenFilter} 白名单使用。
 * 可在 Nacos {@code template} 中覆盖 {@link #excludePatterns}。
 * </p>
 */
@Data
@ConfigurationProperties(prefix = "secure")
public class SecureProperties {

    /**
     * 免鉴权路径列表（Ant 风格）。
     * 默认放行：内部接口、Feign、登录、Swagger 文档等。
     */
    private List<String> excludePatterns = new ArrayList<>(List.of(
        "/internal/**",    // 微服务内部接口
        "/feign/**",       // Feign 回调接口
        "/auth/**",        // 认证服务登录/验证码
        "/v3/api-docs/**", // OpenAPI 文档
        "/swagger-ui/**",  // Swagger UI
        "/doc.html",       // Knife4j 文档入口
        "/webjars/**",     // 文档静态资源
        "/error"           // Spring Boot 错误页
    ));
}
