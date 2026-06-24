package com.example.template.order.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 创建订单请求对象。
 * <p>
 * 由订单 Controller 接收，经 {@code @Valid} 校验后传入 Service。
 * </p>
 */
@Data
@Schema(description = "创建订单请求")
public class OrderCreateRequest implements Serializable {

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "下单用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @NotBlank(message = "用户账号不能为空")
    @Schema(description = "下单用户账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    private String userAccount;

    @NotNull(message = "订单金额不能为空")
    @DecimalMin(value = "0.01", message = "订单金额必须大于0")
    @Schema(description = "订单金额（元）", requiredMode = Schema.RequiredMode.REQUIRED, example = "99.90")
    private BigDecimal amount;

    @Schema(description = "订单备注", example = "测试订单")
    private String remark;
}
