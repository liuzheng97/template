package com.example.template.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录成功后返回给前端的 Token 信息。
 * <p>
 * 由 auth 服务组装，对外 HTTP 响应使用；Redis 中存储的是 {@link UserDetail}。
 * </p>
 */
@Data
@Schema(description = "Token信息")
public class TokenInfo implements Serializable {

    @Schema(description = "访问令牌，请求业务接口时放入 Header token-auth")
    private String accessToken;

    @Schema(description = "过期时间，单位：秒")
    private Long expiresIn;

    @Schema(description = "当前登录用户简要信息")
    private UserInfo userInfo;
}
