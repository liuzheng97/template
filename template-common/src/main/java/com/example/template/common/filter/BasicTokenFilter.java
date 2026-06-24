package com.example.template.common.filter;

import cn.hutool.core.util.StrUtil;
import com.example.template.common.constant.TokenConstant;
import com.example.template.common.context.UserContext;
import com.example.template.common.exception.InvalidTokenException;
import com.example.template.common.model.UserDetail;
import com.example.template.common.props.SecureProperties;
import com.example.template.common.token.TokenStore;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Token 鉴权过滤器。
 * <p>
 * 拦截业务接口，从请求头提取 Token，校验 Redis 中是否存在有效会话。
 * 白名单路径（内部接口、文档、登录等）直接放行。
 * 校验通过后将 {@link UserDetail} 写入 {@link com.example.template.common.context.UserContext}，
 * 供业务方法通过 {@code UserContext.get()} 获取登录人信息。
 * </p>
 */
@Slf4j
@RequiredArgsConstructor
public class BasicTokenFilter extends OncePerRequestFilter {

    /** 安全白名单配置 */
    private final SecureProperties secureProperties;
    /** Token 会话存储，用于校验 Token 是否有效 */
    private final TokenStore tokenStore;
    /** 异常解析器，将鉴权失败转为 HTTP 401 响应 */
    private final HandlerExceptionResolver handlerExceptionResolver;
    /** Ant 风格路径匹配器，用于白名单判断 */
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String uri = request.getRequestURI();
        // 1. 白名单或 OPTIONS 预检请求直接放行
        if (shouldSkip(uri) || "OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // 2. 从请求头提取 Token
            String token = extractToken(request);
            if (StrUtil.isBlank(token)) {
                throw new InvalidTokenException("未登录或 Token 缺失");
            }
            // 3. 从 Redis 查询会话，不存在则 Token 已过期或被注销
            UserDetail detail = tokenStore.getByToken(token);
            if (detail == null) {
                throw new InvalidTokenException("Token 已失效，请重新登录");
            }
            // 4. 写入当前线程登录上下文，供 Controller / Service 直接获取
            UserContext.set(detail);
            // 5. 校验通过，继续后续 Filter 与 Controller
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 6. 鉴权失败，交由全局异常处理器返回 JSON
            handlerExceptionResolver.resolveException(request, response, null, e);
        } finally {
            // 7. 清理 ThreadLocal，防止线程池复用导致用户串号
            UserContext.clear();
        }
    }

    /**
     * 判断当前请求 URI 是否在白名单中，无需 Token 即可访问。
     *
     * @param uri 请求路径
     * @return true 表示跳过鉴权
     */
    private boolean shouldSkip(String uri) {
        // 内部接口与 Feign 接口始终放行
        if (uri.contains("/internal/") || uri.startsWith("/feign/")) {
            return true;
        }
        // 匹配 secure.exclude-patterns 配置的白名单
        for (String pattern : secureProperties.getExcludePatterns()) {
            if (pathMatcher.match(pattern, uri)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从请求头 token-auth 中提取访问令牌，支持 bearer 前缀。
     *
     * @param request HTTP 请求
     * @return Token 字符串，缺失时返回 null
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(TokenConstant.TOKEN_HEADER);
        if (StrUtil.isBlank(header)) {
            return null;
        }
        // 支持 "bearer {token}" 格式
        if (header.toLowerCase().startsWith(TokenConstant.BEARER_PREFIX)) {
            return header.substring(TokenConstant.BEARER_PREFIX.length()).trim();
        }
        return header.trim();
    }
}
