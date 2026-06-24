package com.example.template.common.exception;

/**
 * 业务异常。
 * <p>
 * 用于可预期的业务错误（如账号锁定、订单状态非法），
 * 由 {@link GlobalExceptionHandler} 捕获并转为统一 JSON 响应。
 * </p>
 */
public class ServiceException extends RuntimeException {

    /** HTTP 语义错误码，默认 500 */
    private final int code;

    /**
     * 使用默认错误码 500 构造业务异常。
     *
     * @param message 错误提示
     */
    public ServiceException(String message) {
        this(500, message);
    }

    /**
     * 使用自定义错误码构造业务异常。
     *
     * @param code    错误码，如 401
     * @param message 错误提示
     */
    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * @return 业务错误码
     */
    public int getCode() {
        return code;
    }
}
