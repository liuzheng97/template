package com.example.template.user.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户数据传输对象。
 * <p>
 * 对外 API 与 Feign 内部调用均使用此对象，不包含密码字段。
 * </p>
 */
@Data
@Schema(description = "用户信息")
public class UserDTO implements Serializable {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "登录账号")
    private String account;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "状态：1 启用，0 禁用")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
