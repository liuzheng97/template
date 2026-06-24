package com.example.template.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.template.auth.service.AuthService;
import com.example.template.common.api.R;
import com.example.template.common.constant.CacheNames;
import com.example.template.common.constant.TokenConstant;
import com.example.template.common.constant.UserConstant;
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
 * 登录入口在 auth，Token 生成委托 system 服务；本服务负责账号锁定与 IP 防刷。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    /** system 服务 Token Feign 客户端 */
    private final ITokenClient tokenClient;
    /** Redis 工具，用于登录锁与 IP 防刷 */
    private final RedisUtil redisUtil;

    @Override
    public TokenInfo login(String account, String password, String clientType, String clientIp) {
        // 1. 检查 IP 是否因 30 分钟内多次失败被锁定
        if (StrUtil.isNotBlank(clientIp) && redisUtil.hasKey(CacheNames.LOGIN_LOCK_IP + clientIp)) {
            throw new ServiceException(TokenConstant.MSG_LOGIN_IP_LOCKED);
        }
        // 2. 检查账号是否因 30 分钟内多次登录失败被锁定
        if (redisUtil.hasKey(CacheNames.LOGIN_LOCK + account)) {
            throw new ServiceException(TokenConstant.MSG_LOGIN_ACCOUNT_LOCKED);
        }
        // 3. 委托 system 服务验密并生成 Token 会话
        R<UserDetail> response = tokenClient.genToken(account, password, clientType);
        // 4. 登录失败：区分账号禁用与普通密码错误
        if (!response.isSuccess() || response.getData() == null) {
            if (UserConstant.MSG_ACCOUNT_DISABLED.equals(response.getMsg())) {
                throw new ServiceException(403, UserConstant.MSG_ACCOUNT_DISABLED);
            }
            handleLoginFailure(account, clientIp);
        }
        // 5. 登录成功：清除该账号失败计数与锁定标记（IP 失败计数保留）
        redisUtil.del(CacheNames.LOGIN_FAIL + account);
        redisUtil.del(CacheNames.LOGIN_LOCK + account);
        return toTokenInfo(response.getData());
    }

    @Override
    public void logout(String token) {
        // 委托 system 服务清除 Redis 中的 Token 会话
        tokenClient.tokenLogout(token);
    }

    /**
     * 处理登录失败：30 分钟窗口内累加账号/IP 失败次数，达到上限则锁定。
     */
    private void handleLoginFailure(String account, String clientIp) {
        int accountFails = incrementFailCount(CacheNames.LOGIN_FAIL + account);
        int ipFails = 0;
        if (StrUtil.isNotBlank(clientIp)) {
            ipFails = incrementFailCount(CacheNames.LOGIN_FAIL_IP + clientIp);
        }
        // 账号 30 分钟内失败达上限：锁定账号
        if (accountFails >= TokenConstant.LOGIN_MAX_RETRY) {
            redisUtil.set(CacheNames.LOGIN_LOCK + account, "1",
                TokenConstant.LOGIN_LOCK_MINUTES, TimeUnit.MINUTES);
            redisUtil.del(CacheNames.LOGIN_FAIL + account);
            throw new ServiceException(TokenConstant.MSG_LOGIN_ACCOUNT_LOCKED);
        }
        // IP 30 分钟内失败达上限：锁定 IP
        if (StrUtil.isNotBlank(clientIp) && ipFails >= TokenConstant.LOGIN_IP_MAX_RETRY) {
            redisUtil.set(CacheNames.LOGIN_LOCK_IP + clientIp, "1",
                TokenConstant.LOGIN_IP_LOCK_MINUTES, TimeUnit.MINUTES);
            redisUtil.del(CacheNames.LOGIN_FAIL_IP + clientIp);
            throw new ServiceException(TokenConstant.MSG_LOGIN_IP_LOCKED);
        }
        int remaining = TokenConstant.LOGIN_MAX_RETRY - accountFails;
        throw new ServiceException("账号或密码有误，还可尝试 " + remaining + " 次");
    }

    /**
     * 失败次数原子 +1（Redis INCR），并设置 30 分钟统计窗口 TTL。
     */
    private int incrementFailCount(String key) {
        long count = redisUtil.increment(key, TokenConstant.LOGIN_FAIL_WINDOW_MINUTES, TimeUnit.MINUTES);
        return (int) count;
    }

    /**
     * 将 system 返回的 UserDetail 转换为 auth 对外响应 TokenInfo。
     */
    private TokenInfo toTokenInfo(UserDetail detail) {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setAccessToken(detail.getToken());
        tokenInfo.setExpiresIn(detail.getExpiresIn());
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(detail.getUserId());
        userInfo.setAccount(detail.getAccount());
        userInfo.setName(detail.getName());
        userInfo.setStatus(detail.getStatus());
        tokenInfo.setUserInfo(userInfo);
        return tokenInfo;
    }
}
