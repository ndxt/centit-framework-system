/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2016/4/11 9:38:17                            */
/*==============================================================*/


drop table if exists F_ADDRESS_BOOK;

drop table if exists F_DATACATALOG;

drop table if exists F_DATADICTIONARY;

drop table if exists F_OPTDATASCOPE;

--drop index IND_OptID_OPTMETHOD on F_OPTDEF;

drop table if exists F_OPTDEF;

drop index ind_Tag_ID on F_OPT_LOG;

drop table if exists F_OPT_LOG;

drop table if exists F_OptInfo;

drop table if exists F_OptInfoData;

--drop index Ind_Filter_Table_Class_Name on F_QUERY_FILTER_CONDITION;

drop table if exists F_QUERY_FILTER_CONDITION;

drop table if exists F_RANKGRANT;

drop table if exists F_ROLEINFO;

drop table if exists F_ROLEPOWER;

drop table if exists F_STAT_MONTH;

drop table if exists F_SYS_NOTIFY;

drop table if exists F_UNITINFO;

drop index ind_regemail on F_USERINFO;

drop index Ind_loginname on F_USERINFO;

drop table if exists F_USERINFO;

drop table if exists F_USERROLE;

drop table if exists F_USERSETTING;

drop table if exists F_USERUNIT;

drop table if exists F_USER_FAVORITE;

drop index Ind_query_filter_modle_code on F_USER_QUERY_FILTER;

drop table if exists F_USER_QUERY_FILTER;

drop table if exists F_WORK_CLASS;

drop table if exists F_WORK_DAY;

drop table if exists M_InnerMsg;

drop table if exists M_InnerMsg_Recipient;

drop table if exists M_MsgAnnex;

drop table if exists P_TASK_LIST;

/*==============================================================*/
/* Table: F_ADDRESS_BOOK                                        */
/*==============================================================*/
create table F_ADDRESS_BOOK
(
   ADDRBOOKID           numeric(10,0) not null,
   BodyType             varchar(2) not null comment '�û�/����/��λ',
   BodyCode             varchar(16) not null comment '�û�/����/��λ ���',
   representation       varchar(200),
   UnitName             varchar(200),
   DeptName             varchar(100),
   RankName             varchar(50),
   Email                varchar(60),
   Email2               varchar(60),
   Email3               varchar(60),
   HomePage             varchar(100),
   QQ                   varchar(20),
   MSN                  varchar(60),
   wangwang             varchar(20),
   buzPhone             varchar(20),
   buzphone2            varchar(20),
   buzfax               varchar(20),
   assiphone            varchar(20),
   callbacphone         varchar(20),
   carphone             varchar(20),
   unitphone            varchar(20),
   homephone            varchar(20),
   homephone2           varchar(20),
   homephone3           varchar(20),
   homefax              varchar(20),
   mobilephone          varchar(20),
   mobilephone2         varchar(20),
   mobilephone3         varchar(20),
   unitzip              varchar(8),
   unitProvince         varchar(20),
   unitCity             varchar(20),
   unitDistrict         varchar(20),
   unitStreet           varchar(20),
   unitAddress          varchar(60),
   homezip              varchar(8),
   homeProvince         varchar(20),
   homeCity             varchar(20),
   homeDistrict         varchar(20),
   homeStreet           varchar(20),
   homeAddress          varchar(60),
   home2zip             varchar(8),
   home2Province        varchar(20),
   home2City            varchar(20),
   home2District        varchar(20),
   home2Street          varchar(20),
   home2Address         varchar(60),
   inuseAddress         varchar(1) comment '��λ/סլ/סլ2',
   SearchString         varchar(1000) comment 'ǰ������ֶε���������ĸ������ ���ӵĴ�',
   memo                 varchar(500),
   LastModifyDate       date,
   CreateDate           date,
   primary key (ADDRBOOKID)
);

