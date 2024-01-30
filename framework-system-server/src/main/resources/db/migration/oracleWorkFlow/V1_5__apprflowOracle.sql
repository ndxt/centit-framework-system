call p_drop_ifExists('C_FORM_DEF');
create table C_FORM_DEF
(
  ID             VARCHAR2(100) not null,
  OPT_FORM_ID    VARCHAR2(100),
  OPT_FORM_URL   VARCHAR2(500),
  IS_EDIT        CHAR(1),
  OPT_SUBMIT_URL VARCHAR2(500)
);
comment on column C_FORM_DEF.ID is 'id';
comment on column C_FORM_DEF.OPT_FORM_ID is '表单id';
comment on column C_FORM_DEF.OPT_FORM_URL is '表单url';
comment on column C_FORM_DEF.IS_EDIT is '是否可编辑';
comment on column C_FORM_DEF.OPT_SUBMIT_URL is '提交url';
alter table C_FORM_DEF add primary key (ID);
call p_drop_ifExists('C_GENERAL_MODULE_PARAM');
create table C_GENERAL_MODULE_PARAM
(
  MODULE_CODE            VARCHAR2(100) not null,
  MODULE_NAME            VARCHAR2(100),
  HAS_FORM               CHAR(1),
  FORM_ID                VARCHAR2(100),
  FORM_POSTION           CHAR(1),
  HAS_IDEA               CHAR(1),
  IDEA_LABEL             VARCHAR2(100),
  IDEA_CATALOG           VARCHAR2(100),
  OPT_FLOW_VAR_PROCESSER VARCHAR2(100),
  NEED_FLOW_VAR          VARCHAR2(1),
  IS_OPT_VAR             VARCHAR2(1),
  IDEA_CONTENT           VARCHAR2(100),
  HAS_ORGROLE            CHAR(1),
  XB_ORGROLE_CODE        VARCHAR2(100),
  XB_ORGROLE_NAME        VARCHAR2(100),
  XB_ORGROLE_FILTER      VARCHAR2(100),
  ZB_ORGROLE_CODE        VARCHAR2(100),
  ZB_ORGROLE_NAME        VARCHAR2(100),
  ZB_ORGROLE_FILTER      VARCHAR2(100),
  ASSIGN_TEAMROLE        CHAR(1),
  TEAMROLE_CODE          VARCHAR2(100),
  TEAMROLE_NAME          VARCHAR2(100),
  TEAMROLE_FILTER        VARCHAR2(100),
  TEAMROLE_CHECK         CHAR(1),
  ASSIGN_ENGINEROLE      VARCHAR2(100),
  ENGINEROLE_CODE        VARCHAR2(100),
  ENGINEROLE_NAME        VARCHAR2(100),
  ENGINEROLE_FILTER      VARCHAR2(100),
  HAS_ATTENTION          CHAR(1),
  ATTENTION_LABEL        VARCHAR2(100),
  ATTENTION_FILTER       VARCHAR2(100),
  HAS_STUFF              CHAR(1),
  HAS_DOCUMENT           CHAR(1),
  DOCUMENT_LABEL         VARCHAR2(100),
  DOCUMENT_URL           VARCHAR2(1000),
  CAN_DEFER              CHAR(1),
  CAN_ROLLBACK           CHAR(1),
  CAN_CLOSE              CHAR(1),
  DOC_READONLY           CHAR(1),
  QUICK_CONTENT          CHAR(1),
  QUICK_CONTENT_RESULT   VARCHAR2(100),
  REMARK                 VARCHAR2(4000),
  LAST_UPDATE_TIME       DATE,
  TEMPLATE_LIST          VARCHAR2(4000),
  HAS_TEMPLATE           CHAR(1),
  COULD_EDIT             CHAR(1),
  SEND_SMS               CHAR(1),
  can_restart  char(1),
  custom_Button char(1),
  button_url varchar2(1000),
  button_label VARCHAR2(255)
);
comment on column C_GENERAL_MODULE_PARAM.MODULE_CODE
  is '模块id';
comment on column C_GENERAL_MODULE_PARAM.MODULE_NAME
  is '环节名称';
comment on column C_GENERAL_MODULE_PARAM.HAS_FORM
  is '是否嵌入表单';
comment on column C_GENERAL_MODULE_PARAM.FORM_ID
  is '表单id';
comment on column C_GENERAL_MODULE_PARAM.FORM_POSTION
  is '表单位置';
comment on column C_GENERAL_MODULE_PARAM.HAS_IDEA
  is '是否需要结果';
comment on column C_GENERAL_MODULE_PARAM.IDEA_LABEL
  is '结果标签';
comment on column C_GENERAL_MODULE_PARAM.IDEA_CATALOG
  is '结果代码';
comment on column C_GENERAL_MODULE_PARAM.OPT_FLOW_VAR_PROCESSER
  is '获取流程变量接口地址';
comment on column C_GENERAL_MODULE_PARAM.NEED_FLOW_VAR
  is '是否从业务获取流程变量';
comment on column C_GENERAL_MODULE_PARAM.IS_OPT_VAR
  is '意见标签';
comment on column C_GENERAL_MODULE_PARAM.IDEA_CONTENT
  is '结果意见标签';
comment on column C_GENERAL_MODULE_PARAM.HAS_ORGROLE
  is '是否处室批分';
comment on column C_GENERAL_MODULE_PARAM.XB_ORGROLE_CODE
  is '协办处室code';
comment on column C_GENERAL_MODULE_PARAM.XB_ORGROLE_NAME
  is '协办处室名称';
comment on column C_GENERAL_MODULE_PARAM.XB_ORGROLE_FILTER
  is '协办处室表达式';
comment on column C_GENERAL_MODULE_PARAM.ZB_ORGROLE_CODE
  is '主办处室code';
comment on column C_GENERAL_MODULE_PARAM.ZB_ORGROLE_NAME
  is '主办处室名称';
comment on column C_GENERAL_MODULE_PARAM.ZB_ORGROLE_FILTER
  is '主办处室表达式';
comment on column C_GENERAL_MODULE_PARAM.ASSIGN_TEAMROLE
  is '是否需要办件角色';
comment on column C_GENERAL_MODULE_PARAM.TEAMROLE_CODE
  is '办件角色code';
comment on column C_GENERAL_MODULE_PARAM.TEAMROLE_NAME
  is '办件角色名称';
comment on column C_GENERAL_MODULE_PARAM.TEAMROLE_FILTER
  is '办件角色表达式';
comment on column C_GENERAL_MODULE_PARAM.TEAMROLE_CHECK
  is '办件角色验证';
comment on column C_GENERAL_MODULE_PARAM.ASSIGN_ENGINEROLE
  is '是否使用权限引擎';
comment on column C_GENERAL_MODULE_PARAM.ENGINEROLE_CODE
  is '权限引擎code';
comment on column C_GENERAL_MODULE_PARAM.ENGINEROLE_NAME
  is '权限引擎名称';
comment on column C_GENERAL_MODULE_PARAM.ENGINEROLE_FILTER
  is '权限引擎表达式';
comment on column C_GENERAL_MODULE_PARAM.HAS_ATTENTION
  is '是否需要关注';
comment on column C_GENERAL_MODULE_PARAM.ATTENTION_LABEL
  is '关注标签';
comment on column C_GENERAL_MODULE_PARAM.ATTENTION_FILTER
  is '关注人员表达式';
comment on column C_GENERAL_MODULE_PARAM.HAS_STUFF
  is '是否需要材料';
comment on column C_GENERAL_MODULE_PARAM.HAS_DOCUMENT
  is '是否有文书操作';
comment on column C_GENERAL_MODULE_PARAM.DOCUMENT_LABEL
  is '文书标签';
comment on column C_GENERAL_MODULE_PARAM.DOCUMENT_URL
  is '文书链接';
comment on column C_GENERAL_MODULE_PARAM.CAN_DEFER
  is '是否可以暂缓';
comment on column C_GENERAL_MODULE_PARAM.CAN_ROLLBACK
  is '是否可以回退';
comment on column C_GENERAL_MODULE_PARAM.CAN_CLOSE
  is '是否可以办结';
comment on column C_GENERAL_MODULE_PARAM.DOC_READONLY
  is '文书查看';
comment on column C_GENERAL_MODULE_PARAM.QUICK_CONTENT
  is '是否需要快捷意见';
comment on column C_GENERAL_MODULE_PARAM.QUICK_CONTENT_RESULT
  is '快捷意见字典项';
comment on column C_GENERAL_MODULE_PARAM.REMARK
  is '备注';
comment on column C_GENERAL_MODULE_PARAM.LAST_UPDATE_TIME
  is '最新更新时间';
COMMENT ON COLUMN c_general_module_param.can_restart IS '是否可以退回首节点（发起人）';
alter table C_GENERAL_MODULE_PARAM
  add primary key (MODULE_CODE);
call p_drop_ifExists('F_WORK_DAY');
create table F_WORK_DAY
(
  WORK_DAY       DATE not null,
  DAY_TYPE       CHAR(1) not null,
  WORK_TIME_TYPE VARCHAR2(20),
  WORK_DAY_DESC  VARCHAR2(255)
);
comment on column F_WORK_DAY.WORK_DAY
  is '日期';
comment on column F_WORK_DAY.DAY_TYPE
  is '日期类型';
comment on column F_WORK_DAY.WORK_TIME_TYPE
  is '时间类型';
comment on column F_WORK_DAY.WORK_DAY_DESC
  is '日期描述';
alter table F_WORK_DAY add primary key (WORK_DAY);

call p_drop_ifExists('OPT_IDEA_INFO');
create table OPT_IDEA_INFO
(
  PROC_ID       VARCHAR2(50) not null,
  NODE_INST_ID  NUMBER(12) not null,
  FLOW_INST_ID  VARCHAR2(100),
  UNIT_CODE     VARCHAR2(100),
  UNIT_NAME     VARCHAR2(100),
  USER_CODE     VARCHAR2(100),
  USER_NAME     VARCHAR2(100),
  TRANS_DATE    DATE,
  IDEA_CODE     VARCHAR2(100),
  TRANS_IDEA    VARCHAR2(100),
  TRANS_CONTENT VARCHAR2(2000),
  NODE_NAME     VARCHAR2(100),
  FLOW_PHASE    VARCHAR2(100),
  NODE_CODE     VARCHAR2(100),
  GRANTOR       VARCHAR2(100)
);
comment on column OPT_IDEA_INFO.PROC_ID
  is '主键id';
