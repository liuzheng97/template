package com.example.template.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.template.common.constant.OrderConstant;
import com.example.template.common.exception.ServiceException;
import com.example.template.common.redis.RedisUtil;
import com.example.template.order.api.dto.OrderCreateRequest;
import com.example.template.order.api.dto.OrderDTO;
import com.example.template.order.entity.BizOrder;
import com.example.template.order.enums.OrderStatusEnum;
import com.example.template.order.mapper.BizOrderMapper;
import com.example.template.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 订单业务实现。
 * <p>
 * 负责订单全生命周期：创建、查询、支付、取消，并演示 Redis 缓存联动。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<BizOrderMapper, BizOrder> implements OrderService {

    /** Redis 工具，用于订单详情缓存 */
    private final RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDTO createOrder(OrderCreateRequest request) {
        // 1. 组装订单实体
        BizOrder order = new BizOrder();
        order.setOrderNo(generateOrderNo());
        order.setUserId(request.getUserId());
        order.setUserAccount(request.getUserAccount());
        order.setAmount(request.getAmount());
        order.setStatus(OrderStatusEnum.PENDING.getCode()); // 初始状态：待支付
        order.setRemark(request.getRemark());
        // 2. 持久化到数据库
        save(order);
        // 3. 写入 Redis 缓存
        cacheOrder(order);
        return toDto(order);
    }

    @Override
    public OrderDTO getByOrderNo(String orderNo) {
        String cacheKey = OrderConstant.ORDER_CACHE_PREFIX + orderNo;
        // 尝试读取缓存（模版示例：仍查库保证数据一致性）
        String cached = redisUtil.get(cacheKey);
        if (StringUtils.hasText(cached)) {
            // 缓存命中时仍查库，保证模版示例中 Redis 与 DB 联动可见
        }
        // 从数据库查询订单
        BizOrder order = getOne(new LambdaQueryWrapper<BizOrder>().eq(BizOrder::getOrderNo, orderNo));
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        cacheOrder(order);
        return toDto(order);
    }

    @Override
    public Page<OrderDTO> pageOrders(long current, long size, Long userId, Integer status) {
        // 1. 构建动态查询条件
        LambdaQueryWrapper<BizOrder> wrapper = new LambdaQueryWrapper<BizOrder>()
            .eq(userId != null, BizOrder::getUserId, userId)
            .eq(status != null, BizOrder::getStatus, status)
            .orderByDesc(BizOrder::getCreateTime);
        // 2. 分页查询并转 DTO
        Page<BizOrder> page = page(new Page<>(current, size), wrapper);
        Page<OrderDTO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(page.getRecords().stream().map(this::toDto).toList());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDTO payOrder(String orderNo) {
        BizOrder order = getRequiredOrder(orderNo);
        // 仅待支付订单允许支付
        if (order.getStatus() == null || order.getStatus() != OrderStatusEnum.PENDING.getCode()) {
            throw new ServiceException("仅待支付订单可支付");
        }
        order.setStatus(OrderStatusEnum.PAID.getCode());
        updateById(order);
        cacheOrder(order);
        return toDto(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDTO cancelOrder(String orderNo) {
        BizOrder order = getRequiredOrder(orderNo);
        // 已完成或已取消的订单不可再取消
        if (OrderStatusEnum.COMPLETED.getCode() == order.getStatus()
            || OrderStatusEnum.CANCELLED.getCode() == order.getStatus()) {
            throw new ServiceException("当前订单状态不可取消");
        }
        order.setStatus(OrderStatusEnum.CANCELLED.getCode());
        updateById(order);
        // 取消后清除缓存
        redisUtil.del(OrderConstant.ORDER_CACHE_PREFIX + orderNo);
        return toDto(order);
    }

    /**
     * 查询订单，不存在时抛业务异常。
     *
     * @param orderNo 订单编号
     * @return 订单实体
     */
    private BizOrder getRequiredOrder(String orderNo) {
        BizOrder order = getOne(new LambdaQueryWrapper<BizOrder>().eq(BizOrder::getOrderNo, orderNo));
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        return order;
    }

    /**
     * 将订单编号写入 Redis 缓存（模版示例简化实现）。
     *
     * @param order 订单实体
     */
    private void cacheOrder(BizOrder order) {
        redisUtil.set(OrderConstant.ORDER_CACHE_PREFIX + order.getOrderNo(), order.getOrderNo(),
            OrderConstant.ORDER_CACHE_TTL_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * 使用雪花算法生成唯一订单编号。
     *
     * @return 订单编号，格式 ORD + 雪花 ID
     */
    private String generateOrderNo() {
        return "ORD" + IdUtil.getSnowflakeNextIdStr();
    }

    /**
     * 实体转 DTO，并填充状态描述。
     *
     * @param order 订单实体
     * @return 订单 DTO
     */
    private OrderDTO toDto(BizOrder order) {
        OrderDTO dto = new OrderDTO();
        BeanUtil.copyProperties(order, dto);
        dto.setStatusName(OrderStatusEnum.getDescByCode(order.getStatus()));
        return dto;
    }
}
