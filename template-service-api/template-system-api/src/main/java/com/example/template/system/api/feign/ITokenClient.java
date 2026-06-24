package com.example.template.system.api.feign;

import com.example.template.common.api.R;
import com.example.template.common.constant.AppConstant;
import com.example.template.common.model.UserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Token 服务 Feign 客户端。
 * <p>
 * 由 auth 服务调用，实际实现在 system 服务的 {@code TokenClient}。
 * </p>
 */
@FeignClient(name = AppConstant.APPLICATION_SYSTEM_NAME) // 注册名 system
public interface ITokenClient {

    /** Feign 内部接口路径前缀，不经过网关对外暴露 */
    String API_PREFIX = "/feign/sys/token";

    /**
     * 校验账号密码并生成 Token 会话。
     *
     * @param account    账号
     * @param password   密码
     * @param clientType 客户端类型：web / app/weixin，决定 Token 有效期
     * @return 含 Token 的用户会话
     */
    @Operation(summary = "生成访问令牌(内部)")
    @GetMapping(API_PREFIX + "/gen-token")
    R<UserDetail> genToken(
        @Parameter(description = "账号") @RequestParam("account") String account,
        @Parameter(description = "密码") @RequestParam("password") String password,
        @Parameter(description = "客户端类型：web/app/weixin") @RequestParam("clientType") String clientType);

    /**
     * 注销 Token 会话，清除 Redis。
     *
     * @param token 访问令牌
     * @return 是否成功
     */
    @Operation(summary = "注销访问令牌(内部)")
    @GetMapping(API_PREFIX + "/token-logout")
    R<Boolean> tokenLogout(@Parameter(description = "访问令牌") @RequestParam("token") String token);
}
