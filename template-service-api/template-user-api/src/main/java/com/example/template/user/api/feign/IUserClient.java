package com.example.template.user.api.feign;

import com.example.template.common.api.R;
import com.example.template.user.api.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务 Feign 客户端。
 * <p>
 * 由 system 服务的 TokenClient 调用，完成账号查询与密码校验。
 * </p>
 */
@FeignClient(value = "user", path = "/user") // 目标服务名 user，统一前缀 /user
public interface IUserClient {

    /**
     * 根据账号查询用户。
     *
     * @param account 登录账号
     * @return 用户信息
     */
    @Operation(summary = "根据账号查询用户(内部)")
    @GetMapping("/internal/by-account")
    R<UserDTO> getByAccount(@Parameter(description = "登录账号") @RequestParam("account") String account);

    /**
     * 校验账号密码。
     *
     * @param account  登录账号
     * @param password 明文密码
     * @return 校验通过时返回用户信息
     */
    @Operation(summary = "校验账号密码(内部)")
    @PostMapping("/internal/validate")
    R<UserDTO> validate(
        @Parameter(description = "登录账号") @RequestParam("account") String account,
        @Parameter(description = "明文密码") @RequestParam("password") String password);
}
