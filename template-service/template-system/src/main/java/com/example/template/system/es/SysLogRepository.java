package com.example.template.system.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 系统日志 Elasticsearch 仓储接口。
 * <p>
 * 由 Spring Data ES 自动实现，仅在 {@code spring.elasticsearch.enabled=true} 时注册。
 * </p>
 */
public interface SysLogRepository extends ElasticsearchRepository<SysLog, String> {

    /**
     * 按模块名称检索日志列表。
     *
     * @param module 模块名称（Keyword 精确匹配）
     * @return 日志文档列表
     */
    List<SysLog> findByModule(String module);
}
