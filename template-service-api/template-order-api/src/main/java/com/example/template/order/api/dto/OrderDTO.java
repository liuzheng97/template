package com.example.template.order.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单数据传输对象。
 * <p>
 * 对外 API 与 Feign 内部调用均使用此对象。
 * </p>
 */
@Data
@Schema(description = "订单信息")
public class OrderDTO implements Serializable {

    @Schema(description = "订单主键ID")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "下单用户ID")
    private Long userId;

    @Schema(description = "下单用户账号")
    private String userAccount;

    @Schema(description = "订单金额（元）")
    private BigDecimal amount;

    @Schema(description = "订单状态：0待支付 1已支付 2已取消 3已完成")
    private Integer status;

    @Schema(description = "订单状态中文描述")
    private String statusName;

    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
