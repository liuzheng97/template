package com.example.template.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.template.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 接口。
 * <p>
 * 对应数据库表 sys_user，由 MyBatis-Plus 提供基础 CRUD。
 * </p>
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
