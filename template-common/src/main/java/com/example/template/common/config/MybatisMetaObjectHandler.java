package com.example.template.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器。
 * <p>
 * 在 insert/update 时自动填充 {@code createTime}、{@code updateTime} 字段，
 * 配合 {@link com.example.template.common.entity.BaseEntity} 使用。
 * </p>
 */
@Component
@ConditionalOnClass(MetaObjectHandler.class)
@ConditionalOnBean(MybatisPlusInterceptor.class) // 确保 MyBatis-Plus 已完整初始化
public class MybatisMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 新增时填充创建时间与更新时间
        strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时仅刷新更新时间
        strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }
}
