package com.example.template.common.context;

import com.example.template.common.model.UserDetail;

/**
 * 当前登录用户感知接口。
 * <p>
 * 业务 Service 可实现此接口，通过 default 方法便捷获取登录人信息，仿 sfa-center 的 {@code CurrentUserAware}。
 * </p>
 *
 * <pre>
 * public class OrderServiceImpl implements OrderService, CurrentUserAware {
 *     public OrderDTO create(OrderCreateRequest req) {
 *         Long userId = requireUserId();
 *         ...
 *     }
 * }
 * </pre>
 */
public interface CurrentUserAware {

    /**
     * 获取当前登录用户完整信息。
     *
     * @return 登录会话，未登录时返回 null
     */
    default UserDetail currentUser() {
        return UserContext.get();
    }

    /**
     * 获取当前登录用户 ID，未登录时返回 null。
     */
    default Long currentUserId() {
        return UserContext.getUserId();
    }

    /**
     * 获取当前登录账号，未登录时返回 null。
     */
    default String currentAccount() {
        return UserContext.getAccount();
    }

    /**
     * 获取当前登录用户姓名，未登录时返回 null。
     */
    default String currentUserName() {
        return UserContext.getName();
    }

    /**
     * 获取当前登录用户 ID，未登录时抛异常。
     *
     * @return 用户 ID
     */
    default Long requireUserId() {
        Long userId = currentUserId();
        if (userId == null) {
            throw new IllegalStateException("未获取到登录用户信息");
        }
        return userId;
    }
}
