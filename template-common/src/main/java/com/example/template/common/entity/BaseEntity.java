package com.example.template.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据库实体基类。
 * <p>
 * 包含通用主键与时间字段，业务实体（如 SysUser、BizOrder）继承此类。
 * 时间字段由 {@link com.example.template.common.config.MybatisMetaObjectHandler} 自动填充。
 * </p>
 */
@Data
@Schema(description = "实体基类")
public class BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO) // 数据库自增主键
    @Schema(description = "主键ID")
    private Long id;

    @TableField(fill = FieldFill.INSERT) // 仅插入时填充
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时均填充
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
