package com.example.template.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.template.user.entity.SysUser;

/**
 * 用户业务接口。
 * <p>
 * 继承 MyBatis-Plus {@link IService}，提供基础 CRUD 能力。
 * </p>
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据账号查询用户（精确匹配）。
     *
     * @param account 登录账号
     * @return 用户实体，不存在时返回 null
     */
    SysUser getByAccount(String account);
}