alter table F_ADDRESS_BOOK comment
  'ϵͳ��ά��һ��ͳһ��ͨѶ¼ ģ�飬��ҪĿ����Ϊ���Ժ��� ͳһ�Ľӿ�';

/*==============================================================*/
/* Table: F_DATACATALOG                                         */
/*==============================================================*/
create table F_DATACATALOG
(
   CATALOGCODE          varchar(16) not null,
   CATALOGNAME          varchar(64) not null,
   CATALOGSTYLE         char(1) not null comment 'F : ��ܹ��е� U:�û� S��ϵͳ  G����',
   CATALOGTYPE          char(1) not null comment 'T����״��� L:�б�
            ',
   CATALOGDESC          varchar(256),
   FieldDesc            varchar(256) comment '�ֶ���������ͬ�ֶ��÷ֺŸ���',
   updateDate           date,
   CreateDate           date,
   optID                varchar(16) comment 'ҵ����࣬ʹ�������ֵ�DICTIONARYTYPE������',
   needCache            char(1) default '1',
   creator              varchar(32),
   updator              varchar(32),
   primary key (CATALOGCODE)
);

alter table F_DATACATALOG comment '���״̬	 U:�û� S��ϵͳ��G����
�����ʽ  T����״��� L:�б�
';


/*==============================================================*/
/* Table: F_DATADICTIONARY                                      */
/*==============================================================*/
create table F_DATADICTIONARY
(
   CATALOGCODE          varchar(16) not null,
   DATACODE             varchar(16) not null,
   EXTRACODE            varchar(16) comment '�����ֵ�ĸ������',
   EXTRACODE2           varchar(16) comment 'Ĭ�ϵ������ֶ�',
   DATATAG              char(1) comment 'N������D��ͣ�ã��û������Խ�������ֶ�',
   DATAVALUE            varchar(2048),
   DATASTYLE            char(1) comment 'F : ��ܹ��е� U:�û� S��ϵͳ  G����',
   DATADESC             varchar(256),
   LastModifyDate       date,
   CreateDate           date,
   DATAORDER            numeric(6,0) comment '�����ֶ�',
   primary key (CATALOGCODE, DATACODE)
);

alter table F_DATADICTIONARY comment '�����ֵ䣺���һЩ�������� ���������ʾ��Ϣ������һЩ ���������ƵĶ�Ӧ������ ״̬����ɫ����ͷ�� �ȵ�
';

/*==============================================================*/
/* Table: F_OPTDATASCOPE                                        */
/*==============================================================*/
create table F_OPTDATASCOPE
(
   optScopeCode         varchar(16) not null,
   OptID                varchar(16),
   scopeName            varchar(64),
   FilterCondition      varchar(1024) comment '������䣬�����еĲ��� [mt] ҵ��� [uc] �û����� [uu] �û���������',
   scopeMemo            varchar(1024) comment '����Ȩ��˵��',
   FilterGroup          varchar(16) default 'G',
   primary key (optScopeCode)
);

/*==============================================================*/
/* Table: F_OPTDEF                                              */
/*==============================================================*/
create table F_OPTDEF
(
   OPTCODE              varchar(32) not null,
   OptID                varchar(32),
   OPTNAME              varchar(100),
   OPTMETHOD            varchar(50) comment '�������� ����',
   OPTURL               varchar(256),
   OPTDESC              varchar(256),
   IsInWorkflow         char(1) comment '�Ƿ�Ϊ���̲������� F������  T �� ��',
   updateDate           date,
   CreateDate           date,
   OPTREQ               varchar(8),
   optOrder 			numeric(4),
   creator              varchar(32),
   updator              varchar(32),
   primary key (OPTCODE)
);

/*==============================================================*/
/* Index: IND_OptID_OPTMETHOD                                   */
/*==============================================================*/
create index IND_OptID_OPTMETHOD on F_OPTDEF
(
   OptID,
   OPTMETHOD
);

