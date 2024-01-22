CREATE TABLE [f_userunit](
	[user_unit_id] [varchar](32) NOT NULL,
	[unit_code] [varchar](32) NOT NULL,
	[user_code] [varchar](32) NOT NULL,
	[is_primary] [char](1) NOT NULL,
	[user_station] [varchar](16) NOT NULL,
	[user_rank] [varchar](16) NOT NULL,
	[rank_memo] [varchar](256) NULL,
	[user_order] [decimal](8, 0) NULL,
	[update_date] [datetime2](7) NULL,
	[create_date] [datetime2](7) NULL,
	[creator] [varchar](32) NULL,
	[updator] [varchar](32) NULL
) ON [PRIMARY]
GO

CREATE TABLE [f_usersetting](
	[user_code] [varchar](32) NOT NULL,
	[param_code] [varchar](32) NOT NULL,
	[param_value] [varchar](2048) NOT NULL,
	[opt_id] [varchar](32) NOT NULL,
	[param_name] [varchar](200) NULL,
	[create_date] [datetime2](7) NULL
) ON [PRIMARY]
GO

CREATE TABLE [f_userrole](
	[user_code] [varchar](32) NOT NULL,
	[role_code] [varchar](32) NOT NULL,
	[obtain_date] [datetime2](7) NOT NULL,
	[secede_date] [datetime2](7) NULL,
	[change_desc] [varchar](256) NULL,
	[update_date] [datetime2](7) NULL,
	[create_date] [datetime2](7) NULL,
	[creator] [varchar](32) NULL,
	[updator] [varchar](32) NULL
) ON [PRIMARY]
GO

CREATE TABLE [f_userinfo](
	[user_code] [varchar](32) NOT NULL,
	[user_pin] [varchar](100) NULL,
	[user_type] [char](1) NULL,
	[is_valid] [char](1) NOT NULL,
	[login_name] [varchar](100) NOT NULL,
	[user_name] [varchar](300) NOT NULL,
	[user_tag] [varchar](100) NULL,
	[english_name] [varchar](300) NULL,
	[user_desc] [varchar](256) NULL,
	[login_times] [decimal](6, 0) NULL,
	[active_time] [datetime2](7) NULL,
	[top_unit] [varchar](32) NULL,
	[reg_email] [varchar](60) NULL,
	[user_pwd] [varchar](20) NULL,
	[pwd_expired_time] [datetime2](7) NULL,
	[reg_cell_phone] [varchar](15) NULL,
	[id_card_no] [varchar](20) NULL,
	[primary_unit] [varchar](32) NULL,
	[user_word] [varchar](100) NULL,
	[user_order] [decimal](4, 0) NULL,
	[update_date] [datetime2](7) NULL,
	[create_date] [datetime2](7) NULL,
	[creator] [varchar](32) NULL,
	[updator] [varchar](32) NULL
) ON [PRIMARY]
GO

CREATE TABLE [f_user_query_filter](
	[filter_no] [decimal](12, 0) NOT NULL,
	[user_code] [varchar](8) NOT NULL,
	[modle_code] [varchar](64) NOT NULL,
	[filter_name] [varchar](200) NOT NULL,
	[filter_value] [varchar](3200) NOT NULL,
	[is_default] [char](1) NULL,
	[create_date] [datetime2](7) NULL
) ON [PRIMARY]
GO

CREATE TABLE [f_unitrole](
	[unit_code] [varchar](32) NOT NULL,
	[role_code] [varchar](32) NOT NULL,
	[obtain_date] [datetime2](7) NOT NULL,
	[secede_date] [datetime2](7) NULL,
	[change_desc] [varchar](256) NULL,
	[update_date] [datetime2](7) NULL,
	[create_date] [datetime2](7) NULL,
	[creator] [varchar](32) NULL,
	[updator] [varchar](32) NULL
) ON [PRIMARY]
GO

CREATE TABLE [f_unitinfo](
	[unit_code] [varchar](32) NOT NULL,
	[parent_unit] [varchar](32) NULL,
	[unit_type] [char](1) NULL,
	[is_valid] [char](1) NOT NULL,
	[unit_tag] [varchar](100) NULL,
	[unit_name] [varchar](300) NOT NULL,
	[english_name] [varchar](300) NULL,
	[dep_no] [varchar](100) NULL,
	[unit_desc] [varchar](256) NULL,
	[unit_short_name] [varchar](32) NULL,
	[unit_word] [varchar](100) NULL,
	[unit_grade] [decimal](4, 0) NULL,
	[unit_order] [decimal](4, 0) NULL,
	[update_date] [datetime2](7) NULL,
	[create_date] [datetime2](7) NULL,
	[creator] [varchar](32) NULL,
	[updator] [varchar](32) NULL,
	[unit_path] [varchar](1000) NULL,
	[unit_manager] [varchar](32) NULL
) ON [PRIMARY]
GO

