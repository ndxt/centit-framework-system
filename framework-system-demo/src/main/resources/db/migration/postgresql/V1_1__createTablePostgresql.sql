/*==============================================================*/
/* H2 数据库脚本                                                  */
/*==============================================================*/
drop sequence if exists s_optdefcode;
drop sequence if exists s_sys_log;
drop sequence if exists s_unitcode;
drop sequence if exists s_user_unit_id;
drop sequence if exists s_usercode;
drop sequence if exists S_MSGCODE;
drop sequence if exists S_RECIPIENT;
drop sequence if exists S_ROLECODE;
drop sequence if exists S_Filter_No;
create sequence S_Filter_No;
create sequence S_MSGCODE;
create sequence s_sys_log;

create sequence S_OPTDEFCODE start with 1001000 INCREMENT BY 1;

create sequence S_RECIPIENT;

create sequence S_UNITCODE start with 10 INCREMENT BY 1;

create sequence S_USER_UNIT_ID start with 10 INCREMENT BY 1;

create sequence S_USERCODE start with 10 INCREMENT BY 1;

create sequence S_ROLECODE start with 10 INCREMENT BY 1;
drop table if exists F_DATACATALOG cascade;

drop table if exists F_DATADICTIONARY cascade;

drop table if exists F_OPTDATASCOPE cascade;

drop table if exists F_OPTDEF cascade;

drop table if exists F_OPT_LOG cascade;

drop table if exists F_OptInfo cascade;

drop table if exists F_QUERY_FILTER_CONDITION cascade;

drop table if exists F_ROLEINFO cascade;

drop table if exists F_ROLEPOWER cascade;

drop table if exists F_SYS_NOTIFY cascade;

drop table if exists F_UNITINFO cascade;

drop table if exists F_USERINFO cascade;

drop table if exists F_USERROLE cascade;

drop table if exists F_USERSETTING cascade;

drop table if exists F_USERUNIT cascade;

drop table if exists F_USER_QUERY_FILTER cascade;

drop table if exists F_UNITROLE cascade;
/*==============================================================*/
/* Table: F_DATACATALOG                                         */
/*==============================================================*/
create table F_DATACATALOG
(
   CATALOG_CODE         varchar(32) not null,
   CATALOG_NAME         varchar(64) not null,
   CATALOG_STYLE        char(1) not null ,
   CATALOG_TYPE         char(1) not null  ,
   CATALOG_DESC         varchar(256),
   Field_Desc           varchar(1024) ,
   update_Date          date,
   Create_Date          date,
   TOP_UNIT             varchar(32),
   OS_ID               varchar(32),
   opt_ID               varchar(32),
   need_Cache           char(1) default '1',
   creator              varchar(32),
   updator              varchar(32)
);
comment on table  F_DATACATALOG is '类别状态   U:用户 S：系统，G国标类别形式  T：树状表格 L:列表';
comment on column F_DATACATALOG.CATALOG_STYLE is  'F : 框架固有的 U:用户 S：系统  G国标';
comment on column F_DATACATALOG.CATALOG_TYPE is 'T：树状表格 L:列表';
comment on column F_DATACATALOG.Field_Desc is  '字段描述，不同字段用分号隔开';
comment on column F_DATACATALOG.opt_ID is  '业务分类，使用数据字典DICTIONARYTYPE中数据';
alter table F_DATACATALOG add primary key (CATALOG_CODE);

create table F_DATADICTIONARY
(
   CATALOG_CODE         varchar(32) not null,
   DATA_CODE            varchar(32) not null,
   EXTRA_CODE           varchar(16)  ,
   EXTRA_CODE2          varchar(16) ,
   DATA_TAG             char(1) ,
   DATA_VALUE           varchar(2048),
   DATA_STYLE           char(1) ,
   DATA_DESC            varchar(256),
   Last_Modify_Date     date,
   Create_Date          date,
   DATA_ORDER           numeric(6,0)
);
comment on column F_DATADICTIONARY.EXTRA_CODE is '树型字典的父类代码' ;
comment on column F_DATADICTIONARY.DATA_TAG is 'N正常，D已停用，用户可以自解释这个字段';
comment on column F_DATADICTIONARY.DATA_STYLE is 'F : 框架固有的 U:用户 S：系统  G国标' ;
comment on table F_DATADICTIONARY is'数据字典：存放一些常量数据 比如出物提示信息，还有一些 代码与名称的对应表，比如 状态，角色名，头衔 等等';

