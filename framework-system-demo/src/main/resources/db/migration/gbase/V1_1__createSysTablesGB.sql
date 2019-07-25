
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

drop table if exists M_InnerMsg cascade;

drop table if exists M_InnerMsg_Recipient cascade;

drop table if exists M_MsgAnnex cascade;
drop table if exists F_UNITROLE cascade;
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
create sequence s_optdefcode INCREMENT BY 1 start with 1001000;
create sequence s_sys_log;
create sequence s_unitcode  INCREMENT BY 1  start with 10;
create sequence s_user_unit_id INCREMENT BY 1 start with 10;
create sequence s_usercode INCREMENT BY 1 start with 10;
create sequence S_MSGCODE ;
create sequence S_RECIPIENT ;
create sequence S_ROLECODE INCREMENT BY 1 start with 10;

create table F_DATACATALOG
(
   CATALOG_CODE         varchar(32) not null,
   CATALOG_NAME         varchar(64) not null,
   CATALOG_STYLE        char(1) not null ,
   CATALOG_TYPE         char(1) not null  ,
   CATALOG_DESC         lvarchar(256),
   Field_Desc           lvarchar(1024) ,
   update_Date          date,
   Create_Date          date,
   opt_ID               varchar(32) ,
   need_Cache           char(1) default '1',
   creator              varchar(32),
   updator              varchar(32),
   primary key (CATALOG_CODE)
);


create table F_DATADICTIONARY
(
   CATALOG_CODE         varchar(32) not null,
   DATA_CODE            varchar(32) not null,
   EXTRA_CODE           varchar(16)  ,
   EXTRA_CODE2          varchar(16) ,
   DATA_TAG             char(1) ,
   DATA_VALUE           lvarchar(2048),
   DATA_STYLE           char(1) ,
   DATA_DESC            lvarchar(256),
   Last_Modify_Date     date,
   Create_Date          date,
   DATA_ORDER           int,
   primary key (CATALOG_CODE, DATA_CODE)
);

create table F_OPTDATASCOPE
(
   opt_Scope_Code       varchar(32) not null,
   Opt_ID               varchar(32),
   scope_Name           varchar(64),
   Filter_Condition     lvarchar(1024)  ,
   scope_Memo           lvarchar(1024),
   primary key (opt_Scope_Code)
);


create table F_OPTDEF
(
   OPT_CODE             varchar(32) not null,
   Opt_ID               varchar(32),
   OPT_NAME             varchar(100),
   OPT_METHOD           varchar(50)  ,
   OPT_URL              lvarchar(256),
   OPT_DESC             lvarchar(256),
   opt_Order            int,
   Is_In_Workflow       char(1)  ,
   update_Date          date,
   Create_Date          date,
   OPT_REQ              varchar(8),
   creator              varchar(32),
   updator              varchar(32),
   primary key (OPT_CODE)
);


