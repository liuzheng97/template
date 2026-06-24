package com.example.template.system.controller;

import com.example.template.common.api.R;
import com.example.template.system.api.dto.SysLogDTO;
import com.example.template.system.service.SysLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统管理接口。
 * <p>
 * 提供系统日志 ES 状态查询、写入与检索能力；ES 未启用时返回友好提示。
 * </p>
 */
@Tag(name = "系统管理", description = "系统日志与 ES 检索接口")
@RestController
@RequestMapping("/sys")
@RequiredArgsConstructor
public class SysLogController {

    /** 系统日志业务服务 */
    private final SysLogService sysLogService;

    @Operation(summary = "ES是否启用", description = "用于判断当前环境是否已接入 Elasticsearch")
    @GetMapping("/log/es-status")
    public R<Boolean> esStatus() {
        return R.data(sysLogService.isEnabled());
    }

    @Operation(summary = "保存系统日志到ES", description = "将日志文档写入 Elasticsearch 索引 sys_log")
    @PostMapping("/log")
    public R<SysLogDTO> save(@RequestBody SysLogDTO dto) {
        // ES 未启用时直接返回失败提示
        if (!sysLogService.isEnabled()) {
            return R.fail("Elasticsearch 未启用，请设置 spring.elasticsearch.enabled=true");
        }
        return R.data(sysLogService.save(dto));
    }

    @Operation(summary = "按模块检索系统日志", description = "根据模块名称查询 ES 中的日志列表")
    @GetMapping("/log/search")
    public R<List<SysLogDTO>> search(@Parameter(description = "模块名称，如 auth、order") @RequestParam String module) {
        if (!sysLogService.isEnabled()) {
            return R.fail("Elasticsearch 未启用，请设置 spring.elasticsearch.enabled=true");
        }
        return R.data(sysLogService.searchByModule(module));
    }
}
