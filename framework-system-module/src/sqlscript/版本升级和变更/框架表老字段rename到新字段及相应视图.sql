alter table F_DATACATALOG rename column CATALOGCODE to CATALOG_CODE;
alter table F_DATACATALOG rename column catalogname to CATALOG_NAME;
alter table F_DATACATALOG rename column catalogstyle to CATALOG_STYLE;
alter table F_DATACATALOG rename column catalogtype to CATALOG_TYPE;
alter table F_DATACATALOG rename column catalogdesc to CATALOG_DESC;
alter table F_DATACATALOG rename column fielddesc to FIELD_DESC;
alter table F_DATACATALOG rename column updatedate to UPDATE_DATE;
alter table F_DATACATALOG rename column createdate to CREATE_DATE;
alter table F_DATACATALOG rename column optid to OPT_ID;
alter table F_DATACATALOG rename column needcache to NEED_CACHE;


alter table F_DATADICTIONARY rename column catalogcode to CATALOG_CODE;
alter table F_DATADICTIONARY rename column datacode to DATA_CODE;
alter table F_DATADICTIONARY rename column extracode to EXTRA_CODE;
alter table F_DATADICTIONARY rename column extracode2 to EXTRA_CODE2;
alter table F_DATADICTIONARY rename column datatag to DATA_TAG;
alter table F_DATADICTIONARY rename column datavalue to DATA_VALUE;
alter table F_DATADICTIONARY rename column datastyle to DATA_STYLE;
alter table F_DATADICTIONARY rename column datadesc to DATA_DESC;
alter table F_DATADICTIONARY rename column lastmodifydate to LAST_MODIFY_DATE;
alter table F_DATADICTIONARY rename column createdate to CREATE_DATE;
alter table F_DATADICTIONARY rename column dataorder to DATA_ORDER;


alter table F_OPTDATASCOPE rename column optscopecode to OPT_SCOPE_CODE;
alter table F_OPTDATASCOPE rename column optid to OPT_ID;
alter table F_OPTDATASCOPE rename column scopename to SCOPE_NAME;
alter table F_OPTDATASCOPE rename column filtercondition to FILTER_CONDITION;
alter table F_OPTDATASCOPE rename column scopememo to SCOPE_MEMO;
alter table F_OPTDATASCOPE rename column filtergroup to FILTER_GROUP;


alter table F_OPTDEF rename column optcode to OPT_CODE;
alter table F_OPTDEF rename column optid to OPT_ID;
alter table F_OPTDEF rename column optname to OPT_NAME;
alter table F_OPTDEF rename column optmethod to OPT_METHOD;
alter table F_OPTDEF rename column opturl to OPT_URL;
alter table F_OPTDEF rename column optdesc to OPT_DESC;
alter table F_OPTDEF rename column isinworkflow to IS_IN_WORKFLOW;
alter table F_OPTDEF rename column updatedate to UPDATE_DATE;
alter table F_OPTDEF rename column createdate to CREATE_DATE;
alter table F_OPTDEF rename column optreq to OPT_REQ;
alter table F_OPTDEF rename column optorder to OPT_ORDER;

alter table F_OPTFLOWNOINFO rename column ownercode to OWNER_CODE;
alter table F_OPTFLOWNOINFO rename column codecode to CODE_CODE;
alter table F_OPTFLOWNOINFO rename column codedate to CODE_DATE;
alter table F_OPTFLOWNOINFO rename column curno to CUR_NO;
alter table F_OPTFLOWNOINFO rename column lastcodedate to LAST_CODE_DATE;
alter table F_OPTFLOWNOINFO rename column createdate to CREATE_DATE;
alter table F_OPTFLOWNOINFO rename column lastmodifydate to LAST_MODIFY_DATE;

alter table F_OPTFLOWNOPOOL rename column ownercode to OWNER_CODE;
alter table F_OPTFLOWNOPOOL rename column codecode to CODE_CODE;
alter table F_OPTFLOWNOPOOL rename column codedate to CODE_DATE;
alter table F_OPTFLOWNOPOOL rename column curno to CUR_NO;
alter table F_OPTFLOWNOPOOL rename column createdate to CREATE_DATE;