create table F_OPT_LOG
(
   log_Id               int not null,
   log_Level            varchar(2) not null,
   user_code            varchar(8) not null,
   opt_time             date not null,
   Opt_Content          lvarchar(1000) not null  ,
   New_Value            clob ,
   Old_Value            clob ,
   Opt_ID               varchar(32) not null  ,
   OPT_Method           varchar(64)  ,
   opt_Tag              varchar(200),
   primary key (log_Id)
);
create table F_OptInfo
(
   Opt_ID               varchar(32) not null,
   Opt_Name             varchar(100) not null,
   Pre_Opt_ID           varchar(32) not null,
   opt_Route            lvarchar(256)  ,
   opt_url              lvarchar(256),
   Form_Code            varchar(4),
   Opt_Type             char(1)  ,
   Msg_No               int,
   Msg_Prm              lvarchar(256),
   Is_In_ToolBar        char(1),
   Img_Index            int,
   Top_Opt_ID           varchar(32),
   Order_Ind            int ,
   FLOW_CODE            varchar(8) ,
   Page_Type            char(1)  default 'I' ,
   Icon                 lvarchar(512),
   height               int,
   width                int,
   update_Date          date,
   Create_Date          date,
   creator              varchar(32),
   updator              varchar(32),
   primary key (Opt_ID)
);
create table F_QUERY_FILTER_CONDITION
(
   CONDITION_NO         int not null,
   Table_Class_Name     varchar(64) not null  ,
   Param_Name           varchar(64) not null ,
   Param_Label          varchar(120) not null ,
   Param_Type      varchar(8) ,
   Default_Value        varchar(100),
   Filter_Sql           varchar(200) ,
   Select_Data_type     char(1)  default 'N' not null ,
   Select_Data_Catalog  varchar(64) ,
   Select_SQL           lvarchar(1000)  ,
   Select_JSON          lvarchar(2000),
   CREATE_DATE          date,
   primary key (CONDITION_NO)
);
create table F_ROLEINFO
(
   ROLE_CODE            varchar(32) not null,
   ROLE_NAME            varchar(64),
   ROLE_TYPE            char(1) not null ,
   UNIT_CODE            varchar(32),
   IS_VALID             char(1) not null,
   ROLE_DESC            lvarchar(256),
   update_Date          date,
   Create_Date          date,
   creator              varchar(32),
   updator              varchar(32),
   primary key (ROLE_CODE)
);
create table F_ROLEPOWER
(
   ROLE_CODE            varchar(32) not null,
   OPT_CODE             varchar(32) not null,
   opt_Scope_Codes      lvarchar(1000) ,
   update_Date          date,
   Create_Date          date,
   creator              varchar(32),
   updator              varchar(32),
   primary key (ROLE_CODE, OPT_CODE)
);
create table F_SYS_NOTIFY
(
   Notify_ID            int not null,
   Notify_Sender        varchar(100),
   Notify_Receiver      varchar(100) not null,
   Msg_Subject          varchar(200),
   Msg_Content          lvarchar(2000) not null,
   notice_Type          varchar(100),
   Notify_State         char(1)  ,
   Error_Msg            lvarchar(500),
   Notify_Time          date,
   opt_Tag              varchar(200)  ,
   OPT_Method           varchar(64)  ,
   Opt_ID               varchar(32) not null,
   primary key (Notify_ID)
);
create table F_UNITINFO
(
   UNIT_CODE            varchar(32) not null,
   PARENT_UNIT          varchar(32),
   UNIT_TYPE            char(1) ,
   IS_VALID             char(1) not null ,
   UNIT_TAG             varchar(100)  ,
   UNIT_NAME            lvarchar(300) not null,
   english_Name         lvarchar(300),
   dep_no               varchar(100)  ,
   UNIT_DESC            lvarchar(256),
   UNIT_SHORT_NAME      varchar(32),
   unit_Word            varchar(100),
   unit_Grade           int,
   unit_Order           int,
   update_Date          date,
   Create_Date          date,
   creator              varchar(32),
   updator              varchar(32),
   UNIT_PATH            lvarchar(1000),
   UNIT_MANAGER         varchar(32),
   primary key (UNIT_CODE)
);
create table F_USERINFO
(
   USER_CODE            varchar(32) not null,
   USER_PIN             varchar(100),
   USER_TYPE            char(1) default 'U'  ,
   IS_VALID             char(1) not null ,
   LOGIN_NAME           varchar(100) not null,
   User_Name            lvarchar(300) not null ,
   USER_TAG             varchar(100) ,
   english_Name         lvarchar(300),
   USER_DESC            lvarchar(256),
   Login_Times          int,
   Active_Time          date,
   TOP_UNIT             varchar(32),
   Reg_Email            varchar(60) ,
   USER_PWD             varchar(20) ,
   pwd_Expired_Time     date,
   REG_CELL_PHONE       varchar(15),
   ID_CARD_NO           varchar(20),
   primary_Unit         varchar(32),
   user_Word            varchar(100)  ,
   user_Order           int,
   update_Date          date,
   Create_Date          date,
   creator              varchar(32),
   updator              varchar(32),
   primary key (USER_CODE)
);
create table F_USERROLE
(
   USER_CODE            varchar(32) not null,
   ROLE_CODE            varchar(32) not null,
   OBTAIN_DATE          date not null,
   SECEDE_DATE          date,
   CHANGE_DESC          lvarchar(256),
   update_Date          date,
   Create_Date          date,
   creator              varchar(32),
   updator              varchar(32),
   primary key (USER_CODE, ROLE_CODE)
);
create table F_USERSETTING
(
   USER_CODE            varchar(32) not null ,
   Param_Code           varchar(32) not null,
   Param_Value          lvarchar(2048) not null,
   opt_ID               varchar(32) not null,
   Param_Name           varchar(200),
   Create_Date          date,
   primary key (USER_CODE, Param_Code)
);
create table F_USERUNIT
(
   USER_UNIT_ID         varchar(32) not null,
   UNIT_CODE            varchar(32) not null,
   USER_CODE            varchar(32) not null,
   Is_Primary           char(1)  default '1' not null ,
   User_Station         varchar(16) not null,
   User_Rank            varchar(16) not null  ,
   Rank_Memo            lvarchar(256)  ,
   USER_ORDER           int default 0,
   update_Date          date,
   Create_Date          date,
   creator              varchar(32),
   updator              varchar(32),
   primary key (USER_UNIT_ID)
);
create table F_USER_QUERY_FILTER
(
   FILTER_NO            int not null,
   user_Code            varchar(8) not null,
   modle_code           varchar(64) not null  ,
   filter_name          varchar(200) not null ,
   filter_value         lvarchar(3200) not null,
   IS_DEFAULT           char(1),
   CREATE_DATE          date,
   primary key (FILTER_NO)
);
create table M_InnerMsg
(
   Msg_Code             varchar(32) not null ,
   Sender               varchar(128),
   Send_Date            date,
   Msg_Title            varchar(128),
   Msg_Type             varchar(16) ,
   Mail_Type            char(1) ,
   Mail_UnDel_Type      char(1),
   Receive_Name         lvarchar(2048) ,
   Hold_Users           int,
   msg_State            char(1)  ,
   msg_Content          blob,
   Email_Id             varchar(8)  ,
   Opt_ID               varchar(32) not null  ,
   OPT_Method           varchar(64) ,
   opt_Tag              varchar(200),
   primary key (Msg_Code)
);
create table M_InnerMsg_Recipient
(
   Msg_Code             varchar(16) not null,
   Receive              varchar(8) not null,
   Reply_Msg_Code       int,
   Receive_Type         char(1)  ,
   Mail_Type            char(1)  ,
   msg_State            char(1)  ,
   ID                   varchar(32) not null,
   primary key (ID)
);
create table M_MsgAnnex
(
   Msg_Code             varchar(16) not null,
   Info_Code            varchar(16) not null,
   Msg_Annex_Id         varchar(32) not null,
   primary key (Msg_Annex_Id)
);
create table F_UNITROLE
(
   UNIT_CODE            varchar(32) not null,
   ROLE_CODE            varchar(32) not null,
   OBTAIN_DATE          date not null,
   SECEDE_DATE          date,
   CHANGE_DESC          lvarchar(256),
   update_Date          date,
   Create_Date          date,
   creator              varchar(32),
   updator              varchar(32),
   primary key (UNIT_CODE, ROLE_CODE)
);
drop view if exists v_hi_unitinfo;
CREATE  VIEW v_hi_unitinfo AS
SELECT a.unit_code AS top_unit_code, b.unit_code, b.unit_type, b.parent_unit, b.is_valid, b.unit_name,b.unit_desc,b.unit_short_name, b.unit_order, b.dep_no,
       b.unit_word,b.unit_grade,
       LENGTH(b.Unit_Path)- LENGTH(REPLACE(b.Unit_Path,'/','')) - LENGTH(a.Unit_Path) + LENGTH(REPLACE(a.Unit_Path,'/',''))+1  AS hi_level,
       substr(b.Unit_Path ,  LENGTH(a.Unit_Path)+1) AS Unit_Path
  FROM F_UNITINFO a , F_UNITINFO b
 WHERE b.Unit_Path LIKE a.Unit_Path||'%';
