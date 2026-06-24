package com.example.template.common.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一 API 响应包装类。
 * <p>
 * 所有 Controller 对外返回均使用此类，保证前端解析格式一致。
 * </p>
 *
 * @param <T> 业务数据类型
 */
@Data
@Schema(description = "统一响应")
public class R<T> implements Serializable {

    @Schema(description = "状态码，200 表示成功")
    private int code;

    @Schema(description = "提示信息")
    private String msg;

    @Schema(description = "业务数据")
    private T data;

    /**
     * 返回成功响应。
     *
     * @param data 业务数据
     * @param <T>  数据类型
     * @return 成功响应体
     */
    public static <T> R<T> data(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    /**
     * 返回失败响应（默认错误码 500）。
     *
     * @param msg 错误提示
     * @param <T> 数据类型
     * @return 失败响应体
     */
    public static <T> R<T> fail(String msg) {
        R<T> r = new R<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }

    /**
     * 返回自定义错误码的失败响应。
     *
     * @param code 错误码
     * @param msg  错误提示
     * @param <T>  数据类型
     * @return 失败响应体
     */
    public static <T> R<T> fail(int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    /**
     * 判断响应是否成功。
     *
     * @return code == 200 时返回 true
     */
    public boolean isSuccess() {
        return code == 200;
    }
}