/*==============================================================*/
/* Table: F_OPT_LOG                                             */
/*==============================================================*/
create table F_OPT_LOG
(
   logId                numeric(12,0) not null,
   logLevel             varchar(2) not null,
   usercode             varchar(8) not null,
   opttime              date not null,
   OptContent           varchar(1000) not null comment '��������',
   NewValue             text comment '��ֵ',
   OldValue             text comment 'ԭֵ',
   OptID                varchar(64) not null comment 'ģ�飬���߱�',
   OPTMethod            varchar(64) comment '�����������ֶ�',
   optTag               varchar(200) comment 'һ�����ڹ�����ҵ������ı�ʶ����������ȵ�',
   primary key (logId)
);

/*==============================================================*/
/* Index: ind_Tag_ID                                            */
/*==============================================================*/
create index ind_Tag_ID on F_OPT_LOG
(
   optTag
);

/*==============================================================*/
/* Table: F_OptInfo                                             */
/*==============================================================*/
create table F_OptInfo
(
   OptID                varchar(32) not null,
   OptName              varchar(100) not null,
   PreOptID             varchar(32) not null,
   optRoute             varchar(256) comment '��angularjs·��ƥ��',
   opturl               varchar(256),
   FormCode             varchar(4),
   OptType              char(1) comment ' S:ʵʩҵ��, O:��ͨҵ��, W:����ҵ��, I :��Ŀҵ��',
   MsgNo                numeric(10,0),
   MsgPrm               varchar(256),
   IsInToolBar          char(1),
   ImgIndex             numeric(10,0),
   TopOptID             varchar(8),
   OrderInd             numeric(4,0) comment '���˳��ֻ����ͬһ����ҵ��������',
   FLOWCODE             varchar(8) comment 'ͬһ�����������Ӧ��ֻ��һ����Ч�İ汾',
   PageType             char(1) not null default 'I' comment 'D : DIV I:iFrame',
   Icon                 varchar(512),
   height               numeric(10,0),
   width                numeric(10,0),
   updateDate           date,
   CreateDate           date,
   creator              varchar(32),
   updator              varchar(32),
   primary key (OptID)
);

/*==============================================================*/
/* Table: F_OptInfoData                                         */
/*==============================================================*/
create table F_OptInfoData
(
   TBCODE               varchar(32) not null,
   OptID                varchar(8) not null,
   LastModifyDate       date,
   CreateDate           date,
   primary key (TBCODE, OptID)
);

alter table F_OptInfoData comment 'ҵ��ģ��ͱ��Ƕ�Զ�Ĺ�ϵ,������������Ϊ����Ȩ������ʱ��һ���������';

/*==============================================================*/
/* Table: F_QUERY_FILTER_CONDITION                              */
/*==============================================================*/
create table F_QUERY_FILTER_CONDITION
(
   CONDITION_NO         numeric(12,0) not null,
   Table_Class_Name     varchar(64) not null comment '���ݿ��������po������',
   Param_Name           varchar(64) not null comment '������',
   Param_Label          varchar(120) not null comment '�����������ʾ',
   Param_Type           varchar(8) comment '�������ͣ�S �ַ�����L ���֣� N ��С�������ݣ� D ���ڣ� T ʱ����� Y �꣬ M ��',
   Default_Value        varchar(100),
   Filter_Sql           varchar(200) comment '������䣬����ƴװ��sql�����',
   Select_Data_type     char(1) not null default 'N' comment '�������������ݣ� N ��û�У� D �����ֵ�, S ͨ��sql����ã� J json����ֱ�ӻ�ȡ
            ',
   Select_Data_Catalog  varchar(64) comment '�����ֵ�',
   Select_SQL           varchar(1000) comment '�����������ֶε�sql���',
   Select_JSON          varchar(2000) comment 'KEY,Value��ֵ�ԣ�JSON��ʽ',
   primary key (CONDITION_NO)
);