drop view if exists F_V_USERROLES;
create view F_V_USERROLES as
  select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'D' as OBTAIN_TYPE, b.ROLE_TYPE, b.UNIT_CODE,
    b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,a.USER_CODE, null as INHERITED_FROM
  from F_USERROLE a join F_ROLEINFO b on (a.ROLE_CODE=b.ROLE_CODE)
  where a.OBTAIN_DATE <=  sysdate and (a.SECEDE_DATE is null or a.SECEDE_DATE > sysdate) and b.IS_VALID='T'
union
  select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'I' as OBTAIN_TYPE, b.ROLE_TYPE, b.UNIT_CODE,
    b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,c.USER_CODE, a.UNIT_CODE as INHERITED_FROM
  from F_UNITROLE a join F_ROLEINFO b on (a.ROLE_CODE = b.ROLE_CODE) JOIN F_USERUNIT c on( a.UNIT_CODE = c.UNIT_CODE)
  where a.OBTAIN_DATE <=  sysdate and (a.SECEDE_DATE is null or a.SECEDE_DATE > sysdate) and b.IS_VALID='T'
union
  select 'public' as ROLE_CODE, 'public' as ROLE_NAME, 'T' as IS_VALID, 'D' as OBTAIN_TYPE, 'P' as ROLE_TYPE, null as UNIT_CODE,
      'public' as  ROLE_DESC, null as CREATE_DATE, null as UPDATE_DATE , a.USER_CODE, NULL as INHERITED_FROM
  from F_USERINFO a;