comment on column OPT_IDEA_INFO.NODE_INST_ID
  is '节点id';
comment on column OPT_IDEA_INFO.FLOW_INST_ID
  is '流程id';
comment on column OPT_IDEA_INFO.UNIT_CODE
  is '部门编码';
comment on column OPT_IDEA_INFO.UNIT_NAME
  is '部门名称';
comment on column OPT_IDEA_INFO.USER_CODE
  is '用户编码';
comment on column OPT_IDEA_INFO.USER_NAME
  is '用户姓名';
comment on column OPT_IDEA_INFO.TRANS_DATE
  is '操作时间';
comment on column OPT_IDEA_INFO.IDEA_CODE
  is '结果代码';
comment on column OPT_IDEA_INFO.TRANS_IDEA
  is '结果内容';
comment on column OPT_IDEA_INFO.TRANS_CONTENT
  is '办理意见';
comment on column OPT_IDEA_INFO.NODE_NAME
  is '环节名称';
comment on column OPT_IDEA_INFO.FLOW_PHASE
  is '流程阶段';
comment on column OPT_IDEA_INFO.NODE_CODE
  is '节点编码';
comment on column OPT_IDEA_INFO.GRANTOR
  is '委托人';
alter table OPT_IDEA_INFO
  add primary key (PROC_ID, NODE_INST_ID);

call p_drop_ifExists('OPT_PROC_ATTENTION');
create table OPT_PROC_ATTENTION
(
  node_inst_id NUMBER,
  user_code    VARCHAR2(100) not null,
  att_set_time DATE,
  flow_inst_id VARCHAR2(100),
  form_id      VARCHAR2(100),
  att_set_user VARCHAR2(32),
  is_att       VARCHAR2(1),
  att_type     VARCHAR2(1),
  content      VARCHAR2(1000),
  read_date    DATE,
  attent_id    VARCHAR2(50) not null
);
-- Add comments to the columns
comment on column OPT_PROC_ATTENTION.user_code
  is '发送方，比如A在批分步骤发送给B关注，这里存B';
comment on column OPT_PROC_ATTENTION.att_set_time
  is '发送时间，比如A在批分步骤发送给B关注，这里存A的时间';
comment on column OPT_PROC_ATTENTION.att_set_user
  is '发送方，比如A在批分步骤发送给B关注，这里存A';
comment on column OPT_PROC_ATTENTION.is_att
  is '0：待阅  1：已阅';
comment on column OPT_PROC_ATTENTION.att_type
  is '0：保存  1：提交';
comment on column OPT_PROC_ATTENTION.content
  is '意见';
comment on column OPT_PROC_ATTENTION.read_date
  is '给予意见，发送阅件时间';
alter table OPT_PROC_ATTENTION
  add primary key (ATTENT_ID);

call p_drop_ifExists('OPT_PROC_INFO');
create table OPT_PROC_INFO
(
  NODE_INST_ID  NUMBER(12) not null,
  FLOW_INST_ID  VARCHAR2(100),
  NODE_CODE     VARCHAR2(100),
  FLOW_PHASE    VARCHAR2(100),
  NODE_NAME     VARCHAR2(100),
  TRANS_CONTENT VARCHAR2(100),
  IDEA_CODE     VARCHAR2(100),
  TRANS_IDEA    VARCHAR2(100),
  TRANS_DATE    DATE,
  USER_CODE     VARCHAR2(100),
  UNIT_CODE     VARCHAR2(100),
  GRANTOR       VARCHAR2(100)
);
comment on column OPT_PROC_INFO.FLOW_INST_ID
  is '流程id';
comment on column OPT_PROC_INFO.NODE_CODE
  is '环节代码';
comment on column OPT_PROC_INFO.FLOW_PHASE
  is '流程阶段';
comment on column OPT_PROC_INFO.NODE_NAME
  is '环节名称';
comment on column OPT_PROC_INFO.TRANS_CONTENT
  is '办理意见';
comment on column OPT_PROC_INFO.IDEA_CODE
  is '办理决定code';
comment on column OPT_PROC_INFO.TRANS_IDEA
  is '办理决定名称';
comment on column OPT_PROC_INFO.TRANS_DATE
  is '提交时间';
comment on column OPT_PROC_INFO.USER_CODE
  is '操作人';
comment on column OPT_PROC_INFO.UNIT_CODE
  is '操作部门';
comment on column OPT_PROC_INFO.GRANTOR
  is '委托人';
alter table OPT_PROC_INFO
  add primary key (NODE_INST_ID);

call p_drop_ifExists('OPT_STUFF_INFO');
create table OPT_STUFF_INFO
(
  STUFF_ID     VARCHAR2(100) not null,
  NODE_INST_ID NUMBER(12),
  FLOW_INST_ID VARCHAR2(100),
  FILE_TYPE    VARCHAR2(100),
  GROUP_ID     VARCHAR2(100),
  SORT_ID      VARCHAR2(100),
  UPLOAD_USER  VARCHAR2(100),
  NODE_NAME    VARCHAR2(100),
  FILE_NAME    VARCHAR2(100),
  FILE_ID      VARCHAR2(100),
  CREATE_TIME  DATE,
  DEL_FLAG     VARCHAR2(10),
  TEMPLATE_ID  VARCHAR2(1000),
  DOWNLOAD_URL VARCHAR2(255)
);
comment on column OPT_STUFF_INFO.STUFF_ID
  is '材料id';
comment on column OPT_STUFF_INFO.NODE_INST_ID
  is '环节id';
comment on column OPT_STUFF_INFO.FLOW_INST_ID
  is '流程id';
comment on column OPT_STUFF_INFO.FILE_TYPE
  is '附件类型';
comment on column OPT_STUFF_INFO.UPLOAD_USER
  is '上传人';
comment on column OPT_STUFF_INFO.NODE_NAME
  is '节点名称';
comment on column OPT_STUFF_INFO.FILE_NAME
  is '文件名称';
comment on column OPT_STUFF_INFO.FILE_ID
  is '文件服务对应id';
comment on column OPT_STUFF_INFO.CREATE_TIME
  is '创建时间';
COMMENT ON COLUMN opt_stuff_info.DOWNLOAD_URL IS '文件下载链接';
alter table OPT_STUFF_INFO
  add primary key (STUFF_ID);

call p_drop_ifExists('OPT_TEMPLATE_FILE');
create table OPT_TEMPLATE_FILE
(
  RECORD_ID    VARCHAR2(100) not null,
  FILE_BODY    BLOB,
  FLOW_INST_ID VARCHAR2(100),
  FILE_TYPE    VARCHAR2(100),
  FILE_NAME    VARCHAR2(100),
  TEMPLATE_ID  NUMBER(10),
  CREATE_TIME  DATE,
  DEL_FLAG     VARCHAR2(10),
  VERSION_ID   NUMBER(10),
  USER_NAME    VARCHAR2(100),
  IS_USED      VARCHAR2(100),
  TEMP_TYPE    VARCHAR2(255),
  ORDER_BY     VARCHAR2(2),
  FILE_SIZE    NUMBER,
  FILE_PATH    VARCHAR2(255),
  DE_SCRIPT    VARCHAR2(255),
  CODE_CODE    VARCHAR2(100),
  BOOK_MARK    VARCHAR2(4000),
  DATA_URL     VARCHAR2(255)
);
-- Add comments to the columns
comment on column OPT_TEMPLATE_FILE.RECORD_ID
  is '主键id';
comment on column OPT_TEMPLATE_FILE.FILE_BODY
  is '文件内容';
comment on column OPT_TEMPLATE_FILE.FLOW_INST_ID
  is '流程id';
comment on column OPT_TEMPLATE_FILE.FILE_TYPE
  is '文件类型';
comment on column OPT_TEMPLATE_FILE.FILE_NAME
  is '文件名称';
comment on column OPT_TEMPLATE_FILE.TEMPLATE_ID
  is '模板编码';
comment on column OPT_TEMPLATE_FILE.CREATE_TIME
  is '创建时间';
comment on column OPT_TEMPLATE_FILE.DEL_FLAG
  is '删除标志';
comment on column OPT_TEMPLATE_FILE.VERSION_ID
  is '版本id';
comment on column OPT_TEMPLATE_FILE.USER_NAME
  is '用户姓名';
comment on column OPT_TEMPLATE_FILE.IS_USED
  is '是否可用';
comment on column OPT_TEMPLATE_FILE.TEMP_TYPE
  is '模板类型';
comment on column OPT_TEMPLATE_FILE.ORDER_BY
  is '排序号';
comment on column OPT_TEMPLATE_FILE.FILE_SIZE
  is '文件大小';
comment on column OPT_TEMPLATE_FILE.FILE_PATH
  is '文件路径';
comment on column OPT_TEMPLATE_FILE.DE_SCRIPT
  is '描述';
comment on column OPT_TEMPLATE_FILE.CODE_CODE
  is '编码';
comment on column OPT_TEMPLATE_FILE.BOOK_MARK
  is '书签';
comment on column OPT_TEMPLATE_FILE.DATA_URL
  is '数据地址';
-- Create/Recreate primary, unique and foreign key constraints
alter table OPT_TEMPLATE_FILE
  add primary key (RECORD_ID);

call p_drop_ifExists('TEMPLATE_BOOKMARKS');
create table TEMPLATE_BOOKMARKS
(
  BOOKMARK_ID           NUMBER not null,
  RECORD_ID             VARCHAR2(255),
  BOOKMARK_NAME         VARCHAR2(255),
  BOOKMARK_ENGLISH_NAME VARCHAR2(255),
  USEFUL                VARCHAR2(255),
  STATICAL              VARCHAR2(255),
  INSERT_NODE           VARCHAR2(255),
  INSERT_TIMES          VARCHAR2(255),
  CREATE_TIME           DATE
);
-- Add comments to the columns
comment on column TEMPLATE_BOOKMARKS.BOOKMARK_ID
  is '书签主键';
comment on column TEMPLATE_BOOKMARKS.RECORD_ID
  is '模板id';
comment on column TEMPLATE_BOOKMARKS.BOOKMARK_NAME
  is '书签名称';
comment on column TEMPLATE_BOOKMARKS.BOOKMARK_ENGLISH_NAME
  is '书签转换英文名称';
comment on column TEMPLATE_BOOKMARKS.USEFUL
  is '是否使用';
comment on column TEMPLATE_BOOKMARKS.STATICAL
  is '是否静态';
comment on column TEMPLATE_BOOKMARKS.INSERT_NODE
  is '插入节点';
