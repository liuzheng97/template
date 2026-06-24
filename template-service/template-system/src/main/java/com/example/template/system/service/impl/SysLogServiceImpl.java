package com.example.template.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.example.template.system.api.dto.SysLogDTO;
import com.example.template.system.es.SysLog;
import com.example.template.system.es.SysLogRepository;
import com.example.template.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 系统日志业务实现。
 * <p>
 * 通过 ObjectProvider 延迟获取 ES Repository，避免 ES 关闭时强依赖导致启动失败。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class SysLogServiceImpl implements SysLogService {

    /** ES 仓储提供者，ES 未启用时 getIfAvailable 返回 null */
    private final ObjectProvider<SysLogRepository> sysLogRepositoryProvider;

    /** ES 开关，来自 spring.elasticsearch.enabled，默认 false */
    @Value("${spring.elasticsearch.enabled:false}")
    private boolean elasticsearchEnabled;

    @Override
    public boolean isEnabled() {
        // 配置开启且 Repository Bean 存在时才认为 ES 可用
        return elasticsearchEnabled && sysLogRepositoryProvider.getIfAvailable() != null;
    }

    @Override
    public SysLogDTO save(SysLogDTO dto) {
        if (!isEnabled()) {
            return dto;
        }
        // 1. DTO 转 ES 文档实体
        SysLog log = new SysLog();
        BeanUtil.copyProperties(dto, log);
        // 2. 补全 ID 与创建时间
        if (log.getId() == null) {
            log.setId(IdUtil.fastSimpleUUID());
        }
        if (log.getCreateTime() == null) {
            log.setCreateTime(LocalDateTime.now());
        }
        // 3. 写入 Elasticsearch
        SysLog saved = sysLogRepositoryProvider.getObject().save(log);
        return toDto(saved);
    }

    @Override
    public List<SysLogDTO> searchByModule(String module) {
        if (!isEnabled()) {
            return Collections.emptyList();
        }
        // 按模块名称查询并转 DTO 列表
        return sysLogRepositoryProvider.getObject().findByModule(module).stream()
            .map(this::toDto)
            .toList();
    }

    /**
     * ES 文档实体转 DTO。
     *
     * @param log ES 日志文档
     * @return 日志 DTO
     */
    private SysLogDTO toDto(SysLog log) {
        SysLogDTO dto = new SysLogDTO();
        BeanUtil.copyProperties(log, dto);
        return dto;
    }
}