/*==============================================================*/
/* Index: Ind_Filter_Table_Class_Name                           */
/*==============================================================*/
create index Ind_Filter_Table_Class_Name on F_QUERY_FILTER_CONDITION
(
   Table_Class_Name
);

/*==============================================================*/
/* Table: F_RANKGRANT                                           */
/*==============================================================*/
create table F_RANKGRANT
(
   RANK_grant_ID        numeric(12,0) not null,
   granter              varchar(8) not null,
   UNITCODE             varchar(6) not null,
   UserStation          varchar(4) not null,
   UserRank             varchar(2) not null comment 'RANK ���벻�� 0��ͷ�Ŀ��Խ�������',
   beginDate            date not null,
   grantee              varchar(8) not null,
   endDate              date,
   grantDesc            varchar(256),
   LastModifyDate       date,
   CreateDate           date,
   primary key (RANK_grant_ID, UserRank)
);

/*==============================================================*/
/* Table: F_ROLEINFO                                            */
/*==============================================================*/
create table F_ROLEINFO
(
   ROLECODE             varchar(32) not null,
   ROLENAME             varchar(64),
   ROLETYPE             char(1) not null comment 'SΪϵͳ���ܽ�ɫ I Ϊ��Ŀ��ɫ W��������ɫ',
   UNITCODE             varchar(32),
   ISVALID              char(1) not null,
   ROLEDESC             varchar(256),
   updateDate           date,
   CreateDate           date,
   creator              varchar(32),
   updator              varchar(32),
   primary key (ROLECODE)
);

/*==============================================================*/
/* Table: F_ROLEPOWER                                           */
/*==============================================================*/
create table F_ROLEPOWER
(
   ROLECODE             varchar(32) not null,
   OPTCODE              varchar(32) not null,
   optScopeCodes        varchar(1000) comment '�ö��Ÿ��������ݷ�Χ��ϣ���\all ��ʾȫ����',
   updateDate           date,
   CreateDate           date,
   creator              varchar(32),
   updator              varchar(32),
   primary key (ROLECODE, OPTCODE)
);

/*==============================================================*/
/* Table: F_STAT_MONTH                                          */
/*==============================================================*/
create table F_STAT_MONTH
(
   YEARMONTH            varchar(6) not null comment 'YYYYMM',
   BeginDay             date not null,
   EendDay              date not null,
   EndSchedule          char(1) comment '����ֶκ���',
   BeginSchedule        char(1) comment '����ֶκ���',
   primary key (YEARMONTH)
);

alter table F_STAT_MONTH comment 'OAҵ��ͳ���£������Զ���ͳ���µ���ֹ����';

/*==============================================================*/
/* Table: F_SYS_NOTIFY                                          */
/*==============================================================*/
create table F_SYS_NOTIFY
(
   Notify_ID            numeric(12,0) not null,
   Notify_Sender        varchar(100),
   Notify_Receiver      varchar(100) not null,
   Msg_Subject          varchar(200),
   Msg_Content          varchar(2000) not null,
   notice_Type          varchar(100),
   Notify_State         char(1) comment '0 �ɹ��� 1 ʧ�� 2 ���ֳɹ�',
   Error_Msg            varchar(500),
   Notify_Time          date,
   optTag               varchar(200) comment 'һ�����ڹ�����ҵ������',
   OPTMethod            varchar(64) comment '�����������ֶ�',
   OptID                varchar(64) not null comment 'ģ�飬���߱�',
   primary key (Notify_ID)
);