alter table F_OPTINFO rename column optid to OPT_ID;
alter table F_OPTINFO rename column optname to OPT_NAME;
alter table F_OPTINFO rename column preoptid to PRE_OPT_ID;
alter table F_OPTINFO rename column optroute to OPT_ROUTE;
alter table F_OPTINFO rename column opturl to OPT_URL;
alter table F_OPTINFO rename column formcode to FORM_CODE;
alter table F_OPTINFO rename column opttype to OPT_TYPE;
alter table F_OPTINFO rename column msgno to MSG_NO;
alter table F_OPTINFO rename column msgprm to MSG_PRM;
alter table F_OPTINFO rename column isintoolbar to IS_IN_TOOLBAR;
alter table F_OPTINFO rename column imgindex to IMG_INDEX;
alter table F_OPTINFO rename column topoptid to TOP_OPT_ID;
alter table F_OPTINFO rename column orderind to ORDER_IND;
alter table F_OPTINFO rename column flowcode to FLOW_CODE;
alter table F_OPTINFO rename column pagetype to PAGE_TYPE;
alter table F_OPTINFO rename column updatedate to UPDATE_DATE;
alter table F_OPTINFO rename column createdate to CREATE_DATE;

alter table F_OPT_LOG rename column logid to LOG_ID;
alter table F_OPT_LOG rename column loglevel to LOG_LEVEL;
alter table F_OPT_LOG rename column usercode to USER_CODE;
alter table F_OPT_LOG rename column opttime to OPT_TIME;
alter table F_OPT_LOG rename column optcontent to OPT_CONTENT;
alter table F_OPT_LOG rename column newvalue to NEW_VALUE;
alter table F_OPT_LOG rename column oldvalue to OLD_VALUE;
alter table F_OPT_LOG rename column optid to OPT_ID;
alter table F_OPT_LOG rename column optmethod to OPT_METHOD;
alter table F_OPT_LOG rename column opttag to OPT_TAG;

alter table F_ROLEINFO rename column rolecode to ROLE_CODE;
alter table F_ROLEINFO rename column rolename to ROLE_NAME;
alter table F_ROLEINFO rename column roletype to ROLE_TYPE;
alter table F_ROLEINFO rename column unitcode to UNIT_CODE;
alter table F_ROLEINFO rename column isvalid to IS_VALID;
alter table F_ROLEINFO rename column roledesc to ROLE_DESC;
alter table F_ROLEINFO rename column updatedate to UPDATE_DATE;
alter table F_ROLEINFO rename column createdate to CREATE_DATE;

alter table F_ROLEPOWER rename column rolecode to ROLE_CODE;
alter table F_ROLEPOWER rename column optcode to OPT_CODE;
alter table F_ROLEPOWER rename column optscopecodes to OPT_SCOPE_CODES;
alter table F_ROLEPOWER rename column updatedate to UPDATE_DATE;
alter table F_ROLEPOWER rename column createdate to CREATE_DATE;


alter table F_SYS_NOTIFY rename column opttag to OPT_TAG;
alter table F_SYS_NOTIFY rename column optmethod to OPT_METHOD;
alter table F_SYS_NOTIFY rename column optid to OPT_ID;


