insert into C_FORM_DEF (ID, OPT_FORM_ID, OPT_FORM_URL, IS_EDIT, OPT_SUBMIT_URL)
values ('12', 'vacationForm', 'http://localhost:8080/apprSingle/apprDemo/vacationDemo/vacationDemoEmbed.html', 'N', '/apprDemo/workflow/optDemo/test');
insert into C_FORM_DEF (ID, OPT_FORM_ID, OPT_FORM_URL, IS_EDIT, OPT_SUBMIT_URL)
values ('15', 'taskForm', 'http://localhost:8080/apprSingle/apprDemo/taskDemo/taskDemoEmbed.html', 'N', '/apprDemo/workflow/optDemo/test');
insert into C_FORM_DEF (ID, OPT_FORM_ID, OPT_FORM_URL, IS_EDIT, OPT_SUBMIT_URL)
values ('33', 'fnksview', 'http://localhost:8080/apprSingle/workflow/req/offer/getReqByNo', 'N', null);
insert into C_FORM_DEF (ID, OPT_FORM_ID, OPT_FORM_URL, IS_EDIT, OPT_SUBMIT_URL)
values ('34', 'offerReqForm', 'http://localhost:8080/apprSingle/workflow/req/offer/editReqByNo', 'Y', 'http://localhost:8080/apprSingle/workflow/req/offer/updateOfferReq');
insert into C_FORM_DEF (ID, OPT_FORM_ID, OPT_FORM_URL, IS_EDIT, OPT_SUBMIT_URL)
values ('5', 'incomedoc', 'http://localhost:8080/doctrans/modules/doc/incomedoc/incomedocOutview.html', 'N', '/apprDemo/workflow/optDemo/test');
insert into C_FORM_DEF (ID, OPT_FORM_ID, OPT_FORM_URL, IS_EDIT, OPT_SUBMIT_URL)
values ('1', 'demo', 'http://localhost:8080/apprSingle/modules/demo/demo.jsp', 'N', null);


