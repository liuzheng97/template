package com.example.template.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举。
 * <p>
 * 与 biz_order.status 字段对应，提供状态码与中文描述。
 * </p>
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    /** 待支付，可支付或取消 */
    PENDING(0, "待支付"),

    /** 已支付，可取消（未完成前） */
    PAID(1, "已支付"),

    /** 已取消，终态 */
    CANCELLED(2, "已取消"),

    /** 已完成，终态 */
    COMPLETED(3, "已完成");

    /** 数据库存储的状态码 */
    private final int code;

    /** 状态中文描述 */
    private final String desc;

    /**
     * 根据状态码获取中文描述。
     *
     * @param code 状态码
     * @return 描述文本，未知状态返回「未知状态」
     */
    public static String getDescByCode(Integer code) {
        if (code == null) {
            return "";
        }
        for (OrderStatusEnum value : values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        return "未知状态";
    }
}