comment on column TEMPLATE_BOOKMARKS.INSERT_TIMES
  is '插入次数';
comment on column TEMPLATE_BOOKMARKS.CREATE_TIME
  is '创建时间';
alter table TEMPLATE_BOOKMARKS
  add primary key (BOOKMARK_ID);

call p_drop_ifExists('T_OFFERS_REQ');
create table T_OFFERS_REQ
(
  REQ_NO        VARCHAR2(50) not null,
  REQ_USERCODE  VARCHAR2(50),
  REQ_DEPT      VARCHAR2(50),
  REQ_DATE      DATE,
  CUSTOMER_CODE VARCHAR2(50),
  SUM           CHAR(10),
  CUSTOMER_NAME VARCHAR2(500),
  ISVALID       CHAR(1),
  REMARK        VARCHAR2(2000)
);
alter table T_OFFERS_REQ
  add primary key (REQ_NO);

call p_drop_ifExists('T_OFFERS_REQ_SUB');
create table T_OFFERS_REQ_SUB
(
  REQ_SUBID    VARCHAR2(50) not null,
  REQ_NO       VARCHAR2(50),
  BUSS_CODE    VARCHAR2(50),
  BUSS_DESC    VARCHAR2(2000),
  PRODUCT_LINE CHAR(50),
  LIST_PRICE   NUMBER,
  PRICE_UNIT   NUMBER,
  REQ_PRICE    NUMBER,
  REQ_NUM      NUMBER,
  DISCOUNT     NUMBER,
  ALLOW_PRICE  NUMBER,
  TOTAL        NUMBER,
  ISVALID      CHAR(1)
);
alter table T_OFFERS_REQ_SUB
  add primary key (REQ_SUBID);

call p_drop_ifExists('WF_ACTION_LOG');
create table WF_ACTION_LOG
(
  ACTION_ID    NUMBER(12) not null,
  NODE_INST_ID NUMBER(12),
  ACTION_TYPE  VARCHAR2(2) not null,
  ACTION_TIME  DATE not null,
  USER_CODE    VARCHAR2(32),
  ROLE_TYPE    VARCHAR2(32),
  ROLE_CODE    VARCHAR2(32),
  GRANTOR      VARCHAR2(32)
);
comment on column WF_ACTION_LOG.ACTION_ID
  is '活动编号';
comment on column WF_ACTION_LOG.NODE_INST_ID
  is '节点实例编号';
comment on column WF_ACTION_LOG.ACTION_TYPE
  is '活动类别';
comment on column WF_ACTION_LOG.ACTION_TIME
  is '活动时间';
comment on column WF_ACTION_LOG.USER_CODE
  is '操作用户';
comment on column WF_ACTION_LOG.ROLE_TYPE
  is '角色类别';
comment on column WF_ACTION_LOG.ROLE_CODE
  is '角色代码';
comment on column WF_ACTION_LOG.GRANTOR
  is '委托人';
alter table WF_ACTION_LOG
  add primary key (ACTION_ID);

call p_drop_ifExists('WF_ACTION_TASK');
create table WF_ACTION_TASK
(
  TASK_ID      NUMBER(12) not null,
  NODE_INST_ID NUMBER(12),
  ASSIGN_TIME  DATE not null,
  EXPIRE_TIME  DATE,
  USER_CODE    VARCHAR2(32),
  ROLE_TYPE    VARCHAR2(32),
  ROLE_CODE    VARCHAR2(32),
  TASK_STATE   CHAR(1),
  IS_VALID     CHAR(1),
  AUTH_DESC    VARCHAR2(255)
);
comment on column WF_ACTION_TASK.TASK_ID
  is '活动编号';
comment on column WF_ACTION_TASK.NODE_INST_ID
  is '节点实例编号';
comment on column WF_ACTION_TASK.ASSIGN_TIME
  is '分配时间';
comment on column WF_ACTION_TASK.EXPIRE_TIME
  is '过期时间';
comment on column WF_ACTION_TASK.USER_CODE
  is '操作用户';
comment on column WF_ACTION_TASK.ROLE_TYPE
  is '角色类别';
comment on column WF_ACTION_TASK.ROLE_CODE
  is '角色代码';
comment on column WF_ACTION_TASK.TASK_STATE
  is '活动状态';
comment on column WF_ACTION_TASK.IS_VALID
  is '是否生效';
comment on column WF_ACTION_TASK.AUTH_DESC
  is '授权说明';
alter table WF_ACTION_TASK
  add primary key (TASK_ID);

call p_drop_ifExists('WF_FLOWOPT');
create table WF_FLOWOPT
(
  OPTID     VARCHAR2(100) not null,
  OPTNAME   VARCHAR2(1000),
  OPTURL    VARCHAR2(1000),
  IS_EDIT   CHAR(1),
  UNIT_NAME VARCHAR2(300) not null
);
comment on column WF_FLOWOPT.OPTID
  is '业务id，用序列';
comment on column WF_FLOWOPT.OPTNAME
  is '业务名称';
comment on column WF_FLOWOPT.OPTURL
  is '业务url';
comment on column WF_FLOWOPT.IS_EDIT
  is '是否可编辑';
comment on column WF_FLOWOPT.UNIT_NAME
  is '部门名称';
alter table WF_FLOWOPT
  add primary key (OPTID);

call p_drop_ifExists('WF_FLOW_DEFINE');
create table WF_FLOW_DEFINE
(
  FLOW_CODE         VARCHAR2(32) not null,
  VERSION           NUMBER(20) default '0' not null,
  FLOW_NAME         VARCHAR2(120),
  FLOW_CLASS        VARCHAR2(4) not null,
  FLOW_PUBLISH_DATE DATE,
  FLOW_STATE        CHAR(1),
  FLOW_DESC         VARCHAR2(500),
  FLOW_XML_DESC     CLOB,
  TIME_LIMIT        VARCHAR2(20),
  EXPIRE_OPT        CHAR(1),
  OPT_ID            VARCHAR2(32),
  AT_PUBLISH_DATE   DATE,
  OS_ID             VARCHAR2(32)
);
comment on column WF_FLOW_DEFINE.FLOW_CODE
  is '流程代码';
comment on column WF_FLOW_DEFINE.VERSION
  is '流程版本号';
comment on column WF_FLOW_DEFINE.FLOW_NAME
  is '流程名称';
comment on column WF_FLOW_DEFINE.FLOW_CLASS
  is '流程类别';
comment on column WF_FLOW_DEFINE.FLOW_PUBLISH_DATE
  is '发布时间';
comment on column WF_FLOW_DEFINE.FLOW_STATE
  is '流程状态';
comment on column WF_FLOW_DEFINE.FLOW_DESC
  is '流程描述';
comment on column WF_FLOW_DEFINE.FLOW_XML_DESC
  is '流程定义XML';
comment on column WF_FLOW_DEFINE.TIME_LIMIT
  is '预期时间';
comment on column WF_FLOW_DEFINE.EXPIRE_OPT
  is '逾期处理办法';
comment on column WF_FLOW_DEFINE.OPT_ID
  is '业务代码';
comment on column WF_FLOW_DEFINE.AT_PUBLISH_DATE
  is '计划发布时间';
alter table WF_FLOW_DEFINE
  add primary key (VERSION, FLOW_CODE);

call p_drop_ifExists('WF_FLOW_INSTANCE');
create table WF_FLOW_INSTANCE
(
  FLOW_INST_ID     NUMBER(12) not null,
  VERSION          NUMBER(4),
  FLOW_CODE        VARCHAR2(32),
  FLOW_OPT_NAME    VARCHAR2(100),
  FLOW_OPT_TAG     VARCHAR2(100),
  CREATE_TIME      DATE not null,
  IS_TIMER         CHAR(1),
  PROMISE_TIME     NUMBER(10),
  TIME_LIMIT       NUMBER(10),
  LAST_UPDATE_USER VARCHAR2(32),
  LAST_UPDATE_TIME DATE,
  INST_STATE       CHAR(1),
  IS_SUB_INST      CHAR(1),
  PRE_INST_ID      NUMBER(16),
  PRE_NODE_INST_ID NUMBER(16),
  UNIT_CODE        VARCHAR2(32),
  USER_CODE        VARCHAR2(32)
);
comment on column WF_FLOW_INSTANCE.FLOW_INST_ID
  is '流程实例ID';
comment on column WF_FLOW_INSTANCE.VERSION
  is '流程版本号';
comment on column WF_FLOW_INSTANCE.FLOW_CODE
  is '流程代码';
comment on column WF_FLOW_INSTANCE.FLOW_OPT_NAME
  is '流程业务名称';
comment on column WF_FLOW_INSTANCE.FLOW_OPT_TAG
  is '流程业务标记';
comment on column WF_FLOW_INSTANCE.CREATE_TIME
  is '创建时间';
comment on column WF_FLOW_INSTANCE.IS_TIMER
  is '是否计时';
comment on column WF_FLOW_INSTANCE.PROMISE_TIME
  is '承诺完成时间';
comment on column WF_FLOW_INSTANCE.TIME_LIMIT
  is '剩余时间';
comment on column WF_FLOW_INSTANCE.LAST_UPDATE_USER
  is '最后更新人';
comment on column WF_FLOW_INSTANCE.LAST_UPDATE_TIME
  is '最后更新时间';
comment on column WF_FLOW_INSTANCE.INST_STATE
  is '流程状态';
comment on column WF_FLOW_INSTANCE.IS_SUB_INST
  is '是否是子流程';
comment on column WF_FLOW_INSTANCE.PRE_INST_ID
  is '父流程实例';
comment on column WF_FLOW_INSTANCE.PRE_NODE_INST_ID
  is '父流程节点实例';
comment on column WF_FLOW_INSTANCE.UNIT_CODE
  is '所属机构';
comment on column WF_FLOW_INSTANCE.USER_CODE
  is '创建人';
alter table WF_FLOW_INSTANCE
  add primary key (FLOW_INST_ID);

call p_drop_ifExists('WF_FLOW_STAGE');
create table WF_FLOW_STAGE
(
  STAGE_ID        NUMBER(12) not null,
  VERSION         NUMBER(4),
  FLOW_CODE       VARCHAR2(32),
  STAGE_CODE      VARCHAR2(32) not null,
  STAGE_NAME      VARCHAR2(60),
  IS_ACCOUNT_TIME CHAR(1),
  LIMIT_TYPE      CHAR(1),
  TIME_LIMIT      VARCHAR2(20),
  EXPIRE_OPT      CHAR(1)
);
comment on column WF_FLOW_STAGE.STAGE_ID
  is '阶段编号';