/*==============================================================*/
/* Table: F_UNITINFO                                            */
/*==============================================================*/
create table F_UNITINFO
(
   UNITCODE             varchar(32) not null,
   PARENTUNIT           varchar(32),
   UNITTYPE             char(1) comment '��������/ �ʵ�滮/���/��������',
   ISVALID              char(1) not null comment 'T:��Ч F:��Ч',
   UNITTAG              varchar(100) comment '�û�������ϵͳ����',
   UNITNAME             varchar(300) not null,
   englishName          varchar(300),
   depno                varchar(100) comment '��֯�������룺',
   UNITDESC             varchar(256),
   ADDRBOOKID           numeric(10,0),
   UNITSHORTNAME        varchar(32),
   unitWord             varchar(100),
   unitGrade            numeric(4,0),
   unitOrder            numeric(4,0),
   updateDate           date,
   CreateDate           date,
   extJsonInfo          varchar(1000),
   creator              varchar(32),
   updator              varchar(32),
   UNITPATH 			VARCHAR(1000),
   primary key (UNITCODE)
);

/*==============================================================*/
/* Table: F_USERINFO                                            */
/*==============================================================*/
create table F_USERINFO
(
   USERCODE             varchar(32) not null,
   USERPIN              varchar(100),
   USERTYPE             char(1) default 'U' comment '��������/��������/ϵͳ����',
   ISVALID              char(1) not null comment 'T:��Ч F:��Ч',
   LOGINNAME            varchar(100) not null,
   UserName             varchar(300) not null comment '�ǳ�',
   USERTAG              varchar(100) comment '���ڵ�����ϵͳ����',
   englishName          varchar(300),
   USERDESC             varchar(256),
   LoginTimes           numeric(6,0),
   ActiveTime           date,
   LoginIP              varchar(16),
   ADDRBOOKID           numeric(10,0),
   RegEmail             varchar(60) comment 'ע����Email�������ظ�',
   USERPWD              varchar(20) comment '�����Ҫ������',
   pwdExpiredTime       date,
   REGCELLPHONE         varchar(15),
   primaryUnit          varchar(32),
   userWord             varchar(100) comment '΢�ź�',
   userOrder            numeric(4,0),
   updateDate           date,
   CreateDate           date,
   extJsonInfo          varchar(1000),
   creator              varchar(32),
   updator              varchar(32),
   primary key (USERCODE)
);

/*==============================================================*/
/* Index: Ind_loginname                                         */
/*==============================================================*/
create unique index Ind_loginname on F_USERINFO
(
   LOGINNAME
);

/*==============================================================*/
/* Index: ind_regemail                                          */
/*==============================================================*/
create unique index ind_regemail on F_USERINFO
(
   RegEmail
);

/*==============================================================*/
/* Table: F_USERROLE                                            */
/*==============================================================*/
create table F_USERROLE
(
   USERCODE             varchar(32) not null,
   ROLECODE             varchar(32) not null,
   OBTAINDATE           date not null,
   SECEDEDATE           date,
   CHANGEDESC           varchar(256),
   updateDate           date,
   CreateDate           date,
   creator              varchar(32),
   updator              varchar(32),
   primary key (USERCODE, ROLECODE)
);

/*==============================================================*/
/* Table: F_USERSETTING                                         */
/*==============================================================*/
create table F_USERSETTING
(
   USERCODE             varchar(8) not null comment 'DEFAULT:ΪĬ������
            SYS001~SYS999: Ϊϵͳ���÷���
            ��һ���û���,������ϵͳ��һ�����÷���',
   ParamCode            varchar(16) not null,
   ParamValue           varchar(2048) not null,
   optID                varchar(16) not null,
   ParamName            varchar(200),
   CreateDate           date,
   primary key (USERCODE, ParamCode)
);

/*==============================================================*/
/* Table: F_USERUNIT                                            */
/*==============================================================*/
create table F_USERUNIT
(
   USERUNITID           varchar(16) not null,
   UNITCODE             varchar(6) not null,
   USERCODE             varchar(8) not null,
   IsPrimary            char(1) not null default '1' comment 'T��Ϊ���� F����ְ',
   UserStation          varchar(16) not null,
   UserRank             varchar(2) not null comment 'RANK ���벻�� 0��ͷ�Ŀ��Խ�������',
   RankMemo             varchar(256) comment '��ְ��ע',
   USERORDER            numeric(8,0) default 0,
   updateDate           date,
   CreateDate           date,
   creator              varchar(32),
   updator              varchar(32),
   primary key (USERUNITID)
);

