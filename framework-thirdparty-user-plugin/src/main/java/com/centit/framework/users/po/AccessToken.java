package com.centit.framework.users.po;

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
import java.io.Serializable;
import java.util.Date;

/**
 * 授权所需的token
 */
//@Builder
@Entity
@Table(name = "T_ACCESS_TOKEN")
@ApiModel(value = "授权所需的token对象", description = "授权所需的token")
public class AccessToken implements Serializable {

    private static final long serialVersionUID = -4575022001861515379L;

    @Id
    @Column(name = "APPID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    @Length(max = 64)
    @ApiModelProperty(value = "APPID", name = "appId")
    private String appId;

    @Column(name = "ACCESSTOKEN")
    @ApiModelProperty(value = "获取到的凭证", name = "suiteid")
    private String accessToken;

    @Column(name = "EXPIREIN")
    @ApiModelProperty(value = "凭证有效时间，单位：秒", name = "suiteid")
    private long expireIn;

    @Column(name = "EXPIRETIME")
    @ApiModelProperty(value = "过期时间", name = "expireTime")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, value = "today()")
    private Date expireTime;

    @Column(name = "CREATETIME")
    @ApiModelProperty(value = "创建时间", name = "createTime")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, value = "today()")
    private Date createTime;

    @Column(name = "UPDATETIME")
    @ApiModelProperty(value = "更新时间", name = "updateTime")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date updateTime;

    private String refreshToken;
    private int refreshTokenExpireIn;
    private String uid;
    private String openId;
    private String accessCode;
    private String unionId;

    public AccessToken() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(long expireIn) {
        this.expireIn = expireIn;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getRefreshTokenExpireIn() {
        return refreshTokenExpireIn;
    }

    public void setRefreshTokenExpireIn(int refreshTokenExpireIn) {
        this.refreshTokenExpireIn = refreshTokenExpireIn;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}