comment on column WF_FLOW_STAGE.VERSION
  is '流程版本号';
comment on column WF_FLOW_STAGE.FLOW_CODE
  is '流程代码';
comment on column WF_FLOW_STAGE.STAGE_CODE
  is '阶段代码';
comment on column WF_FLOW_STAGE.STAGE_NAME
  is '阶段名称';
comment on column WF_FLOW_STAGE.IS_ACCOUNT_TIME
  is '是否记入执行时间';
comment on column WF_FLOW_STAGE.LIMIT_TYPE
  is '期限类别';
comment on column WF_FLOW_STAGE.TIME_LIMIT
  is '期限时间';
comment on column WF_FLOW_STAGE.EXPIRE_OPT
  is '逾期处理办法';
alter table WF_FLOW_STAGE
  add primary key (STAGE_ID);

call p_drop_ifExists('WF_FLOW_TEAM_ROLE');
create table WF_FLOW_TEAM_ROLE
(
  FLOW_TEAM_ROLE_ID VARCHAR2(32) not null,
  FLOW_CODE         VARCHAR2(32) not null,
  ROLE_CODE         VARCHAR2(100) not null,
  ROLE_NAME         VARCHAR2(100) not null,
  TEAM_ROLE_ORDER   NUMBER,
  CREATE_TIME       DATE default sysdate,
  MODIFY_TIME       DATE default sysdate,
  VERSION           NUMBER(4)
);
comment on column WF_FLOW_TEAM_ROLE.FLOW_TEAM_ROLE_ID
  is '定义id';
comment on column WF_FLOW_TEAM_ROLE.FLOW_CODE
  is '流程编码';
comment on column WF_FLOW_TEAM_ROLE.ROLE_CODE
  is '角色编码';
comment on column WF_FLOW_TEAM_ROLE.ROLE_NAME
  is '角色名称';
comment on column WF_FLOW_TEAM_ROLE.TEAM_ROLE_ORDER
  is '排序';
comment on column WF_FLOW_TEAM_ROLE.CREATE_TIME
  is '创建时间';
comment on column WF_FLOW_TEAM_ROLE.MODIFY_TIME
  is '更新时间';
comment on column WF_FLOW_TEAM_ROLE.VERSION
  is '版本号';
alter table WF_FLOW_TEAM_ROLE
  add primary key (FLOW_TEAM_ROLE_ID);

call p_drop_ifExists('WF_FLOW_VARIABLE');
create table WF_FLOW_VARIABLE
(
  FLOW_INST_ID NUMBER(12) not null,
  RUN_TOKEN    VARCHAR2(20) not null,
  VAR_NAME     VARCHAR2(50) not null,
  VAR_VALUE    VARCHAR2(256) not null,
  VAR_TYPE     CHAR(1) not null
);
comment on column WF_FLOW_VARIABLE.FLOW_INST_ID
  is '流程实例ID';
comment on column WF_FLOW_VARIABLE.RUN_TOKEN
  is '运行令牌';
comment on column WF_FLOW_VARIABLE.VAR_NAME
  is '变量名';
comment on column WF_FLOW_VARIABLE.VAR_VALUE
  is '变量值';
comment on column WF_FLOW_VARIABLE.VAR_TYPE
  is '变量类型';
alter table WF_FLOW_VARIABLE
  add primary key (FLOW_INST_ID, RUN_TOKEN, VAR_NAME);

call p_drop_ifExists('WF_FLOW_VARIABLE_DEFINE');
create table WF_FLOW_VARIABLE_DEFINE
(
  FLOW_VARIABLE_ID VARCHAR2(32) not null,
  FLOW_CODE        VARCHAR2(32) not null,
  VARIABLE_NAME    VARCHAR2(100) not null,
  VARIABLE_TYPE    VARCHAR2(100),
  VARIABLE_ORDER   NUMBER,
  CREATE_TIME      DATE default sysdate,
  MODIFY_TIME      DATE default sysdate,
  VERSION          NUMBER(4)
);
comment on column WF_FLOW_VARIABLE_DEFINE.FLOW_VARIABLE_ID
  is '变量id';
comment on column WF_FLOW_VARIABLE_DEFINE.FLOW_CODE
  is '流程编码';
comment on column WF_FLOW_VARIABLE_DEFINE.VARIABLE_NAME
  is '变量名称';
comment on column WF_FLOW_VARIABLE_DEFINE.VARIABLE_TYPE
  is '变量类型';
comment on column WF_FLOW_VARIABLE_DEFINE.VARIABLE_ORDER
  is '变量排序';
comment on column WF_FLOW_VARIABLE_DEFINE.CREATE_TIME
  is '创建时间';
comment on column WF_FLOW_VARIABLE_DEFINE.MODIFY_TIME
  is '更新时间';
comment on column WF_FLOW_VARIABLE_DEFINE.VERSION
  is '版本号';
alter table WF_FLOW_VARIABLE_DEFINE
  add primary key (FLOW_VARIABLE_ID);

call p_drop_ifExists('WF_INST_ATTENTION');
create table WF_INST_ATTENTION
(
  FLOW_INST_ID NUMBER(12) not null,
  USER_CODE    VARCHAR2(50) not null,
  ATT_SET_TIME DATE,
  ATT_SET_USER VARCHAR2(32),
  ATT_SET_MEMO VARCHAR2(255)
);
comment on column WF_INST_ATTENTION.FLOW_INST_ID
  is '流程实例ID';
comment on column WF_INST_ATTENTION.USER_CODE
  is '关注人';
comment on column WF_INST_ATTENTION.ATT_SET_TIME
  is '关注设置时间';
comment on column WF_INST_ATTENTION.ATT_SET_USER
  is '关注设置人员';
comment on column WF_INST_ATTENTION.ATT_SET_MEMO
  is '设置备注';
alter table WF_INST_ATTENTION
  add primary key (USER_CODE, FLOW_INST_ID);

call p_drop_ifExists('WF_MANAGE_ACTION');
create table WF_MANAGE_ACTION
(
  ACTION_ID    NUMBER(12) not null,
  FLOW_INST_ID NUMBER(12),
  NODE_INST_ID NUMBER(12),
  ACTION_TYPE  VARCHAR2(2) not null,
  ACTION_TIME  DATE not null,
  USER_CODE    VARCHAR2(32),
  ROLE_TYPE    VARCHAR2(32),
  ROLE_CODE    VARCHAR2(32),
  ADMIN_DESC   VARCHAR2(1000)
);
comment on column WF_MANAGE_ACTION.ACTION_ID
  is '活动编号';
comment on column WF_MANAGE_ACTION.FLOW_INST_ID
  is '流程实例ID';
comment on column WF_MANAGE_ACTION.NODE_INST_ID
  is '节点实例ID';
comment on column WF_MANAGE_ACTION.ACTION_TYPE
  is '活动类别';
comment on column WF_MANAGE_ACTION.ACTION_TIME
  is '活动时间';
comment on column WF_MANAGE_ACTION.USER_CODE
  is '操作用户';
comment on column WF_MANAGE_ACTION.ROLE_TYPE
  is '角色类别';
comment on column WF_MANAGE_ACTION.ROLE_CODE
  is '角色代码';
comment on column WF_MANAGE_ACTION.ADMIN_DESC
  is '管理活动说明';
alter table WF_MANAGE_ACTION
  add primary key (ACTION_ID);

call p_drop_ifExists('WF_NODE');
create table WF_NODE
(
  NODE_ID           NUMBER(12) not null,
  FLOW_CODE         VARCHAR2(32),
  VERSION           NUMBER(4),
  NODE_TYPE         VARCHAR2(1) not null,
  NODE_NAME         VARCHAR2(120),
  OPT_TYPE          VARCHAR2(1),
  OS_ID             VARCHAR2(32) default 'TEST',
  OPT_ID            VARCHAR2(32),
  OPT_CODE          VARCHAR2(1000),
  OPT_BEAN          VARCHAR2(100),
  OPT_PARAM         VARCHAR2(100),
  SUB_FLOW_CODE     VARCHAR2(32),
  ROUTER_TYPE       VARCHAR2(1),
  ROLE_TYPE         VARCHAR2(32),
  ROLE_CODE         VARCHAR2(32),
  UNIT_EXP          VARCHAR2(64),
  POWER_EXP         VARCHAR2(512),
  MULTI_INST_TYPE   CHAR(1),
  MULTI_INST_PARAM  VARCHAR2(512),
  CONVERGE_TYPE     CHAR(1),
  CONVERGE_PARAM    VARCHAR2(64),
  NODE_DESC         VARCHAR2(500),
  IS_ACCOUNT_TIME   CHAR(1),
  LIMIT_TYPE        CHAR(1),
  TIME_LIMIT        VARCHAR2(20),
  INHERIT_TYPE      CHAR(1),
  INHERIT_NODE_CODE VARCHAR2(20),
  EXPIRE_OPT        CHAR(1),
  WARNING_RULE      CHAR(1),
  WARNING_PARAM     VARCHAR2(20),
  IS_TRUNK_LINE     CHAR(1),
  STAGE_CODE        VARCHAR2(32),
  NODE_CODE         VARCHAR2(20),
  RISK_INFO         VARCHAR2(4)
);
comment on column WF_NODE.NODE_ID
  is '节点编号';
comment on column WF_NODE.FLOW_CODE
  is '流程代码';
comment on column WF_NODE.VERSION
  is '流程版本号';
comment on column WF_NODE.NODE_TYPE
  is '节点类别';
comment on column WF_NODE.NODE_NAME
  is '节点名';
comment on column WF_NODE.OPT_TYPE
  is '操作类别';
comment on column WF_NODE.OS_ID
  is '业务系统***';
comment on column WF_NODE.OPT_ID
  is '业务模块***';
comment on column WF_NODE.OPT_CODE
  is '业务操作';
comment on column WF_NODE.OPT_BEAN
  is '业务Bean';
comment on column WF_NODE.OPT_PARAM
  is '操作参数';
comment on column WF_NODE.SUB_FLOW_CODE
  is '子流程';
comment on column WF_NODE.ROUTER_TYPE
  is '路由类别 **';
comment on column WF_NODE.ROLE_TYPE
  is '角色类别';
comment on column WF_NODE.ROLE_CODE
  is '角色代码';
comment on column WF_NODE.UNIT_EXP
  is '机构表达式';
