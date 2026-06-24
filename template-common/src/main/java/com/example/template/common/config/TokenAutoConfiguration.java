package com.example.template.common.config;

import com.example.template.common.filter.BasicTokenFilter;
import com.example.template.common.props.SecureProperties;
import com.example.template.common.redis.RedisUtil;
import com.example.template.common.resolver.LoginUserArgumentResolver;
import com.example.template.common.token.RedisTokenStore;
import com.example.template.common.token.TokenStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Token 鉴权自动配置。
 * <p>
 * 在引入 Redis 的 Servlet Web 服务中自动注册 {@link TokenStore} 与 {@link BasicTokenFilter}。
 * </p>
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET) // 仅 Servlet 服务生效
@ConditionalOnBean(RedisUtil.class) // 依赖 Redis 已配置
@EnableConfigurationProperties(SecureProperties.class) // 绑定 secure.* 白名单配置
public class TokenAutoConfiguration {

    /**
     * 注册 Redis Token 存储 Bean，供 system 写入、各服务校验读取。
     *
     * @param redisUtil Redis 操作工具
     * @return Token 会话存储实现
     */
    @Bean
    @ConditionalOnMissingBean // 允许业务服务自定义 TokenStore 实现
    public TokenStore tokenStore(RedisUtil redisUtil) {
        return new RedisTokenStore(redisUtil);
    }

    /**
     * 注册 Token 鉴权过滤器，拦截需登录的业务接口。
     *
     * @param secureProperties           白名单配置
     * @param tokenStore                 Token 存储
     * @param handlerExceptionResolver   用于将鉴权异常转为统一 JSON 响应
     * @return 过滤器注册信息
     */
    @Bean
    public FilterRegistrationBean<BasicTokenFilter> basicTokenFilter(
        SecureProperties secureProperties,
        TokenStore tokenStore,
        HandlerExceptionResolver handlerExceptionResolver) {
        FilterRegistrationBean<BasicTokenFilter> registration = new FilterRegistrationBean<>();
        // 创建过滤器实例并注入依赖
        registration.setFilter(new BasicTokenFilter(secureProperties, tokenStore, handlerExceptionResolver));
        registration.addUrlPatterns("/*"); // 拦截所有路径，内部通过白名单放行
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10); // 尽早执行，优先于业务逻辑
        registration.setName("basicTokenFilter");
        return registration;
    }

    /**
     * 注册 {@link LoginUserArgumentResolver}，支持 Controller 参数注入当前登录用户。
     *
     * @return WebMvc 扩展配置
     */
    @Bean
    public WebMvcConfigurer loginUserWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(new LoginUserArgumentResolver());
            }
        };
    }
}