alter table F_DATADICTIONARY add primary key (CATALOG_CODE, DATA_CODE);

create table F_OPTDATASCOPE
(
   opt_Scope_Code       varchar(32) not null,
   Opt_ID               varchar(32),
   scope_Name           varchar(64),
   Filter_Condition     varchar(1024)  ,
   scope_Memo           varchar(1024)
   --Filter_Group         varchar(16) default 'G'
);
comment on column F_OPTDATASCOPE.Filter_Condition is '条件语句，可以有的参数 [mt] 业务表 [uc] 用户代码 [uu] 用户机构代码';
comment on column F_OPTDATASCOPE.scope_Memo is '数据权限说明';
alter table F_OPTDATASCOPE add primary key (opt_Scope_Code);

create table F_OPTDEF
(
   OPT_CODE             varchar(32) not null,
   Opt_ID               varchar(32),
   OPT_NAME             varchar(100),
   OPT_METHOD           varchar(50)  ,
   OPT_URL              varchar(256),
   OPT_DESC             varchar(256),
   opt_Order            numeric(4,0),
   Is_In_Workflow       char(1)  ,
   update_Date          date,
   Create_Date          date,
   OPT_REQ              varchar(8),
   creator              varchar(32),
   updator              varchar(32)
);
comment on column F_OPTDEF.OPT_METHOD is '操作参数 方法'  ;
comment on column F_OPTDEF.Is_In_Workflow is '是否为流程操作方法 F：不是  T ： 是'  ;
alter table F_OPTDEF add primary key (OPT_CODE);


create table F_OPT_LOG
(
   log_Id               numeric(12,0) not null,
   log_Level            varchar(2) not null,
   user_code            varchar(8) not null,
   opt_time             date not null,
   Opt_Content          varchar(1000) not null  ,
   New_Value            text ,
   Old_Value            text ,
   Opt_ID               varchar(32) not null  ,
   OPT_Method           varchar(64)  ,
   opt_Tag              varchar(200)
);
comment on column F_OPT_LOG.Opt_ID is  '模块，或者表';
comment on column F_OPT_LOG.Opt_ID is  '方法，或者字段';
comment on column F_OPT_LOG.Opt_ID is  '一般用于关联到业务主体的标识、表的主键等等';
alter table F_OPT_LOG  add primary key (log_Id);

create table F_OptInfo
(
   Opt_ID               varchar(32) not null,
   Opt_Name             varchar(100) not null,
   Pre_Opt_ID           varchar(32) not null,
   opt_Route            varchar(256)  ,
   opt_url              varchar(256),
   Form_Code            varchar(4),
   Opt_Type             char(1)  ,
   Msg_No               numeric(10,0),
   Msg_Prm              varchar(256),
   Is_In_ToolBar        char(1),
   Img_Index            numeric(10,0),
   Top_Opt_ID           varchar(32),
   Order_Ind            numeric(4,0) ,
   FLOW_CODE            varchar(8) ,
   Page_Type            char(1)  default 'I' ,
   Icon                 varchar(512),
   height               numeric(10,0),
   width                numeric(10,0),
   update_Date          date,
   Create_Date          date,
   creator              varchar(32),
   updator              varchar(32)
);
comment on column F_OptInfo.opt_Route is    '与angularjs路由匹配'  ;
comment on column F_OptInfo.Opt_Type is    ' S:实施业务, O:普通业务, W:流程业务, I :项目业务'  ;
comment on column F_OptInfo.Order_Ind is     '这个顺序只需在同一个父业务下排序'  ;
comment on column F_OptInfo.Page_Type is    'D : DIV I:iFrame'   ;
alter table F_OptInfo add primary key (Opt_ID);


