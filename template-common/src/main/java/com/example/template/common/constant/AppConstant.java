package com.example.template.common.constant;

/**
 * 应用级常量定义。
 * <p>
 * 包含包扫描路径与各微服务在 Nacos 中的注册名称。
 * </p>
 */
public interface AppConstant {

    /** Feign 客户端扫描的基础包路径 */
    String BASE_PACKAGES = "com.example.template";

    /** 网关服务注册名，对应 spring.application.name=gateway */
    String APPLICATION_GATEWAY_NAME = "gateway";

    /** 认证服务注册名，对应 spring.application.name=auth */
    String APPLICATION_AUTH_NAME = "auth";

    /** 系统服务注册名，对应 spring.application.name=system */
    String APPLICATION_SYSTEM_NAME = "system";

    /** 用户服务注册名，对应 spring.application.name=user */
    String APPLICATION_USER_NAME = "user";

    /** 订单服务注册名，对应 spring.application.name=order */
    String APPLICATION_ORDER_NAME = "order";
}
