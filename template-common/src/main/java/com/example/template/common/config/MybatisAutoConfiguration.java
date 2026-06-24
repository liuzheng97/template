package com.example.template.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * MyBatis-Plus 条件自动配置包装类。
 * <p>
 * 通过独立配置类 + {@link ConditionalOnClass} 避免 auth 等无 MyBatis 依赖的服务
 * 在 {@code @Import} 阶段加载 MyBatis 相关类导致启动失败。
 * </p>
 */
@Configuration
@ConditionalOnClass(name = "com.baomidou.mybatisplus.core.handlers.MetaObjectHandler") // 仅当 classpath 存在 MyBatis 时加载
@Import({
    MybatisPlusConfig.class,      // 分页插件等
    MybatisMetaObjectHandler.class // 自动填充 createTime/updateTime
})
public class MybatisAutoConfiguration {
}
