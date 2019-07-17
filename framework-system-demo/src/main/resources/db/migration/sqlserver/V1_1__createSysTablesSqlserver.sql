IF OBJECT_ID(N'F_DATACATALOG', N'U') IS  NOT  NULL
  DROP table F_DATACATALOG;

IF OBJECT_ID(N'F_DATADICTIONARY', N'U') IS  NOT  NULL
  DROP table F_DATADICTIONARY;

IF OBJECT_ID(N'F_OPTDATASCOPE', N'U') IS  NOT  NULL
  DROP table F_OPTDATASCOPE;

IF OBJECT_ID(N'F_OPTDEF', N'U') IS  NOT  NULL
  DROP table F_OPTDEF;

IF OBJECT_ID(N'F_OPT_LOG', N'U') IS  NOT  NULL
  DROP table F_OPT_LOG;

IF OBJECT_ID(N'F_OptInfo', N'U') IS  NOT  NULL
  DROP table F_OptInfo;

IF OBJECT_ID(N'F_QUERY_FILTER_CONDITION', N'U') IS  NOT  NULL
  DROP table F_QUERY_FILTER_CONDITION;

IF OBJECT_ID(N'F_ROLEINFO', N'U') IS  NOT  NULL
  DROP table F_ROLEINFO;

IF OBJECT_ID(N'F_ROLEPOWER', N'U') IS  NOT  NULL
  DROP table F_ROLEPOWER;

IF OBJECT_ID(N'F_SYS_NOTIFY', N'U') IS  NOT  NULL
  DROP table F_SYS_NOTIFY;

IF OBJECT_ID(N'F_UNITINFO', N'U') IS  NOT  NULL
  DROP table F_UNITINFO;

IF OBJECT_ID(N'F_USERINFO', N'U') IS  NOT  NULL
  DROP table F_USERINFO;

IF OBJECT_ID(N'F_USERROLE', N'U') IS  NOT  NULL
  DROP table F_USERROLE;

IF OBJECT_ID(N'F_USERSETTING', N'U') IS  NOT  NULL
  DROP table F_USERSETTING;

IF OBJECT_ID(N'F_USERUNIT', N'U') IS  NOT  NULL
  DROP table F_USERUNIT;

IF OBJECT_ID(N'F_USER_QUERY_FILTER', N'U') IS  NOT  NULL
  DROP table F_USER_QUERY_FILTER;

IF OBJECT_ID(N'M_InnerMsg', N'U') IS  NOT  NULL
  DROP table M_InnerMsg;

IF OBJECT_ID(N'M_InnerMsg_Recipient', N'U') IS  NOT  NULL
  DROP table M_InnerMsg_Recipient;

IF OBJECT_ID(N'M_MsgAnnex', N'U') IS  NOT  NULL
  DROP table M_MsgAnnex;

IF OBJECT_ID(N'F_UNITROLE', N'U') IS  NOT  NULL
  DROP table F_UNITROLE;

IF OBJECT_ID(N'simulate_sequence', N'U') IS  NOT  NULL
  DROP table simulate_sequence;

create table simulate_sequence
(
  seqname varchar(100) not null primary key,
  currvalue integer,
  increment integer);
INSERT INTO simulate_sequence (seqname, currvalue , increment) VALUES
  ('S_MSGCODE', 0, 1);

INSERT INTO simulate_sequence (seqname, currvalue , increment) VALUES
  ('S_RECIPIENT', 0, 1);

INSERT INTO simulate_sequence (seqname, currvalue , increment) VALUES
  ('S_UNITCODE', 10, 1);

INSERT INTO simulate_sequence (seqname, currvalue , increment) VALUES
  ('S_USERCODE', 10, 1);

INSERT INTO simulate_sequence (seqname, currvalue , increment) VALUES
  ('S_USER_UNIT_ID', 10, 1);

INSERT INTO simulate_sequence (seqname, currvalue , increment) VALUES
  ('S_OPTDEFCODE', 1001000, 1);

INSERT INTO simulate_sequence (seqname, currvalue , increment) VALUES
  ('S_SYS_LOG', 0, 1);

INSERT INTO simulate_sequence (seqname, currvalue , increment) VALUES
  ('S_ROLECODE', 10, 1);
INSERT INTO simulate_sequence (seqname, currvalue , increment) VALUES
  ('S_Filter_No', 0, 1);
