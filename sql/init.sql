-- template 库初始化 | admin/admin123
CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    account     VARCHAR(64)  NOT NULL COMMENT '账号',
    password    VARCHAR(128) NOT NULL COMMENT '密码',
    name        VARCHAR(64)  DEFAULT NULL COMMENT '姓名',
    phone       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态 1启用 0禁用',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_account (account)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

INSERT INTO sys_user (account, password, name, phone, status)
VALUES ('admin', 'admin123', '管理员', '13800000000', 1)
ON DUPLICATE KEY UPDATE account = account;

CREATE TABLE IF NOT EXISTS biz_order (
    id           BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_no     VARCHAR(64)   NOT NULL COMMENT '订单编号',
    user_id      BIGINT        NOT NULL COMMENT '用户ID',
    user_account VARCHAR(64)   NOT NULL COMMENT '用户账号',
    amount       DECIMAL(12,2) NOT NULL COMMENT '订单金额',
    status       TINYINT       NOT NULL DEFAULT 0 COMMENT '状态 0待支付 1已支付 2已取消 3已完成',
    remark       VARCHAR(255)  DEFAULT NULL COMMENT '备注',
    create_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user_id (user_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

INSERT INTO biz_order (order_no, user_id, user_account, amount, status, remark)
VALUES ('ORD202506240001', 1, 'admin', 99.90, 1, '示例订单')
ON DUPLICATE KEY UPDATE order_no = order_no;
