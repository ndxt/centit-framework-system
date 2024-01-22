package com.centit.framework.users.po;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * <p>企业授权信息实体类<p>
 *
 * @author li_hao
 * @version 1.1
 * @date 2018年3月29日
 */
@Entity
public class CorpInfo implements Serializable {

    private static final long serialVersionUID = -883723804589385508L;


    private String corpid;  //授权企业corpid
    private String corpname;  //授权企业名称
    private String suiteid;  //套件id
    private String permanent_code;  //永久授权码
    private String access_token;  //授权方（企业）access_token
    private String jsapi_ticket;  //授权企业的jsapi_ticket
    private String corplogourl;  //企业logo
    private String industry;  //企业所属行业
    private String invitecode;  //邀请码，只有自己邀请的企业才会返回邀请码，可用该邀请码统计不同渠道拉新，否则值为空字符串
    private String licensecode;  //序列号
    private String authchannel;  //渠道码
    private String authchanneltype;  //渠道类型,为了避免渠道码重复，可与渠道码共同确认渠道（可能为空。非空时当前只有满天星类型，值为STAR_ACTIVITY）
    private String isauthenticated;  //企业是否认证
    private String authlevel;  //企业认证等级，0：未认证，1：高级认证，2：中级认证，3：初级认证
    private String inviteurl;  //企业邀请链接
    private String authuserinfo;  //授权方管理员信息
    private String createtime;  //创建时间
    private String updatetime;  //修改时间
    private String isenable;  //启用状态 ;是0 ；否1


    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public String getCorpname() {
        return corpname;
    }

    public void setCorpname(String corpname) {
        this.corpname = corpname;
    }

    public String getSuiteid() {
        return suiteid;
    }

    public void setSuiteid(String suiteid) {
        this.suiteid = suiteid;
    }

    public String getPermanent_code() {
        return permanent_code;
    }

    public void setPermanent_code(String permanent_code) {
        this.permanent_code = permanent_code;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getJsapi_ticket() {
        return jsapi_ticket;
    }

    public void setJsapi_ticket(String jsapi_ticket) {
        this.jsapi_ticket = jsapi_ticket;
    }

    public String getCorplogourl() {
        return corplogourl;
    }

    public void setCorplogourl(String corplogourl) {
        this.corplogourl = corplogourl;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public String getLicensecode() {
        return licensecode;
    }

    public void setLicensecode(String licensecode) {
        this.licensecode = licensecode;
    }

    public String getAuthchannel() {
        return authchannel;
    }

    public void setAuthchannel(String authchannel) {
        this.authchannel = authchannel;
    }

    public String getAuthchanneltype() {
        return authchanneltype;
    }

    public void setAuthchanneltype(String authchanneltype) {
        this.authchanneltype = authchanneltype;
    }

    public String getIsauthenticated() {
        return isauthenticated;
    }

    public void setIsauthenticated(String isauthenticated) {
        this.isauthenticated = isauthenticated;
    }

    public String getAuthlevel() {
        return authlevel;
    }

    public void setAuthlevel(String authlevel) {
        this.authlevel = authlevel;
    }

    public String getInviteurl() {
        return inviteurl;
    }

    public void setInviteurl(String inviteurl) {
        this.inviteurl = inviteurl;
    }

    public String getAuthuserinfo() {
        return authuserinfo;
    }

    public void setAuthuserinfo(String authuserinfo) {
        this.authuserinfo = authuserinfo;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getIsenable() {
        return isenable;
    }

    public void setIsenable(String isenable) {
        this.isenable = isenable;
    }

    @Override
    public String toString() {
        return "CorpInfo [corpid=" + corpid + ", corpname=" + corpname + ", suiteid=" + suiteid + ", permanent_code="
            + permanent_code + ", access_token=" + access_token + ", jsapi_ticket=" + jsapi_ticket
            + ", corplogourl=" + corplogourl + ", industry=" + industry + ", invitecode=" + invitecode
            + ", licensecode=" + licensecode + ", authchannel=" + authchannel + ", authchanneltype="
            + authchanneltype + ", isauthenticated=" + isauthenticated + ", authlevel=" + authlevel + ", inviteurl="
            + inviteurl + ", authuserinfo=" + authuserinfo + ", createtime=" + createtime + ", updatetime="
            + updatetime + ", isenable=" + isenable + "]";
    }


}
