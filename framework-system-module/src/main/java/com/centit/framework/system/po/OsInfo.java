package com.centit.framework.system.po;

import com.centit.framework.core.dao.DictionaryMap;
import com.centit.framework.model.basedata.IOsInfo;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "F_OS_INFO")
@ApiModel(value="系统信息对象",description="系统信息对象 OsInfo")
public class OsInfo implements IOsInfo, java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "OS_ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    @ApiModelProperty(value = "业务系统ID",name = "osId",required = true)
    private String osId;

    @Column(name = "OS_NAME")
    @Length(max = 200, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "系统名",name = "osName")
    private String osName;

    /**
     * 业务系统后台url
     */
    @Column(name = "OS_URL")
    @Length(max = 200, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "系统地址",name = "osUrl")
    private String osUrl;

    /**
     * 业务系统首页
     */
    @Column(name = "OS_HOME_PAGE")
    @Length(max = 300, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "系统地址",name = "osHomePage")
    private String osHomePage;

    @Column(name = "OAUTH_USER")
    @Length(max = 32, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "oauth2 登录用户名",name = "oauthUser")
    private String oauthUser;

    @Column(name = "OAUTH_PASSWORD")
    @Length(max = 128, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "oauth2 登录密码 密文",name = "oauthPassword")
    private String oauthPassword;

    /**
     * 关联的顶层菜单ID
     */
    @Column(name = "REL_OPT_ID")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String relOptId; // 顶层业务编号

    @Column(name = "CREATED")
    @Length(max = 8, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName = "createUserName", value = "userCode")
    private String created;

    @ValueGenerator( strategy= GeneratorType.FUNCTION, value = "today()", condition = GeneratorCondition.ALWAYS )
    @Column(name = "LAST_MODIFY_DATE")
    private Date lastModifyDate;

    @ValueGenerator( strategy= GeneratorType.FUNCTION, value = "today()")
    @Column(name = "CREATE_TIME")
    private Date createTime;

    // Constructors
    /**
     * default constructor
     */
    public OsInfo() {
    }

    public OsInfo(String osId, String osName) {
        this.osId = osId;
        this.osName = osName;
    }

    public String getOsId() {
        return osId;
    }

    public void setOsId(String osId) {
        this.osId = osId;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsUrl() {
        return osUrl;
    }

    public void setOsUrl(String osUrl) {
        this.osUrl = osUrl;
    }

    public String getOsHomePage() {
        return osHomePage;
    }

    public void setOsHomePage(String osHomePage) {
        this.osHomePage = osHomePage;
    }

    public String getOauthUser() {
        return oauthUser;
    }

    public void setOauthUser(String oauthUser) {
        this.oauthUser = oauthUser;
    }

    public String getOauthPassword() {
        return oauthPassword;
    }

    public void setOauthPassword(String oauthPassword) {
        this.oauthPassword = oauthPassword;
    }

    public String getRelOptId() {
        return relOptId;
    }

    public void setRelOptId(String relOptId) {
        this.relOptId = relOptId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