create table F_DATACATALOG
(
   CATALOG_CODE         varchar(32) not null,
   CATALOG_NAME         varchar(64) not null,
   CATALOG_STYLE        char(1) not null ,
   CATALOG_TYPE         char(1) not null  ,
   CATALOG_DESC         varchar(256),
   Field_Desc           varchar(1024) ,
   updatetime_datetime          datetime,
   Create_datetime          datetime,
   opt_ID               varchar(32) ,
   need_Cache           char(1) default '1',
   creator              varchar(32),
   updator              varchar(32)
);
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
   Last_Modify_datetime     datetime,
   Create_datetime          datetime,
   DATA_ORDER           numeric(6,0)
);

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
   updatetime_datetime          datetime,
   Create_datetime          datetime,
   OPT_REQ              varchar(8),
   creator              varchar(32),
   updator              varchar(32)
);
alter table F_OPTDEF add primary key (OPT_CODE);


create table F_OPT_LOG
(
   log_Id               numeric(12,0) not null,
   log_Level            varchar(2) not null,
   user_code            varchar(8) not null,
   opt_time             datetime not null,
   Opt_Content          varchar(1000) not null  ,
   New_Value            text ,
   Old_Value            text ,
   Opt_ID               varchar(32) not null  ,
   OPT_Method           varchar(64)  ,
   opt_Tag              varchar(200)
);
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
   updatetime_datetime          datetime,
   Create_datetime          datetime,
   creator              varchar(32),
   updator              varchar(32)
);
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
   CREATE_datetime          datetime
);
alter table F_QUERY_FILTER_CONDITION add primary key (CONDITION_NO);

create table F_ROLEINFO
(
   ROLE_CODE            varchar(32) not null,
   ROLE_NAME            varchar(64),
   ROLE_TYPE            char(1) not null ,
   UNIT_CODE            varchar(32),
   IS_VALID             char(1) not null,
   ROLE_DESC            varchar(256),
   updatetime_datetime          datetime,
   Create_datetime          datetime,
   creator              varchar(32),
   updator              varchar(32)
);
alter table F_ROLEINFO add primary key (ROLE_CODE);

create table F_ROLEPOWER
(
   ROLE_CODE            varchar(32) not null,
   OPT_CODE             varchar(32) not null,
   opt_Scope_Codes      varchar(1000) ,
   updatetime_datetime          datetime,
   Create_datetime          datetime,
   creator              varchar(32),
   updator              varchar(32)
);
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
   Notify_Time          datetime,
   opt_Tag              varchar(200)  ,
   OPT_Method           varchar(64)  ,
   Opt_ID               varchar(32) not null
);
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
   updatetime_datetime          datetime,
   Create_datetime          datetime,
   --extJsonInfo          varchar(1000),
   creator              varchar(32),
   updator              varchar(32),
   UNIT_PATH            varchar(1000),
   UNIT_MANAGER         varchar(32)
);
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
   Active_Time          datetime,
   TOP_UNIT             varchar(32),
   Reg_Email            varchar(60) ,
   USER_PWD             varchar(20) ,
   pwd_Expired_Time     datetime,
   REG_CELL_PHONE       varchar(15),
   ID_CARD_NO           varchar(20),
   primary_Unit         varchar(32),
   user_Word            varchar(100)  ,
   user_Order           numeric(4,0),
   updatetime_datetime          datetime,
   Create_datetime          datetime,
   --extJsonInfo          varchar(1000),
   creator              varchar(32),
   updator              varchar(32)
);
alter table F_USERINFO add primary key (USER_CODE);

create table F_USERROLE
(
   USER_CODE            varchar(32) not null,
   ROLE_CODE            varchar(32) not null,
   OBTAIN_datetime          datetime not null,
   SECEDE_datetime          datetime,
   CHANGE_DESC          varchar(256),
   updatetime_datetime          datetime,
   Create_datetime          datetime,
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
   Create_datetime          datetime
);
alter table F_USERSETTING add primary key (USER_CODE, Param_Code);

create table F_USERUNIT
(
   USER_UNIT_ID         varchar(32) not null,
   UNIT_CODE            varchar(32) not null,
   USER_CODE            varchar(32) not null,
   Is_Primary           char(1)  default '1' not null ,
   User_Station         varchar(16) not null,
   User_Rank            varchar(16) not null  ,
   Rank_Memo            varchar(256)  ,
   USER_ORDER           numeric(8,0) default 0,
   updatetime_datetime          datetime,
   Create_datetime          datetime,
   creator              varchar(32),
   updator              varchar(32)
);
alter table F_USERUNIT add primary key (USER_UNIT_ID);