create table F_QUERY_FILTER_CONDITION
(
   CONDITION_NO         numeric(12,0) not null,
   Table_Class_Name     varchar(64) not null  ,
   Param_Name           varchar(64) not null ,
   Param_Label          varchar(120) not null ,
   Param_Type      varchar(8) ,
   Default_Value        varchar(100),
   Filter_Sql           varchar(200) ,
   Select_Data_type     char(1)  default 'N' not null ,
   Select_Data_Catalog  varchar(64) ,
   Select_SQL           varchar(1000)  ,
   Select_JSON          varchar(2000),
   CREATE_DATE          date
);
comment on column F_QUERY_FILTER_CONDITION.Table_Class_Name is  '数据库表代码或者po的类名'     ;
comment on column F_QUERY_FILTER_CONDITION.Param_Type is    '参数类型：S 字符串，L 数字， N 有小数点数据， D 日期， T 时间戳， Y 年， M 月'    ;
comment on column F_QUERY_FILTER_CONDITION.Filter_Sql is    '过滤语句，将会拼装到sql语句中'    ;
comment on column F_QUERY_FILTER_CONDITION.Select_Data_type is  '数据下拉框内容； N ：没有， D 数据字典, S 通过sql语句获得， J json数据直接获取 '      ;
comment on column F_QUERY_FILTER_CONDITION.Select_SQL is     '有两个返回字段的sql语句'  ;
comment on column F_QUERY_FILTER_CONDITION.Select_JSON is   'KEY,Value数值对，JSON格式'    ;
alter table F_QUERY_FILTER_CONDITION add primary key (CONDITION_NO);

create table F_ROLEINFO
(
   ROLE_CODE            varchar(32) not null,
   ROLE_NAME            varchar(64),
   ROLE_TYPE            char(1) not null ,
   UNIT_CODE            varchar(32),
   IS_VALID             char(1) not null,
   ROLE_DESC            varchar(256),
   update_Date          date,
   Create_Date          date,
   creator              varchar(32),
   updator              varchar(32)
);
comment on column F_ROLEINFO.ROLE_TYPE is  'F 为系统 固有的 G 全局的 P 公用的 D 部门的 I 为项目角色 W工作量角色';
alter table F_ROLEINFO add primary key (ROLE_CODE);

create table F_ROLEPOWER
(
   ROLE_CODE            varchar(32) not null,
   OPT_CODE             varchar(32) not null,
   opt_Scope_Codes      varchar(1000) ,
   update_Date          date,
   Create_Date          date,
   creator              varchar(32),
   updator              varchar(32)
);
comment on column F_ROLEPOWER.opt_Scope_Codes is  '用逗号隔开的数据范围结合（空\all 表示全部）';
alter table F_ROLEPOWER add primary key (ROLE_CODE, OPT_CODE);

create table F_SYS_NOTIFY
(
   Notify_ID            numeric(12,0) not null,
   Notify_Sender        varchar(100),
   Notify_Receiver      varchar(100) not null,
   Msg_Subject          varchar(200),
   Msg_Content          varchar(2000) not null,
   notice_Type          varchar(100),
   Notify_State         char(1)  ,
   Error_Msg            varchar(500),
   Notify_Time          date,
   opt_Tag              varchar(200)  ,
   OPT_Method           varchar(64)  ,
   Opt_ID               varchar(32) not null
);
comment on column F_SYS_NOTIFY.Notify_State is '0 成功， 1 失败 2 部分成功'  ;
comment on column F_SYS_NOTIFY.opt_Tag is  '一般用于关联到业务主体' ;
comment on column F_SYS_NOTIFY.OPT_Method is '方法，或者字段'  ;
comment on column F_SYS_NOTIFY.Opt_ID is  '模块，或者表' ;
alter table F_SYS_NOTIFY add primary key (Notify_ID);

