package com.example.template.system.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch 仓储配置。
 * <p>
 * 仅在 {@code spring.elasticsearch.enabled=true} 时启用，
 * 扫描并注册 {@link com.example.template.system.es.SysLogRepository}。
 * </p>
 */
@Configuration
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true") // ES 显式开启才加载
@EnableElasticsearchRepositories(basePackages = "com.example.template.system.es") // 扫描 ES Repository
public class ElasticsearchConfig {
}