insert into C_GENERAL_MODULE_PARAM (MODULE_CODE, MODULE_NAME, HAS_FORM, FORM_ID, FORM_POSTION, HAS_IDEA, IDEA_LABEL, IDEA_CATALOG, OPT_FLOW_VAR_PROCESSER, NEED_FLOW_VAR, IS_OPT_VAR, IDEA_CONTENT, HAS_ORGROLE, XB_ORGROLE_CODE, XB_ORGROLE_NAME, XB_ORGROLE_FILTER, ZB_ORGROLE_CODE, ZB_ORGROLE_NAME, ZB_ORGROLE_FILTER, ASSIGN_TEAMROLE, TEAMROLE_CODE, TEAMROLE_NAME, TEAMROLE_FILTER, TEAMROLE_CHECK, ASSIGN_ENGINEROLE, ENGINEROLE_CODE, ENGINEROLE_NAME, ENGINEROLE_FILTER, HAS_ATTENTION, ATTENTION_LABEL, ATTENTION_FILTER, HAS_STUFF, HAS_DOCUMENT, DOCUMENT_LABEL, DOCUMENT_URL, CAN_DEFER, CAN_ROLLBACK, CAN_CLOSE, DOC_READONLY, QUICK_CONTENT, QUICK_CONTENT_RESULT, REMARK, LAST_UPDATE_TIME)
values ('pf', '批分', null, '5', null, 'N', '结果意见', 'ideaCode', null, 'N', 'Y', '办理意见', 'Y', 'swxbdw', '协办处室', 'D(all)', 'zbcsfb', '主办处室', 'D(all)', 'N', 'pyzr', '批阅主任', 'D(1)U(D)', null, 'N', 'swxby', '权限引擎', 'D(1)U(D)', 'N', '发送人员', 'D(1)U(D)', 'Y', 'N', null, null, null, null, null, null, 'Y', 'quickOpinion', null, null);
insert into C_GENERAL_MODULE_PARAM (MODULE_CODE, MODULE_NAME, HAS_FORM, FORM_ID, FORM_POSTION, HAS_IDEA, IDEA_LABEL, IDEA_CATALOG, OPT_FLOW_VAR_PROCESSER, NEED_FLOW_VAR, IS_OPT_VAR, IDEA_CONTENT, HAS_ORGROLE, XB_ORGROLE_CODE, XB_ORGROLE_NAME, XB_ORGROLE_FILTER, ZB_ORGROLE_CODE, ZB_ORGROLE_NAME, ZB_ORGROLE_FILTER, ASSIGN_TEAMROLE, TEAMROLE_CODE, TEAMROLE_NAME, TEAMROLE_FILTER, TEAMROLE_CHECK, ASSIGN_ENGINEROLE, ENGINEROLE_CODE, ENGINEROLE_NAME, ENGINEROLE_FILTER, HAS_ATTENTION, ATTENTION_LABEL, ATTENTION_FILTER, HAS_STUFF, HAS_DOCUMENT, DOCUMENT_LABEL, DOCUMENT_URL, CAN_DEFER, CAN_ROLLBACK, CAN_CLOSE, DOC_READONLY, QUICK_CONTENT, QUICK_CONTENT_RESULT, REMARK, LAST_UPDATE_TIME)
values ('sp2', '市场部审批', null, '34', null, 'Y', '1', '1', null, 'N', 'N', '审批意见', 'N', null, null, null, null, null, null, 'N', 'tech', '转办人', null, null, 'Y', 'swxby', '转办人', 'D(all)U(D)', 'N', null, null, 'N', 'N', null, null, null, null, null, null, 'N', null, null, null);
insert into C_GENERAL_MODULE_PARAM (MODULE_CODE, MODULE_NAME, HAS_FORM, FORM_ID, FORM_POSTION, HAS_IDEA, IDEA_LABEL, IDEA_CATALOG, OPT_FLOW_VAR_PROCESSER, NEED_FLOW_VAR, IS_OPT_VAR, IDEA_CONTENT, HAS_ORGROLE, XB_ORGROLE_CODE, XB_ORGROLE_NAME, XB_ORGROLE_FILTER, ZB_ORGROLE_CODE, ZB_ORGROLE_NAME, ZB_ORGROLE_FILTER, ASSIGN_TEAMROLE, TEAMROLE_CODE, TEAMROLE_NAME, TEAMROLE_FILTER, TEAMROLE_CHECK, ASSIGN_ENGINEROLE, ENGINEROLE_CODE, ENGINEROLE_NAME, ENGINEROLE_FILTER, HAS_ATTENTION, ATTENTION_LABEL, ATTENTION_FILTER, HAS_STUFF, HAS_DOCUMENT, DOCUMENT_LABEL, DOCUMENT_URL, CAN_DEFER, CAN_ROLLBACK, CAN_CLOSE, DOC_READONLY, QUICK_CONTENT, QUICK_CONTENT_RESULT, REMARK, LAST_UPDATE_TIME)
values ('sp3', '市场部审批（转办）', null, '34', null, 'Y', '1', '1', null, 'N', 'N', '审批意见', 'N', null, null, null, null, null, null, 'N', null, null, null, null, 'Y', 'swxby', '转办人', 'D(all)U(D)', 'N', null, null, 'N', 'N', null, null, null, null, null, null, 'N', null, null, null);
insert into C_GENERAL_MODULE_PARAM (MODULE_CODE, MODULE_NAME, HAS_FORM, FORM_ID, FORM_POSTION, HAS_IDEA, IDEA_LABEL, IDEA_CATALOG, OPT_FLOW_VAR_PROCESSER, NEED_FLOW_VAR, IS_OPT_VAR, IDEA_CONTENT, HAS_ORGROLE, XB_ORGROLE_CODE, XB_ORGROLE_NAME, XB_ORGROLE_FILTER, ZB_ORGROLE_CODE, ZB_ORGROLE_NAME, ZB_ORGROLE_FILTER, ASSIGN_TEAMROLE, TEAMROLE_CODE, TEAMROLE_NAME, TEAMROLE_FILTER, TEAMROLE_CHECK, ASSIGN_ENGINEROLE, ENGINEROLE_CODE, ENGINEROLE_NAME, ENGINEROLE_FILTER, HAS_ATTENTION, ATTENTION_LABEL, ATTENTION_FILTER, HAS_STUFF, HAS_DOCUMENT, DOCUMENT_LABEL, DOCUMENT_URL, CAN_DEFER, CAN_ROLLBACK, CAN_CLOSE, DOC_READONLY, QUICK_CONTENT, QUICK_CONTENT_RESULT, REMARK, LAST_UPDATE_TIME)
values ('sq2', '经办人查看结果', null, '33', null, 'Y', '1', '1', null, 'N', 'N', '汇总意见', 'N', null, null, null, null, null, null, 'N', null, null, null, null, 'N', null, null, null, 'N', null, null, 'N', 'N', null, null, null, null, null, null, 'N', null, null, null);
insert into C_GENERAL_MODULE_PARAM (MODULE_CODE, MODULE_NAME, HAS_FORM, FORM_ID, FORM_POSTION, HAS_IDEA, IDEA_LABEL, IDEA_CATALOG, OPT_FLOW_VAR_PROCESSER, NEED_FLOW_VAR, IS_OPT_VAR, IDEA_CONTENT, HAS_ORGROLE, XB_ORGROLE_CODE, XB_ORGROLE_NAME, XB_ORGROLE_FILTER, ZB_ORGROLE_CODE, ZB_ORGROLE_NAME, ZB_ORGROLE_FILTER, ASSIGN_TEAMROLE, TEAMROLE_CODE, TEAMROLE_NAME, TEAMROLE_FILTER, TEAMROLE_CHECK, ASSIGN_ENGINEROLE, ENGINEROLE_CODE, ENGINEROLE_NAME, ENGINEROLE_FILTER, HAS_ATTENTION, ATTENTION_LABEL, ATTENTION_FILTER, HAS_STUFF, HAS_DOCUMENT, DOCUMENT_LABEL, DOCUMENT_URL, CAN_DEFER, CAN_ROLLBACK, CAN_CLOSE, DOC_READONLY, QUICK_CONTENT, QUICK_CONTENT_RESULT, REMARK, LAST_UPDATE_TIME)
values ('xbfb', '协办分办', null, '5', null, 'N', '办理结果', 'ideaCode', null, 'N', 'N', '意见', 'N', null, null, null, null, null, null, 'N', null, null, null, null, 'Y', 'swxby', '办理人员', 'D(U)', 'N', null, null, 'N', 'N', null, null, null, null, null, null, 'N', null, null, null);
insert into C_GENERAL_MODULE_PARAM (MODULE_CODE, MODULE_NAME, HAS_FORM, FORM_ID, FORM_POSTION, HAS_IDEA, IDEA_LABEL, IDEA_CATALOG, OPT_FLOW_VAR_PROCESSER, NEED_FLOW_VAR, IS_OPT_VAR, IDEA_CONTENT, HAS_ORGROLE, XB_ORGROLE_CODE, XB_ORGROLE_NAME, XB_ORGROLE_FILTER, ZB_ORGROLE_CODE, ZB_ORGROLE_NAME, ZB_ORGROLE_FILTER, ASSIGN_TEAMROLE, TEAMROLE_CODE, TEAMROLE_NAME, TEAMROLE_FILTER, TEAMROLE_CHECK, ASSIGN_ENGINEROLE, ENGINEROLE_CODE, ENGINEROLE_NAME, ENGINEROLE_FILTER, HAS_ATTENTION, ATTENTION_LABEL, ATTENTION_FILTER, HAS_STUFF, HAS_DOCUMENT, DOCUMENT_LABEL, DOCUMENT_URL, CAN_DEFER, CAN_ROLLBACK, CAN_CLOSE, DOC_READONLY, QUICK_CONTENT, QUICK_CONTENT_RESULT, REMARK, LAST_UPDATE_TIME)
values ('zbfb', '主办分办', null, '5', null, null, '办理结果', 'ideaCode', null, null, null, null, 'N', null, null, null, null, null, null, 'Y', 'jbr', '承办人', 'D(U)', null, 'N', null, null, null, 'N', null, null, 'N', 'N', null, null, null, null, null, null, 'N', null, null, null);
insert into C_GENERAL_MODULE_PARAM (MODULE_CODE, MODULE_NAME, HAS_FORM, FORM_ID, FORM_POSTION, HAS_IDEA, IDEA_LABEL, IDEA_CATALOG, OPT_FLOW_VAR_PROCESSER, NEED_FLOW_VAR, IS_OPT_VAR, IDEA_CONTENT, HAS_ORGROLE, XB_ORGROLE_CODE, XB_ORGROLE_NAME, XB_ORGROLE_FILTER, ZB_ORGROLE_CODE, ZB_ORGROLE_NAME, ZB_ORGROLE_FILTER, ASSIGN_TEAMROLE, TEAMROLE_CODE, TEAMROLE_NAME, TEAMROLE_FILTER, TEAMROLE_CHECK, ASSIGN_ENGINEROLE, ENGINEROLE_CODE, ENGINEROLE_NAME, ENGINEROLE_FILTER, HAS_ATTENTION, ATTENTION_LABEL, ATTENTION_FILTER, HAS_STUFF, HAS_DOCUMENT, DOCUMENT_LABEL, DOCUMENT_URL, CAN_DEFER, CAN_ROLLBACK, CAN_CLOSE, DOC_READONLY, QUICK_CONTENT, QUICK_CONTENT_RESULT, REMARK, LAST_UPDATE_TIME)
values ('sp1', '主管审核', null, '33', null, 'N', '审批结果22', 'YesOrNo', 'http://localhost:8080/apprSingle/workflow/appr/acceptFlowFormData', 'Y', 'N', '审批意见22', 'Y', 'xbcs', '协办', 'D(all)', null, null, null, 'N', 'jbr', '办理人', 'D(U+1)', null, 'N', 'swxby', '权限人', 'D(U+1)', 'N', null, null, 'N', 'N', null, null, 'Y', 'Y', null, null, 'N', null, null, null);
insert into C_GENERAL_MODULE_PARAM (MODULE_CODE, MODULE_NAME, HAS_FORM, FORM_ID, FORM_POSTION, HAS_IDEA, IDEA_LABEL, IDEA_CATALOG, OPT_FLOW_VAR_PROCESSER, NEED_FLOW_VAR, IS_OPT_VAR, IDEA_CONTENT, HAS_ORGROLE, XB_ORGROLE_CODE, XB_ORGROLE_NAME, XB_ORGROLE_FILTER, ZB_ORGROLE_CODE, ZB_ORGROLE_NAME, ZB_ORGROLE_FILTER, ASSIGN_TEAMROLE, TEAMROLE_CODE, TEAMROLE_NAME, TEAMROLE_FILTER, TEAMROLE_CHECK, ASSIGN_ENGINEROLE, ENGINEROLE_CODE, ENGINEROLE_NAME, ENGINEROLE_FILTER, HAS_ATTENTION, ATTENTION_LABEL, ATTENTION_FILTER, HAS_STUFF, HAS_DOCUMENT, DOCUMENT_LABEL, DOCUMENT_URL, CAN_DEFER, CAN_ROLLBACK, CAN_CLOSE, DOC_READONLY, QUICK_CONTENT, QUICK_CONTENT_RESULT, REMARK, LAST_UPDATE_TIME)
values ('sq', '销售员申请', null, '33', null, 'N', '通过', 'ideacode', null, 'N', 'N', '申请描述', 'N', 'swxbdw', '协办处室', 'D(U-1+1)', 'zbcsfb', '主办处室', 'D(U-1+1)', 'Y', 'jbr', '办件角色', 'D(U)xz(''DM'')', null, 'N', null, null, null, 'N', null, null, 'N', 'N', null, null, null, 'Y', null, null, 'N', null, null, null);
insert into C_GENERAL_MODULE_PARAM (MODULE_CODE, MODULE_NAME, HAS_FORM, FORM_ID, FORM_POSTION, HAS_IDEA, IDEA_LABEL, IDEA_CATALOG, OPT_FLOW_VAR_PROCESSER, NEED_FLOW_VAR, IS_OPT_VAR, IDEA_CONTENT, HAS_ORGROLE, XB_ORGROLE_CODE, XB_ORGROLE_NAME, XB_ORGROLE_FILTER, ZB_ORGROLE_CODE, ZB_ORGROLE_NAME, ZB_ORGROLE_FILTER, ASSIGN_TEAMROLE, TEAMROLE_CODE, TEAMROLE_NAME, TEAMROLE_FILTER, TEAMROLE_CHECK, ASSIGN_ENGINEROLE, ENGINEROLE_CODE, ENGINEROLE_NAME, ENGINEROLE_FILTER, HAS_ATTENTION, ATTENTION_LABEL, ATTENTION_FILTER, HAS_STUFF, HAS_DOCUMENT, DOCUMENT_LABEL, DOCUMENT_URL, CAN_DEFER, CAN_ROLLBACK, CAN_CLOSE, DOC_READONLY, QUICK_CONTENT, QUICK_CONTENT_RESULT, REMARK, LAST_UPDATE_TIME)
values ('demo', 'demo', null, '1', null, 'N', '决定', 'ideacode', null, 'N', 'N', '意见', 'Y', 'xbcs', '协办处室', 'D(all)', null, null, null, 'N', null, null, null, null, 'N', null, null, null, 'N', null, null, 'N', 'N', null, null, null, null, null, null, 'N', null, null, null);
insert into C_GENERAL_MODULE_PARAM (MODULE_CODE, MODULE_NAME, HAS_FORM, FORM_ID, FORM_POSTION, HAS_IDEA, IDEA_LABEL, IDEA_CATALOG, OPT_FLOW_VAR_PROCESSER, NEED_FLOW_VAR, IS_OPT_VAR, IDEA_CONTENT, HAS_ORGROLE, XB_ORGROLE_CODE, XB_ORGROLE_NAME, XB_ORGROLE_FILTER, ZB_ORGROLE_CODE, ZB_ORGROLE_NAME, ZB_ORGROLE_FILTER, ASSIGN_TEAMROLE, TEAMROLE_CODE, TEAMROLE_NAME, TEAMROLE_FILTER, TEAMROLE_CHECK, ASSIGN_ENGINEROLE, ENGINEROLE_CODE, ENGINEROLE_NAME, ENGINEROLE_FILTER, HAS_ATTENTION, ATTENTION_LABEL, ATTENTION_FILTER, HAS_STUFF, HAS_DOCUMENT, DOCUMENT_LABEL, DOCUMENT_URL, CAN_DEFER, CAN_ROLLBACK, CAN_CLOSE, DOC_READONLY, QUICK_CONTENT, QUICK_CONTENT_RESULT, REMARK, LAST_UPDATE_TIME)
values ('dj', '登记', null, '1', null, 'N', '办理结果', 'ideaCode', null, 'N', 'N', '办理意见', 'N', null, null, null, null, null, null, 'N', null, null, null, null, 'N', null, null, null, 'N', null, null, 'N', 'N', null, null, null, null, null, null, 'N', null, null, null);
insert into c_general_module_param (MODULE_CODE, MODULE_NAME, HAS_FORM, FORM_ID, FORM_POSTION, HAS_IDEA, IDEA_LABEL, IDEA_CATALOG, OPT_FLOW_VAR_PROCESSER, NEED_FLOW_VAR, IS_OPT_VAR, IDEA_CONTENT, HAS_ORGROLE, XB_ORGROLE_CODE, XB_ORGROLE_NAME, XB_ORGROLE_FILTER, ZB_ORGROLE_CODE, ZB_ORGROLE_NAME, ZB_ORGROLE_FILTER, ASSIGN_TEAMROLE, TEAMROLE_CODE, TEAMROLE_NAME, TEAMROLE_FILTER, TEAMROLE_CHECK, ASSIGN_ENGINEROLE, ENGINEROLE_CODE, ENGINEROLE_NAME, ENGINEROLE_FILTER, HAS_ATTENTION, ATTENTION_LABEL, ATTENTION_FILTER, HAS_STUFF, HAS_DOCUMENT, DOCUMENT_LABEL, DOCUMENT_URL, CAN_DEFER, CAN_ROLLBACK, CAN_CLOSE, DOC_READONLY, QUICK_CONTENT, QUICK_CONTENT_RESULT, REMARK, LAST_UPDATE_TIME, TEMPLATE_LIST, HAS_TEMPLATE, COULD_EDIT, SEND_SMS)
values ('preDemo', '公安demo', '', '2', '', 'N', '办理决定', 'ideacode', '', 'N', 'N', '办理意见', 'N', '', '', '', '', '', '', 'Y', 'jbr', '发送', 'D(U)', '', 'N', '', '', '', 'N', '', '', 'N', 'N', '', '', '', '', '', '', 'N', '', '', null, '', 'N', 'N', 'N');


insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE, CREATOR, UPDATOR)
values ('zbOrgRole', '流程主办处室字典', 'U', 'L', null, '{"dataCode":{"value":"编码","isUse":"T"},"dataValue":{"value":"数值","isUse":"T"},"extraCode":{"value":"扩展编码","isUse":"F"},"extraCode2":{"value":"扩展编码2","isUse":"F"},"dataTag":{"value":"数据标记","isUse":"F"},"dataDesc":{"value":"数据描述","isUse":"T"}}', to_date('25-10-2018 14:28:44', 'dd-mm-yyyy hh24:mi:ss'), null, null, '1', null, null);
insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE, CREATOR, UPDATOR)
values ('xbOrgRole', '协办部门字典', 'U', 'L', null, '{"dataCode":{"value":"编码","isUse":"T"},"dataValue":{"value":"数值","isUse":"T"},"extraCode":{"value":"扩展编码","isUse":"F"},"extraCode2":{"value":"扩展编码2","isUse":"F"},"dataTag":{"value":"数据标记","isUse":"F"},"dataDesc":{"value":"数据描述","isUse":"T"}}', to_date('25-10-2018 14:29:09', 'dd-mm-yyyy hh24:mi:ss'), null, null, '1', null, null);
insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE, CREATOR, UPDATOR)
values ('ptbmxx', '业务系统编码', 'U', 'L', null, '{"dataCode":{"value":"编码","isUse":"T"},"dataValue":{"value":"数值","isUse":"T"},"extraCode":{"value":"扩展编码","isUse":"F"},"extraCode2":{"value":"扩展编码2","isUse":"F"},"dataTag":{"value":"数据标记","isUse":"F"},"dataDesc":{"value":"数据描述","isUse":"T"}}', to_date('29-10-2018 14:46:47', 'dd-mm-yyyy hh24:mi:ss'), null, null, '1', null, null);
insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE, CREATOR, UPDATOR)
values ('WFExpireOpt', '流程预期处理方法', 'S', 'L', 'N：通知， O:不处理 ， S：挂起，E：终止', '流程预期处理方法', null, null, null, '1', null, null);
insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE, CREATOR, UPDATOR)
values ('FlowUnitRole', '机构表达式', 'U', 'L', '用户流程机构', null, null, null, null, null, null, null);
insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE, CREATOR, UPDATOR)
values ('LIMIT_TYPE', '流程期限类别', 'S', 'L', '期限类别 I ： 未设置（ignore 默认 ）、N 无 (无期限 none ) 、 F 每实例固定期限 fix 、C 节点固定期限  cycle、H 继承上一个节点剩余时间 hierarchical。', '流程预期类别', null, null, null, '1', null, null);
insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE, CREATOR, UPDATOR)
values ('WfActionType', '流程活动类型', 'S', 'L', '创建流程同时创建首节点  W 创建节点 C 更改数据 U 提交节点 S 挂起节点 A 唤醒节点 R  终止节点 E  唤醒超时节点 X', '流程活动类型', null, null, null, null, null, null);
insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE, CREATOR, UPDATOR)
values ('WFFlowState', '流程状态', 'S', 'L', 'A 草稿  E 已发布 (A,E仅对0版本有效) B 正常 C 过期 D 禁用', '编码,数值', null, null, null, '1', null, null);
insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE, CREATOR, UPDATOR)
values ('WFInstType', '流程、节点实例状态', 'S', 'L', 'N 正常/ R 运行(保留)/ C 完成/ S 挂起  / X 超时挂起eXpire / D 强制提交  /B 已回退  /E 因为流程完成而结束 / F 因为流程强制结束而被强制结束 / W 等待子流程返回  / I 失效\r\n ', '流程实例状态', null, null, null, '1', null, null);
insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE, CREATOR, UPDATOR)
values ('WFRoleType', '流程角色类别', 'S', 'L', '流程角色类别。一共四种 en：引擎，bj：办件 gw：岗位，xz：行政', null, null, null, null, null, null, null);
insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE, CREATOR, UPDATOR)
values ('ideaCode', '通用审批', 'U', 'L', null, '{dataCode:{value:编码,isUse:T},dataValue:{value:数值,isUse:T},extraCode:{value:扩展编码,isUse:F},extraCode2:{value:扩展编码2,isUse:F},dataTag:{value:数据标记,isUse:F},dataDesc:{value:数据描述,isUse:T}}', to_date('29-06-2018 10:49:29', 'dd-mm-yyyy hh24:mi:ss'), null, null, '1', null, null);
insert into F_DATACATALOG (CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE, CREATOR, UPDATOR)
values ('ideacode', '结果代码', 'U', 'L', null, '{dataCode:{value:编码,isUse:T},dataValue:{value:数值,isUse:T},extraCode:{value:扩展编码,isUse:F},extraCode2:{value:扩展编码2,isUse:F},dataTag:{value:数据标记,isUse:F},dataDesc:{value:数据描述,isUse:T}}', to_date('26-06-2018 16:48:48', 'dd-mm-yyyy hh24:mi:ss'), null, 'WORKFLOW', '1', null, null);


insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('FlowUserRole', 'jbr', null, null, 'T', '经办人', 'U', null, null, null, 1);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('zbOrgRole', 'zbcs', null, null, 'T', '主办处室', 'U', null, null, null, 1);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('xbOrgRole', 'xbcs', null, null, 'T', '协办处室', 'U', null, null, null, 1);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('ideacode', 'rollback', null, null, 'T', '退回', 'U', null, null, null, 3);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('ptbmxx', 'LOCAL', null, null, 'T', '市局公安系统', 'U', '南京市局情报系统', null, null, 1);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('FlowUnitRole', 'unitexp1', null, null, 'T', '机构表达式1', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('LIMIT_TYPE', 'C', null, null, 'T', '节点固定期限  cycle', null, null, null, null, 4);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('LIMIT_TYPE', 'F', null, null, 'T', '每实例固定期限 fix', null, null, null, null, 3);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('LIMIT_TYPE', 'H', null, null, 'T', '继承上一个节点剩余时间', null, null, null, null, 5);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('LIMIT_TYPE', 'I', null, null, 'T', '未设置（ignore 默认 ）', null, null, null, null, 1);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('LIMIT_TYPE', 'N', null, null, 'T', '无 (无期限 none )', null, null, null, null, 2);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WfActionType', 'A', null, null, 'T', '挂起节点', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WfActionType', 'C', null, null, 'T', '创建节点', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WfActionType', 'E', null, null, 'T', '终止节点', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WfActionType', 'R', null, null, 'T', '唤醒节点', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WfActionType', 'S', null, null, 'T', '提交节点', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WfActionType', 'U', null, null, 'T', '更改数据', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WfActionType', 'W', null, null, 'T', '创建首节点', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WfActionType', 'X', null, null, 'T', '唤醒超时节点', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFFlowState', 'A', null, null, null, '草稿', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFFlowState', 'B', null, null, null, '正常', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFFlowState', 'C', null, null, null, '过期', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFFlowState', 'D', null, null, null, '禁用', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFFlowState', 'E', null, null, null, '已发布', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFInstType', 'B', null, null, 'T', '已退回', null, '流程实例状态', null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFInstType', 'C', null, null, 'T', '完成', null, '流程实例状态', null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFInstType', 'F', null, null, 'T', '被强制结束', null, ' 因为流程强制结束而被强制结束', null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFInstType', 'N', null, null, 'T', '正常', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFInstType', 'P', null, null, null, '暂停', null, '流程实例状态', null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFInstType', 'S', null, null, 'T', '等待前置节点完成', null, '流程实例状态', null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFInstType', 'W', null, null, null, '等待子流程返回', null, '流程实例状态', null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFRoleType', 'bj', null, null, 'T', '办件角色', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFRoleType', 'en', null, null, 'T', '权限引擎', null, 'en：引擎', null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFRoleType', 'gw', null, null, 'T', '岗位角色', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFRoleType', 'xz', null, null, 'T', '行政角色', null, null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFExpireOpt', 'E', null, null, 'T', '终止', null, null, null, null, 4);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFExpireOpt', 'N', null, null, 'T', '通知', null, null, null, null, 1);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFExpireOpt', 'O', null, null, 'T', '不处理', null, null, null, null, 2);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('WFExpireOpt', 'S', null, null, 'T', '挂起', null, null, null, null, 3);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'sqmj', null, null, 'T', '社区民警', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'pcssz', null, null, 'T', '派出所所长', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'znddz', null, null, 'T', '职能大队队长', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'fjjz', null, null, 'T', '分局局长', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'jzzdz', null, null, 'T', '技侦支队支队长', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'sjjz', null, null, 'T', '市局局长', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'wafzdz', null, null, 'T', '网安法制大队长', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'wazdz', null, null, 'T', '网安支队支队长', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'waajddz', null, null, 'T', '网安案件大队大队长', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', 'fjzhs', null, null, 'T', '分局指挥室', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '769231', '6', null, 'T', '网安案件大队', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '751366', '7', null, 'T', '人口大队', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '769208', '8', null, 'T', '网安法制大队', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '769215', '4', null, 'T', '网安支队', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '19707', '1', null, 'T', '市局领导', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '19718', '3', null, 'T', '业务支队领导', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '19744', '2', null, 'T', '分局领导', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '19824', '5', null, 'T', '业务大队领导', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '20082', '9', null, 'T', '派出所领导', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '20277', '10', null, 'T', '民警', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '597562', '11', null, 'T', '群体授权审批员', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '5669', '3', null, 'T', '业务支队领导', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '5782', '1', null, 'T', '分局领导', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '577464', '2', null, 'T', '群体管理员', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '758016', '3', null, 'T', '市局领导审批', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '625205', '4', null, 'T', '高级用户', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '625381', '5', null, 'T', '普通用户', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '53828', '6', null, 'T', '指挥中心', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '55764', '7', null, 'T', '社区民警', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '55687', '8', null, 'T', '内勤', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '55717', '9', null, 'T', '执法勤务', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '57144', '10', null, 'T', '网安', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '57132', '11', null, 'T', '技侦', 'S', null, null, null, null);




insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '55520', '12', null, 'T', '所领导', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '55795', '13', null, 'T', '巡警', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '72515', '14', null, 'T', '指挥中心副主任', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '72033', '15', null, 'T', '人口支队', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '734608', '16', null, 'T', '社区所长', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('StationType', '209466', '17', null, 'T', '查看所有重点人', 'S', null, null, null, null);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('ideaCode', 'T', null, null, 'T', '同意', 'U', null, null, null, 1);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('ideaCode', 'F', null, null, 'T', '驳回', 'U', null, null, null, 2);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('ideacode', 'T', null, null, 'T', '批准', 'S', null, null, null, 1);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('ideacode', 'F', null, null, 'T', '不批准', 'S', null, null, null, 2);
insert into F_DATADICTIONARY (CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER)
values ('ideaCode', 'S', null, null, 'T', '测试', 'U', null, null, null, 3);



insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('41', 'WORKYW', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000260', 'LCXWSL', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10001', 'WORKFLOW', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10002', 'WFDEFINE', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10003', 'FLOWMGR', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10004', 'USERFLOW', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000216', 'bx_lc', '通用审批', '11', 'apprSingle/approval/approval.html', null, null, null, null, null, 'R', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000220', 'DTDB', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('1000000', 'OPTMNG', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000200', 'BJRETRIEVE', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000201', 'PFRESTART', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000205', 'ATTENTIONS', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000206', 'TJLB', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000207', 'CSCD', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000208', 'GENERALMODULE', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000209', 'FORMDEF', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000214', 'OS_INFO', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000240', 'GLTEST', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000202', 'TASKDELEGATE', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000203', 'MYTASKS', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000204', 'USERTASKS', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000210', 'MODULETEST', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000211', 'apprDemo', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000212', 'taskDemo', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('10000213', 'vacationDemo', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('4', 'LIUCC', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);
insert into F_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, OPT_URL, OPT_DESC, OPT_ORDER, IS_IN_WORKFLOW, UPDATE_DATE, CREATE_DATE, OPT_REQ, CREATOR, UPDATOR)
values ('22', 'DLYM', '查看', 'search', '/changeme', '查看（系统默认）', null, null, null, null, 'CRUD', null, null);



insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('WORKYW', '流程业务定义', 'WORKFLOW', 'modules/wfopt/wfOpt.html', '...', null, 'O', null, null, 'Y', null, null, 0, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('FLOWMGR', '流程实例管理', 'WORKFLOW', 'modules/sys/flowmanager/flowmanager.html', 'modules/sys/flowmanager/flowmanager.html', null, 'O', null, null, 'Y', null, null, 2, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('USERFLOW', '用户流程任务管理', 'MYTASKS', 'modules/sys/useroptmgr/useroptmanager.html', 'modules/sys/useroptmgr/useroptmanager.html', null, 'O', null, null, 'Y', null, null, 3, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('WFDEFINE', '流程定义', 'WORKFLOW', 'modules/sys/flowdefine/flowdefine.html', 'modules/sys/flowdefine/flowdefine.html', null, 'O', null, null, 'Y', null, null, 1, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('WORKFLOW', '工作流', '0', '...', '...', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('DTDB', '动态待办', 'MYTASKS', 'flowExtend/userTask/userDynamicTasks.html', 'flowExtend/userTask/userDynamicTasks.html', null, 'O', null, null, 'Y', null, null, 1, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('OPTMNG', '业务管理', '0', '...', '...', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('BJRETRIEVE', '办件回收', 'OPTMNG', 'flowExtend/bjRetrieve/bjRetrieveList.html', 'flowExtend/bjRetrieve/bjRetrieveList.html', null, 'O', null, null, 'Y', null, null, 1, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('PFRESTART', '重新批分', 'OPTMNG', 'flowExtend/pfRestart/pfRestartList.html', 'flowExtend/pfRestart/pfRestartList.html', null, 'O', null, null, 'Y', null, null, 2, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('ATTENTIONS', '我的关注', 'MYTASKS', 'flowExtend/attention/attentionList.html', 'flowExtend/attention/attentionList.html', null, 'O', null, null, 'Y', null, null, 2, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('TJLB', '特价预申请查看', 'MYTASKS', 'modules/offerReq/offReqList.jsp', 'modules/offerReq/offerReqList.jsp', null, 'O', null, null, 'Y', null, null, 6, null, 'I', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('CSCD', '特价预申请登记', 'MYTASKS', 'modules/offerReq/offerReqForm.jsp', 'modules/offerReq/offerReqForm.jsp', null, 'O', null, null, 'Y', null, null, 99, null, 'I', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('GENERALMODULE', '通用审批模块', 'WORKFLOW', 'generalModule/generalModule.html', 'generalModule/generalModule.html', null, 'O', null, null, 'Y', null, null, 4, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('FORMDEF', '业务表单', 'WORKFLOW', 'formDef/formDef.html', 'formDef/formDef.html', null, 'O', null, null, 'Y', null, null, 5, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('TASKDELEGATE', '任务委托', 'OPTMNG', 'flowExtend/taskDelegate/taskDelegateList.html', 'flowExtend/taskDelegate/taskDelegateList.html', null, 'O', null, null, 'Y', null, null, 3, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('MYTASKS', '我的任务', '0', '...', '...', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('USERTASKS', '我的待办', 'MYTASKS', 'flowExtend/userTask/userTasks.html', 'flowExtend/userTask/userTasks.html', null, 'O', null, null, 'Y', null, null, 1, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('apprDemo', '审批流业务示例', '0', '...', '...', null, 'O', null, null, 'Y', null, null, 1, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('taskDemo', '任务分配Demo', 'apprDemo', 'appDemo/taskDemo/taskDemo.html', 'appDemo/taskDemo/taskDemo.html', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('vacationDemo', '请假Demo', 'apprDemo', 'appDemo/vacationDemo/vacationDemo.html', 'appDemo/vacationDemo/vacationDemo.html', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('LIUCC', '审批操作管理', 'OPTMNG', 'apprExam/apprExam.html', 'apprExam/apprExam.html', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, null, null, null, null);
insert into F_OPTINFO (OPT_ID, OPT_NAME, PRE_OPT_ID, OPT_ROUTE, OPT_URL, FORM_CODE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, TOP_OPT_ID, ORDER_IND, FLOW_CODE, PAGE_TYPE, ICON, HEIGHT, WIDTH, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('DLYM', '独立页面', 'OPTMNG', 'newApproval/newApproval.html', 'newApproval/newApproval.html', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, null, null, null, null);


insert into F_ROLEINFO (ROLE_CODE, ROLE_NAME, ROLE_TYPE, UNIT_CODE, IS_VALID, ROLE_DESC, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '一般角色', 'G', null, 'T', null, null, to_date('26-06-2018 11:18:17', 'dd-mm-yyyy hh24:mi:ss'), 'admin', null);



insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'ORGMAG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'UNITMAG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000001', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000002', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000059', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '150', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'USERUNIT', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000029', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000030', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000031', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'USERROLE', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000032', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000033', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000034', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'USERMAG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000070', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000071', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000072', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000073', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'DEPLOY', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'DICTSET', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000007', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000008', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000009', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000010', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '128', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'OPTINFO', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '124', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '125', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '126', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'OPTLOG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000020', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '120', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'OS_INFO', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000214', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'ROLEMAG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '166', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '167', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '168', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '8', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'MYTASKS', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'USERTASKS', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000204', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'DTDB', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000220', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'ATTENTIONS', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000205', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'TJLB', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000206', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'CSCD', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000207', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'GLTEST', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000240', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'OPTMNG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'BJRETRIEVE', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000200', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'PFRESTART', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000201', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'TASKDELEGATE', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000202', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'DLYM', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '22', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'LCXWSL', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000260', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'LIUCC', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '4', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'SYS_CONFIG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'UNITINFO', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '172', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'DICTSET_M', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000042', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000043', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000044', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000045', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '130', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'OPT_LOG_QUERY', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '1000041', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'USERINFO', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '169', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'WORKFLOW', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'WFDEFINE', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10002', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'FLOWMGR', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10003', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'USERFLOW', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10004', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'GENERALMODULE', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000208', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'FORMDEF', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000209', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'MODULETEST', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '10000210', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', 'WORKYW', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('1', '41', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '41', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'WORKYW', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10000260', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'LCXWSL', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10000240', null, null, null, null, null);

insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'GLTEST', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10002', null, to_date('26-06-2018 09:50:44', 'dd-mm-yyyy hh24:mi:ss'), to_date('26-06-2018 09:50:44', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10003', null, to_date('26-06-2018 09:50:44', 'dd-mm-yyyy hh24:mi:ss'), to_date('26-06-2018 09:50:44', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10004', null, to_date('26-06-2018 09:50:44', 'dd-mm-yyyy hh24:mi:ss'), to_date('26-06-2018 09:50:44', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10000200', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10000201', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10000202', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10000204', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10000205', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10000206', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10000207', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10000208', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10000209', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'ATTENTIONS', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'BJRETRIEVE', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'CSCD', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'DICTSET', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'DICTSET_M', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'FLOWMGR', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'FORMDEF', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'GENERALMODULE', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'LOGINCAS', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'MYTASKS', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'OPTINFO', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'OPTLOG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'OPTMNG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'OPT_LOG_QUERY', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'ORGMAG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'PFRESTART', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'ROLEMAG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'SYS_CONFIG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'TASKDELEGATE', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'TJLB', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'UNITINFO', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'UNITMAG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'USERFLOW', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'USERINFO', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'USERMAG', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'USERROLE', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'USERTASKS', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'USERUNIT', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'WFDEFINE', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '10000220', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'DTDB', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '4', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'LIUCC', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', '22', null, null, null, null, null);
insert into F_ROLEPOWER (ROLE_CODE, OPT_CODE, OPT_SCOPE_CODES, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('sysadmin', 'DLYM', null, null, null, null, null);


insert into F_UNITINFO (UNIT_CODE, PARENT_UNIT, UNIT_TYPE, IS_VALID, UNIT_TAG, UNIT_NAME, ENGLISH_NAME, DEP_NO, UNIT_DESC, UNIT_SHORT_NAME, UNIT_WORD, UNIT_GRADE, UNIT_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, UNIT_PATH, UNIT_MANAGER)
values ('D00005', 'U00001', 'A', 'T', null, '业务部门', null, null, null,  'YW', 'YW', null, 7, to_date('18-06-2018 20:19:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('18-06-2018 20:19:00', 'dd-mm-yyyy hh24:mi:ss'),  null, null, '/U00001/D00005/', null);
insert into F_UNITINFO (UNIT_CODE, PARENT_UNIT, UNIT_TYPE, IS_VALID, UNIT_TAG, UNIT_NAME, ENGLISH_NAME, DEP_NO, UNIT_DESC, UNIT_SHORT_NAME, UNIT_WORD, UNIT_GRADE, UNIT_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, UNIT_PATH, UNIT_MANAGER)
values ('AAA', 'U00001', 'I', 'T', null, '产品线AAA', null, null, null,  'AAA', 'AAA', null, 2, null, null,  null, null, '/U00001/AAA/', null);
insert into F_UNITINFO (UNIT_CODE, PARENT_UNIT, UNIT_TYPE, IS_VALID, UNIT_TAG, UNIT_NAME, ENGLISH_NAME, DEP_NO, UNIT_DESC,  UNIT_SHORT_NAME, UNIT_WORD, UNIT_GRADE, UNIT_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, UNIT_PATH, UNIT_MANAGER)
values ('BBB', 'U00001', 'O', 'T', null, '产品线BBB', null, null, '1111',  'BBB', 'BBB', null, 3, to_date('19-11-2018 11:07:02', 'dd-mm-yyyy hh24:mi:ss'), null,  null, null, '/U00001/BBB/', null);
insert into F_UNITINFO (UNIT_CODE, PARENT_UNIT, UNIT_TYPE, IS_VALID, UNIT_TAG, UNIT_NAME, ENGLISH_NAME, DEP_NO, UNIT_DESC, UNIT_SHORT_NAME, UNIT_WORD, UNIT_GRADE, UNIT_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR, UNIT_PATH, UNIT_MANAGER)
values ('CCC', 'U00001', 'A', 'T', null, '产品线CCC', null, null, null,  'CCC', 'CCC', null, 4, null, null,  null, null, '/U00001/CCC/', null);
insert into F_UNITINFO (UNIT_CODE, PARENT_UNIT, UNIT_TYPE, IS_VALID, UNIT_TAG, UNIT_NAME, ENGLISH_NAME, DEP_NO, UNIT_DESC, UNIT_SHORT_NAME, UNIT_WORD, UNIT_GRADE, UNIT_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, UNIT_PATH, UNIT_MANAGER)
values ('DDD', 'U00001', 'A', 'T', null, '产品线DDD', null, null, null,  'DDD', 'DDD', null, 5, null, null,  null, null, '/U00001/DDD/', null);
insert into F_UNITINFO (UNIT_CODE, PARENT_UNIT, UNIT_TYPE, IS_VALID, UNIT_TAG, UNIT_NAME, ENGLISH_NAME, DEP_NO, UNIT_DESC,  UNIT_SHORT_NAME, UNIT_WORD, UNIT_GRADE, UNIT_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, UNIT_PATH, UNIT_MANAGER)
values ('EEE', 'U00001', 'A', 'T', null, '产品线EEE', null, null, null,  'EEE', 'EEE', null, 6, null, null,  null, null, '/U00001/EEE/', null);
insert into F_UNITINFO (UNIT_CODE, PARENT_UNIT, UNIT_TYPE, IS_VALID, UNIT_TAG, UNIT_NAME, ENGLISH_NAME, DEP_NO, UNIT_DESC,  UNIT_SHORT_NAME, UNIT_WORD, UNIT_GRADE, UNIT_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, UNIT_PATH, UNIT_MANAGER)
values ('D00021', 'AAA', 'A', 'T', null, 'AAA-1', null, null, null,  'A1', 'A1', null, 1, to_date('29-10-2018 14:22:18', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-10-2018 14:22:18', 'dd-mm-yyyy hh24:mi:ss'),  null, null, '/U00001/AAA//D00021/', null);
insert into F_UNITINFO (UNIT_CODE, PARENT_UNIT, UNIT_TYPE, IS_VALID, UNIT_TAG, UNIT_NAME, ENGLISH_NAME, DEP_NO, UNIT_DESC,  UNIT_SHORT_NAME, UNIT_WORD, UNIT_GRADE, UNIT_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, UNIT_PATH, UNIT_MANAGER)
values ('D00022', 'D00021', 'A', 'T', null, 'AAA-1-1', null, null, null,  'A1-1', 'A11', null, 1, to_date('29-10-2018 14:24:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-10-2018 14:24:00', 'dd-mm-yyyy hh24:mi:ss'), null, null, '/U00001/AAA//D00021//D00022/', null);


insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,   REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000017', '$2a$11$z9eMN8lMcRO.PCoXeVncpOd6czW1KJqBRH1R8BaXA7hj3eKi8VC12', 'U', 'T', '210001', 'A\\B负责人', null, null, null, null, null,   null, null, null, null, null, 'BBB', null, 1, null, null,  null, null, null);
insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,   REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000018', '$2a$11$vj/g6ddn8o4bUgAETvIQEOjOqtmOss759DZZyXOIHnMut/shg.Tvq', 'U', 'T', '210002', 'CDE负责人', null, null, null, null, null,   null, null, null, null, null, 'EEE', null, 1, null, null,  null, null, null);
insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,  REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000019', '$2a$11$L2/Vof6x99kTHTaXQqkxq.kamM1Fsk8CXjUmNberDy0KQLp6yiPcm', 'U', 'T', 'sqr', '申请人', null, null, null, null, null,   null, null, null, null, null, 'D00005', null, 3, null, null,  null, null, null);
insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,  REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000020', '$2a$11$ajb5MWph0wJq5./MDTuUaOEyIANqIgI7IUbCocQdfRGmMjJV/TdNK', 'U', 'T', 'zg', '主管', null, null, null, null, null,  null, null, null, null, null, 'D00005', null, 1, null, null,  null, null, null);
insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,   REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000021', '$2a$11$pRli0u1.2Sy8nUAHrQzk7uH/SXWLazcTA5sjS1sfewqZEDBmI.4B2', 'U', 'T', 'AAA', 'A/B产品线', null, null, null, null, null,   null, null, null, null, null, 'BBB', null, 5, null, null, null, null, null);
insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,   REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000022', '$2a$11$1savm9XuBcJosXo0nTGZAeWL3/JdmsVvv8KHzU06NB1nSra3H6CYC', 'U', 'T', 'BBB', 'CDE产品线', null, null, null, null, null,   null, null, null, null, null, 'CCC', null, 5, to_date('25-10-2018 20:10:04', 'dd-mm-yyyy hh24:mi:ss'), null,  null, null, null);
insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,   REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000025', '$2a$11$qxWU8K/ztgWn4DZaqZ/Ez.S6fvPkfGqCUmNYqBq2QaITodTydkmP2', 'U', 'T', 'AAA2', 'A/B产品线2', null, null, null, null, null,   null, null, null, null, null, 'BBB', null, 6, null, null,  null, null, null);
insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,   REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000001', '$2a$11$KJZ0PZFF5SOOiPrUwlpMGOdxjk79B60WiGDMoWwk4zxvVgEMjjjmq', 'U', 'T', 'AAA1', 'A', null, null, null, null, null,   null, null, null, null, null, 'D00021', null, 1, to_date('29-10-2018 14:31:42', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-10-2018 14:31:42', 'dd-mm-yyyy hh24:mi:ss'),  null, null, null);
insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,   REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000002', '$2a$11$12xCslB.N5Fnn2ADP67pIeldcSQRlrQ6VzOBM3iCsORcAtDlYuzCS', 'U', 'T', 'AAA11', 'AAA11', null, null, null, null, null,  null, null, null, null, null, 'D00022', null, 1, to_date('29-10-2018 14:32:21', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-10-2018 14:32:21', 'dd-mm-yyyy hh24:mi:ss'),  null, null, null);
insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,   REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000047', '$2a$11$EwKBElaYacsvbusoopl4ruZ4aNwuPOYlxZOkrLVyEcb.dcTns2zPG', 'U', 'T', 'cs3', 'cs3', null, null, null, null, null,   null, null, null, null, null, 'D00005', null, 9, to_date('04-12-2018 16:31:48', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-12-2018 16:31:48', 'dd-mm-yyyy hh24:mi:ss'),  null, null, null);
insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,   REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000048', '$2a$11$D7LJXrfkX6A4xdwJ7r6QleEoDZVqb1h0Y82.WUjDvxVysGlgidR4u', 'U', 'T', 'cs4', 'cs4', null, null, null, null, null,   null, null, null, null, null, 'D00005', null, 8, to_date('04-12-2018 16:57:24', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-12-2018 16:57:24', 'dd-mm-yyyy hh24:mi:ss'),  null, null, null);
insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,   REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000049', '$2a$11$9UubjZst27/dPx8j5Ql6.uFZyxbVuLq6/ArGq3ichjZ6.e7q8ykeO', 'U', 'T', 'cs5', 'cs5', null, null, null, null, null,   null, null, null, null, null, 'D00005', null, 8, to_date('04-12-2018 17:29:42', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-12-2018 17:29:42', 'dd-mm-yyyy hh24:mi:ss'),  null, null, null);
insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,   REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000045', '$2a$11$vy5GPgj7Ejaudmuu9JLQIe84EDMEEKhmqcp.g0YZzORIeShw1qlUS', 'U', 'T', 'cs1', 'cs1', null, null, null, null, null,   null, null, null, null, null, 'D00005', null, 4, to_date('04-12-2018 15:59:19', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-12-2018 15:59:19', 'dd-mm-yyyy hh24:mi:ss'),  null, null, null);
insert into F_USERINFO (USER_CODE, USER_PIN, USER_TYPE, IS_VALID, LOGIN_NAME, USER_NAME, USER_TAG, ENGLISH_NAME, USER_DESC, LOGIN_TIMES, ACTIVE_TIME,  REG_EMAIL, USER_PWD, PWD_EXPIRED_TIME, REG_CELL_PHONE, ID_CARD_NO, PRIMARY_UNIT, USER_WORD, USER_ORDER, UPDATE_DATE, CREATE_DATE,  CREATOR, UPDATOR, TOP_UNIT)
values ('U0000046', '$2a$11$IlLg3SEP.IwMq/INAmI4r.LW7a9wIeaFc2zfOzi8LgAnUe5zjA8lC', 'U', 'T', 'cs2', 'cs2', null, null, null, null, null,  null, null, null, null, null, 'D00005', null, 5, to_date('04-12-2018 16:01:16', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-12-2018 16:01:16', 'dd-mm-yyyy hh24:mi:ss'),  null, null, null);


insert into F_USERROLE (USER_CODE, ROLE_CODE, OBTAIN_DATE, SECEDE_DATE, CHANGE_DESC, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('U0000002', 'sysadmin', to_date('29-10-2018', 'dd-mm-yyyy'), null, null, null, to_date('29-10-2018 16:08:27', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into F_USERROLE (USER_CODE, ROLE_CODE, OBTAIN_DATE, SECEDE_DATE, CHANGE_DESC, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('U0000001', 'sysadmin', to_date('29-10-2018', 'dd-mm-yyyy'), null, null, null, to_date('29-10-2018 16:08:39', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into F_USERROLE (USER_CODE, ROLE_CODE, OBTAIN_DATE, SECEDE_DATE, CHANGE_DESC, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('U0000017', '1', to_date('18-06-2018', 'dd-mm-yyyy'), null, null, null, to_date('18-06-2018 20:31:00', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into F_USERROLE (USER_CODE, ROLE_CODE, OBTAIN_DATE, SECEDE_DATE, CHANGE_DESC, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('U0000018', '1', to_date('18-06-2018', 'dd-mm-yyyy'), null, null, null, to_date('18-06-2018 20:31:00', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into F_USERROLE (USER_CODE, ROLE_CODE, OBTAIN_DATE, SECEDE_DATE, CHANGE_DESC, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('U0000019', '1', to_date('18-06-2018', 'dd-mm-yyyy'), null, null, null, to_date('18-06-2018 20:31:00', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into F_USERROLE (USER_CODE, ROLE_CODE, OBTAIN_DATE, SECEDE_DATE, CHANGE_DESC, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('U0000020', '1', to_date('18-06-2018', 'dd-mm-yyyy'), null, null, null, to_date('18-06-2018 20:31:00', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into F_USERROLE (USER_CODE, ROLE_CODE, OBTAIN_DATE, SECEDE_DATE, CHANGE_DESC, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('U0000021', '1', to_date('19-06-2018', 'dd-mm-yyyy'), null, null, null, to_date('19-06-2018 14:59:00', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into F_USERROLE (USER_CODE, ROLE_CODE, OBTAIN_DATE, SECEDE_DATE, CHANGE_DESC, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('U0000022', '1', to_date('19-06-2018', 'dd-mm-yyyy'), null, null, null, to_date('19-06-2018 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into F_USERROLE (USER_CODE, ROLE_CODE, OBTAIN_DATE, SECEDE_DATE, CHANGE_DESC, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('U0000025', '1', to_date('19-06-2018', 'dd-mm-yyyy'), null, null, null, to_date('19-06-2018 16:25:00', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into F_USERROLE (USER_CODE, ROLE_CODE, OBTAIN_DATE, SECEDE_DATE, CHANGE_DESC, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('noname', 'sysadmin', to_date('28-06-2018 15:26:14', 'dd-mm-yyyy hh24:mi:ss'), null, null, null, null, null, null);



insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('U0000019', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('23-10-2018 10:19:56', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('U0000022', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('25-10-2018 15:40:23', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('U0000020', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('30-09-2018 16:03:18', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('U0000017', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('23-10-2018 15:47:43', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('U0000018', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('23-10-2018 15:49:43', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('U0000021', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('23-10-2018 15:48:52', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('U0000001', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('27-06-2018 19:08:40', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('26934', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('28-06-2018 15:23:40', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('26960', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('29-06-2018 11:26:23', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('U0000005', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('29-06-2018 11:26:45', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('27009', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('02-07-2018 17:49:13', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('27025', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('02-07-2018 17:50:27', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('27013', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('04-07-2018 20:33:57', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('u0000000', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', null);
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('U0000003', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('26-06-2018 16:13:52', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('U0000004', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('26-06-2018 16:38:28', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('26928', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('29-06-2018 14:11:33', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('26997', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('02-07-2018 17:46:52', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('26967', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('29-06-2018 10:43:14', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_USERSETTING (USER_CODE, PARAM_CODE, PARAM_VALUE, OPT_ID, PARAM_NAME, CREATE_DATE)
values ('U0000002', 'LOCAL_LANG', 'zh_CN', 'SYS', '用户默认区域语言', to_date('28-06-2018 10:15:21', 'dd-mm-yyyy hh24:mi:ss'));



insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000043', 'CCC', 'U0000022', 'T', '20277', 'ST', null, 0, to_date('25-10-2018 20:10:04', 'dd-mm-yyyy hh24:mi:ss'), to_date('25-10-2018 20:10:04', 'dd-mm-yyyy hh24:mi:ss'), 'U0000021', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000063', 'D00021', 'U0000001', 'T', '55764', 'DM', null, 1, to_date('29-10-2018 14:31:42', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-10-2018 14:31:42', 'dd-mm-yyyy hh24:mi:ss'), 'U0000019', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000064', 'D00022', 'U0000002', 'T', '55795', 'DM', null, 1, to_date('29-10-2018 14:32:21', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-10-2018 14:32:21', 'dd-mm-yyyy hh24:mi:ss'), 'U0000019', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000085', 'D00005', 'U0000047', 'T', '209466', 'PM', null, 9, to_date('04-12-2018 16:31:48', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-12-2018 16:31:48', 'dd-mm-yyyy hh24:mi:ss'), 'U0000020', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000018', 'AAA', 'U0000017', 'F', 'mang', 'DM', null, 1, to_date('13-08-2018 11:28:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('18-06-2018 20:08:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', 'u0000000');
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000020', 'CCC', 'U0000018', 'F', 'mang', 'DM', null, 1, to_date('13-08-2018 11:28:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('18-06-2018 20:11:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', 'u0000000');
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000021', 'DDD', 'U0000018', 'F', 'mang', 'DM', null, 0, to_date('13-08-2018 11:28:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('18-06-2018 20:11:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', 'u0000000');
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000025', 'D00005', 'U0000019', 'T', 'tech', 'ST', null, 3, to_date('18-06-2018 20:34:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('18-06-2018 20:34:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000026', 'D00005', 'U0000020', 'T', 'mang', 'DM', null, 1, to_date('18-06-2018 20:34:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('18-06-2018 20:34:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000027', 'BBB', 'U0000017', 'T', 'mang', 'DM', null, 1, to_date('13-08-2018 11:28:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('18-06-2018 20:35:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', 'u0000000');
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000028', 'EEE', 'U0000018', 'T', 'mang', 'DM', null, 1, to_date('13-08-2018 11:29:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('18-06-2018 20:35:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', 'u0000000');
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000029', 'AAA', 'U0000021', 'F', 'tech', 'ST', null, 5, to_date('19-06-2018 15:02:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('19-06-2018 14:59:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000034', 'BBB', 'U0000021', 'T', 'tech', 'ST', null, 5, to_date('19-06-2018 15:02:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('19-06-2018 15:02:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000036', 'DDD', 'U0000022', 'F', 'tech', 'ST', null, 0, to_date('19-06-2018 15:03:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('19-06-2018 15:02:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000037', 'EEE', 'U0000022', 'F', 'tech', 'ST', null, 0, to_date('19-06-2018 15:03:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('19-06-2018 15:03:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000038', 'CCC', 'U0000022', 'F', 'tech', 'GM', null, 0, to_date('25-10-2018 20:10:04', 'dd-mm-yyyy hh24:mi:ss'), to_date('19-06-2018 15:03:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000039', 'AAA', 'U0000025', 'F', 'tech', 'ST', null, 6, to_date('19-06-2018 16:26:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('19-06-2018 16:25:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000040', 'BBB', 'U0000025', 'T', 'tech', 'ST', null, 0, to_date('19-06-2018 16:26:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('19-06-2018 16:26:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000041', 'D00005', 'u0000000', 'F', 'mang', 'GM', null, 0, to_date('09-07-2018 16:09:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('21-06-2018 17:10:00', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000086', 'D00005', 'U0000048', 'T', 'jzzdz', 'DM', null, 8, to_date('04-12-2018 16:57:24', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-12-2018 16:57:24', 'dd-mm-yyyy hh24:mi:ss'), 'U0000020', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000087', 'D00005', 'U0000049', 'T', '209466', 'DM', null, 8, to_date('04-12-2018 17:29:42', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-12-2018 17:29:42', 'dd-mm-yyyy hh24:mi:ss'), 'U0000020', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000002', 'U00001', 'u0000000', 'T', 'sjjz', 'JZ', null, 1, to_date('26-06-2018 15:12:52', 'dd-mm-yyyy hh24:mi:ss'), to_date('26-06-2018 15:12:52', 'dd-mm-yyyy hh24:mi:ss'), 'u0000000', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000083', 'D00005', 'U0000045', 'T', 'sqmj', 'DM', null, 4, to_date('04-12-2018 15:59:19', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-12-2018 15:59:19', 'dd-mm-yyyy hh24:mi:ss'), 'U0000020', null);
insert into F_USERUNIT (USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR)
values ('s000000084', 'D00005', 'U0000046', 'T', '209466', 'DM', null, 5, to_date('04-12-2018 16:01:16', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-12-2018 16:01:16', 'dd-mm-yyyy hh24:mi:ss'), 'U0000020', null);

insert into WF_FLOW_TEAM_ROLE (FLOW_TEAM_ROLE_ID, FLOW_CODE, ROLE_CODE, ROLE_NAME, TEAM_ROLE_ORDER, CREATE_TIME, MODIFY_TIME, VERSION)
values ('832', '19', 'jbr', '经办人', null, to_date('22-10-2018 19:36:49', 'dd-mm-yyyy hh24:mi:ss'), to_date('22-10-2018 19:36:49', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into WF_FLOW_TEAM_ROLE (FLOW_TEAM_ROLE_ID, FLOW_CODE, ROLE_CODE, ROLE_NAME, TEAM_ROLE_ORDER, CREATE_TIME, MODIFY_TIME, VERSION)
values ('919', '61', 'jbr', '经办人', null, to_date('23-10-2018 14:57:49', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2018 14:57:49', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into WF_FLOW_TEAM_ROLE (FLOW_TEAM_ROLE_ID, FLOW_CODE, ROLE_CODE, ROLE_NAME, TEAM_ROLE_ORDER, CREATE_TIME, MODIFY_TIME, VERSION)
values ('1392', '41', 'jbr', '经办人', null, to_date('19-11-2018 11:20:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('19-11-2018 11:20:32', 'dd-mm-yyyy hh24:mi:ss'), 0);



insert into WF_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, UPDATE_DATE)
values ('59', '45', '通用审批', '/approval.html', to_date('29-10-2018 15:20:10', 'dd-mm-yyyy hh24:mi:ss'));
insert into WF_OPTDEF (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, UPDATE_DATE)
values ('60', '45', '置文号', '/zwh.html', to_date('29-10-2018 15:20:10', 'dd-mm-yyyy hh24:mi:ss'));
insert into wf_optdef (OPT_CODE, OPT_ID, OPT_NAME, OPT_METHOD, UPDATE_DATE)
values ('63', '47', '市局审批', '/pre_give_power_audit.html', to_date('20-12-2018 10:40:45', 'dd-mm-yyyy hh24:mi:ss'));




insert into WF_OPTINFO (OPT_ID, OPT_NAME, OPT_URL, UPDATE_DATE)
values ('45', '发改业务', 'approval', to_date('10-10-2018 10:25:36', 'dd-mm-yyyy hh24:mi:ss'));
insert into wf_optinfo (OPT_ID, OPT_NAME, OPT_URL, UPDATE_DATE)
values ('47', '市局工作流', '/flowExtend/handle', to_date('20-12-2018 10:39:41', 'dd-mm-yyyy hh24:mi:ss'));



--市局业务模块数据
insert into wf_module (MODULE_CODE, MODULE_NAME, MODULE_DESC, OPT_CODE)
values ('QZSQ', '前置授权', '前置授权模块', '4030');

insert into wf_module (MODULE_CODE, MODULE_NAME, MODULE_DESC, OPT_CODE)
values ('RYXZ', '人员新增', '人员新增模块', '9030');

insert into wf_module (MODULE_CODE, MODULE_NAME, MODULE_DESC, OPT_CODE)
values ('QXGL', '权限管理', '权限管理模块', '7010');
commit;

insert into wf_module_interface (INTERFACE_CODE, MODULE_CODE, INTERFACE_ADDRESS, INTERFACE_DESC, INTERFACE_ID)
values ('insert.msgUrl', 'QXGL', '/api/v1/hero/authchange/approveauth/noLoginInsertTodoMessage
', '新增消息中心', '11');

insert into wf_module_interface (INTERFACE_CODE, MODULE_CODE, INTERFACE_ADDRESS, INTERFACE_DESC, INTERFACE_ID)
values ('send.msgUrl', 'QXGL', '/api/v1/hero/authchange/approveauth/approveApplyAuth
', '更新业务数据', '12');

insert into wf_module_interface (INTERFACE_CODE, MODULE_CODE, INTERFACE_ADDRESS, INTERFACE_DESC, INTERFACE_ID)
values ('update.msgUrl', 'QXGL', '/api/v1/hero/authchange/approveauth/UpdateMessageStatus
', '更新消息中心', '13');

insert into wf_module_interface (INTERFACE_CODE, MODULE_CODE, INTERFACE_ADDRESS, INTERFACE_DESC, INTERFACE_ID)
values ('bookMarkUrl', 'QZSQ', '/api/v1/object/preAuth/getWordData', '书签获取', '14');

insert into wf_module_interface (INTERFACE_CODE, MODULE_CODE, INTERFACE_ADDRESS, INTERFACE_DESC, INTERFACE_ID)
values ('insert.msgUrl', 'QZSQ', '/api/v1/object/preAuth/noLoginInsertTodoMessage', '新增消息中心', '15');

insert into wf_module_interface (INTERFACE_CODE, MODULE_CODE, INTERFACE_ADDRESS, INTERFACE_DESC, INTERFACE_ID)
values ('send.msgUrl', 'QZSQ', '/api/v1/object/preAuth/changePreAuthStatus', '更新业务状态', '16');

insert into wf_module_interface (INTERFACE_CODE, MODULE_CODE, INTERFACE_ADDRESS, INTERFACE_DESC, INTERFACE_ID)
values ('update.msgUrl', 'QZSQ', '/api/v1/object/preAuth/noLoginUpdateMessageStatus', '更新消息中心', '17');

insert into wf_module_interface (INTERFACE_CODE, MODULE_CODE, INTERFACE_ADDRESS, INTERFACE_DESC, INTERFACE_ID)
values ('insert.msgUrl', 'RYXZ', '/api/v1/aimobject/personcenter/addObjectPersonMessageCenter', '新增消息中心', '18');

insert into wf_module_interface (INTERFACE_CODE, MODULE_CODE, INTERFACE_ADDRESS, INTERFACE_DESC, INTERFACE_ID)
values ('send.msgUrl', 'RYXZ', '/api/v1/aimobject/personcenter/workFlowEnd', '更新业务数据', '19');

insert into wf_module_interface (INTERFACE_CODE, MODULE_CODE, INTERFACE_ADDRESS, INTERFACE_DESC, INTERFACE_ID)
values ('update.msgUrl', 'RYXZ', '/api/v1/aimobject/personcenter/updateObjectPersonMessageCenter', '更新消息中心', '20');
commit;



insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('flowOptTag', '16', 'String', '业务id', 'Y', 'bussId', '业务id', '112');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('approveState', '16', 'String', '1- 审批通过 2- 审批驳回', 'Y', 'approveState', '1- 审批通过 2- 审批驳回', '113');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('objType', '15', 'String', '信息类别:我的消息(6000)\我的待办(6020)', 'Y', 'objType', '信息类别:我的消息(6000)\我的待办(6020)', '118');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('nodeOptUrl', '15', 'String', '业务跳转的url', 'Y', 'linkUrl', '业务跳转的url', '119');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('flowInstId', '15', 'String', '流程id', 'Y', 'businessId', '业务id', '120');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('nodeInstId', '15', 'String', '节点id', 'Y', 'businessStep', '业务步骤', '121');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('userIds', '15', 'String', '消息接收人ID列表', 'Y', 'userIds', '消息接收人ID列表', '122');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('sendSMS', '15', 'String', '是否发送短信', 'Y', 'sendSMS', '是否发送短信', '123');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('approveState', '12', 'String', '1- 审批通过 2- 审批驳回', 'Y', 'status', '1- 审批通过 2- 审批驳回', '135');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('approvePersonId', '12', 'String', '审批人', 'Y', 'approvePersonId', '审批人', '136');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('flowOptTag', '12', 'String', '业务id', 'Y', 'id', '业务id', '134');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('nodeInstId', '18', 'String', '节点id', 'Y', 'businessStep', '业务步骤', '137');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('sendSMS', '18', 'String', '是否发送短信', 'Y', 'sendSMS', '是否发送短信', '138');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('userIds', '18', 'String', '消息接收人ID列表', 'Y', 'userIds', '消息接收人ID列表', '139');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('flowInstId', '18', 'String', '流程id', 'Y', 'businessId', '业务id', '140');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('nodeOptUrl', '18', 'String', '业务跳转的url', 'Y', 'linkUrl', '业务跳转的url', '141');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('objType', '18', 'String', '信息类别:我的消息(6000)\我的待办(6020)', 'Y', 'objType', '信息类别:我的消息(6000)\我的待办(6020)', '142');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('approveState', '19', 'String', '1- 审批通过 2- 审批驳回', 'Y', 'approveState', '1- 审批通过 2- 审批驳回', '143');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('flowOptTag', '19', 'String', '业务id', 'Y', 'bussId', '业务id', '144');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('approveStatusName', '20', 'String', '审批状态(未审批，审批通过，驳回)', 'Y', 'approveStatusName', '审批状态(未审批，审批通过，驳回)', '145');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('flowInstId', '20', 'String', '流程id', 'Y', 'businessId', '业务ID', '146');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('approveStatus', '20', 'String', '审批状态(1:未审批， 3:审批通过，4:驳回)', 'Y', 'approveStatus', '审批状态(1:未审批， 3:审批通过，4:驳回)', '147');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('nodeInstId', '20', 'String', '节点id', 'Y', 'businessStep', '业务流程', '148');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('flowInstId', '17', 'String', '流程id', 'Y', 'businessId', '业务ID', '130');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('nodeInstId', '17', 'String', '节点id', 'Y', 'businessStep', '业务流程', '131');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('approveStatus', '17', 'String', '审批状态(1:未审批， 3:审批通过，4:驳回)', 'Y', 'approveStatus', '审批状态(1:未审批， 3:审批通过，4:驳回)', '132');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('approveStatusName', '17', 'String', '审批状态(未审批，审批通过，驳回)', 'Y', 'approveStatusName', '审批状态(未审批，审批通过，驳回)', '133');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('nodeInstId', '11', 'String', '节点id', 'Y', 'businessStep', '业务步骤', '149');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('sendSMS', '11', 'String', '是否发送短信', 'Y', 'sendSMS', '是否发送短信', '150');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('userIds', '11', 'String', '消息接收人ID列表', 'Y', 'userIds', '消息接收人ID列表', '151');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('flowInstId', '11', 'String', '流程id', 'Y', 'businessId', '业务id', '152');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('nodeOptUrl', '11', 'String', '业务跳转的url', 'Y', 'linkUrl', '业务跳转的url', '153');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('objType', '11', 'String', '信息类别:我的消息(6000)\我的待办(6020)', 'Y', 'objType', '信息类别:我的消息(6000)\我的待办(6020)', '154');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('approveStatusName', '13', 'String', '审批状态(未审批，审批通过，驳回)', 'Y', 'approveStatusName', '审批状态(未审批，审批通过，驳回)', '155');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('flowInstId', '13', 'String', '流程id', 'Y', 'businessId', '业务ID', '156');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('approveStatus', '13', 'String', '审批状态(1:未审批， 3:审批通过，4:驳回)', 'Y', 'approveStatus', '审批状态(1:未审批， 3:审批通过，4:驳回)', '157');

insert into wf_interface_parameter (PARAMETER_CODE, INTERFACE_ID, PARAMETER_TYPE, PARAMETER_DESC, IS_REQUIRED, BUSINESS_CODE, BUSINESS_DESC, PARAMETER_ID)
values ('nodeInstId', '13', 'String', '节点id', 'Y', 'businessStep', '业务流程', '158');

commit;

