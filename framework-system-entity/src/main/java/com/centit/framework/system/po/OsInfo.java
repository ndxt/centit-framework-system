package com.centit.framework.system.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.framework.model.basedata.IOsInfo;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import lombok.Data;

@Entity
@Data
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

    @Column(name = "OS_TYPE")
    @Length(max = 16, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "系统名",name = "osName")
    private String osType;
    /**
     * 业务系统后台url
     */
    @Column(name = "OS_URL")
    @Length(max = 200, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "系统地址",name = "osUrl")
    private String osUrl;

    @Column(name = "TOP_UNIT")
    @Length(max = 32, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "系统地址",name = "osUrl")
    private String topUnit;
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
    // 顶层业务编号
    private String relOptId;

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

    @ApiModelProperty(value = "应用设置")
    @Column(name = "PAGE_FLOW")
    @JSONField(serialize=false)
    @Basic(fetch = FetchType.LAZY)
    private String  pageFlow;

    @Column(name = "is_delete")
    @NotBlank(message = "字段不能为空[T/F]")
    @Length(max = 1, message = "字段长度不能大于{max}")
    private Boolean isDelete;

    @ApiModelProperty(value = "图片id")
    @Column(name = "pic_id")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String  picId;
}
