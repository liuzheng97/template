package com.example.template.system.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统日志数据传输对象。
 * <p>
 * 用于 HTTP 接口与 ES 文档之间的数据传递。
 * </p>
 */
@Data
@Schema(description = "系统日志")
public class SysLogDTO implements Serializable {

    @Schema(description = "日志ID")
    private String id;

    @Schema(description = "模块名称")
    private String module;

    @Schema(description = "日志内容")
    private String content;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