drop view if exists F_V_Opt_Role_Map;
create  view F_V_Opt_Role_Map(opt_url, opt_req, role_code, opt_id, opt_code) as
select (c.opt_url ||b.OPT_URL) as opt_url, b.opt_req, a.role_code, c.opt_id, b.opt_code
  from F_ROLEPOWER a
  join F_OPTDEF b
    on (a.opt_code = b.opt_code)
  join F_OptInfo c
    on (b.opt_id = c.opt_id)
 where c.Opt_Type <> 'W'
   and c.opt_url <> '...'
 order by c.opt_url, b.opt_req, a.role_code;
drop view if exists F_V_UserOptDataScopes;
create  view F_V_UserOptDataScopes as
select  distinct a.User_Code, c. OPT_ID ,  c.OPT_METHOD , b.opt_Scope_Codes
from F_V_USERROLES a  join F_ROLEPOWER   b on (a.Role_Code=b.Role_Code)
         join F_OPTDEF  c on(b.OPT_CODE=c.OPT_CODE);
drop view if exists F_V_UserOptList;
create  view F_V_UserOptList as
select  distinct a.User_Code,  c.OPT_CODE,  c.OPT_NAME  ,  c. OPT_ID ,  c.OPT_METHOD
from F_V_USERROLES a  join F_ROLEPOWER   b on (a.Role_Code=b.Role_Code)
         join F_OPTDEF  c on(b.OPT_CODE=c.OPT_CODE);

drop view if exists f_v_useroptmoudlelist;
create  view f_v_useroptmoudlelist as
select  distinct a.User_Code,d.Opt_ID, d.Opt_Name , d.Pre_Opt_ID  ,
            d.Form_Code  , d.opt_url, d.opt_route, d.Msg_No , d.Msg_Prm, d.Is_In_ToolBar ,
            d.Img_Index,d.Top_Opt_ID ,d.Order_Ind,d.Page_Type,d.opt_type
from F_V_USERROLES a  join F_ROLEPOWER b on (a.Role_Code=b.Role_Code)
         join F_OPTDEF  c on(b.OPT_CODE=c.OPT_CODE)
        join F_OptInfo d on(c.OPT_ID=d.Opt_ID)
where d.opt_url<>'...';
drop view if exists f_v_optdef_url_map;
create view f_v_optdef_url_map as
select c.opt_url||b.OPT_URL as opt_def_url, b.opt_req, b.opt_code
from F_OPTDEF b join F_OptInfo c
    on (b.opt_id = c.opt_id)
 where c.Opt_Type <> 'W'
   and c.opt_url <> '...' and b.opt_req is not null;
drop view if exists v_opt_tree;
create  view v_opt_tree as
   select i.opt_id as MENU_ID,i.pre_opt_id as PARENT_ID,i.opt_name as MENU_NAME,i.order_ind
   from F_OptInfo i where i.is_in_toolbar ='Y'
   union all
   select d.opt_code as MENU_ID,d.opt_id as PARENT_ID,d.opt_name as MENU_NAME,0 as order_ind
   from F_OPTDEF d;