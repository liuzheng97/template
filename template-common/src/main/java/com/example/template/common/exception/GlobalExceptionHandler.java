package com.example.template.common.exception;

import com.example.template.common.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器。
 * <p>
 * 仅在 Servlet Web 服务中生效；网关为 WebFlux，不加载此类。
 * </p>
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class GlobalExceptionHandler {

    /**
     * 处理业务异常，返回自定义错误码与消息。
     */
    @ExceptionHandler(ServiceException.class)
    public R<Void> handleServiceException(ServiceException e) {
        return R.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理 Token 无效异常，统一返回 HTTP 语义 401。
     */
    @ExceptionHandler(InvalidTokenException.class)
    public R<Void> handleInvalidTokenException(InvalidTokenException e) {
        return R.fail(401, e.getMessage());
    }

    /**
     * 处理未捕获的系统异常，避免堆栈信息泄露给前端。
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return R.fail("系统异常，请稍后重试");
    }
}
