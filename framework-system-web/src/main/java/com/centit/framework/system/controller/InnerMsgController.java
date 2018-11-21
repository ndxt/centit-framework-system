package com.centit.framework.system.controller;

import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.model.basedata.OperationLog;
import com.centit.framework.system.po.InnerMsg;
import com.centit.framework.system.po.InnerMsgRecipient;
import com.centit.framework.system.service.InnerMessageManager;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.json.JsonPropertyUtils;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 内部消息、公告
 */
@Controller
@RequestMapping("/innermsg")
@Api(tags= "内部消息、公告操作接口",value = "内部消息、公告接口维护")
public class InnerMsgController extends BaseController {

    @Resource
    @NotNull
    public InnerMessageManager innerMessageManager;

    public String getOptId() {
      return  "InnerMsg";
    }
    /**
     * 查询收件箱
     * @param pageDesc  PageDesc
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     */
    @ApiOperation(value="查询收件箱",notes="查询收件箱。")
    @RequestMapping(value = "/inbox", method = { RequestMethod.GET })
    public void listInbox(PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);

        String receive = (String) searchColumn.get("receive");
        if (StringUtils.isBlank(receive)) {
            searchColumn.put("receive", WebOptUtils.getLoginUser(request).getUserInfo().getUserCode());
        }
        List<InnerMsgRecipient> listObjects  = innerMessageManager.listMsgRecipientsCascade(searchColumn, pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, DictionaryMapUtils.objectsToJSONArray(listObjects));
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 未读消息数量
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @ApiOperation(value="未读消息数量",notes="未读消息数量。")
    @RequestMapping(value = "/unreadMsgCount", method = { RequestMethod.GET })
    public void unreadMsgCount(HttpServletRequest request, HttpServletResponse response) {
        String currUser = WebOptUtils.getLoginUser(request).getUserInfo().getUserCode();
        long unreadMsg = innerMessageManager.getUnreadMessageCount(currUser);
        JsonResultUtils.writeSingleDataJson(unreadMsg, response);
    }
    /**
     * 查询发件箱
     * @param pageDesc   PageDesc
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     */
    @ApiOperation(value="查询发件箱",notes="查询发件箱。")
    @RequestMapping(value = "/outbox", method = { RequestMethod.GET })
    public void listOutbox(PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);

        String sender = (String) searchColumn.get("sender");
        if (StringUtils.isBlank(sender)) {
            searchColumn.put("sender", WebOptUtils.getLoginUser(request).getUserInfo().getUserCode());
        }

        List<InnerMsg> listObjects = innerMessageManager.listInnerMsgs(searchColumn,pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, DictionaryMapUtils.objectsToJSONArray(listObjects));
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 是否有发公告权限
     * @param response   HttpServletResponse
     */
    @ApiOperation(value="是否有发公告权限",notes="是否有发公告权限。")
    @RequestMapping(value = "/cangivenotify", method = { RequestMethod.GET })
    public void cangivenotify(HttpServletResponse response) {
        boolean s = CodeRepositoryUtil.checkUserOptPower("MSGMAG", "givenotify");
        JsonResultUtils.writeSingleDataJson(s, response);
    }

    /**
     * 是否有发公告权限
     * @param msgCode   msgCode
     * @param response   HttpServletResponse
     */
    @ApiOperation(value="是否有发公告权限",notes="是否有发公告权限。")
    @ApiImplicitParam(
        name = "msgCode", value="消息代码",
        required= true, paramType = "path", dataType= "String")
    @RequestMapping(value = "/{msgCode}", method = { RequestMethod.GET })
    public void getInnerMsg(@PathVariable String msgCode, HttpServletResponse response) {
        InnerMsgRecipient msgCopy = innerMessageManager.getMsgRecipientById(msgCode);
        JsonResultUtils.writeSingleDataJson(msgCopy, response);
    }

