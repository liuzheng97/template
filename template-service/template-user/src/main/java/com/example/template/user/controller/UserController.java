package com.example.template.user.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.template.common.api.R;
import com.example.template.user.api.dto.UserDTO;
import com.example.template.user.entity.SysUser;
import com.example.template.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理接口。
 * <p>
 * 对外提供用户分页查询；内部接口供 system 服务 Feign 调用完成验密。
 * </p>
 */
@Tag(name = "用户管理", description = "用户查询与内部认证接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    /** 用户业务服务 */
    private final SysUserService sysUserService;

    @Operation(summary = "用户分页列表", description = "按创建时间倒序分页查询用户")
    @GetMapping("/page")
    public R<Page<UserDTO>> page(
        @Parameter(description = "当前页，从 1 开始") @RequestParam(defaultValue = "1") long current,
        @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") long size) {
        // 1. 分页查询用户，按创建时间倒序
        Page<SysUser> page = sysUserService.page(new Page<>(current, size),
            new LambdaQueryWrapper<SysUser>().orderByDesc(SysUser::getCreateTime));
        // 2. 实体转 DTO 返回
        Page<UserDTO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(page.getRecords().stream().map(this::toDto).toList());
        return R.data(result);
    }

    @Operation(summary = "根据账号查询用户(内部)", description = "供其他微服务 Feign 调用，路径含 /internal 免 Token")
    @GetMapping("/internal/by-account")
    public R<UserDTO> getByAccount(@Parameter(description = "登录账号") @RequestParam String account) {
        SysUser user = sysUserService.getByAccount(account);
        if (user == null) {
            return R.fail("用户不存在");
        }
        return R.data(toDto(user));
    }

    @Operation(summary = "校验账号密码(内部)", description = "供 system TokenClient 验密，路径含 /internal 免 Token")
    @PostMapping("/internal/validate")
    public R<UserDTO> validate(
        @Parameter(description = "登录账号") @RequestParam String account,
        @Parameter(description = "明文密码") @RequestParam String password) {
        // 1. 根据账号查用户
        SysUser user = sysUserService.getByAccount(account);
        // 2. 比对明文密码（生产环境应使用加密比对）
        if (user == null || !password.equals(user.getPassword())) {
            return R.fail("账号或密码错误");
        }
        return R.data(toDto(user));
    }

    /**
     * 实体转 DTO，不暴露密码字段。
     *
     * @param user 数据库用户实体
     * @return 用户 DTO
     */
    private UserDTO toDto(SysUser user) {
        UserDTO dto = new UserDTO();
        BeanUtil.copyProperties(user, dto);
        return dto;
    }
}
