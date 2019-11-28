package com.centit.framework.system.po;

import com.centit.framework.core.dao.DictionaryMap;
import com.centit.framework.model.basedata.IOptInfo;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
// 业务模块表
@Entity
@Table(name = "F_OPTINFO")
@ApiModel(value="业务菜单对象",description="业务菜单对象 OptInfo")
public class OptInfo implements IOptInfo, java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "OPT_ID")
    @ValueGenerator(strategy = GeneratorType.UUID)
    @ApiModelProperty(value = "业务菜单编号",name = "optId",required = true)
    private String optId; // 业务编号

    @OrderBy
    @Column(name = "PRE_OPT_ID")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String preOptId; // 上级业务模块编号

    @Column(name = "OPT_NAME")
    @Length(max = 100, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "业务菜单名称，字段长度不能大于100",name = "optName",required = true)
    private String optName; // 业务名称
    /**
     * S:实施业务, O:普通业务, W:流程业务, I:项目业务
     */
    @Column(name = "OPT_TYPE")
    @Length(max = 1, message = "字段长度必须为{max}")
    @ApiModelProperty(value = "业务类别 S:实施业务, O:普通业务, W:流程业务, I:项目业务",name = "optType")
    @DictionaryMap(fieldName = "optTypeText", value = "OptType")
    private String optType; // 业务类别

    @Column(name = "FORM_CODE")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String formCode; // 界面代码(C/S)

    /**
     * 系统菜单路由
     *
     * 与angularjs路由匹配
     */
    @Column(name = "OPT_ROUTE")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String optRoute;

    @Column(name = "OPT_URL")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String optUrl; // 业务url（b/s）

    @Column(name = "IS_IN_TOOLBAR")
    private String isInToolbar; // 是否放入工具栏

    @Column(name = "IMG_INDEX")
    @Range(max = 100000, message = "数值不能大于{max}")
    private Long imgIndex; // 图标编号

    @Column(name = "TOP_OPT_ID")
    @Length(max = 32, message = "字段长度不能大于{max}")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, value = "optId")
    private String topOptId; // 顶层业务编号

    @Column(name = "PAGE_TYPE")
    @Length(max = 1, message = "字段长度必须为{max}")
    @ApiModelProperty(value = "页面打开方式 D: DIV I： iFrame",name = "pageType",required = true)
    private String pageType; // 页面打开方式 D: DIV I： iFrame

    @OrderBy
    @Column(name = "ORDER_IND")
    @Range(max = 100000, message = "数值不能大于{max}")
    private Long orderInd; // 业务顺序

    @Column(name = "ICON")
    @Length(max = 512, message = "字段长度不能大于{max}")
    private String icon;// 图标

    @Column(name = "HEIGHT")
    @Range(max = 100000, message = "数值不能大于{max}")
    private Long height;// 高度

    @Column(name = "WIDTH")
    @Range(max = 100000, message = "数值不能大于{max}")
    private Long width;// 宽度

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createDate;

    //创建人、更新人、更新时间
    /**
     * CREATOR(创建人) 创建人
     */
    @Column(name = "CREATOR")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String  creator;
       /**
     * UPDATOR(更新人) 更新人
     */
    @Column(name = "UPDATOR")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String  updator;
    /**
     * UPDATEDATE(更新时间) 更新时间
     */
    @Column(name = "UPDATE_DATE")
    @ValueGenerator(strategy= GeneratorType.FUNCTION, condition= GeneratorCondition.ALWAYS, value="today()")
    private Date  updateDate;
    //结束

    @Transient
    private List<OptInfo> children;

    @Transient
    private String state;

    @Override
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    @Transient
    private List<OptMethod> optMethods;

    @Transient
    private List<OptDataScope> dataScopes;

    // Constructors

    public List<OptDataScope> getDataScopes() {
         if (null == dataScopes) {
             dataScopes = new ArrayList<>();
         }
        return dataScopes;
    }
    public void setDataScopes(List<OptDataScope> dataScopes) {
        this.dataScopes = dataScopes;
    }
    /**
     * default constructor
     */
    public OptInfo() {
    }

    public OptInfo(String optid, String optname) {

        this.optId = optid;

        this.optName = optname;
    }
    public String getOptId() {
        return this.optId;
    }

    public void setOptId(String optid) {
        this.optId = optid;
    }

    // Property accessors

    public String getPreOptId() {
        return this.preOptId;
    }

    public void setPreOptId(String preoptid) {
        this.preOptId = preoptid;
    }

    public String toString() {
        return this.optName;
    }

    public String getOptName() {
        return this.optName;
    }

    public void setOptName(String optname) {
        this.optName = optname;
    }

    public String getFormCode() {
        return this.formCode;
    }

    public void setFormCode(String formcode) {
        this.formCode = formcode;
    }

    public String getOptUrl() {
        if (this.optUrl == null)
            return "...";
        return this.optUrl;
    }

    public void setOptUrl(String opturl) {
        this.optUrl = opturl;
    }

    public String getIsInToolbar() {
        return this.isInToolbar;
    }

    public void setIsInToolbar(String isintoolbar) {
        this.isInToolbar = isintoolbar;
    }

    public Long getImgIndex() {
        return this.imgIndex;
    }

    public void setImgIndex(Long imgindex) {
        this.imgIndex = imgindex;
    }

    public String getTopOptId() {
        return this.topOptId;
    }

    public void setTopOptId(String topoptid) {
        this.topOptId = topoptid;
    }

    /**
     * S:实施业务, O:普通业务, W:流程业务, I:项目业务
     * @return OptType
     */
    public String getOptType() {
        return this.optType;
    }

    /**
     * S:实施业务, O:普通业务, W:流程业务, I:项目业务
     * @param opttype  opttype
     */
    public void setOptType(String opttype) {
        this.optType = opttype;
    }

    public Long getOrderInd() {
        return this.orderInd;
    }

    public void setOrderInd(Long orderind) {
        this.orderInd = orderind;
    }

    public String getOptRoute() {
        return optRoute;
    }

    public void setOptRoute(String optRoute) {
        this.optRoute = optRoute;
    }

    /**
     * 页面打开方式 D: DIV I： iFrame
     *
     * @return PageType
     */
    public String getPageType() {
        return pageType;
    }

    /**
     * 页面打开方式 D: DIV I： iFrame
     *
     * @param pageType pageType
     */
    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
        this.creator=other.creator;
        this.updator=other.updator;
        this.updateDate=other.updateDate;
        this.state = other.getState();
        this.createDate = other.getCreateDate();
    }
    public List<OptInfo> getChildren() {
        return children;
    }

    public void  addChild(OptInfo child) {
        if(children==null)
            children = new ArrayList<>();
        children.add(child);
    }

    public void setChildren(List<OptInfo> children) {
        this.children = children;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public List<OptMethod> getOptMethods() {
        if (null == optMethods) {
            optMethods = new ArrayList<>();
        }
        return optMethods;
    }

    public void setOptMethods(List<OptMethod> optDefs) {
        this.optMethods = optDefs;
    }

    public OptInfo(String optId) {
        super();
        this.optId = optId;
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

    //创建人、更新人、更新时间
    public String getCreator() {
          return this.creator;
      }

      public void setCreator(String creator) {
          this.creator = creator;
      }

      public String getUpdator() {
          return this.updator;
      }

      public void setUpdator(String updator) {
          this.updator = updator;
      }

      public Date getUpdateDate() {
          return updateDate;
      }

      public void setUpdateDate(Date updateDate) {
          this.updateDate = updateDate;
      }


}
