package com.example.template.order;

import com.example.template.common.config.CommonAutoConfiguration;
import com.example.template.common.constant.AppConstant;
import com.example.template.common.launch.TemplateLauncher;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 订单服务启动类。
 * <p>
 * 职责：订单创建、查询、支付、取消等业务。
 * </p>
 */
@SpringBootApplication // 仅扫描 com.example.template.order
@EnableDiscoveryClient // 注册到 Nacos，服务名 order
@MapperScan("com.example.template.order.mapper") // 扫描 MyBatis Mapper 接口
@ImportAutoConfiguration(CommonAutoConfiguration.class) // 按需加载 Redis、Token 过滤器、OpenAPI 等
public class OrderApplication {

    public static void main(String[] args) {
        TemplateLauncher.run(AppConstant.APPLICATION_ORDER_NAME, OrderApplication.class, args);
    }
}
