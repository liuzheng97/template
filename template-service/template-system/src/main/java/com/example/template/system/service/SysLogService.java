package com.example.template.system.service;

import com.example.template.system.api.dto.SysLogDTO;

import java.util.List;

/**
 * 系统日志业务接口。
 * <p>
 * 封装 Elasticsearch 日志的写入与检索，ES 关闭时优雅降级。
 * </p>
 */
public interface SysLogService {

    /**
     * 判断 Elasticsearch 是否已启用且仓储可用。
     *
     * @return true 表示可正常读写 ES
     */
    boolean isEnabled();

    /**
     * 保存日志到 Elasticsearch。
     *
     * @param dto 日志 DTO
     * @return 保存后的日志 DTO
     */
    SysLogDTO save(SysLogDTO dto);

    /**
     * 按模块名称检索日志列表。
     *
     * @param module 模块名称
     * @return 日志列表，ES 未启用时返回空列表
     */
    List<SysLogDTO> searchByModule(String module);
}