create table F_USER_QUERY_FILTER
(
   FILTER_NO            numeric(12,0) not null,
   user_Code            varchar(8) not null,
   modle_code           varchar(64) not null  ,
   filter_name          varchar(200) not null ,
   filter_value         varchar(3200) not null,
   IS_DEFAULT           char(1),
   CREATE_datetime          datetime
);
alter table F_USER_QUERY_FILTER add primary key (FILTER_NO);

create table M_InnerMsg
(
   Msg_Code             varchar(32) not null ,
   Sender               varchar(128),
   Send_datetime            datetime,
   Msg_Title            varchar(128),
   Msg_Type             varchar(16) ,
   Mail_Type            char(1) ,
   Mail_UnDel_Type      char(1),
   Receive_Name         varchar(2048) ,
   Hold_Users           numeric(8,0)  ,
   msg_State            char(1)  ,
   msg_Content          image,
   Email_Id             varchar(8)  ,
   Opt_ID               varchar(32) not null  ,
   OPT_Method           varchar(64) ,
   opt_Tag              varchar(200)
);
alter table M_InnerMsg add primary key (Msg_Code);

create table M_InnerMsg_Recipient
(
   Msg_Code             varchar(16) not null,
   Receive              varchar(8) not null,
   Reply_Msg_Code       int,
   Receive_Type         char(1)  ,
   Mail_Type            char(1)  ,
   msg_State            char(1)  ,
   ID                   varchar(32) not null
);
alter table M_InnerMsg_Recipient add primary key (ID);

create table M_MsgAnnex
(
   Msg_Code             varchar(16) not null,
   Info_Code            varchar(16) not null,
   Msg_Annex_Id         varchar(32) not null
);
alter table M_MsgAnnex  add primary key (Msg_Annex_Id);

create table F_UNITROLE
(
   UNIT_CODE            varchar(32) not null,
   ROLE_CODE            varchar(32) not null,
   OBTAIN_datetime          datetime not null,
   SECEDE_datetime          datetime,
   CHANGE_DESC          varchar(256),
   updatetime_datetime          datetime,
   Create_datetime          datetime,
   creator              varchar(32),
   updator              varchar(32)
);
alter table F_UNITROLE add primary key (UNIT_CODE, ROLE_CODE);

IF EXISTS(SELECT 1 FROM sys.views WHERE name = 'v_hi_unitinfo')
  DROP view v_hi_unitinfo;

CREATE  VIEW v_hi_unitinfo AS
SELECT a.unit_code AS top_unit_code, b.unit_code, b.unit_type, b.parent_unit, b.is_valid, b.unit_name,b.unit_desc,b.unit_short_name, b.unit_order, b.dep_no,
       b.unit_word,b.unit_grade,
       Datalength(b.Unit_Path)- Datalength(REPLACE(b.Unit_Path,'/','')) - Datalength(a.Unit_Path) + Datalength(REPLACE(a.Unit_Path,'/',''))+1  AS hi_level,
       Substring(b.Unit_Path ,0,  Datalength(a.Unit_Path)+1) AS Unit_Path
  FROM F_UNITINFO a , F_UNITINFO b
 WHERE b.Unit_Path LIKE a.Unit_Path+'%';

IF EXISTS(SELECT 1 FROM sys.views WHERE name = 'F_V_USERROLES')
  DROP view F_V_USERROLES;
create  view F_V_USERROLES as
  select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'D' as OBTAIN_TYPE, b.ROLE_TYPE, b.UNIT_CODE,
    b.ROLE_DESC, b.CREATE_datetime, b.UPdatetime_datetime ,a.USER_CODE, null as INHERITED_FROM
  from F_USERROLE a join F_ROLEINFO b on (a.ROLE_CODE=b.ROLE_CODE)
  where a.OBTAIN_datetime <=  Getdatetime() and (a.SECEDE_datetime is null or a.SECEDE_datetime > Getdatetime()) and b.IS_VALID='T'