comment on column WF_NODE.POWER_EXP
  is '权限表达式';
comment on column WF_NODE.MULTI_INST_TYPE
  is '多实例类别**';
comment on column WF_NODE.MULTI_INST_PARAM
  is '多实例变量**';
comment on column WF_NODE.CONVERGE_TYPE
  is '汇聚条件类别**';
comment on column WF_NODE.CONVERGE_PARAM
  is '汇聚参数**';
comment on column WF_NODE.NODE_DESC
  is '节点描述';
comment on column WF_NODE.IS_ACCOUNT_TIME
  is '是否记入执行时间';
comment on column WF_NODE.LIMIT_TYPE
  is '期限类别';
comment on column WF_NODE.TIME_LIMIT
  is '期限时间';
comment on column WF_NODE.INHERIT_TYPE
  is '期限继承类别';
comment on column WF_NODE.INHERIT_NODE_CODE
  is '继承环节代码';
comment on column WF_NODE.EXPIRE_OPT
  is '逾期处理办法';
comment on column WF_NODE.WARNING_RULE
  is '预警规则**';
comment on column WF_NODE.WARNING_PARAM
  is '预警参数**';
comment on column WF_NODE.IS_TRUNK_LINE
  is '是否是主干节点';
comment on column WF_NODE.STAGE_CODE
  is '节点阶段';
comment on column WF_NODE.NODE_CODE
  is '环节代码';
comment on column WF_NODE.RISK_INFO
  is '风险信息';
alter table WF_NODE
  add primary key (NODE_ID);

call p_drop_ifExists('WF_NODE_INSTANCE');
create table WF_NODE_INSTANCE
(
  NODE_INST_ID      NUMBER(12) not null,
  FLOW_INST_ID      NUMBER(12),
  NODE_ID           NUMBER(12),
  CREATE_TIME       DATE,
  START_TIME        DATE,
  IS_TIMER          CHAR(1),
  PROMISE_TIME      NUMBER(10),
  TIME_LIMIT        NUMBER(10),
  PREV_NODE_INST_ID NUMBER(12),
  NODE_STATE        VARCHAR2(2),
  SUB_FLOW_INST_ID  NUMBER(12),
  UNIT_CODE         VARCHAR2(32),
  STAGE_CODE        VARCHAR2(32),
  ROLE_TYPE         VARCHAR2(32),
  ROLE_CODE         VARCHAR2(32),
  USER_CODE         VARCHAR2(32),
  NODE_PARAM        VARCHAR2(1000),
  TRANS_ID          NUMBER(12),
  TASK_ASSIGNED     VARCHAR2(1) default 'F',
  RUN_TOKEN         VARCHAR2(20),
  GRANTOR           VARCHAR2(32),
  LAST_UPDATE_USER  VARCHAR2(32),
  LAST_UPDATE_TIME  DATE,
  TRANS_PATH        VARCHAR2(256)
);
comment on column WF_NODE_INSTANCE.NODE_INST_ID
  is '节点实例编号';
comment on column WF_NODE_INSTANCE.FLOW_INST_ID
  is '流程实例ID';
comment on column WF_NODE_INSTANCE.NODE_ID
  is '节点编号';
comment on column WF_NODE_INSTANCE.CREATE_TIME
  is '创建时间';
comment on column WF_NODE_INSTANCE.START_TIME
  is '开始时间';
comment on column WF_NODE_INSTANCE.IS_TIMER
  is '是否计时';
comment on column WF_NODE_INSTANCE.PROMISE_TIME
  is '承诺完成时间';
comment on column WF_NODE_INSTANCE.TIME_LIMIT
  is '剩余时间';
comment on column WF_NODE_INSTANCE.PREV_NODE_INST_ID
  is '上一个节点实例';
comment on column WF_NODE_INSTANCE.NODE_STATE
  is '节点状态';
comment on column WF_NODE_INSTANCE.SUB_FLOW_INST_ID
  is '子流程实例ID';
comment on column WF_NODE_INSTANCE.UNIT_CODE
  is '所属机构';
comment on column WF_NODE_INSTANCE.STAGE_CODE
  is '阶段代码';
comment on column WF_NODE_INSTANCE.ROLE_TYPE
  is '角色类别';
comment on column WF_NODE_INSTANCE.ROLE_CODE
  is '角色代码';
comment on column WF_NODE_INSTANCE.USER_CODE
  is '所属人员***';
comment on column WF_NODE_INSTANCE.NODE_PARAM
  is '节点自定义变量***';
comment on column WF_NODE_INSTANCE.TRANS_ID
  is '应用路径';
comment on column WF_NODE_INSTANCE.TASK_ASSIGNED
  is '任务分配方式';
comment on column WF_NODE_INSTANCE.RUN_TOKEN
  is '运行令牌';
comment on column WF_NODE_INSTANCE.GRANTOR
  is '委托人';
comment on column WF_NODE_INSTANCE.LAST_UPDATE_USER
  is '最后更新人';
comment on column WF_NODE_INSTANCE.LAST_UPDATE_TIME
  is '最后更新时间';
alter table WF_NODE_INSTANCE
  add primary key (NODE_INST_ID);

call p_drop_ifExists('WF_OPTDEF');
create table WF_OPTDEF
(
  OPT_CODE    VARCHAR2(32) not null,
  OPT_ID      VARCHAR2(32),
  OPT_NAME    VARCHAR2(100),
  OPT_METHOD  VARCHAR2(50),
  UPDATE_DATE DATE,
  extend_method varchar2(500)
);
alter table WF_OPTDEF
  add primary key (OPT_CODE);
COMMENT ON COLUMN WF_OPTDEF.extend_method IS '扩展方法，比如移动端方法';

call p_drop_ifExists('WF_OPTINFO');
create table WF_OPTINFO
(
  OPT_ID      VARCHAR2(32) not null,
  OPT_NAME    VARCHAR2(100) not null,
  OPT_URL     VARCHAR2(256),
  UPDATE_DATE DATE,
  view_url varchar2(255)
);
alter table WF_OPTINFO
  add primary key (OPT_ID);
COMMENT ON COLUMN wf_optinfo.view_url IS '已办url';

call p_drop_ifExists('WF_ORGANIZE');
create table WF_ORGANIZE
(
  FLOW_INST_ID NUMBER(12) not null,
  UNIT_CODE    VARCHAR2(32) not null,
  ROLE_CODE    VARCHAR2(32) not null,
  UNIT_ORDER   NUMBER(4),
  AUTH_DESC    VARCHAR2(255),
  AUTH_TIME    DATE default sysdate
);
comment on column WF_ORGANIZE.FLOW_INST_ID
  is '流程实例ID';
comment on column WF_ORGANIZE.UNIT_CODE
  is '机构代码';
comment on column WF_ORGANIZE.ROLE_CODE
  is '结构角色';
comment on column WF_ORGANIZE.UNIT_ORDER
  is '排序号';
comment on column WF_ORGANIZE.AUTH_DESC
  is '授权说明';
comment on column WF_ORGANIZE.AUTH_TIME
  is '创建时间';
alter table WF_ORGANIZE
  add primary key (FLOW_INST_ID, UNIT_CODE, ROLE_CODE);

call p_drop_ifExists('WF_ROLE_RELEGATE');
create table WF_ROLE_RELEGATE
(
  RELEGATE_NO   NUMBER(12) not null,
  GRANTOR       VARCHAR2(32) not null,
  GRANTEE       VARCHAR2(32) not null,
  IS_VALID      CHAR(1) default 'T',
  RECORDER      VARCHAR2(32),
  RELEGATE_TIME DATE not null,
  EXPIRE_TIME   DATE,
  UNIT_CODE     VARCHAR2(32),
  ROLE_TYPE     VARCHAR2(32),
  ROLE_CODE     VARCHAR2(32),
  RECORD_DATE   DATE,
  GRANT_DESC    VARCHAR2(256)
);
comment on column WF_ROLE_RELEGATE.RELEGATE_NO
  is '委托编号';
comment on column WF_ROLE_RELEGATE.GRANTOR
  is '委托人';
comment on column WF_ROLE_RELEGATE.GRANTEE
  is '受委托人';
comment on column WF_ROLE_RELEGATE.IS_VALID
  is '状态';
comment on column WF_ROLE_RELEGATE.RECORDER
  is '录入人员';
comment on column WF_ROLE_RELEGATE.RELEGATE_TIME
  is '委托开始时间';
comment on column WF_ROLE_RELEGATE.EXPIRE_TIME
  is '截止时间';
comment on column WF_ROLE_RELEGATE.UNIT_CODE
  is '委托机构';
comment on column WF_ROLE_RELEGATE.ROLE_TYPE
  is '委托角色类别';
comment on column WF_ROLE_RELEGATE.ROLE_CODE
  is '委托角色';
comment on column WF_ROLE_RELEGATE.RECORD_DATE
  is '录入时间';
comment on column WF_ROLE_RELEGATE.GRANT_DESC
  is '授予说明';
alter table WF_ROLE_RELEGATE
  add primary key (RELEGATE_NO);

call p_drop_ifExists('WF_ROUTER_NODE');
create table WF_ROUTER_NODE
(
  NODEID        NUMBER(12) not null,
  WFCODE        VARCHAR2(32),
  VERSION       NUMBER(4),
  ROUTERTYPE    VARCHAR2(1) not null,
  NODENAME      VARCHAR2(120),
  NODEDESC      VARCHAR2(500),
  ROLETYPE      VARCHAR2(32),
  ROLECODE      VARCHAR2(32),
  UNITEXP       VARCHAR2(64),
  POWEREXP      VARCHAR2(512),
  SELFDEFPARAM  VARCHAR2(512),
  CONVERGETYPE  CHAR(1),
  CONVERGEPARAM VARCHAR2(64),
  OPTBEAN       VARCHAR2(100)
);
comment on column WF_ROUTER_NODE.NODEID
  is '节点编号';
comment on column WF_ROUTER_NODE.WFCODE
  is '流程代码';
comment on column WF_ROUTER_NODE.VERSION
  is '流程版本号';
comment on column WF_ROUTER_NODE.ROUTERTYPE
  is '路由类别';
comment on column WF_ROUTER_NODE.NODENAME
  is '节点名';
comment on column WF_ROUTER_NODE.NODEDESC
  is '节点描述';
comment on column WF_ROUTER_NODE.ROLETYPE
  is '角色类别';
comment on column WF_ROUTER_NODE.ROLECODE
  is '角色代码';
