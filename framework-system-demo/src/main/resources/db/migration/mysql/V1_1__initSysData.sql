-- 创建用户

insert into F_UNITINFO
(UNIT_CODE, PARENT_UNIT, UNIT_TYPE, IS_VALID, UNIT_TAG,
UNIT_NAME, ENGLISH_NAME, DEP_NO, UNIT_DESC, ADDRBOOK_ID,
UNIT_SHORT_NAME, UNIT_WORD, UNIT_GRADE, UNIT_ORDER, UPDATE_DATE,
CREATE_DATE, EXTJSONINFO, CREATOR, UPDATOR, UNIT_PATH)
values
('U00001', null, 'N', 'T', null,
'根机构', 'root', null, '根机构', null,
'root', null, null, 1, NOW(),
NOW(), null, 'u0000000', 'u0000000', '/U00001');

insert into F_USERINFO (USER_CODE, USER_PIN, IS_VALID, LOGIN_NAME, USER_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME, LOGIN_IP, ADDRBOOK_ID, REG_EMAIL, USER_ORDER, USER_PWD, REG_CELL_PHONE, CREATE_DATE,CREATOR,UPDATOR,UPDATE_DATE)
values ('noname', '67b74fe1423796dfe8db34b959b81fbd', 'F', 'noname', '匿名用户', '匿名用户', null, null, '', null, 'noname@centit.com', 1, '', '', str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s'),'u0000000','u0000000',now());
insert into F_USERINFO (USER_CODE, USER_PIN, IS_VALID, LOGIN_NAME, USER_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME, LOGIN_IP, ADDRBOOK_ID, REG_EMAIL, USER_ORDER, USER_PWD, REG_CELL_PHONE, CREATE_DATE ,CREATOR,UPDATOR,UPDATE_DATE,primary_Unit)
values ('u0000000', '$2a$11$DbyFNhHeCES5CKoMuM5sXepY7GM35sZkUSqQbjYJnFTzJ2GDIYGLK', 'T', 'admin', '管理员', '', null, null, '', null, 'codefan@centit.com', 1, '', '18017458877', str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s'),'u0000000','u0000000',now(),'U00001');

INSERT INTO F_USERUNIT(USER_UNIT_ID, UNIT_CODE, USER_CODE, Is_Primary, User_Station, User_Rank, Rank_Memo, User_Order,
update_Date, Create_Date) VALUES ('s000000000','U00001','u0000000','T','pf','CZ',null,'1','2014-12-12','2014-12-12');

-- 初始化数据字典
INSERT INTO F_DATACATALOG VALUES ('CatalogStyle', '字典类型', 'F', 'L', 'F : 框架固有的 U:用户 S：系统', '{\"dataCode\":{\"value\":\"类型编码\",\"isUse\":\"T\"},\"dataValue\":{\"value\":\"类型说明\",\"isUse\":\"T\"},\"extraCode\":{\"value\":\"扩展编码\",\"isUse\":\"F\"},\"extraCode2\":{\"value\":\"排序\",\"isUse\":\"F\"},\"dataTag\":{\"value\":\"数据标记\",\"isUse\":\"F\"},\"dataDesc\":{\"value\":\"数据描述\",\"isUse\":\"T\"}}', '2017-12-2 17:11:01', '2016-4-7 00:00:00', 'DICTSET_M', '1', 'u0000000', 'u0000000');
INSERT INTO F_DATACATALOG VALUES ('CatalogType', '字典结构', 'F', 'L', 'L:列表T:树形 测试修改', '{\"dataCode\":{\"value\":\"结构编码\",\"isUse\":\"T\"},\"dataValue\":{\"value\":\"结构名称\",\"isUse\":\"T\"},\"extraCode\":{\"value\":\"扩展编码\",\"isUse\":\"F\"},\"extraCode2\":{\"value\":\"排序\",\"isUse\":\"F\"},\"dataTag\":{\"value\":\"数据标记\",\"isUse\":\"F\"},\"dataDesc\":{\"value\":\"数据描述\",\"isUse\":\"T\"}}', '2017-12-2 17:12:07', '2016-4-7 00:00:00', 'DICTSET_M', '1', 'u0000000', 'u0000000');
INSERT INTO F_DATACATALOG VALUES ('FlowUserRole', '工作流流程角色', 'S', 'L', '', '{\"dataCode\":{\"value\":\"角色编码\",\"isUse\":\"T\"},\"dataValue\":{\"value\":\"角色名称\",\"isUse\":\"T\"},\"extraCode\":{\"value\":\"扩展编码\",\"isUse\":\"F\"},\"extraCode2\":{\"value\":\"排序\",\"isUse\":\"T\"},\"dataTag\":{\"value\":\"数据标记\",\"isUse\":\"F\"},\"dataDesc\":{\"value\":\"数据描述\",\"isUse\":\"T\"}}', '2017-12-2 17:08:05', NULL, 'SYS_CONFIG', '1', NULL, NULL);
INSERT INTO F_DATACATALOG VALUES ('LogLevel', '日志类型', 'F', 'L', '日志类型', '{\"dataCode\":{\"value\":\"类型编码\",\"isUse\":\"T\"},\"dataValue\":{\"value\":\"类型名称\",\"isUse\":\"T\"},\"extraCode\":{\"value\":\"扩展编码\",\"isUse\":\"F\"},\"extraCode2\":{\"value\":\"扩展编码2\",\"isUse\":\"F\"},\"dataTag\":{\"value\":\"数据标记\",\"isUse\":\"F\"},\"dataDesc\":{\"value\":\"数据描述\",\"isUse\":\"T\"}}', '2017-12-2 17:12:44', '2016-4-7 00:00:00', 'OPT_LOG_QUERY', '1', 'u0000000', 'u0000000');
INSERT INTO F_DATACATALOG VALUES ('MsgType', '消息类型', 'F', 'L', '测试测试', '{\"dataCode\":{\"value\":\"类型编码\",\"isUse\":\"T\"},\"dataValue\":{\"value\":\"类型名称\",\"isUse\":\"T\"},\"extraCode\":{\"value\":\"扩展编码\",\"isUse\":\"F\"},\"extraCode2\":{\"value\":\"扩展编码2\",\"isUse\":\"F\"},\"dataTag\":{\"value\":\"数据标记\",\"isUse\":\"F\"},\"dataDesc\":{\"value\":\"数据描述\",\"isUse\":\"T\"}}', '2017-12-2 17:13:04', '2016-4-7 00:00:00', 'USER_SETTING', '1', 'u0000000', 'u0000000');
INSERT INTO F_DATACATALOG VALUES ('OptType', '业务类别', 'F', 'L', '业务类别', '{\"dataCode\":{\"value\":\"类别编码\",\"isUse\":\"T\"},\"dataValue\":{\"value\":\"类别名称\",\"isUse\":\"T\"},\"extraCode\":{\"value\":\"扩展编码\",\"isUse\":\"F\"},\"extraCode2\":{\"value\":\"排序\",\"isUse\":\"F\"},\"dataTag\":{\"value\":\"数据标记\",\"isUse\":\"F\"},\"dataDesc\":{\"value\":\"数据描述\",\"isUse\":\"T\"}}', '2017-12-2 17:14:04', '2016-4-7 00:00:00', 'SYSCONF', '1', 'u0000000', 'u0000000');
INSERT INTO F_DATACATALOG VALUES ('RankType', '行政职务类别', 'S', 'L', '业务职务类别，类别代码长度为2。数值越低等级越高', '{\"dataCode\":{\"value\":\"职务代码\",\"isUse\":\"T\"},\"dataValue\":{\"value\":\"职务名称\",\"isUse\":\"T\"},\"extraCode\":{\"value\":\"等级\",\"isUse\":\"T\"},\"extraCode2\":{\"value\":\"排序\",\"isUse\":\"T\"},\"dataTag\":{\"value\":\"数据标记\",\"isUse\":\"F\"},\"dataDesc\":{\"value\":\"数据描述\",\"isUse\":\"T\"}}', '2017-12-2 16:53:15', NULL, 'SYS_CONFIG', '0', 'u0000000', 'u0000000');
INSERT INTO F_DATACATALOG VALUES ('StationType', '岗位角色', 'S', 'L', '业务岗位类别，类别代码长度为4', '{\"dataCode\":{\"value\":\"岗位编码\",\"isUse\":\"T\"},\"dataValue\":{\"value\":\"岗位名称\",\"isUse\":\"T\"},\"extraCode\":{\"value\":\"扩展编码\",\"isUse\":\"F\"},\"extraCode2\":{\"value\":\"排序\",\"isUse\":\"F\"},\"dataTag\":{\"value\":\"数据标记\",\"isUse\":\"F\"},\"dataDesc\":{\"value\":\"数据描述\",\"isUse\":\"T\"}}', '2017-12-2 17:14:34', '2016-4-7 00:00:00', 'USERUNIT', '0', 'u0000000', 'u0000000');
INSERT INTO F_DATACATALOG VALUES ('SUPPORT_LANG', '系统支持的语言', 'S', 'L', '系统支持的语言,需要在system.properties中把参数sys.multi_lang设置为true才会生效', NULL, '2016-1-28 20:33:23', NULL, 'DICTSET', '1', 'u0000000', 'u0000000');
INSERT INTO F_DATACATALOG VALUES ('userSettingKey', '用户设置参数', 'S', 'L', '用户可以设置的参数', '{\"dataCode\":{\"value\":\"参数编码\",\"isUse\":\"T\"},\"dataValue\":{\"value\":\"参数名称\",\"isUse\":\"T\"},\"extraCode\":{\"value\":\"扩展编码\",\"isUse\":\"F\"},\"extraCode2\":{\"value\":\"排序\",\"isUse\":\"F\"},\"dataTag\":{\"value\":\"数据标记\",\"isUse\":\"F\"},\"dataDesc\":{\"value\":\"参数填写说明\",\"isUse\":\"T\"}}', '2017-12-2 17:15:27', '2016-4-7 00:00:00', 'DICTSET_M', '1', 'u0000000', 'u0000000');
INSERT INTO F_DATACATALOG VALUES ('YesOrNo', '是否', 'F', 'L', '', '{\"dataCode\":{\"value\":\"编码\",\"isUse\":\"T\"},\"dataValue\":{\"value\":\"数值\",\"isUse\":\"T\"},\"extraCode\":{\"value\":\"扩展编码\",\"isUse\":\"F\"},\"extraCode2\":{\"value\":\"排序\",\"isUse\":\"F\"},\"dataTag\":{\"value\":\"数据标记\",\"isUse\":\"F\"},\"dataDesc\":{\"value\":\"数据描述\",\"isUse\":\"T\"}}', '2017-12-2 17:14:57', '2016-4-7 00:00:00', 'SYS_CONFIG', '1', 'u0000000', 'u0000000');

INSERT INTO f_datadictionary VALUES ('CatalogStyle', 'F', NULL, NULL, 'T', '框架固有', 'F', '任何地方都不允许编辑，只能有开发人员给出更新脚本添加、更改和删除', NULL, NULL, 0);
INSERT INTO f_datadictionary VALUES ('CatalogStyle', 'G', NULL, NULL, 'T', '国标', 'F', '这个暂时不考虑可以在字典类别中进行描述', NULL, NULL, 1);
INSERT INTO f_datadictionary VALUES ('CatalogStyle', 'S', NULL, NULL, 'T', '系统参数', 'F', '实施人员可以在实施入口对数据字典的类别和字典条目进行CRUD操作', NULL, NULL, 2);
INSERT INTO f_datadictionary VALUES ('CatalogStyle', 'U', NULL, NULL, 'T', '用户参数', 'F', '管理员入口 和 实施人员入口 都 对这类别字典类别和条目进行CRUD', NULL, NULL, 3);
INSERT INTO f_datadictionary VALUES ('CatalogType', 'L', NULL, NULL, 'T', '列表', 'F', '列表', '2015-12-1 11:41:23', NULL, 0);
INSERT INTO f_datadictionary VALUES ('CatalogType', 'T', NULL, NULL, 'T', '树形', 'F', '树形', '2015-12-1 11:41:23', NULL, 1);
INSERT INTO f_datadictionary VALUES ('FlowUserRole', 'auditor', NULL, '03', 'T', '审核人', 'U', '流程业务的审核人', NULL, NULL, 2);
INSERT INTO f_datadictionary VALUES ('FlowUserRole', 'operator', NULL, '02', 'T', '经办人', 'U', '流程业务的经办人', NULL, NULL, 1);
INSERT INTO f_datadictionary VALUES ('FlowUserRole', 'request', NULL, '01', 'T', '申请人', 'U', '流程业务的申请人', NULL, NULL, 0);
INSERT INTO f_datadictionary VALUES ('LogLevel', '0', NULL, NULL, 'T', '操作日志', 'F', NULL, '2016-4-7 00:00:00', '2016-4-7 00:00:00', 0);
INSERT INTO f_datadictionary VALUES ('LogLevel', '1', NULL, NULL, 'T', '错误提示', 'F', NULL, '2016-4-7 00:00:00', '2016-4-7 00:00:00', 1);
INSERT INTO f_datadictionary VALUES ('MsgType', 'A', NULL, NULL, 'T', '公告', 'F', '给部门群发的消息', '2016-2-25 17:55:21', NULL, 0);
INSERT INTO f_datadictionary VALUES ('MsgType', 'P', NULL, NULL, 'T', '个人消息', 'F', NULL, '2016-2-25 17:55:21', NULL, 1);
INSERT INTO f_datadictionary VALUES ('OptType', 'I', NULL, NULL, 'T', '项目业务', 'F', '项目业务', '2015-4-1 01:00:00', '2015-4-1 01:00:00', 2);
INSERT INTO f_datadictionary VALUES ('OptType', 'O', NULL, NULL, 'T', '普通业务', 'F', '普通业务', '2015-4-1 01:00:00', '2015-4-1 01:00:00', 0);
INSERT INTO f_datadictionary VALUES ('OptType', 'S', NULL, NULL, 'T', '实施业务', 'F', '实施业务', '2015-4-1 01:00:00', '2015-4-1 01:00:00', 1);
INSERT INTO f_datadictionary VALUES ('OptType', 'W', NULL, NULL, 'T', '工作流业务', 'F', '工作流业务', '2015-4-1 01:00:00', '2015-4-1 01:00:00', 3);
INSERT INTO f_datadictionary VALUES ('RankType', 'CM', '1', '01', 'T', '董事长', 'S', '', NULL, NULL, 0);
INSERT INTO f_datadictionary VALUES ('RankType', 'DM', '5', '05', 'T', '部门经理', 'S', '', NULL, NULL, 1);
INSERT INTO f_datadictionary VALUES ('RankType', 'GM', '2', '02', 'T', '总经理', 'S', '', NULL, NULL, 2);
INSERT INTO f_datadictionary VALUES ('RankType', 'PM', '3', '03', 'T', '副总经理', 'S', '', NULL, NULL, 3);
INSERT INTO f_datadictionary VALUES ('RankType', 'ST', '10', '10', 'T', '员工', 'S', '', NULL, NULL, 4);
INSERT INTO f_datadictionary VALUES ('StationType', 'info', NULL, '03', 'T', '信息维护', 'S', '', NULL, NULL, 2);
INSERT INTO f_datadictionary VALUES ('StationType', 'mang', NULL, '01', 'T', '管理岗', 'S', '', NULL, NULL, 0);
INSERT INTO f_datadictionary VALUES ('StationType', 'serv', NULL, '04', 'T', '后勤', 'S', '', NULL, NULL, 3);
INSERT INTO f_datadictionary VALUES ('StationType', 'tech', NULL, '02', 'T', '技术岗', 'S', '', NULL, NULL, 1);
INSERT INTO f_datadictionary VALUES ('SUPPORT_LANG', 'en_US', NULL, NULL, 'T', 'English', 'S', NULL, '2016-1-28 20:33:23', NULL, 0);
INSERT INTO f_datadictionary VALUES ('SUPPORT_LANG', 'zh_CN', NULL, NULL, 'T', '中文', 'S', NULL, '2016-1-28 20:33:23', NULL, 1);
INSERT INTO f_datadictionary VALUES ('userSettingKey', 'LOCAL_LANG', NULL, NULL, 'T', '语言', 'S', '设置用户语言', NULL, NULL, NULL);
INSERT INTO f_datadictionary VALUES ('userSettingKey', 'receiveways', NULL, NULL, 'T', '消息接收方式', 'S', '用户接收消息的方式，可以是多个用逗号隔开', NULL, NULL, NULL);
INSERT INTO f_datadictionary VALUES ('YesOrNo', 'F', NULL, NULL, NULL, '否', 'F', NULL, NULL, NULL, NULL);
INSERT INTO f_datadictionary VALUES ('YesOrNo', 'T', NULL, NULL, NULL, '是', 'F', NULL, NULL, NULL, NULL);

-- 初始化业务菜单
insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DEPTMAG', '部门管理', '0', '...', '...', null, 'O', null, null, 'Y', null, null, null, null, 'I', 'icon-base icon-base-computer', null, null, str_to_date('12-01-2016 17:04:01', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DEPTPOW', '下属部门管理', 'DEPTMAG', 'modules/sys/deptpow/deptpow.html', '/system/deptManager', null, 'O', null, null, 'Y', null, null, 0, null, 'D', 'icon-base icon-base-user', null, null, str_to_date('12-01-2016 17:10:45', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DEPTROLE', '部门角色定义', 'DEPTMAG', 'modules/sys/deptrole/deptrole.html', '/system/deptManager!', null, 'O', null, null, 'Y', null, null, 0, null, 'D', 'icon-base icon-base-gear', null, null, str_to_date('12-01-2016 17:10:41', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DEPTUSERINFO', '部门用户管理', 'DEPTMAG', 'modules/sys/deptuserinfo/deptuserinfo.html', '/system/userDef', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, str_to_date('12-01-2016 17:11:02', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('UNITMAG', '机构管理', 'ORGMAG', 'modules/sys/unitinfo/unitinfo.html', '/system/unitinfo', null, 'O', null, null, 'Y', null, null, 2, null, 'D', null, null, null, str_to_date('14-03-2016 14:41:07', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('USERMAG', '用户管理', 'ORGMAG', 'modules/sys/userinfo/userinfo.html', '/system/userinfo', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, str_to_date('18-02-2016 17:46:43', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('USERROLE', '用户角色', 'ORGMAG', '/modules/sys/userrole.html', '/system/userrole', null, 'O', null, null, 'N', null, null, null, null, 'D', null, null, null, null, null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('USERUNIT', '用户机构', 'ORGMAG', '/modules/sys/userunit.html', '/system/userunit', null, 'O', null, null, 'N', null, null, null, null, 'D', null, null, null, null, null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DICTSET_M', '数据字典', 'SYS_CONFIG', 'modules/sys/dictionary/dictionary.html', '/system/dictionary', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), str_to_date('23-12-2014 14:04:59', '%d-%m-%Y %H:%i:%s'),'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('OPT_LOG_QUERY', '系统日志', 'SYS_CONFIG', 'modules/sys/loginfo/loginfo.html', '/system/optlog', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, str_to_date('27-11-2015 11:19:09', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('USER_SETTING', '设置中心', 'SYS_CONFIG', '/modules/sys/usersetting.html', '/system/usersetting', null, 'N', null, null, 'N', null, null, null, null, 'D', null, null, null, str_to_date('23-12-2014 16:52:40', '%d-%m-%Y %H:%i:%s'), str_to_date('23-12-2014 16:52:40', '%d-%m-%Y %H:%i:%s'),'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('CALENDAR', '日历', 'SYS_CONFIG', '/modules/sys/schedule/schedule.html', '/system/calendar', null, 'O', null, null, 'Y', null, null, null, null, 'D', 'icon-base icon-base-calendar', null, null, str_to_date('04-03-2015 09:55:31', '%d-%m-%Y %H:%i:%s'), str_to_date('04-03-2015 09:55:31', '%d-%m-%Y %H:%i:%s'),'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DEPLOY', '实施菜单', '0', '...', '...', null, 'S', null, null, 'Y', null, null, null, null, 'D', '444', null, null, str_to_date('15-12-2014 14:10:08', '%d-%m-%Y %H:%i:%s'), str_to_date('15-12-2014 14:10:08', '%d-%m-%Y %H:%i:%s'),'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('SYSCONF', '系统配置', 'DEPLOY', '...', '...', null, 'O', null, null, 'Y', null, null, null, null, 'I', 'icon-base icon-base-gear', null, null, null, null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DICTSET', '数据字典管理', 'SYSCONF', 'modules/sys/dictionary/dictionary.admin.html', '/system/dictionary', null, 'S', null, null, 'Y', null, null, 0, null, 'D', 'icon-base icon-base-gear', null, null, str_to_date('18-02-2016 17:48:18', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('OPTINFO', '系统业务', 'SYSCONF', 'modules/sys/optinfo/optinfo.html', '/system/optinfo', null, 'S', null, null, 'Y', null, null, 4, null, 'D', null, null, null, str_to_date('30-01-2016 19:50:37', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('OPTLOG', '系统日志', 'SYSCONF', 'modules/sys/loginfo/loginfo.admin.html', '/system/optlog', null, 'S', null, null, 'Y', null, null, null, null, 'D', null, null, null, null, null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('ROLEMAG', '角色定义', 'ORGMAG', 'modules/sys/roleinfo/roleinfo.html', '/system/roleinfo', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, null, null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('ORGMAG', '组织管理', '0', '...', '...', null, 'O', null, null, 'Y', null, null, 3, null, 'I', 'icon-base icon-base-user', null, null, str_to_date('31-01-2016 15:55:53', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('SYS_CONFIG', '系统配置', '0', '...', '...', null, 'O', null, null, 'Y', null, null, null, null, 'D', 'icon-base icon-base-gear', null, null, null, null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('LOGINCAS', 'CAS登录入口', '0', '/system/mainframe/logincas', '/system/mainframe', null, 'O', null, null, 'N', null, null, null, null, 'D', null, null, null, str_to_date('07-04-2016 15:06:08', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');


insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000038', 'USER_SETTING', '新增或更新用户设置', null, null, null, null, null, '/*', 'CU','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000037', 'USER_SETTING', '获取用户设置参数', null, '获取当前用户设置的参数', null, null, null, '/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000039', 'USER_SETTING', '删除用户设置参数', null, '删除用户设置参数', null, null, null, '/*', 'D','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000040', 'USER_SETTING', '导出用户设置参数', null, '导出用户设置参数', null, null, null, '/export', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000051', 'USERUNIT', '获取单个用户机构关联信息', null, '获取单个用户机构关联信息', null, null, null, '/*/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000020', 'OPTLOG', '查询', 'list', '查询系统日志', null, null, null, '/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000041', 'OPT_LOG_QUERY', '查看日志详情', null, '查看单条日志', null, str_to_date('27-11-2015 11:19:09', '%d-%m-%Y %H:%i:%s'), null, '/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000070', 'USERMAG', '用户列表', null, '用户列表', null, str_to_date('18-02-2016 17:46:43', '%d-%m-%Y %H:%i:%s'), null, '/', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000029', 'USERUNIT', '新增用户机构关联', null, '添加用户关联机构', null, null, null, '/', 'C','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000032', 'USERROLE', '新增用户角色关联', null, '添加用户关联角色', null, null, null, '/', 'C','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000030', 'USERUNIT', '编辑用户机构关联', null, '更新用户机构关联信息', null, null, null, '/*/*', 'U','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000031', 'USERUNIT', '删除用户机构关联', null, '删除用户关联机构关联', null, null, null, '/*/*', 'D','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000033', 'USERROLE', '编辑用户角色关联', null, '更新用户关联角色信息', null, null, null, '/*/*', 'U','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000034', 'USERROLE', '删除用户角色关联', null, '删除用户关联角色', null, null, null, '/*/*', 'D','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000071', 'USERMAG', '创建用户', null, '创建用户', null, str_to_date('18-02-2016 17:46:44', '%d-%m-%Y %H:%i:%s'), null, '/', 'C','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000072', 'USERMAG', '更新用户', null, '更新用户', null, str_to_date('18-02-2016 17:46:44', '%d-%m-%Y %H:%i:%s'), null, '/*', 'U','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000073', 'USERMAG', '删除用户', null, '删除用户', null, str_to_date('18-02-2016 17:46:44', '%d-%m-%Y %H:%i:%s'), null, '/*', 'D','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000052', 'DICTSET', '查询单个数据目录', null, '查询单个数据目录', null, str_to_date('18-02-2016 17:48:18', '%d-%m-%Y %H:%i:%s'), null, '/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000053', 'DICTSET', '查询单个数据字典', null, '查询单个数据字典', null, str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/dictionary/*/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000054', 'DICTSET', '获取缓存中所有数据字典', null, '获取缓存中所有数据字典', null, str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/cache/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000009', 'DICTSET', '删除数据字典', 'DELTE', '删除数据字典', 'F', str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/dictionary/*/*', 'D','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000007', 'DICTSET', '列举字典', 'LIST', '初始页面', 'F', str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/data', 'U','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000008', 'DICTSET', '新增/编辑数据字典', 'EDIT', '新增/编辑数据字典', 'F', str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/dictionary/*/*', 'CU','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000010', 'DICTSET', '新增/编辑数据目录', 'editDetail', '编辑/新建数据目录', 'F', str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/*', 'CU','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000011', 'DICTSET', '删除数据目录', 'deleteDetail', '删除字典目录', 'F', str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/*', 'D','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000042', 'DICTSET_M', '查询单个数据目录', null, '查询单个数据目录', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000043', 'DICTSET_M', '查询单个数据字典', null, '查询单个数据字典', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/*/dictionary/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000044', 'DICTSET_M', '新增/编辑数据目录', null, '新增/编辑数据目录', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/*', 'CU','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000045', 'DICTSET_M', '新增/编辑数据字典', null, '新增/编辑数据字典', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/*/dictionary/*', 'CU','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000046', 'DICTSET_M', '删除数据目录', null, '删除数据目录', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/*', 'D','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000047', 'DICTSET_M', '删除数据字典', null, '删除数据字典', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/*/dictionary/*', 'D','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000048', 'DICTSET_M', '获取缓存中所有数据字典', null, '获取缓存中所有数据字典', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/cache/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000059', 'UNITMAG', '查看所有机构', null, '查看所有机构', null, str_to_date('14-03-2016 14:41:07', '%d-%m-%Y %H:%i:%s'), null, '/', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000001', 'UNITMAG', '新建/编辑机构', 'EDIT', '新建和更新机构', 'F', str_to_date('14-03-2016 14:41:07', '%d-%m-%Y %H:%i:%s'), null, '/*', 'CU','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000002', 'UNITMAG', '禁用/恢复机构', 'DELETE', '更新机构状态', 'F', str_to_date('14-03-2016 14:41:07', '%d-%m-%Y %H:%i:%s'), null, '/*/status/*', 'U','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000080', 'LOGINCAS', 'CAS登录入口', null, null, null, null, null, '/logincas', 'RCU','u0000000','u0000000');

-- 用户、角色、权限初始化


-- 初始化角色信息
insert into F_ROLEINFO (ROLE_CODE, ROLE_NAME,ROLE_TYPE, IS_VALID, ROLE_DESC, CREATE_DATE, UPDATE_DATE,CREATOR,UPDATOR)
values ('DEPLOY', '实施人员','G','T', '实施人员角色', str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s'), now(),'u0000000','u0000000');

insert into F_ROLEINFO (ROLE_CODE, ROLE_NAME,ROLE_TYPE, IS_VALID, ROLE_DESC, CREATE_DATE, UPDATE_DATE,CREATOR,UPDATOR)
values ('SYSADMIN', '系统管理员','G', 'T', '所有系统配置功能', str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s'), now(),'u0000000','u0000000');

insert into F_ROLEINFO (ROLE_CODE, ROLE_NAME,ROLE_TYPE, IS_VALID, ROLE_DESC, CREATE_DATE, UPDATE_DATE,CREATOR,UPDATOR)
values ('anonymous', '匿名角色','F', 'T', '匿名用户角色', str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s'), now(),'u0000000','u0000000');

insert into F_ROLEINFO (ROLE_CODE, ROLE_NAME,ROLE_TYPE, IS_VALID, ROLE_DESC, CREATE_DATE, UPDATE_DATE,CREATOR,UPDATOR)
values ('public', '公共角色','F', 'T', '公共角色权限会默认给不包括匿名用户的所有人', str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s'), now(),'u0000000','u0000000');

insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, UPDATE_DATE, CREATE_DATE, OPT_SCOPE_CODES,CREATOR,UPDATOR)
values ('public', '1000080', str_to_date('11-04-2016 10:21:17', '%d-%m-%Y %H:%i:%s'), str_to_date('11-04-2016 10:21:17', '%d-%m-%Y %H:%i:%s'), '','u0000000','u0000000');



insert into F_OPTDEF(opt_code,opt_id,opt_name,opt_method,opt_desc,
			is_in_workflow,UPDATE_DATE,create_date,opt_url,opt_req,CREATOR,UPDATOR)
select  sequence_nextval('S_OPTDEFCODE'),opt_id , '查看', 'list',  '查看',
		'F',now(),now(),'/*','R' ,CREATOR,UPDATOR
		from F_OptInfo where opt_id not in (select opt_id from F_OPTDEF);

insert into F_ROLEPOWER(role_code,opt_code,update_Date,create_date,opt_scope_codes,CREATOR,UPDATOR)
	select 'SYSADMIN',opt_code,now(),now(),'',CREATOR,UPDATOR from F_OPTDEF;

insert into F_USERROLE (USER_CODE, ROLE_CODE, OBTAIN_DATE,
			SECEDE_DATE, CHANGE_DESC, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('u0000000', 'SYSADMIN', STR_TO_DATE('23-05-2012','%d-%m-%Y'),
	STR_TO_DATE('01-10-2020', '%d-%m-%Y'),'' ,now(), now(),'u0000000','u0000000');
	commit;

