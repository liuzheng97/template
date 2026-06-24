package com.example.template.system.feign;

import cn.hutool.core.util.IdUtil;
import com.example.template.common.api.R;
import com.example.template.common.constant.TokenConstant;
import com.example.template.common.model.UserDetail;
import com.example.template.common.token.TokenStore;
import com.example.template.system.api.feign.ITokenClient;
import com.example.template.user.api.dto.UserDTO;
import com.example.template.user.api.feign.IUserClient;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * Token Feign 实现类。
 * <p>
 * 登录核心逻辑：调用 user 服务验密 → 生成 UUID Token → 写入 Redis 会话。
 * 由 auth 服务通过 {@link ITokenClient} 远程调用，不对外暴露 HTTP。
 * </p>
 */
@Hidden // 不在 Swagger 文档中展示
@RestController
@RequiredArgsConstructor
public class TokenClient implements ITokenClient {

    /** 用户服务 Feign 客户端，用于校验账号密码 */
    private final IUserClient userClient;
    /** Token 会话存储，写入 Redis */
    private final TokenStore tokenStore;

    @Override
    public R<UserDetail> genToken(String account, String password, String clientType) {
        // 1. 调用 user 服务校验账号密码
        R<UserDTO> response = userClient.validate(account, password);
        if (!response.isSuccess() || response.getData() == null) {
            return R.fail(response.getMsg() != null ? response.getMsg() : "账号或密码错误");
        }
        UserDTO user = response.getData();
        // 2. 检查账号状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            return R.fail("账号已禁用");
        }
        // 3. 按客户端类型确定 Token 过期时间
        long expireSeconds = TokenConstant.expireSeconds(clientType);
        // 4. 生成访问令牌
        String token = IdUtil.fastSimpleUUID();
        // 5. 组装用户会话详情
        UserDetail detail = new UserDetail();
        detail.setUserId(user.getId());
        detail.setAccount(user.getAccount());
        detail.setName(user.getName());
        detail.setToken(token);
        detail.setExpiresIn(expireSeconds);
        // 6. 写入 Redis
        tokenStore.saveToken(token, detail, expireSeconds);
        return R.data(detail);
    }

    @Override
    public R<Boolean> tokenLogout(String token) {
        // 1. 根据 Token 查询会话
        UserDetail detail = tokenStore.getByToken(token);
        if (detail != null) {
            // 2. 清除 Redis 中的双向映射
            tokenStore.removeToken(token, detail.getUserId());
        }
        return R.data(true);
    }
}