alter table F_USERUNIT comment 'ͬһ���˿����ڶ�����ŵ��β�ͬ��ְλ';

/*==============================================================*/
/* Table: F_USER_FAVORITE                                       */
/*==============================================================*/
create table F_USER_FAVORITE
(
   USERCODE             varchar(8) not null comment 'DEFAULT:ΪĬ������
            SYS001~SYS999: Ϊϵͳ���÷���
            ��һ���û���,������ϵͳ��һ�����÷���',
   OptID                varchar(16) not null,
   LastModifyDate       date,
   CreateDate           date,
   primary key (USERCODE, OptID)
);

/*==============================================================*/
/* Table: F_USER_QUERY_FILTER                                   */
/*==============================================================*/
create table F_USER_QUERY_FILTER
(
   FILTER_NO            numeric(12,0) not null,
   userCode             varchar(8) not null,
   modle_code           varchar(64) not null comment '������Ա���ж��壬�������ظ���������ϵͳ��ģ�������ϵ�ǰ�Ĳ�������',
   filter_name          varchar(200) not null comment '�û����ж��������',
   filter_value         varchar(3200) not null comment '����ֵ��json��ʽ����Ӧһ��map',
   primary key (FILTER_NO)
);

/*==============================================================*/
/* Index: Ind_query_filter_modle_code                           */
/*==============================================================*/
create index Ind_query_filter_modle_code on F_USER_QUERY_FILTER
(
   modle_code
);

/*==============================================================*/
/* Table: F_WORK_CLASS                                          */
/*==============================================================*/
create table F_WORK_CLASS
(
   CLASS_ID             numeric(12,0) not null,
   CLASS_NAME           varchar(50) not null,
   SHORT_NAME           varchar(10) not null,
   begin_time           varchar(6) comment '9:00',
   end_time             varchar(6) comment '+4:00 ''+''��ʾ�ڶ���',
   has_break            char(1),
   break_begin_time     varchar(6) comment '9:00',
   break_end_time       varchar(6) comment '+4:00 ''+''��ʾ�ڶ���',
   class_desc           varchar(500),
   record_date          date,
   recorder             varchar(8),
   primary key (CLASS_ID)
);

alter table F_WORK_CLASS comment 'CLASS_ID
 Ϊ 0 �ı�ʾ��Ϣ�����Բ���������г���
 Ϊ 1 ��ΪĬ�ϰ����Ϣ';

/*==============================================================*/
/* Table: F_WORK_DAY                                            */
/*==============================================================*/
create table F_WORK_DAY
(
   WorkDay              date not null,
   DayType              char(1) not null comment 'A:�����շż٣�B:��ĩ���ݳɹ���ʱ�� C �����ϰ� D�����ݼ�',
   WorkTimeType         varchar(20),
   WorkDayDesc          varchar(255),
   primary key (WorkDay)
);

alter table F_WORK_DAY comment '��������ҵʱ����
A:�����շż� B:��ĩ���ݳɹ���ʱ��  C: �����ϰ�  D:�����ݼ�  
';