CREATE TABLE [f_sys_notify](
	[notify_id] [decimal](12, 0) NOT NULL,
	[notify_sender] [varchar](100) NULL,
	[notify_receiver] [varchar](100) NOT NULL,
	[msg_subject] [varchar](200) NULL,
	[msg_content] [varchar](2000) NOT NULL,
	[notice_type] [varchar](100) NULL,
	[notify_state] [char](1) NULL,
	[error_msg] [varchar](500) NULL,
	[notify_time] [datetime2](7) NULL,
	[opt_tag] [varchar](200) NULL,
	[opt_method] [varchar](64) NULL,
	[opt_id] [varchar](32) NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [f_rolepower](
	[role_code] [varchar](32) NOT NULL,
	[opt_code] [varchar](32) NOT NULL,
	[opt_scope_codes] [varchar](1000) NULL,
	[update_date] [datetime2](7) NULL,
	[create_date] [datetime2](7) NULL,
	[creator] [varchar](32) NULL,
	[updator] [varchar](32) NULL
) ON [PRIMARY]
GO

CREATE TABLE [f_roleinfo](
	[role_code] [varchar](32) NOT NULL,
	[role_name] [varchar](64) NULL,
	[role_type] [char](1) NOT NULL,
	[unit_code] [varchar](32) NULL,
	[is_valid] [char](1) NOT NULL,
	[role_desc] [varchar](256) NULL,
	[update_date] [datetime2](7) NULL,
	[create_date] [datetime2](7) NULL,
	[creator] [varchar](32) NULL,
	[updator] [varchar](32) NULL
) ON [PRIMARY]
GO

CREATE TABLE [f_query_filter_condition](
	[condition_no] [decimal](12, 0) NOT NULL,
	[table_class_name] [varchar](64) NOT NULL,
	[param_name] [varchar](64) NOT NULL,
	[param_label] [varchar](120) NOT NULL,
	[param_type] [varchar](8) NULL,
	[default_value] [varchar](100) NULL,
	[filter_sql] [varchar](200) NULL,
	[select_data_type] [char](1) NOT NULL,
	[select_data_catalog] [varchar](64) NULL,
	[select_sql] [varchar](1000) NULL,
	[select_json] [varchar](2000) NULL,
	[create_date] [datetime2](7) NULL
) ON [PRIMARY]
GO

CREATE TABLE [f_os_info](
	[os_id] [varchar](20) NOT NULL,
	[os_name] [varchar](200) NOT NULL,
	[os_url] [varchar](200) NULL,
	[oauth_password] [varchar](100) NULL,
	[last_modify_date] [datetime2](7) NULL,
	[create_time] [datetime2](7) NULL,
	[created] [varchar](8) NULL,
	[os_home_page] [varchar](300) NULL,
	[oauth_user] [varchar](32) NULL,
	[rel_opt_id] [varchar](32) NULL
) ON [PRIMARY]
GO

CREATE TABLE [f_optinfo](
	[opt_id] [varchar](32) NOT NULL,
	[opt_name] [varchar](100) NOT NULL,
	[pre_opt_id] [varchar](32) NOT NULL,
	[opt_route] [varchar](256) NULL,
	[opt_url] [varchar](256) NULL,
	[form_code] [varchar](4) NULL,
	[opt_type] [char](1) NULL,
	[msg_no] [decimal](10, 0) NULL,
	[msg_prm] [varchar](256) NULL,
	[is_in_toolbar] [char](1) NULL,
	[img_index] [decimal](10, 0) NULL,
	[top_opt_id] [varchar](32) NULL,
	[order_ind] [decimal](4, 0) NULL,
	[flow_code] [varchar](8) NULL,
	[page_type] [char](1) NULL,
	[icon] [varchar](512) NULL,
	[height] [decimal](10, 0) NULL,
	[width] [decimal](10, 0) NULL,
	[update_date] [datetime2](7) NULL,
	[create_date] [datetime2](7) NULL,
	[creator] [varchar](32) NULL,
	[updator] [varchar](32) NULL
) ON [PRIMARY]
GO

CREATE TABLE [f_optdef](
	[opt_code] [varchar](32) NOT NULL,
	[opt_id] [varchar](32) NULL,
	[opt_name] [varchar](100) NULL,
	[opt_method] [varchar](50) NULL,
	[opt_url] [varchar](256) NULL,
	[opt_desc] [varchar](256) NULL,
	[opt_order] [decimal](4, 0) NULL,
	[is_in_workflow] [char](1) NULL,
	[update_date] [datetime2](7) NULL,
	[create_date] [datetime2](7) NULL,
	[opt_req] [varchar](8) NULL,
	[creator] [varchar](32) NULL,
	[updator] [varchar](32) NULL,
 CONSTRAINT [sys_c0068352] PRIMARY KEY CLUSTERED 
(
	[opt_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [f_optdatascope](
	[opt_scope_code] [varchar](32) NOT NULL,
	[opt_id] [varchar](32) NULL,
	[scope_name] [varchar](64) NULL,
	[filter_condition] [varchar](1024) NULL,
	[scope_memo] [varchar](1024) NULL,
 CONSTRAINT [sys_c0068350] PRIMARY KEY CLUSTERED 
(
	[opt_scope_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [f_opt_log](
	[log_id] [decimal](12, 0) NOT NULL,
	[log_level] [varchar](2) NOT NULL,
	[user_code] [varchar](8) NOT NULL,
	[opt_time] [datetime2](7) NOT NULL,
	[opt_content] [varchar](1000) NOT NULL,
	[new_value] [varchar](max) NULL,
	[old_value] [varchar](max) NULL,
	[opt_id] [varchar](32) NOT NULL,
	[opt_method] [varchar](64) NULL,
	[opt_tag] [varchar](200) NULL,
 CONSTRAINT [sys_c0068363] PRIMARY KEY CLUSTERED 
(
	[log_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [f_datadictionary](
	[catalog_code] [varchar](32) NOT NULL,
	[data_code] [varchar](32) NOT NULL,
	[extra_code] [varchar](16) NULL,
	[extra_code2] [varchar](16) NULL,
	[data_tag] [char](1) NULL,
	[data_value] [varchar](2048) NULL,
	[data_style] [char](1) NULL,
	[data_desc] [varchar](256) NULL,
	[last_modify_date] [datetime2](7) NULL,
	[create_date] [datetime2](7) NULL,
	[data_order] [decimal](6, 0) NULL,
 CONSTRAINT [sys_c0068348] PRIMARY KEY CLUSTERED 
(
	[catalog_code] ASC,
	[data_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [f_datacatalog](
	[catalog_code] [varchar](32) NOT NULL,
	[catalog_name] [varchar](64) NOT NULL,
	[catalog_style] [char](1) NOT NULL,
	[catalog_type] [char](1) NOT NULL,
	[catalog_desc] [varchar](256) NULL,
	[field_desc] [varchar](1024) NULL,
	[update_date] [datetime2](7) NULL,
	[create_date] [datetime2](7) NULL,
	[opt_id] [varchar](32) NULL,
	[need_cache] [char](1) NULL,
	[creator] [varchar](32) NULL,
	[updator] [varchar](32) NULL,
 CONSTRAINT [sys_c0068345] PRIMARY KEY CLUSTERED 
(
	[catalog_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [f_database_info](
	[database_code] [varchar](32) NOT NULL,
	[database_name] [varchar](100) NULL,
	[os_id] [varchar](20) NULL,
	[database_url] [varchar](1000) NULL,
	[username] [varchar](100) NULL,
	[password] [varchar](100) NULL,
	[database_desc] [varchar](500) NULL,
	[last_modify_date] [datetime2](7) NULL,
	[create_time] [datetime2](7) NULL,
	[created] [varchar](8) NULL,
 CONSTRAINT [sys_c0068340] PRIMARY KEY CLUSTERED 
(
	[database_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [test](
	[a] [varchar](64) NOT NULL,
	[last_modify_time] [datetime2](7) NULL,
	[b] [varchar](64) NULL
) ON [PRIMARY]
GO

CREATE TABLE [m_msgannex](
	[msg_code] [varchar](16) NOT NULL,
	[info_code] [varchar](16) NOT NULL,
	[msg_annex_id] [varchar](32) NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [m_innermsg_recipient](
	[msg_code] [varchar](16) NOT NULL,
	[receive] [varchar](8) NOT NULL,
	[reply_msg_code] [decimal](38, 30) NULL,
	[receive_type] [char](1) NULL,
	[mail_type] [char](1) NULL,
	[msg_state] [char](1) NULL,
	[id] [varchar](32) NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [m_innermsg](
	[msg_code] [varchar](32) NOT NULL,
	[sender] [varchar](128) NULL,
	[send_date] [datetime2](7) NULL,
	[msg_title] [varchar](128) NULL,
	[msg_type] [varchar](16) NULL,
	[mail_type] [char](1) NULL,
	[mail_undel_type] [char](1) NULL,
	[receive_name] [varchar](2048) NULL,
	[hold_users] [decimal](8, 0) NULL,
	[msg_state] [char](1) NULL,
	[msg_content] [varbinary](max) NULL,
	[email_id] [varchar](8) NULL,
	[opt_id] [varchar](32) NOT NULL,
	[opt_method] [varchar](64) NULL,
	[opt_tag] [varchar](200) NULL
) ON [PRIMARY]
GO

create view [f_v_optdef_url_map] as
select c.opt_url+b.OPT_URL as opt_def_url, b.opt_req, b.opt_code
from F_OPTDEF b join F_OptInfo c
    on (b.opt_id = c.opt_id)
 where c.Opt_Type <> 'W'
   and c.opt_url <> '...' and b.opt_req is not null;
GO

create view [f_v_opt_role_map] as
select c.opt_url+b.OPT_URL as opt_url, b.opt_req, a.role_code, c.opt_id, b.opt_code
  from F_ROLEPOWER a
  join F_OPTDEF b
    on (a.opt_code = b.opt_code)
  join F_OptInfo c
    on (b.opt_id = c.opt_id)
 where c.Opt_Type <> 'W'
   and c.opt_url <> '...';
GO

create view [f_v_lastversionflow] as
select a.FLOW_CODE AS FLOW_CODE,b.version AS VERSION,a.FLOW_NAME AS FLOW_NAME,a.FLOW_CLASS AS FLOW_CLASS,b.FLOW_STATE AS FLOW_STATE,a.FLOW_DESC AS FLOW_DESC,a.FLOW_XML_DESC AS FLOW_XML_DESC,a.Time_Limit AS TIME_LIMIT,a.Expire_Opt AS EXPIRE_OPT,a.Opt_ID AS OPT_ID,a.OS_ID AS OS_ID,a.FLOW_Publish_Date AS FLOW_PUBLISH_DATE,a.AT_PUBLISH_DATE AS AT_PUBLISH_DATE from ((((select wf_flow_define.FLOW_CODE AS FLOW_CODE,max(wf_flow_define.version) AS version from wf_flow_define group by wf_flow_define.FLOW_CODE)) lastversion join wf_flow_define a on(((a.FLOW_CODE = lastversion.FLOW_CODE) and (a.version = 0)))) join wf_flow_define b on(((lastversion.FLOW_CODE = b.FLOW_CODE) and (lastversion.version = b.version))))
GO

create view [v_opt_tree] as
select i.opt_id as MENU_ID,i.pre_opt_id as PARENT_ID,i.opt_name as MENU_NAME,i.order_ind
   from F_OptInfo i where i.is_in_toolbar ='Y'
   union all
   select d.opt_code as MENU_ID,d.opt_id as PARENT_ID,d.opt_name as MENU_NAME,0 as order_ind
   from F_OPTDEF d;
GO

CREATE VIEW [V_HI_UNITINFO] AS SELECT
	a.unit_code AS top_unit_code,
	b.unit_code,
	b.unit_type,
	b.parent_unit,
	b.is_valid,
	b.unit_name,
	b.unit_desc,
	b.unit_short_name,
	b.unit_order,
	b.dep_no,
	b.unit_word,
	b.unit_grade,
	LEN ( b.Unit_Path ) - LEN (
		REPLACE( b.Unit_Path, '/', '' )) - LEN ( a.Unit_Path ) + LEN (
	REPLACE( a.Unit_Path, '/', '' )) + 1 AS hi_level,
	substring ( b.Unit_Path , LEN ( a.Unit_Path ) + 1,0) AS Unit_Path 
FROM
	F_UNITINFO a,
	F_UNITINFO b ;
GO

create view [f_v_useroptmoudlelist] as
select  distinct a.User_Code,d.Opt_ID, d.Opt_Name , d.Pre_Opt_ID  ,
            d.Form_Code  , d.opt_url, d.opt_route, d.Msg_No , d.Msg_Prm, d.Is_In_ToolBar ,
            d.Img_Index,d.Top_Opt_ID ,d.Order_Ind,d.Page_Type,d.opt_type
from F_V_USERROLES a  join F_ROLEPOWER b on (a.Role_Code=b.Role_Code)
         join F_OPTDEF  c on(b.OPT_CODE=c.OPT_CODE)
        join F_OptInfo d on(c.OPT_ID=d.Opt_ID)
where d.opt_url<>'...';
GO

create view [f_v_useroptlist] as
select  distinct a.User_Code,  c.OPT_CODE,  c.OPT_NAME  ,  c. OPT_ID ,  c.OPT_METHOD
from F_V_USERROLES a  join F_ROLEPOWER   b on (a.Role_Code=b.Role_Code)
         join F_OPTDEF  c on(b.OPT_CODE=c.OPT_CODE);
GO

create view [f_v_useroptdatascopes] as
select  distinct a.User_Code, c. OPT_ID ,  c.OPT_METHOD , b.opt_Scope_Codes
from F_V_USERROLES a  join F_ROLEPOWER   b on (a.Role_Code=b.Role_Code)
         join F_OPTDEF  c on(b.OPT_CODE=c.OPT_CODE);
GO