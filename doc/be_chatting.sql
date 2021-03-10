create table user_info
(
    id          bigint unsigned not null auto_increment comment '用户ID',
    username    varchar(128)    not null default '' comment '用户名',
    name        varchar(255)    not null default '' comment '昵称',
    password    varchar(128)    not null default '' comment '密码',
    token       varchar(256)    not null default '' comment '用户token',
    type        int             not null default 0  comment '用户类型：1：普通用户，0：超级管理员',
    sex         int          not null default 0  comment '0:男，1：女',
    age         int             not null default 0  comment '年龄',
    city        varchar(512)    not null default '' comment '城市',
    emotion     int             not null default 0  comment '1：单身，2：离婚，3：已婚，4：恋爱',
    signature   varchar(1024)   not null default '' comment '个人签名',
    article_id  bigint          not null default 0  comment '头像所对应的文件ID',
    deleted     int             not null default 0  comment '是否删除,1已删除，0未删除',
    create_time bigint          not null default 0  comment '创建时间',
    update_time bigint          not null default 0  comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_username` (`username`),
    KEY `idx_type` (`type`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '用户信息表';

create table tag_info
(
    id          bigint unsigned not null auto_increment comment '标签ID',
    name        varchar(512)    not null default '' comment '标签名称',
    deleted     int             not null default 0  comment '是否删除,1已删除，0未删除',
    creator     varchar(512)    not null default '' comment '创建人',
    create_time bigint          not null default 0  comment '创建时间',
    update_time bigint          not null default 0  comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_name` (`name`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '标签信息表';

create table user_tag_relation
(
    id          bigint unsigned not null auto_increment comment '自增ID',
    user_id     bigint          not null default 0 comment '用户ID',
    tag_id      bigint          not null default 0  comment '标签ID',
    deleted     int             not null default 0  comment '是否删除,1：已删除，0：未删除',
    create_time bigint          not null default 0  comment '创建时间',
    update_time bigint          not null default 0  comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_tag_id` (`tag_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '用户标签关联关系表';

create table article_info
(
    id             bigint unsigned not null auto_increment comment '文档ID',
    name           varchar(255)    not null default 0      comment '文档名称',
    path           varchar(255)    not null default ''     comment '文件路径',
    deleted        int             not null default 0      comment '是否删除,1已删除，0未删除',
    create_time    bigint          not null default 0      comment '创建时间',
    update_time    bigint          not null default 0      comment '修改时间',
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '文档信息表';

create table invitation_info
(
    id             bigint unsigned not null auto_increment comment '帖子ID',
    content        text                                    comment '帖子内容',
    user_id        bigint          not null default 0      comment '发帖人的用户ID',
    deleted        int             not null default 0      comment '是否删除,1已删除，0未删除',
    create_time    bigint          not null default 0      comment '创建时间',
    update_time    bigint          not null default 0      comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_user_id` (`user_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '帖子信息表';

create table invitation_like_relation
(
    id             bigint unsigned not null auto_increment comment '自增ID',
    invitation_id  bigint          not null default 0      comment '帖子ID',
    user_id        bigint          not null default 0      comment '点赞人的用户ID',
    deleted        int             not null default 0      comment '是否删除,1已删除，0未删除',
    create_time    bigint          not null default 0      comment '创建时间',
    update_time    bigint          not null default 0      comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_invitation_id` (`invitation_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '帖子点赞表';

create table comment_info
(
    id             bigint unsigned not null auto_increment comment '评论ID',
    invitation_id  bigint          not null default 0      comment '帖子ID',
    user_id        bigint          not null default 0      comment '评论人的用户ID',
    content        varchar(2056)   not null default ''     comment '评论内容',
    deleted        int             not null default 0      comment '是否删除,1已删除，0未删除',
    create_time    bigint          not null default 0      comment '创建时间',
    update_time    bigint          not null default 0      comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_invitation_id` (`invitation_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '评论内容表';

create table user_friend_relation
(
    id             bigint unsigned not null auto_increment comment '自增ID',
    user_id        bigint          not null default 0      comment '用户ID',
    target_user_id bigint          not null default 0      comment '目标用户ID',
    status         int             not null default 0      comment '申请状态：1：通过，2：拒绝，3：申请中',
    deleted        int             not null default 0      comment '是否删除,1已删除，0未删除',
    create_time    bigint          not null default 0      comment '创建时间',
    update_time    bigint          not null default 0      comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_user_id_status` (`user_id`,`status`),
    KEY `idx_target_user_id` (`target_user_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '用户好友关系表';

create table chat_record_info
(
    id             bigint unsigned not null auto_increment comment '自增ID',
    user_id        bigint          not null default 0      comment '用户ID',
    target_user_id bigint          not null default 0      comment '目标用户ID',
    content        varchar(1024)   not null default ''     comment '聊天内容',
    deleted        int             not null default 0      comment '是否删除,1已删除，0未删除',
    create_time    bigint          not null default 0      comment '创建时间',
    update_time    bigint          not null default 0      comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_user_id_target_user_id` (`user_id`,`target_user_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '聊天记录表';