create table F_UNITINFO
(
   UNIT_CODE            varchar(32) not null,
   PARENT_UNIT          varchar(32),
   UNIT_TYPE            char(1) ,
   IS_VALID             char(1) not null ,
   UNIT_TAG             varchar(100)  ,
   UNIT_NAME            varchar(300) not null,
   english_Name         varchar(300),
   dep_no               varchar(100)  ,
   UNIT_DESC            varchar(256),
   UNIT_SHORT_NAME      varchar(32),
   unit_Word            varchar(100),
   unit_Grade           numeric(4,0),
   unit_Order           numeric(4,0),
   update_Date          date,
   Create_Date          date,
   --extJsonInfo          varchar(1000),
   creator              varchar(32),
   updator              varchar(32),
   TOP_UNIT             varchar(32),
   SOCIAL_CREDIT_CODE   varchar(32),
   UNIT_PATH            varchar(1000),
   UNIT_MANAGER         varchar(32)
);
comment on column F_UNITINFO.UNIT_TYPE   is     '发布任务/ 邮电规划/组队/接收任务'        ;
comment on column F_UNITINFO.  UNIT_TAG is        '用户第三方系统管理'    ;
comment on column F_UNITINFO. dep_no  is      '组织机构代码：'      ;
alter table F_UNITINFO add primary key (UNIT_CODE);

create table F_USERINFO
(
   USER_CODE            varchar(32) not null,
   USER_PIN             varchar(100),
   USER_TYPE            char(1) default 'U'  ,
   IS_VALID             char(1) not null ,
   LOGIN_NAME           varchar(100) not null,
   User_Name            varchar(300) not null ,
   USER_TAG             varchar(100) ,
   english_Name         varchar(300),
   USER_DESC            varchar(256),
   Login_Times          numeric(6,0),
   Active_Time          date,
   TOP_UNIT             varchar(32),
   Reg_Email            varchar(60) ,
   USER_PWD             varchar(20) ,
   pwd_Expired_Time     date,
   REG_CELL_PHONE       varchar(15),
   ID_CARD_NO           varchar(20),
   primary_Unit         varchar(32),
   user_Word            varchar(100)  ,
   user_Order           numeric(4,0),
   update_Date          date,
   Create_Date          date,
   --extJsonInfo          varchar(1000),
   creator              varchar(32),
   updator              varchar(32)
);
comment on column F_USERINFO.    USER_TYPE   is     '发布任务/接收任务/系统管理'    ;
comment on column F_USERINFO.   Reg_Email    is       '注册用Email，不能重复'   ;
comment on column F_USERINFO.   user_Word    is    '微信号'     ;
alter table F_USERINFO add primary key (USER_CODE);

create table F_USERROLE
(
   USER_CODE            varchar(32) not null,
   ROLE_CODE            varchar(32) not null,
   OBTAIN_DATE          date not null,
   SECEDE_DATE          date,
   CHANGE_DESC          varchar(256),
   update_Date          date,
   Create_Date          date,
   creator              varchar(32),
   updator              varchar(32)
);
alter table F_USERROLE add primary key (USER_CODE, ROLE_CODE);

create table F_USERSETTING
(
   USER_CODE            varchar(32) not null ,
   Param_Code           varchar(32) not null,
   Param_Value          varchar(2048) not null,
   opt_ID               varchar(32) not null,
   Param_Name           varchar(200),
   Create_Date          date
);
comment on column F_USERSETTING.USER_CODE is  'DEFAULT:为默认设置
            SYS001~SYS999: 为系统设置方案
            是一个用户号,或者是系统的一个设置方案';
alter table F_USERSETTING add primary key (USER_CODE, Param_Code);