comment on column WF_ROUTER_NODE.UNITEXP
  is '机构表达式';
comment on column WF_ROUTER_NODE.POWEREXP
  is '权限表达式';
comment on column WF_ROUTER_NODE.SELFDEFPARAM
  is '多实例变量';
comment on column WF_ROUTER_NODE.CONVERGETYPE
  is '汇聚条件类别';
comment on column WF_ROUTER_NODE.CONVERGEPARAM
  is '汇聚参数';
comment on column WF_ROUTER_NODE.OPTBEAN
  is '外埠判断Bean';
alter table WF_ROUTER_NODE
  add primary key (NODEID);

call p_drop_ifExists('WF_RUNTIME_WARNING');
create table WF_RUNTIME_WARNING
(
  WARNING_ID    NUMBER(12) not null,
  FLOW_INST_ID  NUMBER(12),
  NODE_INST_ID  NUMBER(12) not null,
  FLOW_STAGE    VARCHAR2(4),
  OBJ_TYPE      CHAR(1),
  WARNING_TYPE  CHAR(1),
  WARNING_STATE CHAR(1) default 'N',
  WARNING_CODE  VARCHAR2(16),
  WARNING_TIME  DATE,
  WARNINGID_MSG VARCHAR2(500),
  NOTICE_STATE  CHAR(1) default '0',
  SEND_MSG_TIME DATE,
  SEND_USERS    VARCHAR2(100)
);
comment on column WF_RUNTIME_WARNING.WARNING_ID
  is '预警ID';
comment on column WF_RUNTIME_WARNING.FLOW_INST_ID
  is '流程实例ID';
comment on column WF_RUNTIME_WARNING.NODE_INST_ID
  is '节点实例编号';
comment on column WF_RUNTIME_WARNING.FLOW_STAGE
  is '节点阶段**';
comment on column WF_RUNTIME_WARNING.OBJ_TYPE
  is '预警对象';
comment on column WF_RUNTIME_WARNING.WARNING_TYPE
  is '预警类别';
comment on column WF_RUNTIME_WARNING.WARNING_STATE
  is '预警状态';
comment on column WF_RUNTIME_WARNING.WARNING_CODE
  is '预警代码';
comment on column WF_RUNTIME_WARNING.WARNING_TIME
  is '预警时间';
comment on column WF_RUNTIME_WARNING.WARNINGID_MSG
  is '预警内容';
comment on column WF_RUNTIME_WARNING.NOTICE_STATE
  is '通知状态';
comment on column WF_RUNTIME_WARNING.SEND_MSG_TIME
  is '通知消息时间';
comment on column WF_RUNTIME_WARNING.SEND_USERS
  is '通知人员';
alter table WF_RUNTIME_WARNING
  add primary key (WARNING_ID);

call p_drop_ifExists('WF_STAGE_INSTANCE');
create table WF_STAGE_INSTANCE
(
  FLOW_INST_ID     NUMBER(12) not null,
  STAGE_ID         NUMBER(12) not null,
  STAGE_CODE       VARCHAR2(32) not null,
  STAGE_NAME       VARCHAR2(60),
  BEGIN_TIME       DATE,
  STAGE_BEGIN      CHAR(1),
  PROMISE_TIME     NUMBER(10),
  TIME_LIMIT       NUMBER(10),
  LAST_UPDATE_TIME DATE
);
comment on column WF_STAGE_INSTANCE.FLOW_INST_ID
  is '流程实例ID';
comment on column WF_STAGE_INSTANCE.STAGE_ID
  is '阶段编号';
comment on column WF_STAGE_INSTANCE.STAGE_CODE
  is '阶段代码';
comment on column WF_STAGE_INSTANCE.STAGE_NAME
  is '阶段名称';
comment on column WF_STAGE_INSTANCE.BEGIN_TIME
  is '阶段进入时间';
comment on column WF_STAGE_INSTANCE.STAGE_BEGIN
  is '阶段已经进入';
comment on column WF_STAGE_INSTANCE.PROMISE_TIME
  is '承诺完成时间';
comment on column WF_STAGE_INSTANCE.TIME_LIMIT
  is '剩余时间';
comment on column WF_STAGE_INSTANCE.LAST_UPDATE_TIME
  is '最后更新时间';
alter table WF_STAGE_INSTANCE
  add primary key (FLOW_INST_ID, STAGE_ID);

call p_drop_ifExists('WF_TEAM');
create table WF_TEAM
(
  FLOW_INST_ID NUMBER(12) not null,
  ROLE_CODE    VARCHAR2(32) not null,
  USER_CODE    VARCHAR2(32) not null,
  USER_ORDER   NUMBER(4),
  AUTH_DESC    VARCHAR2(255),
  AUTH_TIME    DATE default sysdate
);
comment on column WF_TEAM.FLOW_INST_ID
  is '流程实例ID';
comment on column WF_TEAM.ROLE_CODE
  is '角色代码';
comment on column WF_TEAM.USER_CODE
  is '用户代码';
comment on column WF_TEAM.USER_ORDER
  is '排序号';
comment on column WF_TEAM.AUTH_DESC
  is '授权说明';
comment on column WF_TEAM.AUTH_TIME
  is '创建时间';
alter table WF_TEAM
  add primary key (FLOW_INST_ID, USER_CODE, ROLE_CODE);

call p_drop_ifExists('WF_TRANSITION');
create table WF_TRANSITION
(
  TRANS_ID          NUMBER(12) not null,
  VERSION           NUMBER(4),
  FLOW_CODE         VARCHAR2(32),
  TRANS_CLASS       VARCHAR2(4),
  TRANS_NAME        VARCHAR2(120),
  START_NODE_ID     NUMBER(12),
  END_NODE_ID       NUMBER(12),
  TRANS_CONDITION   VARCHAR2(500),
  TRANS_DESC        VARCHAR2(500),
  IS_ACCOUNT_TIME   CHAR(1),
  LIMIT_TYPE        CHAR(1),
  TIME_LIMIT        VARCHAR2(20),
  INHERIT_TYPE      CHAR(1),
  INHERIT_NODE_CODE VARCHAR2(20),
  CAN_IGNORE        CHAR(1) default 'T'
);
comment on column WF_TRANSITION.TRANS_ID
  is '流转编号';
comment on column WF_TRANSITION.VERSION
  is '流程版本号';
comment on column WF_TRANSITION.FLOW_CODE
  is '流程代码';
comment on column WF_TRANSITION.TRANS_CLASS
  is '流转类型';
comment on column WF_TRANSITION.TRANS_NAME
  is '流转名称';
comment on column WF_TRANSITION.START_NODE_ID
  is '源节点编号';
comment on column WF_TRANSITION.END_NODE_ID
  is '目标节点编号';
comment on column WF_TRANSITION.TRANS_CONDITION
  is '流转条件';
comment on column WF_TRANSITION.TRANS_DESC
  is '流转描述';
comment on column WF_TRANSITION.IS_ACCOUNT_TIME
  is '是否记入执行时间';
comment on column WF_TRANSITION.LIMIT_TYPE
  is '期限类别';
comment on column WF_TRANSITION.TIME_LIMIT
  is '期限时间';
comment on column WF_TRANSITION.INHERIT_TYPE
  is '期限继承类别';
comment on column WF_TRANSITION.INHERIT_NODE_CODE
  is '继承环节代码';
comment on column WF_TRANSITION.CAN_IGNORE
  is '是否可以忽略运行***';
alter table WF_TRANSITION
  add primary key (TRANS_ID);

call p_drop_ifExists('WF_MODULE');
create table WF_MODULE
(
  module_code VARCHAR2(100) not null,
  module_name VARCHAR2(255),
  module_desc VARCHAR2(255),
  opt_code    VARCHAR2(100) not null
);
-- Add comments to the columns
comment on column WF_MODULE.module_code
is '模块代码';
comment on column WF_MODULE.module_name
is '模块名称';
comment on column WF_MODULE.module_desc
is '模块描述';
comment on column WF_MODULE.opt_code
is '业务id(消息中心code)';
-- Create/Recreate primary, unique and foreign key constraints
alter table WF_MODULE
  add primary key (MODULE_CODE);


call p_drop_ifExists('WF_MODULE_INTERFACE');
create table WF_MODULE_INTERFACE
(
  interface_code    VARCHAR2(100) not null,
  module_code       VARCHAR2(100) not null,
  interface_address VARCHAR2(255) not null,
  interface_desc    VARCHAR2(255),
  interface_id      VARCHAR2(100) not null
);
-- Add comments to the columns
comment on column WF_MODULE_INTERFACE.interface_code
is '接口code';
comment on column WF_MODULE_INTERFACE.module_code
is '模块id';
comment on column WF_MODULE_INTERFACE.interface_address
is '接口地址';
comment on column WF_MODULE_INTERFACE.interface_desc
is '接口描述';
comment on column WF_MODULE_INTERFACE.interface_id
is '接口ID（主键）';
-- Create/Recreate primary, unique and foreign key constraints
alter table WF_MODULE_INTERFACE
  add constraint ID primary key (INTERFACE_ID);

call p_drop_ifExists('WF_INTERFACE_PARAMETER');
create table WF_INTERFACE_PARAMETER
(
  parameter_code VARCHAR2(100) not null,
  interface_id   VARCHAR2(100) not null,
  parameter_type VARCHAR2(100),
  parameter_desc VARCHAR2(255),
  is_required    VARCHAR2(10),
  business_code  VARCHAR2(100) not null,
  business_desc  VARCHAR2(255),
  parameter_id   VARCHAR2(100) not null
);
-- Add comments to the columns
comment on column WF_INTERFACE_PARAMETER.parameter_code
is '参数code';
comment on column WF_INTERFACE_PARAMETER.interface_id
is '接口id';
comment on column WF_INTERFACE_PARAMETER.parameter_type
is '参数类型';
comment on column WF_INTERFACE_PARAMETER.parameter_desc
is '参数描述';
comment on column WF_INTERFACE_PARAMETER.is_required
is '是否必须';
comment on column WF_INTERFACE_PARAMETER.business_code
is '业务系统参数code';
comment on column WF_INTERFACE_PARAMETER.business_desc
is '业务系统参数描述';
comment on column WF_INTERFACE_PARAMETER.parameter_id
is '参数ID（主键）';
-- Create/Recreate primary, unique and foreign key constraints
alter table WF_INTERFACE_PARAMETER
  add constraint INTERFANCE_PARAMETER_ID primary key (PARAMETER_ID);