alter table F_UNITINFO rename column unitcode to UNIT_CODE;
alter table F_UNITINFO rename column parentunit to PARENT_UNIT;
alter table F_UNITINFO rename column unittype to UNIT_TYPE;
alter table F_UNITINFO rename column isvalid to IS_VALID;
alter table F_UNITINFO rename column unittag to UNIT_TAG;
alter table F_UNITINFO rename column unitname to UNIT_NAME;
alter table F_UNITINFO rename column englishname to ENGLISH_NAME;
alter table F_UNITINFO rename column depno to DEP_NO;
alter table F_UNITINFO rename column unitdesc to UNIT_DESC;
alter table F_UNITINFO rename column addrbookid to ADDRBOOK_ID;
alter table F_UNITINFO rename column unitshortname to UNIT_SHORT_NAME;
alter table F_UNITINFO rename column unitword to UNIT_WORD;
alter table F_UNITINFO rename column unitgrade to UNIT_GRADE;
alter table F_UNITINFO rename column unitorder to UNIT_ORDER;
alter table F_UNITINFO rename column updatedate to UPDATE_DATE;
alter table F_UNITINFO rename column createdate to CREATE_DATE;
alter table F_UNITINFO rename column unitpath to UNIT_PATH;
alter table F_UNITINFO rename column unitmanager to UNIT_MANAGER;


alter table F_USERINFO rename column usercode to USER_CODE;
alter table F_USERINFO rename column userpin to USER_PIN;
alter table F_USERINFO rename column usertype to USER_TYPE;
alter table F_USERINFO rename column isvalid to IS_VALID;
alter table F_USERINFO rename column loginname to LOGIN_NAME;
alter table F_USERINFO rename column username to USER_NAME;
alter table F_USERINFO rename column usertag to USER_TAG;
alter table F_USERINFO rename column englishname to ENGLISH_NAME;
alter table F_USERINFO rename column userdesc to USER_DESC;
alter table F_USERINFO rename column logintimes to LOGIN_TIMES;
alter table F_USERINFO rename column activetime to ACTIVE_TIME;
alter table F_USERINFO rename column loginip to LOGIN_IP;
alter table F_USERINFO rename column addrbookid to ADDRBOOK_ID;
alter table F_USERINFO rename column regemail to REG_EMAIL;
alter table F_USERINFO rename column userpwd to USER_PWD;
alter table F_USERINFO rename column pwdexpiredtime to PWD_EXPIRED_TIME;
alter table F_USERINFO rename column regcellphone to REG_CELL_PHONE;
alter table F_USERINFO rename column primaryunit to PRIMARY_UNIT;
alter table F_USERINFO rename column userword to USER_WORD;
alter table F_USERINFO rename column userorder to USER_ORDER;
alter table F_USERINFO rename column updatedate to UPDATE_DATE;
alter table F_USERINFO rename column createdate to CREATE_DATE;


alter table F_USERROLE rename column usercode to USER_CODE;
alter table F_USERROLE rename column rolecode to ROLE_CODE;
alter table F_USERROLE rename column obtaindate to OBTAIN_DATE;
alter table F_USERROLE rename column secededate to SECEDE_DATE;
alter table F_USERROLE rename column changedesc to CHANGE_DESC;
alter table F_USERROLE rename column updatedate to UPDATE_DATE;
alter table F_USERROLE rename column createdate to CREATE_DATE;


alter table F_USERSETTING rename column usercode to USER_CODE;
alter table F_USERSETTING rename column paramcode to PARAM_CODE;
alter table F_USERSETTING rename column paramvalue to PARAM_VALUE;
alter table F_USERSETTING rename column optid to OPT_ID;
alter table F_USERSETTING rename column paramname to PARAM_NAME;
alter table F_USERSETTING rename column createdate to CREATE_DATE;


alter table F_USERUNIT rename column userunitid to USER_UNIT_ID;
alter table F_USERUNIT rename column unitcode to UNIT_CODE;
alter table F_USERUNIT rename column usercode to USER_CODE;
alter table F_USERUNIT rename column isprimary to IS_PRIMARY;
alter table F_USERUNIT rename column userstation to USER_STATION;
alter table F_USERUNIT rename column userrank to USER_RANK;
alter table F_USERUNIT rename column rankmemo to RANK_MEMO;
alter table F_USERUNIT rename column userorder to USER_ORDER;
alter table F_USERUNIT rename column updatedate to UPDATE_DATE;
alter table F_USERUNIT rename column createdate to CREATE_DATE;


alter table F_USER_QUERY_FILTER rename column usercode to USER_CODE;