create table F_USERUNIT
(
   USER_UNIT_ID         varchar(32) not null,
   UNIT_CODE            varchar(32) not null,
   USER_CODE            varchar(32) not null,
   REL_TYPE             char(1)  default 'T' not null ,
   User_Station         varchar(16) not null,
   User_Rank            varchar(16) not null,
   POST_Rank            varchar(16),
   Rank_Memo            varchar(256),
   USER_ORDER           numeric(8,0) default 0,
   update_Date          date,
   Create_Date          date,
   TOP_UNIT             varchar(32),
   creator              varchar(32),
   updator              varchar(32)
);
comment on column F_USERUNIT. REL_TYPE  is  'T：为主， F：兼职' ;
comment on column F_USERUNIT. User_Rank  is  'RANK 代码不是 0开头的可以进行授予';
comment on column F_USERUNIT.  Rank_Memo is '任职备注' ;
comment on table F_USERUNIT is '同一个人可能在多个部门担任不同的职位';
alter table F_USERUNIT add primary key (USER_UNIT_ID);


create table F_USER_QUERY_FILTER
(
   FILTER_NO            numeric(12,0) not null,
   user_Code            varchar(8) not null,
   modle_code           varchar(64) not null  ,
   filter_name          varchar(200) not null ,
   filter_value         varchar(3200) not null,
   IS_DEFAULT           char(1),
   CREATE_DATE          date
);
comment on column F_USER_QUERY_FILTER. modle_code is '开发人员自行定义，单不能重复，建议用系统的模块名加上当前的操作方法'  ;
comment on column F_USER_QUERY_FILTER.  filter_name is   '用户自行定义的名称' ;
comment on column F_USER_QUERY_FILTER. filter_value is  '变量值，json格式，对应一个map' ;
alter table F_USER_QUERY_FILTER add primary key (FILTER_NO);

create table F_UNITROLE
(
   UNIT_CODE            varchar(32) not null,
   ROLE_CODE            varchar(32) not null,
   OBTAIN_DATE          date not null,
   SECEDE_DATE          date,
   CHANGE_DESC          varchar(256),
   update_Date          date,
   Create_Date          date,
   creator              varchar(32),
   updator              varchar(32)
);
alter table F_UNITROLE add primary key (UNIT_CODE, ROLE_CODE);

-- v_hi_unitinfo视图脚本

CREATE OR REPLACE VIEW v_hi_unitinfo AS
SELECT a.unit_code AS top_unit_code,  b.unit_code,b.unit_type, b.parent_unit, b.is_valid, b.unit_name,b.unit_desc,b.unit_short_name,b.unit_order,b.dep_no,
       b.unit_word,b.unit_grade,
       LENGTH(b.Unit_Path)- LENGTH(REPLACE(b.Unit_Path,'/','')) - LENGTH(a.Unit_Path) + LENGTH(REPLACE(a.Unit_Path,'/',''))+1  AS hi_level,
       substr(b.Unit_Path ,  LENGTH(a.Unit_Path)+1) AS Unit_Path
  FROM F_UNITINFO a , F_UNITINFO b
 WHERE b.Unit_Path LIKE CONCAT(a.Unit_Path,'%' );


 create or replace view F_V_Opt_Role_Map as
select concat(c.opt_url,b.OPT_URL) as opt_url, b.opt_req, a.role_code, c.opt_id, b.opt_code
  from F_ROLEPOWER a
  join F_OPTDEF b
    on (a.opt_code = b.opt_code)
  join F_OptInfo c
    on (b.opt_id = c.opt_id)
 where c.Opt_Type <> 'W'
   and c.opt_url <> '...'
 order by c.opt_url, b.opt_req, a.role_code;
/*==============================================================*/
/* View: F_V_USERROLES                                          */
/*==============================================================*/
create or replace view F_V_USERROLES as
SELECT b.role_code,
   b.role_name,
   b.is_valid,
   'D'::text AS obtain_type,
   b.role_type,
   b.unit_code,
   b.role_desc,
   b.create_date,
   b.update_date,
   a.user_code,
   NULL::character varying AS inherited_from
FROM (f_userrole a
   JOIN f_roleinfo b ON (((a.role_code)::text = (b.role_code)::text)))