/*==============================================================*/
/* Table: M_InnerMsg                                            */
/*==============================================================*/
create table M_InnerMsg
(
   MsgCode              varchar(16) not null comment '��Ϣ�����Զ��壬ͨ��S_M_INNERMSG��������',
   Sender               varchar(128),
   SendDate             date,
   MsgTitle             varchar(128),
   MsgType              char(1) comment 'P= ����Ϊ��Ϣ  A= ����Ϊ���棨֪ͨ��
            M=�ʼ�',
   MailType             char(1) comment 'I=�ռ���
            O=������
            D=�ݸ���
            T=�ϼ���
            
            
            ',
   MailUnDelType        char(1),
   ReceiveName          varchar(2048) comment 'ʹ�ò��ţ��������������м�ʹ��Ӣ�ķֺŷָ�',
   HoldUsers            numeric(8,0) comment '����Ϊ�����˺ͽ�����������ӣ����ͺͽ�����ɾ����Ϣʱ-1��������Ϊ0ʱ����ɾ��������¼
            
            ��Ϣ����Ϊ�ʼ�ʱ����Ҫ����',
   msgState             char(1) comment 'δ��/�Ѷ�/ɾ��',
   msgContent           longblob,
   EmailId              varchar(8) comment '�û����ö�����ʱʹ��',
   OptID                varchar(64) not null comment 'ģ�飬���߱�',
   OPTMethod            varchar(64) comment '�����������ֶ�',
   optTag               varchar(200) comment 'һ�����ڹ�����ҵ������',
   primary key (MsgCode)
);

alter table M_InnerMsg comment '�ڲ���Ϣ�빫��
���ܴ���,  ��ʵ���Զ�������, ��Ϊ�� �ͷ����� �� һ�Զ�Ĺ�ϵ

                               -&#';

/*==============================================================*/
/* Table: M_InnerMsg_Recipient                                  */
/*==============================================================*/
create table M_InnerMsg_Recipient
(
   MsgCode              varchar(16) not null,
   Receive              varchar(8) not null,
   ReplyMsgCode         int,
   ReceiveType          char(1) comment 'P=����Ϊ��Ϣ
            A=����Ϊ����
            M=�ʼ�',
   MailType             char(1) comment 'T=�ռ���
            C=����
            B=����',
   msgState             char(1) comment 'δ��/�Ѷ�/ɾ�����ռ�������ʱ������ʾ
            
            U=δ��
            R=�Ѷ�
            D=ɾ��',
   ID                   varchar(16) not null,
   primary key (ID)
);

alter table M_InnerMsg_Recipient comment '�ڲ���Ϣ���ʼ����빫���ռ��˼���Ϣ��Ϣ';

/*==============================================================*/
/* Table: M_MsgAnnex                                            */
/*==============================================================*/
create table M_MsgAnnex
(
   MsgCode              varchar(16) not null,
   InfoCode             varchar(16) not null,
   MsgAnnexId           varchar(16) not null,
   primary key (MsgAnnexId)
);

/*==============================================================*/
/* Table: P_TASK_LIST                                           */
/*==============================================================*/
create table P_TASK_LIST
(
   taskid               numeric(12,0) not null comment '�Զ����ɵ���������Ҫһ�����������',
   taskowner            varchar(8) not null comment '˭������',
   tasktag              varchar(1) not null comment '������outlook�е��ʼ���ǣ������ò�ͬ����ɫ������ͼ���ʶ',
   taskrank             varchar(1) not null comment '��������ȼ�',
   taskstatus           varchar(2) not null comment '�����С���ɡ�ȡ������ֹ',
   tasktitle            varchar(256) not null,
   taskmemo             varchar(1000) comment '��Ҫ��������ľ�������',
   tasktype             varchar(8) not null comment '���ˡ���֯����쵼ί�� �ȵ�',
   OptID                varchar(64) not null comment 'ģ�飬���߱�',
   OPTMethod            varchar(64) comment '�����������ֶ�',
   optTag               varchar(200) comment 'һ�����ڹ�����ҵ������',
   creator              varchar(8) not null,
   created              date not null,
   planbegintime        date not null,
   planendtime          date,
   begintime            date,
   endtime              date,
   finishmemo           varchar(1000) comment '��Ҫ��¼�����ִ�й��̺ͽ��',
   noticeSign           varchar(1) comment '���ѱ�־Ϊ����ֹ���ѡ�δ���ѡ�������',
   lastNoticeTime       date comment '���һ������ʱ�䣬�������Ѳ��Կ������Ѷ��',
   taskdeadline         date,
   taskvalue            varchar(2048) comment '���ã��ֶβ���ʱʹ��',
   primary key (taskid)
);

