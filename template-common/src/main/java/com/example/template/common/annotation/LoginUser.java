package com.example.template.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注入当前登录用户注解。
 * <p>
 * 标注在 Controller 方法参数上，由 {@link com.example.template.common.resolver.LoginUserArgumentResolver}
 * 从 {@link com.example.template.common.context.UserContext} 解析并注入 {@link com.example.template.common.model.UserDetail}。
 * </p>
 *
 * <pre>
 * public R&lt;OrderDTO&gt; create(@LoginUser UserDetail user, @RequestBody OrderCreateRequest req) { ... }
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {
}
