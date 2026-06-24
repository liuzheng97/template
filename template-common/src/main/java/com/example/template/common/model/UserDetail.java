package com.example.template.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录用户会话详情。
 * <p>
 * 由 system 服务在登录时生成并序列化存入 Redis；
 * {@link com.example.template.common.filter.BasicTokenFilter} 校验时反序列化使用，
 * 并写入 {@link com.example.template.common.context.UserContext} 供业务层获取登录人信息。
 * </p>
 */
@Data
@Schema(description = "登录用户会话详情")
public class UserDetail implements Serializable {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "登录账号")
    private String account;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "状态：1 启用，0 禁用")
    private Integer status;

    @Schema(description = "访问令牌 UUID")
    private String token;

    @Schema(description = "过期时间，单位：秒")
    private Long expiresIn;
}