alter table M_INNERMSG rename column msgcode to MSG_CODE;
alter table M_INNERMSG rename column senddate to SEND_DATE;
alter table M_INNERMSG rename column msgtitle to MSG_TITLE;
alter table M_INNERMSG rename column msgtype to MSG_TYPE;
alter table M_INNERMSG rename column mailtype to MAIL_TYPE;
alter table M_INNERMSG rename column mailundeltype to MAIL_UNDEL_TYPE;
alter table M_INNERMSG rename column receivename to RECEIVE_NAME;
alter table M_INNERMSG rename column holdusers to HOLD_USERS;
alter table M_INNERMSG rename column msgstate to MSG_STATE;
alter table M_INNERMSG rename column msgcontent to MSG_CONTENT;
alter table M_INNERMSG rename column emailid to EMAIL_ID;
alter table M_INNERMSG rename column optid to OPT_ID;
alter table M_INNERMSG rename column optmethod to OPT_METHOD;
alter table M_INNERMSG rename column opttag to OPT_TAG;

alter table M_INNERMSG_RECIPIENT rename column msgcode to MSG_CODE;
alter table M_INNERMSG_RECIPIENT rename column replymsgcode to REPLY_MSG_CODE;
alter table M_INNERMSG_RECIPIENT rename column receivetype to RECEIVE_TYPE;
alter table M_INNERMSG_RECIPIENT rename column mailtype to MAIL_TYPE;
alter table M_INNERMSG_RECIPIENT rename column msgstate to MSG_STATE;


alter table M_MSGANNEX rename column msgcode to MSG_CODE;
alter table M_MSGANNEX rename column infocode to INFO_CODE;
alter table M_MSGANNEX rename column msgannexid to MSG_ANNEX_ID;


--框架外视图（好像是工作流视图）
create or replace view f_v_wf_optdef_url_map as
select c.opt_url || b.opt_url as optdefurl, b.opt_req, b.opt_code,
       b.opt_desc,b.opt_Method , c.opt_id,b.Opt_Name
from F_OPTDEF b join f_optinfo c
    on (b.opt_id = c.opt_id)
 where c.Opt_Type = 'W'
   --and c.opturl <> '...'
   and b.opt_req is not null;
   
   create or replace view v_inner_user_task_list_fin as
select a.WFINSTID,w.WFCODE,w.version, w.WfOptName,w.wfOptTag,a.nodeinstid, nvl(a.UnitCode,nvl(w.UnitCode,'0000000')) as UnitCode,
        a.usercode,c.ROLETYPE,c.ROLECODE,'一般任务' as AUTHDESC, c.nodecode,
          c.NodeName,c.NodeType,c.OptType as NODEOPTTYPE,d.opt_id,d.Opt_Name,d.Opt_Name as MethodName,
          d.optdefurl as OptUrl,d.opt_Method,c.OptParam ,d.Opt_Desc,a.CREATETIME,a.PromiseTime,a.TIMELIMIT,
          c.OPTCODE,c.ExpireOpt,c.STAGECODE,a.lastupdateuser,a.lastupdatetime,w.inststate
from WF_NODE_INSTANCE a join WF_FLOW_INSTANCE w on (a.WFINSTID=w.WFINSTID)
           join WF_NODE c on (a.NODEID=c.NODEID)
           join f_v_wf_optdef_url_map d on (c.OPTCODE=d.OPT_CODE)
where /*c.NODETYPE<>'R' and --游离节点不会创建时实例*/
    (a.NODESTATE='C') and ( w.inststate='C'  or w.inststate='N')  and a.TASKASSIGNED='S'
union all
select a.WFINSTID,w.WFCODE,w.version, w.WfOptName,w.wfOptTag,a.nodeinstid, nvl(a.UnitCode,nvl(w.UnitCode,'0000000')) as UnitCode,
        b.usercode,b.ROLETYPE,b.ROLECODE,b.AUTHDESC, c.nodecode,
          c.NodeName,c.NodeType,c.OptType as NODEOPTTYPE,d.opt_id,d.Opt_Name,d.Opt_Name as MethodName,
          d.optdefurl as OptUrl,d.opt_Method,c.OptParam ,d.Opt_Desc,a.CREATETIME,a.PromiseTime,a.TIMELIMIT,
          c.OPTCODE,c.ExpireOpt,c.STAGECODE,a.lastupdateuser,a.lastupdatetime,w.inststate