-- Create table
--任务迁移
call p_drop_ifExists('WF_TASK_MOVE');
create table WF_TASK_MOVE
(
  MOVE_NO   VARCHAR2(100) not null,
  FROM_USER VARCHAR2(100),
  TO_USER   VARCHAR2(100),
  MOVE_DESC VARCHAR2(2000),
  OPER_USER VARCHAR2(100),
  OPER_DATE DATE
);
-- Create/Recreate primary, unique and foreign key constraints
alter table WF_TASK_MOVE
  add constraint PK_WF_TASK_MOVE primary key (MOVE_NO);

call p_drop_ifExists('OPT_MSG_LOG');
create table OPT_MSG_LOG
(
  msg_id       VARCHAR2(50) not null,
  msg_content  CLOB not null,
  instid       VARCHAR2(200),
  returnstatus VARCHAR2(10),
  returncode   VARCHAR2(50),
  returnmsg    VARCHAR2(100),
  requesttime  VARCHAR2(100),
  responsetime VARCHAR2(100)
);
-- Add comments to the columns
comment on column OPT_MSG_LOG.msg_id
is 'uuid';
comment on column OPT_MSG_LOG.msg_content
is 'post的json字符串主体';
comment on column OPT_MSG_LOG.instid
is '该字段为接口调用的流水号，每次调用接口时都会有一个instId产生。';
comment on column OPT_MSG_LOG.returnstatus
is '该字段为接口返回状态，该字段值为S或W或E。
S：表示接口调用成功，包括技术上的成功（没有报错）和业务上的成功（单据处理正常）。
W：表示接口警告，包括技术上的警告（如处理时间超长）和业务上的警告（如查询接口未查询到数据）
E：表示接口报错，包括技术上的报错（如验证失败、字段类型不匹配等）和业务上的报错（如单据处理失败、单据号不能匹配等）
';
comment on column OPT_MSG_LOG.returncode
is '该字段是接口返回状态码，与returnMsg成对出现，由业务系统自行制定，状态如下：
returnStatus 是S时，returnCode 要返回A0001-A0999中的一个状态码（如：A0001），并在returnMsg字段中返回状态信息（如单据核销成功）
returnStatus 是E时，returnCode 要返回E0001-E0999中的一个状态码（如：E0001），并在returnMsg字段中返回状态信息（如单据号不匹配）
';
comment on column OPT_MSG_LOG.returnmsg
is '该字段是接口返回状态信息，与returnCode成对出现，由业务系统自行制定。';
comment on column OPT_MSG_LOG.requesttime
is '该字段为提供方收到请求时的系统时间。格式：“yyyy-MM-dd HH24:mm:ss.SSS” 例如：2008-11-11 14:13:222.221';
comment on column OPT_MSG_LOG.responsetime
is '该字段为提供方响应结束时的系统时间。格式：“yyyy-MM-dd HH24:mm:ss.SSS” 例如：2008-11-11 14:13:222.451。responseTime和requestTime差值即为提供方系统处理时间。';
-- Create/Recreate primary, unique and foreign key constraints
alter table OPT_MSG_LOG
  add constraint PK_MSG_LOG primary key (MSG_ID);

call p_drop_ifExists('S_ACTIONLOGNO','2');
create sequence S_ACTIONLOGNO
minvalue 1
maxvalue 9999999999999999999999999999
start with 1561
increment by 1
cache 20;

call p_drop_ifExists('S_ACTIONTASKNO','2');
create sequence S_ACTIONTASKNO
minvalue 1
maxvalue 9999999999999999999999
start with 1038
increment by 1
cache 20;

call p_drop_ifExists('S_ADDRESSID','2');
create sequence S_ADDRESSID
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

call p_drop_ifExists('S_FLOWDEFINE','2');
create sequence S_FLOWDEFINE
minvalue 1
maxvalue 9999999999999999999999999999
start with 181
increment by 1
cache 20;

call p_drop_ifExists('S_FLOWDEFNO','2');
create sequence S_FLOWDEFNO
minvalue 1
maxvalue 9999999999999999999999999999
start with 1412
increment by 1
cache 20;

call p_drop_ifExists('S_FLOWINSTNO','2');
create sequence S_FLOWINSTNO
minvalue 1
maxvalue 9999999999999999999999999999
start with 1150
increment by 1
cache 20;

call p_drop_ifExists('S_FLOWOPT_NO','2');
create sequence S_FLOWOPT_NO
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

call p_drop_ifExists('S_FORMDEFNO','2');
create sequence S_FORMDEFNO
minvalue 1
maxvalue 9999999999999999999999999999
start with 61
increment by 1
cache 20;

call p_drop_ifExists('S_MANAGERACTIONNO','2');
create sequence S_MANAGERACTIONNO
minvalue 1
maxvalue 9999999999999999999999999999
start with 181
increment by 1
cache 20;

call p_drop_ifExists('S_MESSAGE_ID','2');
create sequence S_MESSAGE_ID
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;



call p_drop_ifExists('S_NODEINSTNO','2');
create sequence S_NODEINSTNO
minvalue 1
maxvalue 9999999999999999999999999999
start with 1532
increment by 1
cache 20;

call p_drop_ifExists('S_NOTIFY_ID','2');
create sequence S_NOTIFY_ID
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

call p_drop_ifExists('S_OPTIDEAINFO','2');
create sequence S_OPTIDEAINFO
minvalue 1
maxvalue 9999999999999999999999999999
start with 1081
increment by 1
cache 20;

call p_drop_ifExists('S_OPTPROCINFO','2');
create sequence S_OPTPROCINFO
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

call p_drop_ifExists('S_OPTSTUFFINFO','2');
create sequence S_OPTSTUFFINFO
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

call p_drop_ifExists('S_REQ','2');
create sequence S_REQ
minvalue 1
maxvalue 99999999
start with 121
increment by 1
cache 20;

call p_drop_ifExists('S_WFOPTDEF','2');
create sequence S_WFOPTDEF
minvalue 1
maxvalue 999999999
start with 63
increment by 1
nocache;

call p_drop_ifExists('S_WFOPTINFO','2');
create sequence S_WFOPTINFO
minvalue 1
maxvalue 999999999
start with 47
increment by 1
nocache;

call p_drop_ifExists('S_FLOWMODULEINTERFANCE_ID','2');
create sequence S_FLOWMODULEINTERFANCE_ID
minvalue 1
maxvalue 9999
start with 120
increment by 1
cache 10;

call p_drop_ifExists('S_FLOW_INTERFANCE_PARAMID','2');
create sequence S_FLOW_INTERFANCE_PARAMID
minvalue 1
maxvalue 9999
start with 130
increment by 1
cache 10;

create or replace view lastversion as
select wf_flow_define.FLOW_CODE AS FLOW_CODE, max(wf_flow_define.version) AS version
  from wf_flow_define
  group by wf_flow_define.FLOW_CODE;


create or replace view f_v_lastversionflow as
select a.FLOW_CODE AS FLOW_CODE,b.version AS VERSION,a.FLOW_NAME AS FLOW_NAME,a.FLOW_CLASS AS FLOW_CLASS,
    b.FLOW_STATE AS FLOW_STATE,a.FLOW_DESC AS FLOW_DESC,a.FLOW_XML_DESC AS FLOW_XML_DESC,a.Time_Limit AS TIME_LIMIT,
    a.Expire_Opt AS EXPIRE_OPT,a.Opt_ID AS OPT_ID,a.OS_ID AS OS_ID,a.FLOW_Publish_Date AS FLOW_PUBLISH_DATE,
    a.AT_PUBLISH_DATE AS AT_PUBLISH_DATE
  from lastversion join wf_flow_define a on(a.FLOW_CODE = lastversion.FLOW_CODE and a.version = 0)
    join wf_flow_define b on (lastversion.FLOW_CODE = b.FLOW_CODE and lastversion.version = b.version);

create or replace view v_inner_user_task_list as
  select a.FLOW_INST_ID AS FLOW_INST_ID,w.FLOW_CODE AS FLOW_CODE,w.VERSION AS version,w.FLOW_Opt_Name AS FLOW_OPT_NAME,
         w.FLOW_Opt_Tag AS FLOW_OPT_TAG,a.NODE_INST_ID AS NODE_INST_ID,nvl(a.UNIT_CODE,nvl(w.UNIT_CODE,'0000000')) AS Unit_Code,a.USER_CODE AS user_code,
         c.ROLE_TYPE AS ROLE_TYPE,c.ROLE_CODE AS ROLE_CODE,null AS AUTH_DESC,c.NODE_CODE AS node_code,
         c.NODE_NAME AS Node_Name,c.NODE_TYPE AS Node_Type,c.OPT_TYPE AS NODE_OPT_TYPE,c.OPT_PARAM AS Opt_Param,
         a.CREATE_TIME AS CREATE_TIME,a.promise_Time AS Promise_Time,a.time_Limit AS TIME_LIMIT,c.OPT_CODE AS OPT_CODE,
         c.Expire_Opt AS Expire_Opt,c.STAGE_CODE AS STAGE_CODE,a.last_update_user AS last_update_user,a.last_update_time AS LAST_UPDATE_TIME,
         w.INST_STATE AS inst_state,c.OS_ID AS OS_ID,d.FLOW_NAME AS FLOW_NAME,w.CREATE_TIME AS apply_time
  from wf_node_instance a join wf_flow_instance w on (a.FLOW_INST_ID = w.FLOW_INST_ID)
    join wf_node c on (a.NODE_ID = c.NODE_ID)
    join wf_flow_define d on d.FLOW_CODE = w.FLOW_CODE and d.version = w.VERSION
  where (a.NODE_STATE = 'N' and (w.INST_STATE in ('N','M')) and a.TASK_ASSIGNED = 'S')
  union all
  select a.FLOW_INST_ID AS FLOW_INST_ID,w.FLOW_CODE AS FLOW_CODE,w.VERSION AS version,w.FLOW_Opt_Name AS FLOW_OPT_NAME,
         w.FLOW_Opt_Tag AS FLOW_OPT_TAG,a.NODE_INST_ID AS NODE_INST_ID,nvl(a.UNIT_CODE,nvl(w.UNIT_CODE,'0000000')) AS UnitCode,b.USER_CODE AS user_code,
         b.ROLE_TYPE AS ROLE_TYPE,b.ROLE_CODE AS ROLE_CODE,b.AUTH_DESC AS AUTH_DESC,c.NODE_CODE AS node_code,
         c.NODE_NAME AS Node_Name,c.NODE_TYPE AS Node_Type,c.OPT_TYPE AS NODE_OPT_TYPE,c.OPT_PARAM AS Opt_Param,
         a.CREATE_TIME AS CREATE_TIME,a.promise_Time AS Promise_Time,a.time_Limit AS TIME_LIMIT,c.OPT_CODE AS OPT_CODE,
         c.Expire_Opt AS Expire_Opt,c.STAGE_CODE AS STAGE_CODE,a.last_update_user AS last_update_user,a.last_update_time AS LAST_UPDATE_TIME,
         w.INST_STATE AS inst_state,c.OS_ID AS OS_ID,d.FLOW_NAME AS FLOW_NAME,w.CREATE_TIME AS apply_time
  from wf_node_instance a join wf_flow_instance w on a.FLOW_INST_ID = w.FLOW_INST_ID
    join wf_action_task b on a.NODE_INST_ID = b.NODE_INST_ID
    join wf_node c on a.NODE_ID = c.NODE_ID
    join wf_flow_define d on d.FLOW_CODE = w.FLOW_CODE and d.version = w.VERSION
  where a.NODE_STATE = 'N' and (w.INST_STATE in ('N','M')) and a.TASK_ASSIGNED = 'T' and b.IS_VALID = 'T'
        and b.TASK_STATE = 'A' and (b.EXPIRE_TIME is null or b.EXPIRE_TIME > sysdate);



