DROP TABLE IF EXISTS `f_tenant_info`;
CREATE TABLE `f_tenant_info`  (
`top_unit` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
`unit_name` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`source_url` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`use_limittime` datetime(0) NULL DEFAULT NULL,
`tenant_fee` decimal(10, 0) NULL DEFAULT NULL,
`creator` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`create_time` datetime(0) NULL DEFAULT NULL,
`updator` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`update_time` datetime(0) NULL DEFAULT NULL,
`own_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`is_available` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`apply_time` datetime(0) NULL DEFAULT NULL,
`pass_time` datetime(0) NULL DEFAULT NULL,
`memo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`database_number_limit` decimal(6, 0) NULL DEFAULT NULL,
`os_number_limit` decimal(6, 0) NULL DEFAULT NULL,
`file_space_limit` decimal(6, 0) NULL DEFAULT NULL,
`data_space_limit` decimal(6, 0) NULL DEFAULT NULL,
PRIMARY KEY (`top_unit`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `f_tenant_info`(`top_unit`, `unit_name`, `source_url`, `use_limittime`, `tenant_fee`, `creator`, `create_time`, `updator`, `update_time`, `own_user`, `is_available`, `apply_time`, `pass_time`, `memo`, `remarks`, `database_number_limit`, `os_number_limit`, `file_space_limit`, `data_space_limit`) VALUES ('system', '平台租户', NULL, NULL, 0, 'u0000000', '2021-09-15 00:00:00', NULL, NULL, 'u0000000', 'T', '2021-09-15 00:00:00', NULL, NULL, NULL, 5, 4, 5, 4);
INSERT INTO `f_unitinfo`(`UNIT_CODE`, `PARENT_UNIT`, `UNIT_TYPE`, `IS_VALID`, `UNIT_TAG`, `UNIT_NAME`, `ENGLISH_NAME`, `DEP_NO`, `UNIT_DESC`, `UNIT_SHORT_NAME`, `UNIT_WORD`, `UNIT_GRADE`, `UNIT_ORDER`, `UPDATE_DATE`, `CREATE_DATE`, `CREATOR`, `UPDATOR`, `UNIT_PATH`, `UNIT_MANAGER`, `TOP_UNIT`, `SOCIAL_CREDIT_CODE`) VALUES ('system', '0', 'A', 'T', NULL, 'system', NULL, NULL, NULL, 'system', 'bt000000', NULL, 4, '2021-04-16 10:02:33', '2021-04-16 09:32:13', NULL, NULL, '/0', NULL, 'system', NULL);
INSERT INTO `f_userunit`(`USER_UNIT_ID`, `UNIT_CODE`, `USER_CODE`, `IS_PRIMARY`, `USER_STATION`, `USER_RANK`, `RANK_MEMO`, `USER_ORDER`, `UPDATE_DATE`, `CREATE_DATE`, `CREATOR`, `UPDATOR`, `TOP_UNIT`, `POST_RANK`, `REL_TYPE`) VALUES ('-zzcwTs7S76agcKWr15211', 'system', 'u0000000', 'F', 'ZY', 'YG', NULL, 1000, '2021-04-01 09:00:54', '2021-04-01 09:00:54', NULL, NULL, 'system', NULL, 'F');


DROP TABLE IF EXISTS `work_group`;
CREATE TABLE `work_group`  (
 `group_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '组id',
 `user_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户代码',
 `role_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色',
 `is_valid` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否生效',
 `auth_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
 `creator` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
 `updator` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '更新人',
 `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
 `user_order` decimal(10, 0) NULL DEFAULT NULL COMMENT '排序号',
 `run_token` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '运行令牌',
 `auth_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '授权说明',
 PRIMARY KEY (`group_id`, `user_code`, `role_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `t_tenant_business_log`;
CREATE TABLE `t_tenant_business_log`  (
`BUSINESS_ID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
`TOP_UNIT` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`ASSIGNOR_USER_CODE` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`ASSIGNOR_USER_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`ASSIGNEE_USER_CODE` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`ASSIGNEE_USER_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`BUSINESS_REASON` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`BUSINESS_REMARK` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`APPLY_BUSINESS_TIME` datetime(0) NULL DEFAULT NULL,
`SUCCESS_BUSINESS_TIME` datetime(0) NULL DEFAULT NULL,
`BUSINESS_STATE` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
PRIMARY KEY (`BUSINESS_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `t_tenant_member_apply`;
CREATE TABLE `t_tenant_member_apply`  (
`user_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
`top_unit` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
`inviter_user_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`apply_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`apply_time` datetime(0) NULL DEFAULT NULL,
`apply_state` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`apply_remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`approve_remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`unit_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
PRIMARY KEY (`user_code`, `top_unit`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

