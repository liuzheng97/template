package com.example.template.system.es;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * 系统日志 Elasticsearch 文档实体。
 * <p>
 * 映射索引 sys_log，用于存储各模块的操作日志。
 * </p>
 */
@Data
@Document(indexName = "sys_log") // ES 索引名
@Schema(description = "系统日志ES文档")
public class SysLog {

    @Id // ES 文档主键
    @Schema(description = "日志ID")
    private String id;

    @Field(type = FieldType.Keyword) // 精确匹配，用于按模块筛选
    @Schema(description = "模块名称，如 auth、order")
    private String module;

    @Field(type = FieldType.Text) // 全文检索
    @Schema(description = "日志内容")
    private String content;

    @Field(type = FieldType.Date) // 日期类型
    @Schema(description = "日志创建时间")
    private LocalDateTime createTime;
}
