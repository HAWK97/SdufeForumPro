/******************************************/
/*   DatabaseName = sdufe_forum_pro   */
/*   TableName = users   */
/******************************************/
CREATE TABLE `users` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID（自增主键）',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '最后更新时间',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `introduce` varchar(255) DEFAULT NULL COMMENT '用户简介',
  `password_hash` varchar(255) DEFAULT NULL COMMENT '密码哈希',
  `state` varchar(64) DEFAULT NULL COMMENT '用户状态（ACTIVE，FROZEN）',
  `invite_code` varchar(255) DEFAULT NULL COMMENT '邀请码',
  `telephone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `inviter_id` varchar(255) DEFAULT NULL COMMENT '邀请人用户ID',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `profile_photo_url` varchar(255) DEFAULT NULL COMMENT '用户头像URL',
  `user_role` varchar(128) DEFAULT NULL COMMENT '用户角色',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表'
;

/******************************************/
/*   DatabaseName = sdufe_forum_pro   */
/*   TableName = user_operate_stream   */
/******************************************/
CREATE TABLE `user_operate_stream` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '流水ID（自增主键）',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后更新时间',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户ID',
  `type` varchar(64) DEFAULT NULL COMMENT '操作类型',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `param` text COMMENT '操作参数',
  `extend_info` text COMMENT '扩展字段',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='用户操作流水表'
;

/******************************************/
/*   DatabaseName = sdufe_forum_pro   */
/*   TableName = notice   */
/******************************************/
CREATE TABLE `notice` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID（自增主键）',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后更新时间',
  `notice_title` varchar(512) CHARACTER SET utf8 DEFAULT NULL COMMENT '通知标题',
  `notice_content` text CHARACTER SET utf8 COMMENT '通知内容',
  `notice_type` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '通知类型',
  `send_success_time` datetime DEFAULT NULL COMMENT '发送成功时间',
  `target_address` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '接收地址',
  `state` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '状态',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `retry_times` int DEFAULT NULL COMMENT '重试次数',
  `extend_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知表'
;

/******************************************/
/*   DatabaseName = sdufe_forum_pro   */
/*   TableName = articles   */
/******************************************/
CREATE TABLE `articles` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '文章ID（自增主键）',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '最后更新时间',
  `content` text CHARACTER SET utf8 COMMENT '文章内容',
  `user_id` bigint unsigned NOT NULL COMMENT '用户ID',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章信息表'
;

/******************************************/
/*   DatabaseName = sdufe_forum_pro   */
/*   TableName = user_operate_stream   */
/******************************************/
CREATE TABLE `article_operate_stream` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '流水ID（自增主键）',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后更新时间',
  `article_id` varchar(64) DEFAULT NULL COMMENT '文章ID',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户ID',
  `type` varchar(64) DEFAULT NULL COMMENT '操作类型',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `param` text COMMENT '操作参数',
  `extend_info` text COMMENT '扩展字段',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='文章操作流水表'
;

/******************************************/
/*   DatabaseName = sdufe_forum_pro   */
/*   TableName = images   */
/******************************************/
CREATE TABLE `images` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '图片ID（自增主键）',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '最后更新时间',
  `url` varchar(255) DEFAULT NULL COMMENT '图片URL',
  `article_id` bigint unsigned NOT NULL COMMENT '文章ID',
  `image_state` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '状态',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图片信息表'
;