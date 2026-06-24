package com.example.template.common.resolver;

import com.example.template.common.annotation.LoginUser;
import com.example.template.common.context.UserContext;
import com.example.template.common.exception.InvalidTokenException;
import com.example.template.common.model.UserDetail;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * {@link LoginUser} 参数解析器，将当前登录用户注入 Controller 方法参数。
 */
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 仅处理带 @LoginUser 且类型为 UserDetail 的参数
        return parameter.hasParameterAnnotation(LoginUser.class)
            && UserDetail.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        UserDetail detail = UserContext.get();
        if (detail == null) {
            throw new InvalidTokenException("未登录或 Token 缺失");
        }
        return detail;
    }
}
