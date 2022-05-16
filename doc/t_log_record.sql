CREATE TABLE t_log_record
(
    id          INT(20) UNSIGNED AUTO_INCREMENT COMMENT '主键',
    tenant      varchar(100)  NOT NULL DEFAULT '' COMMENT '租户标识',
    type        varchar(100)  NOT NULL DEFAULT '' COMMENT '日志业务标识',
    biz_no      varchar(100)  NOT NULL DEFAULT '' COMMENT '业务businessNo',
    operator    varchar(50)   NOT NULL DEFAULT '' COMMENT '操作人',
    action      varchar(500)  NOT NULL DEFAULT '' COMMENT '动作',
    sub_type    varchar(100)  NOT NULL DEFAULT '' COMMENT '种类',
    fail        varchar(200)  NOT NULL DEFAULT '' COMMENT '失败原因',
    extra       varchar(2000) NOT NULL DEFAULT '' COMMENT '修改的详细信息，可以为json',
    create_time DATETIME               DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    primary key (id),
    key idx_biz_key (type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 comment '操作日志表';