from WF_NODE_INSTANCE a join WF_FLOW_INSTANCE w on (a.WFINSTID=w.WFINSTID)
           join WF_ACTION_TASK b on (a.NODEINSTID=b.NODEINSTID)
           join WF_NODE c on (a.NODEID=c.NODEID)
           join f_v_wf_optdef_url_map d on (c.OPTCODE=d.OPT_CODE)
where (a.NODESTATE='C') and ( w.inststate='C'  or w.inststate='N') and a.TASKASSIGNED='T'
    and b.ISVALID='T' and  b.TASKSTATE='C' and (b.EXPIRETIME is null or b.EXPIRETIME>sysdate)
union all
select  a.WFINSTID,w.WFCODE,w.version,w.WfOptName,w.wfOptTag,a.nodeinstid, b.Unit_Code ,
         b.user_code,c.ROLETYPE,c.ROLECODE, '系统指定' as AUTHDESC, c.nodecode,
          c.NodeName,c.NodeType,c.OptType as NODEOPTTYPE,d.opt_id,d.Opt_Name,d.Opt_Name as MethodName,
          d.optdefurl as OptUrl,d.opt_Method,c.OptParam,d.Opt_Desc,a.CREATETIME,a.PromiseTime,a.timelimit,
           c.OPTCODE,c.ExpireOpt,c.STAGECODE,a.lastupdateuser,a.lastupdatetime,w.inststate
from WF_NODE_INSTANCE a join WF_FLOW_INSTANCE w on (a.WFINSTID=w.WFINSTID)
       join WF_NODE c on (a.NODEID=c.NODEID)
       join f_v_wf_optdef_url_map d on (c.OPTCODE=d.OPT_CODE) , F_USERUNIT b
where (a.NODESTATE='C') and ( w.inststate='C'  or w.inststate='N') and a.TASKASSIGNED='D' and
        (a.UNITCODE is null or a.UNITCODE=b.UNIT_CODE) and
       (   (c.ROLETYPE='gw' and c.ROLECODE=b.User_Station) or
           (c.ROLETYPE='xz' and c.ROLECODE=b.User_Rank) );
create or replace view v_inner_user_task_list as
select a.WFINSTID,w.WFCODE,w.version, w.WfOptName,w.wfOptTag,a.nodeinstid, nvl(a.UnitCode,nvl(w.UnitCode,'0000000')) as UnitCode,
        a.usercode,c.ROLETYPE,c.ROLECODE,'一般任务' as AUTHDESC, c.nodecode,
          c.NodeName,c.NodeType,c.OptType as NODEOPTTYPE,d.opt_id,d.Opt_Name,d.Opt_Name as MethodName,
          d.optdefurl as OptUrl,d.opt_Method,c.OptParam ,d.Opt_Desc,a.CREATETIME,a.PromiseTime,a.TIMELIMIT,
          c.OPTCODE,c.ExpireOpt,c.STAGECODE,a.lastupdateuser,a.lastupdatetime,w.inststate
from WF_NODE_INSTANCE a join WF_FLOW_INSTANCE w on (a.WFINSTID=w.WFINSTID)
           join WF_NODE c on (a.NODEID=c.NODEID)
           join f_v_wf_optdef_url_map d on (c.OPTCODE=d.OPT_CODE)
where /*c.NODETYPE<>'R' and --游离节点不会创建时实例*/
    a.NODESTATE='N' and w.INSTSTATE='N' and a.TASKASSIGNED='S'
