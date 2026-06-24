package com.example.template.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.template.common.api.R;
import com.example.template.order.api.dto.OrderCreateRequest;
import com.example.template.order.api.dto.OrderDTO;
import com.example.template.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单接口。
 * <p>
 * 提供订单创建、分页查询、详情、支付、取消等 HTTP 入口。
 * 除 /internal 外均需携带 Token 访问。
 * </p>
 */
@Tag(name = "订单管理", description = "订单创建、查询、支付、取消")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    /** 订单业务服务 */
    private final OrderService orderService;

    @Operation(summary = "创建订单", description = "创建一笔待支付订单")
    @PostMapping
    public R<OrderDTO> create(@Valid @RequestBody OrderCreateRequest request) {
        return R.data(orderService.createOrder(request));
    }

    @Operation(summary = "订单分页列表", description = "支持按用户ID和状态筛选")
    @GetMapping("/page")
    public R<Page<OrderDTO>> page(
        @Parameter(description = "当前页，从 1 开始") @RequestParam(defaultValue = "1") long current,
        @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") long size,
        @Parameter(description = "用户ID，可选筛选条件") @RequestParam(required = false) Long userId,
        @Parameter(description = "订单状态：0待支付 1已支付 2已取消 3已完成") @RequestParam(required = false) Integer status) {
        return R.data(orderService.pageOrders(current, size, userId, status));
    }

    @Operation(summary = "根据订单编号查询", description = "对外查询订单详情，需 Token")
    @GetMapping("/{orderNo}")
    public R<OrderDTO> detail(@Parameter(description = "订单编号") @PathVariable String orderNo) {
        return R.data(orderService.getByOrderNo(orderNo));
    }

    @Operation(summary = "根据订单编号查询(内部)", description = "供其他微服务 Feign 调用，路径含 /internal 免 Token")
    @GetMapping("/internal/{orderNo}")
    public R<OrderDTO> internalDetail(@Parameter(description = "订单编号") @PathVariable String orderNo) {
        return R.data(orderService.getByOrderNo(orderNo));
    }

    @Operation(summary = "支付订单", description = "将待支付订单变更为已支付")
    @PostMapping("/{orderNo}/pay")
    public R<OrderDTO> pay(@Parameter(description = "订单编号") @PathVariable String orderNo) {
        return R.data(orderService.payOrder(orderNo));
    }

    @Operation(summary = "取消订单", description = "取消待支付或已支付订单")
    @PostMapping("/{orderNo}/cancel")
    public R<OrderDTO> cancel(@Parameter(description = "订单编号") @PathVariable String orderNo) {
        return R.data(orderService.cancelOrder(orderNo));
    }
}
