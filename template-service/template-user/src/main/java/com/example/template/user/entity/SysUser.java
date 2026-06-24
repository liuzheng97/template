package com.example.template.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.template.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统用户实体。
 * <p>
 * 对应数据库表 sys_user，存储账号、密码及基本信息。
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user") // 映射表名
@Schema(description = "系统用户实体")
public class SysUser extends BaseEntity {

    @Schema(description = "登录账号，唯一")
    private String account;

    @Schema(description = "登录密码（存储明文，生产环境应加密）")
    private String password;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "状态：1 启用，0 禁用")
    private Integer status;
}