union
  select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'I' as OBTAIN_TYPE, b.ROLE_TYPE, b.UNIT_CODE,
    b.ROLE_DESC, b.CREATE_datetime, b.UPdatetime_datetime ,c.USER_CODE, a.UNIT_CODE as INHERITED_FROM
  from F_UNITROLE a join F_ROLEINFO b on (a.ROLE_CODE = b.ROLE_CODE) JOIN F_USERUNIT c on( a.UNIT_CODE = c.UNIT_CODE)
  where a.OBTAIN_datetime <=  Getdatetime() and (a.SECEDE_datetime is null or a.SECEDE_datetime > Getdatetime()) and b.IS_VALID='T'
union
  select 'public' as ROLE_CODE, '公共角色' as ROLE_NAME, 'T' as IS_VALID, 'D' as OBTAIN_TYPE, 'P' as ROLE_TYPE, null as UNIT_CODE,
      '公共角色' as  ROLE_DESC, null as CREATE_datetime, null as UPdatetime_datetime , a.USER_CODE, NULL as INHERITED_FROM
  from F_USERINFO a;

IF EXISTS(SELECT 1 FROM sys.views WHERE name = 'F_V_Opt_Role_Map')
  DROP view F_V_Opt_Role_Map;
create  view F_V_Opt_Role_Map as
select top 100 percent c.opt_url+b.OPT_URL as opt_url, b.opt_req, a.role_code, c.opt_id, b.opt_code
  from F_ROLEPOWER a
  join F_OPTDEF b
    on (a.opt_code = b.opt_code)
  join F_OptInfo c
    on (b.opt_id = c.opt_id)
 where c.Opt_Type <> 'W'
   and c.opt_url <> '...'
 order by c.opt_url, b.opt_req, a.role_code;

IF EXISTS(SELECT 1 FROM sys.views WHERE name = 'F_V_UserOptDataScopes')
  DROP view F_V_UserOptDataScopes;
create  view F_V_UserOptDataScopes as
select  distinct a.User_Code, c. OPT_ID ,  c.OPT_METHOD , b.opt_Scope_Codes
from F_V_USERROLES a  join F_ROLEPOWER   b on (a.Role_Code=b.Role_Code)
         join F_OPTDEF  c on(b.OPT_CODE=c.OPT_CODE);

IF EXISTS(SELECT 1 FROM sys.views WHERE name = 'F_V_UserOptList')
  DROP view F_V_UserOptList;
create  view F_V_UserOptList as
select  distinct a.User_Code,  c.OPT_CODE,  c.OPT_NAME  ,  c. OPT_ID ,  c.OPT_METHOD
from F_V_USERROLES a  join F_ROLEPOWER   b on (a.Role_Code=b.Role_Code)
         join F_OPTDEF  c on(b.OPT_CODE=c.OPT_CODE);

IF EXISTS(SELECT 1 FROM sys.views WHERE name = 'f_v_useroptmoudlelist')
  DROP view f_v_useroptmoudlelist;
create  view f_v_useroptmoudlelist as
select  distinct a.User_Code,d.Opt_ID, d.Opt_Name , d.Pre_Opt_ID  ,
            d.Form_Code  , d.opt_url, d.opt_route, d.Msg_No , d.Msg_Prm, d.Is_In_ToolBar ,
            d.Img_Index,d.Top_Opt_ID ,d.Order_Ind,d.Page_Type,d.opt_type
from F_V_USERROLES a  join F_ROLEPOWER b on (a.Role_Code=b.Role_Code)
         join F_OPTDEF  c on(b.OPT_CODE=c.OPT_CODE)
        join F_OptInfo d on(c.OPT_ID=d.Opt_ID)
where d.opt_url<>'...';

IF EXISTS(SELECT 1 FROM sys.views WHERE name = 'f_v_optdef_url_map')
  DROP view f_v_optdef_url_map;
create  view f_v_optdef_url_map as
select c.opt_url+b.OPT_URL as opt_def_url, b.opt_req, b.opt_code
from F_OPTDEF b join F_OptInfo c
    on (b.opt_id = c.opt_id)
 where c.Opt_Type <> 'W'
   and c.opt_url <> '...' and b.opt_req is not null;

IF EXISTS(SELECT 1 FROM sys.views WHERE name = 'v_opt_tree')
  DROP view v_opt_tree;
create  view v_opt_tree as
   select i.opt_id as MENU_ID,i.pre_opt_id as PARENT_ID,i.opt_name as MENU_NAME,i.order_ind
   from F_OptInfo i where i.is_in_toolbar ='Y'
   union all
   select d.opt_code as MENU_ID,d.opt_id as PARENT_ID,d.opt_name as MENU_NAME,0 as order_ind
   from F_OPTDEF d;