union all
select a.WFINSTID,w.WFCODE,w.version, w.WfOptName,w.wfOptTag,a.nodeinstid, nvl(a.UnitCode,nvl(w.UnitCode,'0000000')) as UnitCode,
        b.usercode,b.ROLETYPE,b.ROLECODE,b.AUTHDESC, c.nodecode,
          c.NodeName,c.NodeType,c.OptType as NODEOPTTYPE,d.opt_id,d.Opt_Name,d.Opt_Name as MethodName,
          d.optdefurl as OptUrl,d.opt_Method,c.OptParam ,d.Opt_Desc,a.CREATETIME,a.PromiseTime,a.TIMELIMIT,
          c.OPTCODE,c.ExpireOpt,c.STAGECODE,a.lastupdateuser,a.lastupdatetime,w.inststate
from WF_NODE_INSTANCE a join WF_FLOW_INSTANCE w on (a.WFINSTID=w.WFINSTID)
           join WF_ACTION_TASK b on (a.NODEINSTID=b.NODEINSTID)
           join WF_NODE c on (a.NODEID=c.NODEID)
           join f_v_wf_optdef_url_map d on (c.OPTCODE=d.OPT_CODE)
where a.NODESTATE='N' and w.INSTSTATE='N' and a.TASKASSIGNED='T'
    and b.ISVALID='T' and  b.TASKSTATE='A' and (b.EXPIRETIME is null or b.EXPIRETIME>sysdate)
union all
select  a.WFINSTID,w.WFCODE,w.version,w.WfOptName,w.wfOptTag,a.nodeinstid, b.Unit_Code ,
         b.user_code,c.ROLETYPE,c.ROLECODE, '系统指定' as AUTHDESC, c.nodecode,
          c.NodeName,c.NodeType,c.OptType as NODEOPTTYPE,d.opt_id,d.Opt_Name,d.Opt_Name as MethodName,
          d.optdefurl as OptUrl,d.opt_Method,c.OptParam,d.Opt_Desc,a.CREATETIME,a.PromiseTime,a.timelimit,
           c.OPTCODE,c.ExpireOpt,c.STAGECODE,a.lastupdateuser,a.lastupdatetime,w.inststate
from WF_NODE_INSTANCE a join WF_FLOW_INSTANCE w on (a.WFINSTID=w.WFINSTID)
       join WF_NODE c on (a.NODEID=c.NODEID)
       join f_v_wf_optdef_url_map d on (c.OPTCODE=d.OPT_CODE) , F_USERUNIT b
where a.NODESTATE='N' and w.INSTSTATE='N'  and a.TASKASSIGNED='D' and
        (a.UNITCODE is null or a.UNITCODE=b.UNIT_CODE) and
       (   (c.ROLETYPE='gw' and c.ROLECODE=b.User_Station) or
           (c.ROLETYPE='xz' and c.ROLECODE=b.User_Rank) );
           
create or replace view user_work as
select  WFINSTID,WFCODE,version,WfOptName,wfOptTag,nodeinstid, UnitCode ,
         usercode,ROLETYPE,ROLECODE, AUTHDESC, nodecode,
          NodeName,NodeType,NODEOPTTYPE,opt_id,Opt_Name,MethodName,
         OptUrl,opt_Method,OptParam,Opt_Desc,CREATETIME,PromiseTime,timelimit,
           OPTCODE,ExpireOpt,STAGECODE,lastupdateuser,lastupdatetime,inststate
           from V_INNER_USER_TASK_LIST_FIN
           union all
            select WFINSTID,WFCODE,version,WfOptName,wfOptTag,nodeinstid, UnitCode ,
         usercode,ROLETYPE,ROLECODE, AUTHDESC, nodecode,
          NodeName,NodeType,NODEOPTTYPE,opt_id,Opt_Name,MethodName,
         OptUrl,opt_Method,OptParam,Opt_Desc,CREATETIME,PromiseTime,timelimit,
           OPTCODE,ExpireOpt,STAGECODE,lastupdateuser,lastupdatetime,inststate
           from V_INNER_USER_TASK_LIST;

create or replace view v_hi_optinfo as
select connect_by_root  t.opt_id  as topoptid, level as hi_level , t.opt_id , t.opt_name
from f_optinfo t
connect by prior t.opt_id = t.pre_opt_id;

