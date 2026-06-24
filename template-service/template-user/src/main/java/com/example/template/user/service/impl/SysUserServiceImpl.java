package com.example.template.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.template.user.entity.SysUser;
import com.example.template.user.mapper.SysUserMapper;
import com.example.template.user.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * 用户业务实现。
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser getByAccount(String account) {
        // 按账号精确查询，期望唯一
        return getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount, account));
    }
}