CREATE OR REPLACE FUNCTION CANROLLBACKNODE ( niId IN number)
  return char
IS
  res char(1);
  BEGIN
    res := '1';
    for r in (select NODE_STATE,NODE_INST_ID from wf_node_instance where PREV_NODE_INST_ID = niId)
    loop
      if r.NODE_STATE <> 'N' and r.NODE_STATE <> 'P' then
        res := '0';
        exit;
      elsif r.NODE_STATE = 'P' then
        if canrollbacknode(r.NODE_INST_ID) = '0' then
          res := '0';
          exit;
        end if;
      end if;
    end loop;
    RETURN res;
  END canrollbacknode;



CREATE OR REPLACE VIEW V_USER_COMPLETE_TASK AS
SELECT c.node_inst_id,
    c.flow_inst_id,
    c.node_id,
    c.create_time,
    c.prev_node_inst_id,
    c.node_state,
    c.SUB_FLOW_INST_ID,
    c.unit_code,
    c.trans_id,
    c.task_assigned,
    c.run_token,
    c.time_limit,
    c.last_update_user,
    c.last_update_time,
    c.is_timer,
    b.flow_opt_name,
    b.flow_opt_tag,
    e.role_type,
    e.role_code,
    e.node_name,
    e.stage_code,
    b.flow_code,
    '1' as canrollback,--canrollbacknode(a.node_inst_id) AS canrollback,
    '' AS xbback
  FROM wf_flow_instance b
    LEFT JOIN wf_node_instance a ON (a.flow_inst_id = b.flow_inst_id)
    LEFT JOIN wf_node_instance c on c.PREV_NODE_INST_ID=a.NODE_INST_ID
    LEFT JOIN wf_node d on a.NODE_ID = d.NODE_ID
    LEFT JOIN wf_node e ON c.node_id = e.node_id
    LEFT JOIN WF_FLOW_DEFINE f ON (f.flow_code =b.flow_code and f.version=b.version)
  WHERE b.inst_state = 'N'
        AND a.node_state = 'C'
        AND d.opt_type <> 'D'
        AND d.opt_type <> 'E'
        AND d.opt_type <> 'S';

create or replace view v_user_task_list as
  select rownum as taskid,tt.FLOW_INST_ID,tt.FLOW_CODE,tt.VERSION,tt.FLOW_OPT_NAME,tt.FLOW_OPT_TAG,tt.NODE_INST_ID,tt.UNIT_CODE,tt.USER_CODE,
    tt.ROLE_TYPE,tt.ROLE_CODE,tt.AUTH_DESC,tt.NODE_CODE,tt.NODE_NAME,tt.NODE_TYPE,tt.NODE_OPT_TYPE,tt.OPT_PARAM,tt.CREATE_TIME,tt.PROMISE_TIME
    ,tt.TIME_LIMIT,tt.OPT_CODE,tt.EXPIRE_OPT,tt.STAGE_CODE,tt.GRANTOR,tt.LAST_UPDATE_USER,tt.LAST_UPDATE_TIME,tt.INST_STATE,
         p.opt_url||o.opt_method AS opt_url,p.opt_name,tt.os_id AS os_id,tt.FLOW_NAME AS flow_name,tt.apply_time AS apply_time
  from ( select a.FLOW_INST_ID AS FLOW_INST_ID,a.FLOW_CODE AS FLOW_CODE,a.version AS version,a.FLOW_OPT_NAME AS FLOW_OPT_NAME,
           a.FLOW_OPT_TAG AS FLOW_OPT_TAG,a.NODE_INST_ID AS NODE_INST_ID,a.Unit_Code AS Unit_Code,a.user_code AS user_code,
           a.ROLE_TYPE AS ROLE_TYPE,a.ROLE_CODE AS ROLE_CODE,a.AUTH_DESC AS AUTH_DESC,a.node_code AS node_code,
           a.Node_Name AS Node_Name,a.Node_Type AS Node_Type,a.NODE_OPT_TYPE AS NODE_OPT_TYPE, a.Opt_Param AS Opt_Param,
           a.CREATE_TIME AS CREATE_TIME,a.Promise_Time AS promise_time,a.TIME_LIMIT AS time_limit,a.OPT_CODE AS OPT_CODE,
           a.Expire_Opt AS Expire_Opt,a.STAGE_CODE AS STAGE_CODE,NULL AS GRANTOR,a.last_update_user AS last_update_user,
           a.LAST_UPDATE_TIME AS LAST_UPDATE_TIME,a.inst_state AS inst_state,a.os_id AS os_id,a.FLOW_NAME AS flow_name,a.apply_time AS apply_time from v_inner_user_task_list a
           union select a.FLOW_INST_ID AS FLOW_INST_ID,a.FLOW_CODE AS FLOW_CODE,a.version AS version,a.FLOW_OPT_NAME AS FLOW_OPT_NAME,
                   a.FLOW_OPT_TAG AS FLOW_OPT_TAG,a.NODE_INST_ID AS NODE_INST_ID,a.Unit_Code AS Unit_Code,a.user_code AS user_code,
                   a.ROLE_TYPE AS ROLE_TYPE,a.ROLE_CODE AS ROLE_CODE,a.AUTH_DESC AS AUTH_DESC,a.node_code AS node_code,
                   a.Node_Name AS Node_Name,a.Node_Type AS Node_Type,a.NODE_OPT_TYPE AS NODE_OPT_TYPE, a.Opt_Param AS Opt_Param,
                   a.CREATE_TIME AS CREATE_TIME,a.Promise_Time AS promise_time,a.TIME_LIMIT AS time_limit,a.OPT_CODE AS OPT_CODE,
                   a.Expire_Opt AS Expire_Opt,a.STAGE_CODE AS STAGE_CODE,b.GRANTOR AS GRANTOR,a.last_update_user AS last_update_user,
                   a.LAST_UPDATE_TIME AS last_update_time,a.inst_state AS inst_state,a.os_id AS os_id,a.FLOW_NAME AS flow_name,
                   a.apply_time AS apply_time from v_inner_user_task_list a join wf_role_relegate b on b.unit_code=a.UNIT_CODE
                   where b.IS_VALID = 'T' and b.RELEGATE_TIME <= sysdate and a.user_code = b.GRANTOR and (b.EXPIRE_TIME is null or b.EXPIRE_TIME >= sysdate)
                    and (b.UNIT_CODE is null or b.UNIT_CODE = a.Unit_Code) and (b.ROLE_TYPE is null or b.ROLE_TYPE = a.ROLE_TYPE) and (b.ROLE_CODE is null or b.ROLE_CODE = a.ROLE_CODE)
        )tt join wf_optdef o on o.opt_code=tt.opt_code join wf_optinfo p on p.opt_id=o.opt_id;


create or replace function getNodeTrans(v_nodeinstid in number) RETURN VARCHAR2 is
  v_nodeid    number;
  v_flow_code number;
  v_version   number;
  v_trans     varchar2(1000);
begin
  select t.node_id, f.flow_code, f.version
    into v_nodeid, v_flow_code, v_version
    from wf_node_instance t
    join wf_flow_instance f
      on t.flow_inst_id = f.flow_inst_id
   where t.node_inst_id = v_nodeinstid;
  for r in (select to_char(t.trans_id) as trans_id
              from wf_transition t
             where t.start_node_id < v_nodeid
               and t.flow_code = v_flow_code
               and t.version = v_version order by t.trans_id asc) loop
    if v_trans is null then
      v_trans := r.trans_id;
    else
      v_trans := v_trans || ',' || r.trans_id;
    end if;
  end loop;
  return v_trans;
 /* update wf_node_instance t
     set t.trans_path = v_trans
   where t.node_inst_id = v_nodeinstid;
  commit;*/


end;



create or replace function num_to_date(in_number NUMBER) return date is
begin
   return(TO_DATE('19700101','yyyymmdd')+ in_number/86400+TO_NUMBER(SUBSTR(TZ_OFFSET(sessiontimezone),1,3))/24 );
end num_to_date;



create or replace procedure p_getNodeTrans(v_nodeinstid number) is
  v_nodeid    number;
  v_flow_code number;
  v_version   number;
  v_trans     varchar2(1000);
begin
  select t.node_id, f.flow_code, f.version
    into v_nodeid, v_flow_code, v_version
    from wf_node_instance t
    join wf_flow_instance f
      on t.flow_inst_id = f.flow_inst_id
   where t.node_inst_id = v_nodeinstid;
  for r in (select to_char(t.trans_id) as trans_id
              from wf_transition t
             where t.start_node_id < v_nodeid
               and t.flow_code = v_flow_code
               and t.version = v_version order by t.trans_id asc) loop
    if v_trans is null then
      v_trans := r.trans_id;
    else
      v_trans := v_trans || ',' || r.trans_id;
    end if;
  end loop;
  update wf_node_instance t
     set t.trans_path = v_trans
   where t.node_inst_id = v_nodeinstid;
  commit;
exception
  when others then
    dbms_output.put_line(dbms_utility.format_error_backtrace);
    rollback;
    return;
  
end p_getNodeTrans;
