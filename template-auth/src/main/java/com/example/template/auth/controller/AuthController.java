package com.example.template.auth.controller;

import com.example.template.auth.service.AuthService;
import com.example.template.common.api.R;
import com.example.template.common.constant.TokenConstant;
import com.example.template.common.model.TokenInfo;
import com.example.template.common.util.ServletUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证授权接口。
 * <p>
 * 对外提供登录、登出等 HTTP 入口，Token 实际由 system 服务生成。
 * </p>
 */
@Tag(name = "认证授权", description = "登录、登出等认证相关接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    /** 认证业务服务 */
    private final AuthService authService;

    @Operation(summary = "账号密码登录", description = "30分钟内账号失败5次或同IP失败10次将锁定5分钟")
    @PostMapping("/token")
    public R<TokenInfo> token(
        @Parameter(description = "账号", required = true) @RequestParam String account,
        @Parameter(description = "密码", required = true) @RequestParam String password,
        @Parameter(description = "登陆端类型", required = true) @RequestParam String clientType,
        HttpServletRequest request) {
        String clientIp = ServletUtils.resolveClientIp(request);
        return R.data(authService.login(account, password, clientType, clientIp));
    }

    @Operation(summary = "退出登录", description = "注销当前访问令牌")
    @GetMapping("/logout")
    public R<Boolean> logout(HttpServletRequest request) {
        // 1. 从请求头读取 token-auth
        String header = request.getHeader(TokenConstant.TOKEN_HEADER);
        // 2. 解析 bearer 前缀或直接取 Token 值
        if (header != null && header.toLowerCase().startsWith(TokenConstant.BEARER_PREFIX)) {
            authService.logout(header.substring(TokenConstant.BEARER_PREFIX.length()).trim());
        } else if (header != null && !header.isBlank()) {
            authService.logout(header.trim());
        }
        return R.data(true);
    }
}
