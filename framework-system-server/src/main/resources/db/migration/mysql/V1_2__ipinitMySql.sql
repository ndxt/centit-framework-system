drop table if exists F_OS_INFO cascade;
drop table if exists F_DATABASE_INFO cascade;
CREATE TABLE F_OS_INFO  (
   OS_ID                VARCHAR(20)                    NOT NULL,
   OS_NAME              VARCHAR(200)                   NOT NULL,
   OS_URL               VARCHAR(200),
   OAUTH_PASSWORD       VARCHAR(100),
   LAST_MODIFY_DATE     DATETIME,
   CREATE_TIME          DATETIME,
   CREATED              VARCHAR(8),
  PRIMARY KEY (OS_ID)
);
CREATE TABLE F_DATABASE_INFO  (
   DATABASE_CODE      VARCHAR(32)                    NOT NULL,
   DATABASE_NAME      VARCHAR(100),
 OS_ID                VARCHAR(20),
   DATABASE_URL       VARCHAR(1000),
   USERNAME           VARCHAR(100),
   PASSWORD           VARCHAR(100),
   DATABASE_DESC      VARCHAR(500),
   LAST_MODIFY_DATE   DATETIME,
   CREATE_TIME        DATETIME,
   CREATED            VARCHAR(8),
 PRIMARY KEY (DATABASE_CODE)
);

insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('OS_INFO', '集成业务系统', 'SYS_CONFIG', 'modules/sys/osinfo/osinfo.html', '/system/sys/os', null, 'O', null, null, 'Y', null, null, null, null, 'D', 'icon-base icon-base-gear', null, null, null, null,'u0000000','u0000000');
insert into F_OptInfo (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE,CREATOR,UPDATOR)
values ('DATABASE', '集成数据库', 'SYS_CONFIG', 'modules/sys/databaseinfo/databaseinfo.html', '/system/sys/databaseinfo', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, str_to_date('07-04-2016 15:06:08', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('45', 'OS_INFO', '所有权限', 'ALL', '更新机构状态', 'F', str_to_date('14-03-2016 14:41:07', '%d-%m-%Y %H:%i:%s'), null, '/*', 'CRUD','u0000000','u0000000');
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_DESC, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_URL, OPT_REQ,CREATOR,UPDATOR)
values ('46', 'DATABASE', '所有权限','ALL', null, null, null, null, '/×', 'CRUD','u0000000','u0000000');
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, UPDATE_DATE, CREATE_DATE, OPT_SCOPE_CODES,CREATOR,UPDATOR)
	values('sysadmin','45',now(),now(),null,'u0000000','u0000000');
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, UPDATE_DATE, CREATE_DATE, OPT_SCOPE_CODES,CREATOR,UPDATOR)
	values('sysadmin','46',now(),now(),null,'u0000000','u0000000');

INSERT INTO F_MYSQL_SEQUENCE (NAME, CURRVALUE , INCREMENT) VALUES
  ('S_DATABASECODE', 0, 1);
