package com.example.template.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.template.order.api.dto.OrderCreateRequest;
import com.example.template.order.api.dto.OrderDTO;
import com.example.template.order.entity.BizOrder;

/**
 * 订单业务接口。
 */
public interface OrderService extends IService<BizOrder> {

    /**
     * 创建待支付订单。
     *
     * @param request 创建请求
     * @return 订单 DTO
     */
    OrderDTO createOrder(OrderCreateRequest request);

    /**
     * 根据订单编号查询详情。
     *
     * @param orderNo 订单编号
     * @return 订单 DTO
     */
    OrderDTO getByOrderNo(String orderNo);

    /**
     * 分页查询订单，支持按用户和状态筛选。
     *
     * @param current 当前页
     * @param size    每页条数
     * @param userId  用户 ID，可为 null
     * @param status  订单状态，可为 null
     * @return 分页结果
     */
    Page<OrderDTO> pageOrders(long current, long size, Long userId, Integer status);

    /**
     * 支付订单，仅待支付状态可支付。
     *
     * @param orderNo 订单编号
     * @return 更新后的订单 DTO
     */
    OrderDTO payOrder(String orderNo);

    /**
     * 取消订单，已完成/已取消状态不可再取消。
     *
     * @param orderNo 订单编号
     * @return 更新后的订单 DTO
     */
    OrderDTO cancelOrder(String orderNo);
}