    /**
     * 公告列表
     * @param field       显示结果中只需要显示的字段
     * @param pageDesc    PageDesc
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     */
    @ApiOperation(value="公告列表",notes="公告列表。")
    @ApiImplicitParam(
        name = "field", value="显示结果中只需要显示的字段",
        allowMultiple=true, paramType = "query", dataType= "String")
    @ApiParam(name="pageDesc",value="分页对象")
    @RequestMapping(value = "/notice", method = { RequestMethod.GET })
    public void listnotify(String[] field, PageDesc pageDesc,
            HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);
        searchColumn.put("msgType", "A");
        List<InnerMsg> listObjects = innerMessageManager.listInnerMsgs(searchColumn,pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, DictionaryMapUtils.objectsToJSONArray(listObjects));
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response, JsonPropertyUtils
                .getIncludePropPreFilter(InnerMsg.class, field));
    }

    /**
     * 按部门发公告，会匹配该部门以及所有子部门的用户，群发消息
     * @param unitCode unitCode
     * @param innerMsg InnerMsg
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception  Exception
     */
    @ApiOperation(value="群发消息",notes="按部门发公告，会匹配该部门以及所有子部门的用户，群发消息。")
    @ApiImplicitParam(
        name = "unitCode", value="机构代码",
        required= true, paramType = "path", dataType= "String")
    @ApiParam(name="innerMsg",value="群发的对象信息",required=true)
    @RequestMapping(value = "/notify/{unitCode}", method = { RequestMethod.POST })
    public void noticeByUnit(@PathVariable String unitCode,@Valid InnerMsg innerMsg,HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!StringUtils.isNotBlank(innerMsg.getSender())) {
            innerMsg.setSender(WebOptUtils.getLoginUser(request).getUserInfo().getUserCode());
            //innerMsg.setSenderName(WebOptUtils.getLoginUserName(request));
        }
        if (null == innerMsg.getSendDate()) {
            innerMsg.setSendDate(new Date());
        }
        innerMessageManager.noticeByUnitCode(unitCode,innerMsg);
        JsonResultUtils.writeSuccessJson(response);
    }


    /**
     * 发送或群发消息，recipient必须包含mInnerMsg对象属性，recipient.receive传入是由userCode拼接成的字符串，以逗号隔开
     * @param recipient InnerMsgRecipient
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @ApiOperation(value="发送或群发消息",notes="发送或群发消息。")
    @ApiParam(name="recipient",value="接收的消息对象",required=true)
    @RequestMapping(value = "/sendMsg", method = { RequestMethod.POST })
    public void sendMsg(@Valid InnerMsgRecipient recipient,HttpServletRequest request,
            HttpServletResponse response) {
        innerMessageManager.sendInnerMsg(recipient,this.getLoginUser(request).getUserInfo().getUserCode());
        //DataPushSocketServer.pushMessage(recipient.getReceive(), "你有新邮件："+ recipient.getMsgTitle());
        JsonResultUtils.writeSingleDataJson(recipient, response);
    }



    /**
     * 获取当前登录用户
     * @param request HttpServletReqeust
     * @param response  HttpServletResponse
     */
    @ApiOperation(value="获取当前登录用户",notes="获取当前登录用户。")
    @RequestMapping(value = "/loginuser", method = { RequestMethod.GET })
    public void getLoginUserCode(HttpServletResponse response,
            HttpServletRequest request) {
        String userCode = this.getLoginUser(request).getUserInfo().getUserCode();
        JsonResultUtils.writeSingleDataJson(userCode, response);
    }

    /**
     * 更新消息内容
     * @param msg  InnerMsg
     * @param msgCode  消息编号
     * @param response  HttpServletResponse
     */
    @ApiOperation(value="更新消息内容",notes="更新消息内容。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "msgCode", value="消息代码",
            required= true, paramType = "path", dataType= "String"),
        @ApiImplicitParam(
            name = "msg", value="json格式，更新的消息对象",
            required= true, paramType = "body", dataTypeClass = InnerMsg.class)
    })
    @RequestMapping(value = "/{msgCode}", method = { RequestMethod.PUT })
    public void mergInnerMsg(@Valid InnerMsg msg, @PathVariable String msgCode,
            HttpServletResponse response) {
        InnerMsg msgCopy = innerMessageManager.getInnerMsgById(msgCode);
        if (null == msgCopy) {
            JsonResultUtils.writeErrorMessageJson("当前机构中无此信息", response);
            return;
        }
        innerMessageManager.updateInnerMsg(msg);
        // 需要返回msg的msgCode给前端recipient保存用
        JsonResultUtils.writeSingleDataJson(msg, response);
    }

    /**
     * 更新接受者信息
     * @param recipient  InnerMsgRecipient
     * @param id 接收者信息编号
     * @param response  HttpServletResponse
     */
    @ApiOperation(value="更新接受者信息",notes="更新接受者信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "id", value="接收者信息编号",
            required= true, paramType = "path", dataType= "String"),
        @ApiImplicitParam(
            name = "recipient", value="json格式，更新的接受者信息对象",
            required= true, paramType = "body", dataTypeClass = InnerMsgRecipient.class)
    })
    @RequestMapping(value = "recipient/{id}", method = { RequestMethod.PUT })
    public void mergInnerMsgRecipient(@Valid InnerMsgRecipient recipient,
            @PathVariable String id, HttpServletResponse response) {
        InnerMsgRecipient recipientCopy =innerMessageManager.getMsgRecipientById(id);
        if (null == recipientCopy) {
            JsonResultUtils.writeErrorMessageJson("当前机构中无此信息", response);
            return;
        }
        innerMessageManager.updateRecipient(recipient);
        // 需要返回msg的msgCode给前端recipient保存用
        JsonResultUtils.writeSingleDataJson(recipient, response);
    }

    /**
     * 删除消息,并没有删除该条记录，而是把msgState字段标记为D
     * @param msgCode 消息编号
     * @param response HttpServletResponse
     */
    @ApiOperation(value="删除消息",notes="删除消息,并没有删除该条记录，而是把msgState字段标记为D。")
    @ApiImplicitParam(
        name = "msgCode", value="信息编号",
        required= true, paramType = "path", dataType= "String")
    @RequestMapping(value = "/{msgCode}", method = { RequestMethod.DELETE })
    public void deleteMsg(@PathVariable String msgCode,
            HttpServletResponse response) {
        innerMessageManager.deleteInnerMsgById(msgCode);
        JsonResultUtils.writeBlankJson(response);
    }

    /**
     * 删除接受者信息,并没有删除该条记录，而是把msgState字段标记为D
     * @param id 接受者信息编号
     * @param request  HttpServletRequest
     * @param response  HttpServletResponse
     */
    @ApiOperation(value="删除接受者信息",notes="删除接受者信息,并没有删除该条记录，而是把msgState字段标记为D。")
    @ApiImplicitParam(
        name = "id", value="接受者信息编号",
        required= true, paramType = "path", dataType= "String")
    @RequestMapping(value = "/recipient/{id}", method = { RequestMethod.DELETE })
    public void deleteRecipient(@PathVariable String id,
             HttpServletRequest request,HttpServletResponse response) {
        /*InnerMsgRecipient recipient = innerMsgRecipientManager
                .getObjectById(id);*/
        innerMessageManager.deleteMsgRecipientById(id);;
        JsonResultUtils.writeBlankJson(response);
        //(request, optId, optTag, optMethod, optContent, oldObject);
        OperationLogCenter.logDeleteObject(request, "recipient", id ,OperationLog.P_OPT_LOG_METHOD_D,
                 "删除接收这信息","");

    }


    /**
     * 往来消息列表
     * @param sender 用户1
     * @param receiver 用户2
     * @param response  HttpServletResponse
     */
    @ApiOperation(value="往来消息列表",notes="获取发送者和接受者往来消息列表")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "sender", value="发送者id",
            required= true, paramType = "path", dataType= "String"),
        @ApiImplicitParam(
            name = "receiver", value="接收者id",
            required= true, paramType = "path", dataType= "String")
    })
    @RequestMapping(value = "/{sender}/{receiver}", method = { RequestMethod.GET })
    public void getMsgExchanges(@PathVariable String sender,
            @PathVariable String receiver, HttpServletResponse response) {
        List<InnerMsgRecipient> recipientlist = innerMessageManager
                .getExchangeMsgRecipients(sender, receiver);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, recipientlist);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

}
