package com.example.template.common.constant;

/**
 * 订单业务常量。
 */
public interface OrderConstant {

    /** 订单详情 Redis 缓存键前缀，完整 Key：order:detail:{orderNo} */
    String ORDER_CACHE_PREFIX = "order:detail:";

    /** 订单详情缓存过期时间（分钟） */
    long ORDER_CACHE_TTL_MINUTES = 30;
}
