package com.centit.framework.system.po;

import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Date;
/**
 * create by scaffold 2016-02-29
 * @author codefan@sina.com

  用户自定义过滤条件表null
*/
@Entity
@Table(name = "F_USER_QUERY_FILTER")
@ApiModel(value="用户自定义过滤条件对象",description="用户自定义过滤条件对象 UserQueryFilter")
public class UserQueryFilter implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

    /**
     * 过滤条件序号 null
     */
    @Id
    @Column(name = "FILTER_NO")
    //@GeneratedValue(generator = "assignedGenerator")
    private Long filterNo;

    @Column(name = "USER_CODE")
    @NotBlank(message = "字段不能为空")
    @ApiModelProperty(value = "用户代码",name = "userCode",required = true)
    @DictionaryMap(fieldName = "userName", value = "userCode")
    private String userCode;// 用户代码

    /**
     * 所属模块编码 开发人员自行定义，单不能重复，建议用系统的模块名加上当前的操作方法
     */
    @Column(name = "MODLE_CODE")
    @NotBlank(message = "字段不能为空")
    @Length(max = 64, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "所属模块编码 开发人员自行定义，单不能重复，建议用系统的模块名加上当前的操作方法",name = "modleCode",required = true)
    private String  modleCode;
    /**
     * 筛选器名称 用户自行定义的名称
     */
    @Column(name = "FILTER_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 64, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "筛选器名称 用户自行定义的名称",name = "filterName",required = true)
    private String  filterName;
    /**
     * 条件变量名 变量值，json格式，对应一个map
     */
    @Column(name = "FILTER_VALUE")
    @NotBlank(message = "字段不能为空")
    @Length(max = 3200, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "条件变量名 变量值，json格式，对应一个map",name = "filterValue",required = true)
    private String  filterValue;

    @Column(name = "IS_DEFAULT")
    @NotBlank(message = "字段不能为空")
    @Length(max = 1, message = "字段长度不能大于{max}")
    private String  isDefault;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ValueGenerator(strategy= GeneratorType.FUNCTION,
                  condition= GeneratorCondition.ALWAYS, value="today()")
    protected Date createDate;

    // Constructors
    /** default constructor */
    public UserQueryFilter() {
    }
    public UserQueryFilter(
        Long filterNo    ,String  userCode
        ,String  modleCode,String  filterName,String  filterValue) {
        this.filterNo = filterNo;
        this.userCode= userCode;
        this.modleCode= modleCode;
        this.filterName= filterName;
        this.filterValue= filterValue;
    }

    public Long getFilterNo() {
        return this.filterNo;
    }

    public void setFilterNo(Long filterNo) {
        this.filterNo = filterNo;
    }
    // Property accessors

    public String getModleCode() {
        return this.modleCode;
    }

    public String getUserCode() {
        return userCode;
    }
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    public void setModleCode(String modleCode) {
        this.modleCode = modleCode;
    }

    public String getFilterName() {
        return this.filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getFilterValue() {
        return this.filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }
    /**
     * T : 最新查看， F： 其他
     * @return  IsDefault
     */
    public String getIsDefault() {
        return this.isDefault;
    }
    /**
     * T : 最新查看， F： 其他
     * @param isDefault isDefaults
     */
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
