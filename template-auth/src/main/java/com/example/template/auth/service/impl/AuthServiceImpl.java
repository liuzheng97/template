package com.example.template.auth.service.impl;

import com.example.template.auth.service.AuthService;
import com.example.template.common.api.R;
import com.example.template.common.constant.CacheNames;
import com.example.template.common.constant.TokenConstant;
import com.example.template.common.exception.ServiceException;
import com.example.template.common.model.TokenInfo;
import com.example.template.common.model.UserDetail;
import com.example.template.common.model.UserInfo;
import com.example.template.common.redis.RedisUtil;
import com.example.template.system.api.feign.ITokenClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 认证业务实现。
 * <p>
 * 登录入口在 auth，Token 生成委托 system 服务；本服务负责登录锁、验证码等辅助逻辑。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    /** system 服务 Token Feign 客户端 */
    private final ITokenClient tokenClient;
    /** Redis 工具，用于登录锁与验证码 */
    private final RedisUtil redisUtil;

    @Override
    public TokenInfo login(String account, String password,String clientType ) {
        // 1. 检查账号是否因多次登录失败被锁定
        if (redisUtil.hasKey(CacheNames.LOGIN_LOCK + account)) {
            throw new ServiceException("账号已锁定，请稍后再试");
        }
        // 2. 委托 system 服务验密并生成 Token 会话
        R<UserDetail> response = tokenClient.genToken(account, password, clientType);
        // 3. 登录失败：记录锁定并抛出业务异常
        if (!response.isSuccess() || response.getData() == null) {
            redisUtil.set(CacheNames.LOGIN_LOCK + account, "1", TokenConstant.LOGIN_LOCK_MINUTES, TimeUnit.MINUTES);
            throw new ServiceException(response.getMsg() != null ? response.getMsg() : "账号或密码错误");
        }
        // 4. 登录成功：清除锁定记录
        redisUtil.del(CacheNames.LOGIN_LOCK + account);
        // 5. 转换为对外返回的 TokenInfo
        return toTokenInfo(response.getData());
    }

    @Override
    public void logout(String token) {
        // 委托 system 服务清除 Redis 中的 Token 会话
        tokenClient.tokenLogout(token);
    }

    @Override
    public void saveCaptcha(String key, String code) {
        // 将验证码写入 Redis，供后续登录校验使用
        redisUtil.set(CacheNames.CAPTCHA_KEY + key, code, TokenConstant.CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * 将 system 返回的 UserDetail 转换为 auth 对外响应 TokenInfo。
     *
     * @param detail 登录会话详情
     * @return 前端可用的 Token 信息
     */
    private TokenInfo toTokenInfo(UserDetail detail) {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setAccessToken(detail.getToken());
        tokenInfo.setExpiresIn(detail.getExpiresIn());
        // 组装简要用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(detail.getUserId());
        userInfo.setAccount(detail.getAccount());
        userInfo.setName(detail.getName());
        tokenInfo.setUserInfo(userInfo);
        return tokenInfo;
    }
}
