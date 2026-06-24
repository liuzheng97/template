package com.example.template.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录用户简要信息。
 * <p>
 * 作为 {@link TokenInfo} 的组成部分返回给前端，不含敏感字段。
 * </p>
 */
@Data
@Schema(description = "登录用户信息")
public class UserInfo implements Serializable {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "登录账号")
    private String account;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "状态：1 启用，0 禁用")
    private Integer status;
}
