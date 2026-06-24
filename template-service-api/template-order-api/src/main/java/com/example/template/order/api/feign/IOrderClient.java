package com.example.template.order.api.feign;

import com.example.template.common.api.R;
import com.example.template.order.api.dto.OrderDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 订单服务 Feign 客户端。
 * <p>
 * 供其他微服务通过内部接口查询订单详情。
 * </p>
 */
@FeignClient(value = "order", path = "/order") // 目标服务名 order，统一前缀 /order
public interface IOrderClient {

    /**
     * 根据订单编号查询订单详情（内部调用）。
     *
     * @param orderNo 订单编号
     * @return 订单信息
     */
    @Operation(summary = "根据订单编号查询订单(内部)")
    @GetMapping("/internal/{orderNo}")
    R<OrderDTO> getByOrderNo(@Parameter(description = "订单编号") @PathVariable("orderNo") String orderNo);
}