WHERE ((a.obtain_date <= now()) AND ((a.secede_date IS NULL) OR (a.secede_date > now())) AND (b.is_valid = 'T'::bpchar))
UNION
SELECT b.role_code,
   b.role_name,
   b.is_valid,
   'I'::text AS obtain_type,
   b.role_type,
   b.unit_code,
   b.role_desc,
   b.create_date,
   b.update_date,
   c.user_code,
   a.unit_code AS inherited_from
FROM ((f_unitrole a
   JOIN f_roleinfo b ON (((a.role_code)::text = (b.role_code)::text)))
   JOIN f_userunit c ON (((a.unit_code)::text = (c.unit_code)::text)))
WHERE ((a.obtain_date <= now()) AND ((a.secede_date IS NULL) OR (a.secede_date > now())) AND (b.is_valid = 'T'::bpchar))
union
select 'public'::varchar(32) as ROLE_CODE, '公共角色'::varchar(64) as ROLE_NAME, 'T'::char(1) as IS_VALID, 'D' as OBTAIN_TYPE, 'P'::char(1) as ROLE_TYPE, null::varchar(32) as UNIT_CODE,
   '公共角色'::varchar(256) as  ROLE_DESC, null as CREATE_DATE, null as UPDATE_DATE , a.USER_CODE, NULL as INHERITED_FROM
from F_USERINFO a;

/*==============================================================*/
/* View: F_V_UserOptDataScopes                                  */
/*==============================================================*/
create or replace view F_V_UserOptDataScopes as
select  distinct a.User_Code, c. OPT_ID ,  c.OPT_METHOD , b.opt_Scope_Codes
from F_V_USERROLES a  join F_ROLEPOWER   b on (a.Role_Code=b.Role_Code)
         join F_OPTDEF  c on(b.OPT_CODE=c.OPT_CODE);
/*==============================================================*/
/* View: F_V_UserOptList                                        */
/*==============================================================*/
create or replace view F_V_UserOptList as
select  distinct a.User_Code,  c.OPT_CODE,  c.OPT_NAME  ,  c. OPT_ID ,  c.OPT_METHOD
from F_V_USERROLES a  join F_ROLEPOWER   b on (a.Role_Code=b.Role_Code)
         join F_OPTDEF  c on(b.OPT_CODE=c.OPT_CODE);

/*==============================================================*/
/* View: F_V_UserOptMoudleList                                  */
/*==============================================================*/

create or replace view f_v_useroptmoudlelist as
select  distinct a.User_Code,d.Opt_ID, d.Opt_Name , d.Pre_Opt_ID  ,
            d.Form_Code  , d.opt_url, d.opt_route, d.Msg_No , d.Msg_Prm, d.Is_In_ToolBar ,
            d.Img_Index,d.Top_Opt_ID ,d.Order_Ind,d.Page_Type,d.opt_type
from F_V_USERROLES a  join F_ROLEPOWER b on (a.Role_Code=b.Role_Code)
         join F_OPTDEF  c on(b.OPT_CODE=c.OPT_CODE)
        join F_OptInfo d on(c.OPT_ID=d.Opt_ID)
where d.opt_url<>'...';

/*==============================================================*/
/* View: f_v_optdef_url_map                                     */
/*==============================================================*/
create or replace view f_v_optdef_url_map as
select concat(c.opt_url,b.OPT_URL) as opt_def_url, b.opt_req, b.opt_code
from F_OPTDEF b join F_OptInfo c
    on (b.opt_id = c.opt_id)
 where c.Opt_Type <> 'W'
   and c.opt_url <> '...' and b.opt_req is not null;

/*==============================================================*/
/* View: v_opt_tree                                             */
/*==============================================================*/
create or replace view v_opt_tree as
   select i.opt_id as MENU_ID,i.pre_opt_id as PARENT_ID,i.opt_name as MENU_NAME,i.order_ind
   from F_OptInfo i where i.is_in_toolbar ='Y'
   union all
   select d.opt_code as MENU_ID,d.opt_id as PARENT_ID,d.opt_name as MENU_NAME,0 as order_ind
   from F_OPTDEF d
;
