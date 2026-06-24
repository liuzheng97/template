package com.example.template.common.exception;

/**
 * Token 无效或未登录异常。
 * <p>
 * 由 {@link com.example.template.common.filter.BasicTokenFilter} 抛出，
 * 由 {@link GlobalExceptionHandler} 捕获并返回 401。
 * </p>
 */
public class InvalidTokenException extends RuntimeException {

    /**
     * @param message 错误提示，如「未登录或 Token 缺失」
     */
    public InvalidTokenException(String message) {
        super(message);
    }
}
