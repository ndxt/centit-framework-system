package com.centit.framework.system.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Date;


/**
 * create by scaffold 2016-03-01
 * @author codefan@sina.com

  系统内置查询方式null
*/
@Entity
@Table(name = "F_QUERY_FILTER_CONDITION")
@ApiModel(value="系统内置查询方式",description="系统内置查询方式对象 QueryFilterCondition")
public class QueryFilterCondition implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

    /**
     * 过滤条件序号 null
     */
    @Id
    @Column(name = "CONDITION_NO")
    //@GeneratedValue(generator = "assignedGenerator")
    @ApiModelProperty(value = "过滤条件序号",name = "conditionNo")
    private Long conditionNo;

    /**
     * 数据库表代码 数据库表代码或者po的类名
     */
    @Column(name = "TABLE_CLASS_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 64, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "数据库表代码或者po的类名",name = "tableClassName",required = true)
    private String  tableClassName;
    /**
     * 参数名 参数名
     */
    @Column(name = "PARAM_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 64, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "参数名 字段不能为空",name = "paramName",required = true)
    private String  paramName;
    /**
     * 参数提示 参数输入框提示
     */
    @Column(name = "PARAM_LABEL")
    @NotBlank(message = "字段不能为空")
    @Length(max = 120, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "参数输入框提示 字段不能为空",name = "paramLabel",required = true)
    private String  paramLabel;
    /**
     * 参数类型 参数类型：S 字符串，L 数字， N 有小数点数据， D 日期， T 时间戳， Y 年， M 月
     */
    @Column(name = "PARAM_TYPE")
    @Length(max = 8, message = "字段长度不能大于{max}")
    private String  paramType;
    /**
     * 参数默认值值 null
     */
    @Column(name = "DEFAULT_VALUE")
    @Length(max = 100, message = "字段长度不能大于{max}")
    private String  defaultValue;
    /**
     * 添加语句 过滤语句，将会拼装到sql语句中
     */
    @Column(name = "FILTER_SQL")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String  filterSql;
    /**
     * 参考类型 数据下拉框内容； N ：没有， D 数据字典, S 通过sql语句获得， J json数据直接获取

     */
    @Column(name = "SELECT_DATA_TYPE")
    @NotBlank(message = "字段不能为空")
    @Length(max = 1, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "参考类型 数据下拉框内容；N ：没有，D 数据字典, S 通过sql语句获得，J json数据直接获取",name = "selectDataType",required = true)
    private String  selectDataType;
    /**
     * 数据字典类别 数据字典
     */
    @Column(name = "SELECT_DATA_CATALOG")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String  selectDataCatalog;
    /**
     * SQL语句 有两个返回字段的sql语句
     */
    @Column(name = "SELECT_SQL")
    @Length(max = 1000, message = "字段长度不能大于{max}")
    private String  selectSql;
    /**
     * 枚举类型 KEY,Value数值对，JSON格式
     */
    @Column(name = "SELECT_JSON")
    @Length(max = 2000, message = "字段长度不能大于{max}")
    private String  selectJson;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createDate;
    // Constructors
    /** default constructor */
    public QueryFilterCondition() {
    }
    public QueryFilterCondition(
        Long conditionNo
        ,String  tableClassName,String  paramName,String  paramLabel,String  selectDataType) {


        this.conditionNo = conditionNo;

        this.tableClassName= tableClassName;
        this.paramName= paramName;
        this.paramLabel= paramLabel;
        this.selectDataType= selectDataType;
    }

    public QueryFilterCondition(
     Long conditionNo
    ,String  tableClassName,String  paramName,String  paramLabel,String  paramType,String  defaultValue,String  filterSql,String  selectDataType,String  selectDataCatalog,String  selectSql,String  selectJson) {


        this.conditionNo = conditionNo;

        this.tableClassName= tableClassName;
        this.paramName= paramName;
        this.paramLabel= paramLabel;
        this.paramType= paramType;
        this.defaultValue= defaultValue;
        this.filterSql= filterSql;
        this.selectDataType= selectDataType;
        this.selectDataCatalog= selectDataCatalog;
        this.selectSql= selectSql;
        this.selectJson= selectJson;
    }



    public Long getConditionNo() {
        return this.conditionNo;
    }

    public void setConditionNo(Long conditionNo) {
        this.conditionNo = conditionNo;
    }
    // Property accessors

    public String getTableClassName() {
        return this.tableClassName;
    }

    public void setTableClassName(String tableClassName) {
        this.tableClassName = tableClassName;
    }

    public String getParamName() {
        return this.paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamLabel() {
        return this.paramLabel;
    }

    public void setParamLabel(String paramLabel) {
        this.paramLabel = paramLabel;
    }

    public String getParamType() {
        return this.paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getFilterSql() {
        return this.filterSql;
    }

    public void setFilterSql(String filterSql) {
        this.filterSql = filterSql;
    }

    public String getSelectDataType() {
        return this.selectDataType;
    }

    public void setSelectDataType(String selectDataType) {
        this.selectDataType = selectDataType;
    }

    public String getSelectDataCatalog() {
        return this.selectDataCatalog;
    }

    public void setSelectDataCatalog(String selectDataCatalog) {
        this.selectDataCatalog = selectDataCatalog;
    }

    public String getSelectSql() {
        return this.selectSql;
    }

    public void setSelectSql(String selectSql) {
        this.selectSql = selectSql;
    }

    public String getSelectJson() {
        return this.selectJson;
    }

    public void setSelectJson(String selectJson) {
        this.selectJson = selectJson;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
