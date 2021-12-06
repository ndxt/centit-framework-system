package com.centit.framework.system.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.dao.DictionaryMap;
//import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.model.basedata.IOptInfo;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OptInfo entity.
 *
 * @author codefan@codefan.com
 */
@Data
@Entity
@Table(name = "F_OPTINFO")
@ApiModel(value = "业务菜单对象", description = "业务菜单对象 OptInfo")
public class OptInfo implements IOptInfo, java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "OPT_ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    @ApiModelProperty(value = "业务菜单编号", name = "optId", required = true)
    private String optId;

    @Column(name = "OS_ID")
    @Length(max = 32, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "应用系统ID", name = "osId", required = true)
    private String osId;

    @OrderBy
    @Column(name = "PRE_OPT_ID")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String preOptId;
    @Column(name = "OPT_NAME")
    @Length(max = 256, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "业务菜单名称，字段长度不能大于256", name = "optName", required = true)
    private String optName;
    /**
     * S:实施业务, O:普通业务, W:流程业务, I:项目业务
     */
    @Column(name = "OPT_TYPE")
    @Length(max = 1, message = "字段长度必须为{max}")
    @ApiModelProperty(value = "业务类别 S:实施业务, O:普通业务, W:流程业务, I:项目业务, C:通用模块, A:应用入口页面", name = "optType")
    @DictionaryMap(fieldName = "optTypeText", value = "OptType")
    private String optType;
    @Column(name = "FORM_CODE")
    @Length(max = 32, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "业务类别 O:普通业务C:通用模块A:应用入口页面", name = "formCode")
    private String formCode;
    @Column(name = "OPT_ROUTE")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String optRoute;
    @Column(name = "OPT_URL")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String optUrl;
    @Column(name = "IS_IN_TOOLBAR")
    private String isInToolbar;
    @Column(name = "IMG_INDEX")
    @Range(max = 100000, message = "数值不能大于{max}")
    private Long imgIndex;
    @Column(name = "TOP_OPT_ID")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String topOptId;
    @Column(name = "PAGE_TYPE")
    @Length(max = 1, message = "字段长度必须为{max}")
    @ApiModelProperty(value = "页面打开方式 D: DIV I： iFrame", name = "pageType", required = true)
    private String pageType;
    @OrderBy
    @Column(name = "ORDER_IND")
    @Range(max = 100000, message = "数值不能大于{max}")
    private Long orderInd;
    @Column(name = "ICON")
    @Length(max = 512, message = "字段长度不能大于{max}")
    private String icon;
    @Column(name = "HEIGHT")
    @Range(max = 100000, message = "数值不能大于{max}")
    private Long height;
    @Column(name = "WIDTH")
    @Range(max = 100000, message = "数值不能大于{max}")
    private Long width;
    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createDate;
    @Column(name = "CREATOR")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String creator;
    @Column(name = "UPDATOR")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String updator;
    @Column(name = "UPDATE_DATE")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date updateDate;
    @Column(name = "DOC_ID")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String docId;
    @Transient
    private List<OptInfo> children;
    @Transient
    private String state;
    @Transient
    private List<OptMethod> optMethods;
    @Transient
    private List<OptDataScope> dataScopes;

    public List<OptDataScope> getDataScopes() {
        if (null == dataScopes) {
            dataScopes = new ArrayList<>();
        }
        return dataScopes;
    }

    @Override
    public String getLocalOptName() {
        String lang = "";
      /*  String lang = WebOptUtils.getCurrentLang(
            RequestThreadLocal.getLocalThreadWrapperRequest()
        );*/
        if (optName == null) {
            return null;
        }
        if (this.optName.startsWith("{") && this.optName.endsWith("}")) {
            JSONObject jsonData = JSON.parseObject(optName);
            String sRet = null;
            if (lang == null) {
                sRet = jsonData.getString("zh_CN");
            } else {
                sRet = jsonData.getString(lang);
                if (sRet == null) {
                    sRet = jsonData.getString("zh_CN");
                }
            }
            return sRet;
        }
        return optName;
    }

    @Override
    public String getOptUrl() {
        if (this.optUrl == null) {
            return "...";
        }
        return this.optUrl;
    }


    public void copy(OptInfo other) {
        this.preOptId = other.getPreOptId();
        this.optName = other.getOptName();
        this.formCode = other.getFormCode();
        this.optUrl = other.getOptUrl();
        this.isInToolbar = other.getIsInToolbar();
        this.imgIndex = other.getImgIndex();
        this.topOptId = other.getTopOptId();
        this.optType = other.getOptType();
        this.orderInd = other.getOrderInd();
        this.pageType = other.getPageType();
        this.icon = other.getIcon();
        this.height = other.getHeight();
        this.width = other.getWidth();
        this.optRoute = other.getOptRoute();
        this.creator = other.creator;
        this.updator = other.updator;
        this.updateDate = other.updateDate;
        this.state = other.getState();
        this.createDate = other.getCreateDate();
        this.osId = other.getOsId();
        this.docId = other.getDocId();
    }

    public void addChild(OptInfo child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    public List<OptMethod> getOptMethods() {
        if (null == optMethods) {
            optMethods = new ArrayList<>();
        }
        return optMethods;
    }

    public void addOptMethod(OptMethod optDef) {
        getOptMethods().add(optDef);
    }

    public void addAllOptMethods(List<OptMethod> optDefs) {
        getOptMethods().clear();
        if (CollectionUtils.isEmpty(optDefs)) {
            return;
        }
        for (OptMethod optDef : optDefs) {
            optDef.setOptId(this.optId);
        }
        getOptMethods().addAll(optDefs);
    }

    public void addAllDataScopes(List<OptDataScope> dataScopeByOptID) {
        getDataScopes().clear();
        if (CollectionUtils.isEmpty(dataScopeByOptID)) {
            return;
        }

        for (OptDataScope dataScope : dataScopeByOptID) {
            dataScope.setOptId(this.optId);
        }

        getDataScopes().addAll(dataScopeByOptID);
    }
}