create or replace view v_node_instdetail as
select f.wfoptname,f.wfopttag,n.nodename,n.roletype,n.rolecode,
d.Opt_Name,d.Opt_Name as MethodName,d.OptDefUrl as OptUrl,d.opt_Method,n.optparam,
 t.NODEINSTID, t.WFINSTID, t.NODEID, t.CREATETIME, t.PREVNODEINSTID, t.NODESTATE,
 t.SUBWFINSTID, t.UNITCODE, t.TRANSID, t.TASKASSIGNED,
 t.RUNTOKEN, t.TIMELIMIT, t.LASTUPDATEUSER, t.LASTUPDATETIME, t.ISTIMER, t.PROMISETIME, n.STAGECODE
  from wf_node_instance t
join wf_node n on t.nodeid =  n.nodeid
join f_v_wf_optdef_url_map d on (n.OPTCODE=d.OPT_CODE)
join wf_flow_instance f on t.wfinstid = f.wfinstid
with read only;

create or replace view v_user_task_list as
select rownum as taskid,t.WFINSTID,t.WFCODE,t.version,t.WFOPTNAME as WFNAME, t.WFOPTNAME,t.WFOPTTAG,t.NODEINSTID,t.UNITCODE,t.USERCODE,
       t.ROLETYPE,t.ROLECODE,t.AUTHDESC,t.nodecode, t.NODENAME,t.NODETYPE,t.NODEOPTTYPE,t.OPT_ID,t.OPT_NAME,
       t.METHODNAME,t.OPTURL,t.OPT_METHOD,t.OPTPARAM,t.OPT_DESC,t.CREATETIME,t.PROMISETIME,
       t.TIMELIMIT,t.OPTCODE,t.EXPIREOPT,t.STAGECODE,t.GRANTOR,t.LASTUPDATEUSER,t.LASTUPDATETIME ,t.inststate
from
   (select a.WFINSTID,a.WFCODE,a.version, a.WfOptName, a.wfOptTag, a.nodeinstid, a.UnitCode, a.usercode, a.ROLETYPE, a.ROLECODE,
     a.AUTHDESC,a.nodecode, a.NodeName, a.NodeType, a.NODEOPTTYPE, a.opt_id, a.Opt_Name, a.MethodName, a.OptUrl, a.opt_Method,
      a.OptParam, a.Opt_Desc, a.CREATETIME, a.promisetime, a.timelimit,  a.OPTCODE, a.ExpireOpt, a.STAGECODE,
      null as GRANTOR, a.lastupdateuser, a.lastupdatetime ,  a.inststate
  from V_INNER_USER_TASK_LIST a
  union select a.WFINSTID,a.WFCODE,a.version, a.WfOptName, a.wfOptTag, a.nodeinstid, a.UnitCode, b.grantee as usercode, a.ROLETYPE, a.ROLECODE,
    a.AUTHDESC,a.nodecode, a.NodeName, a.NodeType, a.NODEOPTTYPE, a.opt_id, a.Opt_Name, a.MethodName, a.OptUrl, a.opt_Method,
    a.OptParam, a.Opt_Desc, a.CREATETIME, a.promisetime, a.timelimit, a.OPTCODE, a.ExpireOpt, a.STAGECODE,
    b.GRANTOR, a.lastupdateuser, a.lastupdatetime ,  a.inststate
    from V_INNER_USER_TASK_LIST a, WF_ROLE_RELEGATE b
    where b.IsValid = 'T' and b.RELEGATETIME <= sysdate and
          ( b.EXPIRETIME is null or b.EXPIRETIME >= sysdate) and
          a.usercode = b.GRANTOR and ( b.UNITCODE is null or b.UNITCODE = a.UnitCode)
          and ( b.ROLETYPE is null or ( b.ROLETYPE = a.ROLETYPE and ( b.ROLECODE is null or b.ROLECODE = a.ROLECODE) ) ))
      t;
