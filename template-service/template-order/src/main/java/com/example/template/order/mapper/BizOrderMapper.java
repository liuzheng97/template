package com.example.template.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.template.order.entity.BizOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单 Mapper 接口。
 * <p>
 * 对应数据库表 biz_order，由 MyBatis-Plus 提供基础 CRUD。
 * </p>
 */
@Mapper
public interface BizOrderMapper extends BaseMapper<BizOrder> {
}
