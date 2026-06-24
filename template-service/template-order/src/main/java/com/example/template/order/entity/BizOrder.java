package com.example.template.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.template.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单实体。
 * <p>
 * 对应数据库表 biz_order，记录订单基本信息与状态。
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_order") // 映射表名
@Schema(description = "订单实体")
public class BizOrder extends BaseEntity {

    @Schema(description = "订单编号，业务唯一标识")
    private String orderNo;

    @Schema(description = "下单用户ID")
    private Long userId;

    @Schema(description = "下单用户账号")
    private String userAccount;

    @Schema(description = "订单金额，单位：元")
    private BigDecimal amount;

    @Schema(description = "订单状态：0待支付 1已支付 2已取消 3已完成")
    private Integer status;

    @Schema(description = "订单备注")
    private String remark;
}
