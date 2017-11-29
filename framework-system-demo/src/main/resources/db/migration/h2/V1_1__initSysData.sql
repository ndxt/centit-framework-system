-- 创建用户

insert into F_UNITINFO
(UNIT_CODE, PARENT_UNIT, UNIT_TYPE, IS_VALID, UNIT_TAG,
UNIT_NAME, ENGLISH_NAME, DEP_NO, UNIT_DESC, ADDRBOOK_ID,
UNIT_SHORT_NAME, UNIT_WORD, UNIT_GRADE, UNIT_ORDER, UPDATE_DATE,
CREATE_DATE, EXTJSONINFO, CREATOR, UPDATOR, UNIT_PATH)
values
('U00001', null, 'N', 'T', null,
'根机构', 'root', null, '根机构', null,
'root', null, null, 1, today(),
today(), null, 'u0000000', 'u0000000', '/U00001');

insert into F_USERINFO (USER_CODE, USER_PIN, IS_VALID, LOGIN_NAME, USER_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME, LOGIN_IP, ADDRBOOK_ID, REG_EMAIL, USER_ORDER, USER_PWD, REG_CELL_PHONE, CREATE_DATE,CREATOR,UPDATOR,UPDATE_DATE)
values ('noname', '67b74fe1423796dfe8db34b959b81fbd', 'F', 'noname', '匿名用户', '匿名用户', null, null, '', null, 'noname@centit.com', 1, '', '', parsedatetime('12-12-2014', 'dd-MM-yyyy', 'en'),'u0000000','u0000000',today());
insert into F_USERINFO (USER_CODE, USER_PIN, IS_VALID, LOGIN_NAME, USER_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME, LOGIN_IP, ADDRBOOK_ID, REG_EMAIL, USER_ORDER, USER_PWD, REG_CELL_PHONE, CREATE_DATE ,CREATOR,UPDATOR,UPDATE_DATE,primary_Unit)
values ('u0000000', '$2a$11$DbyFNhHeCES5CKoMuM5sXepY7GM35sZkUSqQbjYJnFTzJ2GDIYGLK', 'T', 'admin', '管理员', '', null, null, '', null, 'codefan@centit.com', 1, '', '18017458877', parsedatetime('12-12-2014', 'dd-MM-yyyy', 'en'),'u0000000','u0000000',today(),'U00001');

INSERT INTO F_USERUNIT(USER_UNIT_ID, UNIT_CODE, USER_CODE, Is_Primary, User_Station, User_Rank, Rank_Memo, User_Order,
update_Date, Create_Date) VALUES ('s000000000','U00001','u0000000','T','pf','CZ',null,'1','2014-12-12','2014-12-12');

-- 初始化数据字典


insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE,CREATOR,UPDATOR)
values ('MsgType', '消息类型', 'U', 'L', '测试测试', null, parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, 'DICTSET_M', '1','u0000000','u0000000');

insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE,CREATOR,UPDATOR)
values ('CatalogStyle', '字典类型', 'S', 'L', 'F : 框架固有的 U:用户 S：系统', null, null, null, 'DICTSET_M', '1','u0000000','u0000000');

insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE,CREATOR,UPDATOR)
values ('CatalogType', '字典结构', 'S', 'L', 'L:列表T:树形 测试修改', null, parsedatetime('01-12-2015', 'dd-MM-yyyy','en'), null, 'DICTSET_M', '1','u0000000','u0000000');

insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE,CREATOR,UPDATOR)
values ('UnitType', '单位类型', 'U', 'L', '单位类型', null, null, null, 'DICTSET_M', '1','u0000000','u0000000');

insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE,CREATOR,UPDATOR)
values ('OptType', '业务类别', 'S', 'L', '业务类别', '业务类别', null, null, 'DICTSET_M', '1','u0000000','u0000000');

insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE,CREATOR,UPDATOR)
values ('StationType', '岗位角色', 'U', 'L', '业务岗位类别，类别代码长度为4', '业务类别xx', null, null, 'DICTSET_M', '0','u0000000','u0000000');

insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE,CREATOR,UPDATOR)
values ('RankType', '行政职务类别', 'U', 'L', '业务职务类别，类别代码长度为2。数值越低等级越高', '职位代码;等级;未用;职位名称', null, null, 'DICTSET_M', '0','u0000000','u0000000');

insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE,CREATOR,UPDATOR)
values ('SUPPORT_LANG', '系统支持的语言', 'U', 'L', '系统支持的语言,需要在system.properties中把参数sys.multi_lang设置为true才会生效', null, parsedatetime('28-01-2016', 'dd-MM-yyyy','en'), null, 'DICTSET_M', '1','u0000000','u0000000');

insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE,CREATOR,UPDATOR)
values ('LogLevel', '日志类型', 'F', 'L', '日志类型', '日志类型', parsedatetime('07-04-2016', 'dd-MM-yyyy','en'), parsedatetime('07-04-2016', 'dd-MM-yyyy','en'), 'OptLog', '1','u0000000','u0000000');
INSERT INTO f_datacatalog VALUES ('YesOrNo', '是否', 'S', 'L', null, null, null, null, null, '1', null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('MsgType', 'P', null, null, 'T', '个人消息', 'U', null, parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('MsgType', 'A', null, null, 'T', '公告', 'U', '给部门群发的消息', parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('CatalogStyle', 'F', null, null, 'T', '框架固有', 'S', '任何地方都不允许编辑，只能有开发人员给出更新脚本添加、更改和删除', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('CatalogStyle', 'G', null, null, 'T', '国标', 'S', '这个暂时不考虑可以在字典类别中进行描述', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('CatalogStyle', 'S', null, null, 'T', '系统参数', 'S', '实施人员可以在实施入口对数据字典的类别和字典条目进行CRUD操作', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('CatalogStyle', 'U', null, null, 'T', '用户参数', 'S', '管理员入口 和 实施人员入口 都 对这类别字典类别和条目进行CRUD', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('CatalogType', 'L', null, null, 'T', '列表', 'S', '列表',  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('CatalogType', 'T', null, null, 'T', '树形', 'S', '树形',  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('UnitType', 'A', 'CCCC', null, 'T', '管理', 'S', 'administrator', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('UnitType', 'L', 'BBB', null, 'T', '后勤', 'S', 'logistics', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('UnitType', 'O', 'DDD', null, 'T', '业务', 'S', 'operator', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('UnitType', 'R', 'A', null, 'T', '研发', 'S', 'Rearch', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('RankType', 'IM', '3', null, 'T', '项目经理', 'U', '项目经理', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('RankType', 'GM', '1', null, 'T', '总经理', null, null, null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('RankType', 'DM', '2', null, 'T', '部门经理', null, null, null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('RankType', 'EM', '9', null, 'T', '员工', null, null, null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('RankType', 'KZ', '10', null, 'T', '科长', null, '科长', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('RankType', 'ZR', '10', null, 'T', '主任', null, '主任', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('RankType', 'KY', '10', null, 'T', '办公室科员', null, '办公室科员', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('RankType', 'CZ', '10', null, 'T', '处长', null, '处长', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('RankType', 'WDZ', '4', null, 'T', '委党组', null, null, null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('RankType', 'WLD', '5', null, 'T', '委领导', null, '委领导', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('RankType', 'FC', '6', null, 'T', '副处长', null, '副处长', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('RankType', 'JJZ', '7', null, 'T', '纪检组', null, '纪检组', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('RankType', 'JGDW', '8', null, 'T', '机关党委', null, '机关党委', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'slbmfzr', null, null, 'T', '受理部门负责人', 'S', null, null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'fgjz', null, null, 'T', '分管局长', null, null, null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'sfy', null, null, 'T', '收费员', null, null, null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'xbr', null, null, 'T', '协办处室负责人', null, '协办处室负责人', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'bgszr', null, null, 'T', '办公室主任', null, '办公室主任', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'xkdj', null, null, 'T', '许可登记', 'S', '许可登记', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'xkbl', null, null, 'T', '许可办理', 'S', '许可办理', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'dcdb_jcry', null, null, 'T', '督查督办_监察人员', 'S', '发起督办的监察人员', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'dcdb_jbrld', null, null, 'T', '督查督办_经办人领导', 'S', '被督办人的分管领导', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'dcdb_jcld', null, null, 'T', '督查督办_监察领导', 'S', '发起督办的监察人员分管领导', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'dcdb_jbr', null, null, 'T', '督查督办_经办人', 'S', '被督办人', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'fzr', null, null, 'T', '主办处室负责人', null, '主办处室负责人', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'sjr', null, null, 'T', '办公室收件人', null, '办公室收件人', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'pf', null, null, 'T', '办公室批分人', null, '办公室批分人', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'cbr', null, null, 'T', '主办承办人', null, '主办承办人', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'syr', null, null, 'T', '办公室审阅人', null, '办公室审阅人', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'sr', null, null, 'T', '办公室人员', null, '办公室人员', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'fgzr', null, null, 'T', '分管主任', null, '分管主任', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'fw_nw', null, null, 'T', '发文拟文', 'S', null, null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'wysry', null, null, 'T', '文印室人员', null, '文印室人员', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'wyszr', null, null, 'T', '文印室主任', null, '文印室主任', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'spcjbr', null, null, 'T', '审批处经办人', 'S', '审批处经办人', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'spccz', null, null, 'T', '审批处处长', 'S', '审批处处长', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'bgsms', null, null, 'T', '办公室秘书', 'S', '办公室秘书', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'bgsfgzr', null, null, 'T', '办公室分管主任', 'S', '办公室分管主任', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'wld', null, null, 'T', '委领导签发', 'S', '委领导签发', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'bgsfwh', null, null, 'T', '办公室文秘室文号', 'S', '办公室文秘室文号', null, null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('SUPPORT_LANG', 'zh_CN', null, null, 'T', '中文', 'U', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('SUPPORT_LANG', 'en_US', null, null, 'T', 'English', 'U', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, null);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('OptType', 'S', null, null, 'T', '实施业务', 'F', '实施业务',  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'),  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), 2);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('OptType', 'O', null, null, 'T', '普通业务', 'F', '普通业务',  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'),  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), 1);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('OptType', 'I', null, null, 'T', '项目业务', 'F', '项目业务',  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'),  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), 3);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('OptType', 'W', null, null, 'T', '工作流业务', 'F', '工作流业务',  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'),  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), 4);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('LogLevel', '1', null, null, 'T', '错误提示', 'F', null, parsedatetime('07-04-2016', 'dd-MM-yyyy'), parsedatetime('07-04-2016', 'dd-MM-yyyy'), 2);

insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('LogLevel', '0', null, null, 'T', '操作日志', 'F', null, parsedatetime('07-04-2016', 'dd-MM-yyyy'), parsedatetime('07-04-2016', 'dd-MM-yyyy'), 1);

INSERT INTO f_datadictionary VALUES ('YesOrNo', 'F', null, null, null, '否', null, null, null, null, null);
INSERT INTO f_datadictionary VALUES ('YesOrNo', 'T', null, null, null, '是', null, null, null, null, null);

-- 初始化业务菜单
insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DEPTMAG', '部门管理', '0', '...', '...', null, 'O', null, null, 'Y', null, null, null, null, 'I', 'icon-base icon-base-computer', null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DEPTPOW', '下属部门管理', 'DEPTMAG', 'modules/sys/deptpow/deptpow.html', '/system/deptManager', null, 'O', null, null, 'Y', null, null, 0, null, 'D', 'icon-base icon-base-user', null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DEPTROLE', '部门角色定义', 'DEPTMAG', 'modules/sys/deptrole/deptrole.html', '/system/deptManager!', null, 'O', null, null, 'Y', null, null, 0, null, 'D', 'icon-base icon-base-gear', null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DEPTUSERINFO', '部门用户管理', 'DEPTMAG', 'modules/sys/deptuserinfo/deptuserinfo.html', '/system/userDef', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('UNITMAG', '机构管理', 'ORGMAG', 'modules/sys/unitinfo/unitinfo.html', '/system/unitinfo', null, 'O', null, null, 'Y', null, null, 2, null, 'D', null, null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('USERMAG', '用户管理', 'ORGMAG', 'modules/sys/userinfo/userinfo.html', '/system/userinfo', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('USERROLE', '用户角色', 'ORGMAG', '/modules/sys/userrole.html', '/system/userrole', null, 'O', null, null, 'N', null, null, null, null, 'D', null, null, null, null, null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('USERUNIT', '用户机构', 'ORGMAG', '/modules/sys/userunit.html', '/system/userunit', null, 'O', null, null, 'N', null, null, null, null, 'D', null, null, null, null, null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DICTSET_M', '数据字典', 'SYS_CONFIG', 'modules/sys/dictionary/dictionary.html', '/system/dictionary', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'),  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'),'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('OPT_LOG_QUERY', '系统日志', 'SYS_CONFIG', 'modules/sys/loginfo/loginfo.html', '/system/optlog', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('USER_SETTING', '设置中心', 'SYS_CONFIG', '/modules/sys/usersetting.html', '/system/usersetting', null, 'N', null, null, 'N', null, null, null, null, 'D', null, null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'),  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'),'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('CALENDAR', '日历', 'SYS_CONFIG', '/modules/sys/schedule/schedule.html', '/system/calendar', null, 'O', null, null, 'Y', null, null, null, null, 'D', 'icon-base icon-base-calendar', null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), parsedatetime('25-02-2016', 'dd-MM-yyyy','en'),'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DEPLOY', '实施菜单', '0', '...', '...', null, 'S', null, null, 'Y', null, null, null, null, 'D', '444', null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'),  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'),'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('SYSCONF', '系统配置', 'DEPLOY', '...', '...', null, 'O', null, null, 'Y', null, null, null, null, 'I', 'icon-base icon-base-gear', null, null, null, null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DICTSET', '数据字典管理', 'SYSCONF', 'modules/sys/dictionary/dictionary.admin.html', '/system/dictionary', null, 'S', null, null, 'Y', null, null, 0, null, 'D', 'icon-base icon-base-gear', null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('OPTINFO', '系统业务', 'SYSCONF', 'modules/sys/optinfo/optinfo.html', '/system/optinfo', null, 'S', null, null, 'Y', null, null, 4, null, 'D', null, null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('OPTLOG', '系统日志', 'SYSCONF', 'modules/sys/loginfo/loginfo.admin.html', '/system/optlog', null, 'S', null, null, 'Y', null, null, null, null, 'D', null, null, null, null, null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('ROLEMAG', '角色定义', 'ORGMAG', 'modules/sys/roleinfo/roleinfo.html', '/system/roleinfo', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, null, null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('ORGMAG', '组织管理', '0', '...', '...', null, 'O', null, null, 'Y', null, null, 3, null, 'I', 'icon-base icon-base-user', null, null, parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('SYS_CONFIG', '系统配置', '0', '...', '...', null, 'O', null, null, 'Y', null, null, null, null, 'D', 'icon-base icon-base-gear', null, null, null, null,'u0000000','u0000000');

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('LOGINCAS', 'CAS登录入口', '0', '/system/mainframe/logincas', '/system/mainframe', null, 'O', null, null, 'N', null, null, null, null, 'D', null, null, null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null,'u0000000','u0000000');


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
values ('1000041', 'OPT_LOG_QUERY', '查看日志详情', null, '查看单条日志', null, parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000070', 'USERMAG', '用户列表', null, '用户列表', null, parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/', 'R','u0000000','u0000000');

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
values ('1000071', 'USERMAG', '创建用户', null, '创建用户', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/', 'C','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000072', 'USERMAG', '更新用户', null, '更新用户', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*', 'U','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000073', 'USERMAG', '删除用户', null, '删除用户', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*', 'D','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000052', 'DICTSET', '查询单个数据目录', null, '查询单个数据目录', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000053', 'DICTSET', '查询单个数据字典', null, '查询单个数据字典', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/dictionary/*/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000054', 'DICTSET', '获取缓存中所有数据字典', null, '获取缓存中所有数据字典', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/cache/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000009', 'DICTSET', '删除数据字典', 'DELTE', '删除数据字典', 'F',  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/dictionary/*/*', 'D','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000007', 'DICTSET', '列举字典', 'LIST', '初始页面', 'F', parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/data', 'U','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000008', 'DICTSET', '新增/编辑数据字典', 'EDIT', '新增/编辑数据字典', 'F', parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/dictionary/*/*', 'CU','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000010', 'DICTSET', '新增/编辑数据目录', 'editDetail', '编辑/新建数据目录', 'F',  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*', 'CU','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000011', 'DICTSET', '删除数据目录', 'deleteDetail', '删除字典目录', 'F',  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*', 'D','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000042', 'DICTSET_M', '查询单个数据目录', null, '查询单个数据目录', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000043', 'DICTSET_M', '查询单个数据字典', null, '查询单个数据字典', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*/dictionary/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000044', 'DICTSET_M', '新增/编辑数据目录', null, '新增/编辑数据目录', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*', 'CU','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000045', 'DICTSET_M', '新增/编辑数据字典', null, '新增/编辑数据字典', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*/dictionary/*', 'CU','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000046', 'DICTSET_M', '删除数据目录', null, '删除数据目录', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*', 'D','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000047', 'DICTSET_M', '删除数据字典', null, '删除数据字典', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*/dictionary/*', 'D','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000048', 'DICTSET_M', '获取缓存中所有数据字典', null, '获取缓存中所有数据字典', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/cache/*', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000059', 'UNITMAG', '查看所有机构', null, '查看所有机构', null,  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/', 'R','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000001', 'UNITMAG', '新建/编辑机构', 'EDIT', '新建和更新机构', 'F',  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*', 'CU','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000002', 'UNITMAG', '禁用/恢复机构', 'DELETE', '更新机构状态', 'F',  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), null, '/*/status/*', 'U','u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('1000080', 'LOGINCAS', 'CAS登录入口', null, null, null, null, null, '/logincas', 'RCU','u0000000','u0000000');

-- 用户、角色、权限初始化


-- 初始化角色信息
insert into F_ROLEINFO (ROLE_CODE, ROLE_NAME,ROLE_TYPE, IS_VALID, ROLE_DESC, CREATE_DATE, UPDATE_DATE,CREATOR,UPDATOR)
values ('DEPLOY', '实施人员','G','T', '实施人员角色', parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), today(),'u0000000','u0000000');

insert into F_ROLEINFO (ROLE_CODE, ROLE_NAME,ROLE_TYPE, IS_VALID, ROLE_DESC, CREATE_DATE, UPDATE_DATE,CREATOR,UPDATOR)
values ('SYSADMIN', '系统管理员','G', 'T', '所有系统配置功能', parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), today(),'u0000000','u0000000');

insert into F_ROLEINFO (ROLE_CODE, ROLE_NAME,ROLE_TYPE, IS_VALID, ROLE_DESC, CREATE_DATE, UPDATE_DATE,CREATOR,UPDATOR)
values ('anonymous', '匿名角色','F', 'T', '匿名用户角色', parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), today(),'u0000000','u0000000');

insert into F_ROLEINFO (ROLE_CODE, ROLE_NAME,ROLE_TYPE, IS_VALID, ROLE_DESC, CREATE_DATE, UPDATE_DATE,CREATOR,UPDATOR)
values ('public', '公共角色','F', 'T', '公共角色权限会默认给不包括匿名用户的所有人', parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), today(),'u0000000','u0000000');


insert into F_ROLEINFO (ROLE_CODE, ROLE_NAME,ROLE_TYPE, IS_VALID, ROLE_DESC, CREATE_DATE, UPDATE_DATE,CREATOR,UPDATOR)
values ('forbidden', '禁用的功能','F', 'T', '这个角色不能赋给任何人，这个角色中的操作任何人都不可以调用。',  parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), today(),'u0000000','u0000000');


insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, UPDATE_DATE, CREATE_DATE, OPT_SCOPE_CODES,CREATOR,UPDATOR)
values ('public', '1000080', parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), parsedatetime('25-02-2016', 'dd-MM-yyyy','en'), '','u0000000','u0000000');


//sequence_nextval('S_OPTDEFCODE')


insert into F_OPTDEF(opt_code,opt_id,opt_name,opt_method,opt_desc,
			is_in_workflow,UPDATE_DATE,create_date,opt_url,opt_req,CREATOR,UPDATOR)
select NEXT VALUE FOR s_optdefcode ,opt_id , '查看', 'list',  '查看',
		'F',today(),today(),'/*','R' ,CREATOR,UPDATOR
		from F_OptInfo where opt_id not in (select opt_id from F_OPTDEF);

insert into F_ROLEPOWER(role_code,opt_code,update_Date,create_date,opt_scope_codes,CREATOR,UPDATOR)
	select 'SYSADMIN',opt_code,today(),today(),'',CREATOR,UPDATOR from F_OPTDEF;

insert into F_USERROLE (USER_CODE, ROLE_CODE, OBTAIN_DATE,
			SECEDE_DATE, CHANGE_DESC, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('u0000000', 'SYSADMIN', parsedatetime('23-05-2012','dd-MM-yyyy','en'),
	parsedatetime('01-10-2020', 'dd-MM-yyyy','en'),'' ,today(), today(),'u0000000','u0000000');
	commit;

