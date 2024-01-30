package com.centit.framework.users.po;

import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 钉钉套件实体类
 */
@Entity
@Table(name = "T_DT_SUITE")
@ApiModel(value = "钉钉套件实体类对象", description = "钉钉套件实体类对象 DingTalkSuite")
public class DingTalkSuite implements Serializable {

    private static final long serialVersionUID = 4136196817317530554L;

    @Id
    @Column(name = "ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    @Length(max = 64)
    @ApiModelProperty(value = "主键", name = "id")
    private String id;

    @Column(name = "SUITEID")
    @ApiModelProperty(value = "套件id", name = "suiteid")
    private String suiteid;

    @Column(name = "NAME")
    @ApiModelProperty(value = "套件名称", name = "name")
    private String name;

    @Column(name = "TOKEN")
    @ApiModelProperty(value = "注册套件的之时填写的token", name = "token")
    private String token;

    @Column(name = "ENCODING_AES_KEY")
    @ApiModelProperty(value = "注册套件的之时生成的数据加密密钥", name = "encodingAesKey")
    private String encodingAesKey;

    @Column(name = "SUITE_KEY")
    @ApiModelProperty(value = "套件注册成功后生成的suite_key", name = "suiteKey")
    private String suiteKey;

    @Column(name = "SUITE_SECRET")
    @ApiModelProperty(value = "套件注册成功后生成的suite_secret", name = "suiteSecret")
    private String suiteSecret;

    @Column(name = "SUIT_TICKET")
    @ApiModelProperty(value = "钉钉后台推送的套件ticket", name = "suitTicket")
    private String suitTicket;

    @Column(name = "SUITE_ACCESS_TOKEN")
    @ApiModelProperty(value = "获取套件访问Token（suite_access_token）", name = "suiteAccessToken")
    private String suiteAccessToken;

    @Column(name = "CREATETIME")
    @ApiModelProperty(value = "创建时间", name = "createTime")
    @Temporal(TemporalType.TIMESTAMP)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, value = "today()")
    private String createTime;

    @Column(name = "UPDATETIME")
    @ApiModelProperty(value = "更新时间", name = "updateTime")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE,
        condition = GeneratorCondition.ALWAYS, value = "today()")
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuiteid() {
        return suiteid;
    }

    public void setSuiteid(String suiteid) {
        this.suiteid = suiteid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    public String getSuiteKey() {
        return suiteKey;
    }

    public void setSuiteKey(String suiteKey) {
        this.suiteKey = suiteKey;
    }

    public String getSuiteSecret() {
        return suiteSecret;
    }

    public void setSuiteSecret(String suiteSecret) {
        this.suiteSecret = suiteSecret;
    }

    public String getSuitTicket() {
        return suitTicket;
    }

    public void setSuitTicket(String suitTicket) {
        this.suitTicket = suitTicket;
    }

    public String getSuiteAccessToken() {
        return suiteAccessToken;
    }

    public void setSuiteAccessToken(String suiteAccessToken) {
        this.suiteAccessToken = suiteAccessToken;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "DingTalkSuiteService [id=" + id + ", suiteid=" + suiteid + ", name=" + name + ", token=" + token
            + ", encoding_aes_key=" + encodingAesKey + ", suite_key=" + suiteKey + ", suite_secret="
            + suiteSecret + ", suit_ticket=" + suitTicket + ", suite_access_token=" + suiteAccessToken
            + ", createtime=" + createTime + ", updatetime=" + updateTime + "]";
    }

}
