package com.centit.framework.system.po;

import com.centit.framework.model.basedata.IOptDataScope;
import com.centit.framework.model.basedata.IOptMethod;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

/**
 * OptMethod entity.
 *
 * @author codefan@codefan.com
 */
@Entity
@Table(name = "F_OPTDEF")
@ApiModel(value="操作方法对象",description="操作方法对象 OptMethod")
public class OptMethod implements IOptMethod, java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "OPT_CODE")
    //@GeneratedValue(generator = "assignedGenerator")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    @ApiModelProperty(value = "操作代码",name = "optCode")
    private String optCode;// 操作代码

    @Column(name = "OPT_NAME")
    @Length(max = 100, message = "字段长度必须小于{max}")
    @ApiModelProperty(value = "操作名称",name = "optName")
    private String optName; // 操作名称

    @Column(name = "OPT_ID")
    private String optId;

    @Column(name = "OPT_METHOD")
    @Length(max = 50, message = "字段长度必须小于{max}")
    @ApiModelProperty(value = "操作方法",name = "optMethod")
    private String optMethod;// 操作方法

    @Column(name = "OPT_DESC")
    @Length(max = 256, message = "字段长度必须小于{max}")
    @ApiModelProperty(value = "操作说明",name = "optDesc")
    private String optDesc; // 操作说明

    @Column(name = "IS_IN_WORKFLOW")
    @Length(max = 1, message = "字段长度必须为{max}")
    private String isInWorkflow;// 是否是流程操作

    @Column(name = "OPT_URL")
    @Length(max = 256, message = "字段长度必须小于{max}")
    private String optUrl;

    @Column(name = "OPT_REQ")
    @Length(max = 20, message = "字段长度必须小于{max}")
    private String optReq;


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
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE,
        condition = GeneratorCondition.ALWAYS, value="today()" )
    private Date  updateDate;

    /**
     * 方法排序号
     */
    @Column(name = "OPT_ORDER")
    private Integer optOrder;// 操作方法排序
    //结束

    // Constructors

    /**
     * default constructor
     */
    public OptMethod() {
    }

    /**
     * minimal constructor
     *
     * @param optcode String
     * @param optid String
     */
    public OptMethod(String optcode, String optid) {

        this.optCode = optcode;
        this.optId = optid;

    }

    public OptMethod(String optcode, String optname, String optid, String optmethod, String optdesc) {

        this.optCode = optcode;
        this.optName = optname;
        this.optMethod = optmethod;
        this.optId = optid;
        this.optDesc = optdesc;
    }

    public OptMethod(String optcode, String optname, String optid, String optmethod, String optdesc, String isinworkflow) {
        this.optCode = optcode;
        this.optName = optname;
        this.optMethod = optmethod;
        this.optId = optid;
        this.optDesc = optdesc;
        this.isInWorkflow = isinworkflow;
    }

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public String getOptCode() {
        return this.optCode;
    }

    public void setOptCode(String optcode) {
        this.optCode = optcode;
    }

    public String toString() {
        return this.optName;
    }

    // Property accessors

    public String getOptName() {
        return this.optName;
    }

    public void setOptName(String optname) {
        this.optName = optname;
    }

    public String getOptMethod() {
        return this.optMethod;
    }

    public void setOptMethod(String optmethod) {
        this.optMethod = optmethod;
    }

    public String getOptDesc() {
        return this.optDesc;
    }

    public void setOptDesc(String optdesc) {
        this.optDesc = optdesc;
    }

    public void setIsInWorkflow(String isinworkflow) {
        this.isInWorkflow = isinworkflow;
    }

    public String getIsInWorkflow() {
        return isInWorkflow;
    }

    public String getOptUrl() {
        return optUrl;
    }

    public void setOptUrl(String optUrl) {
        this.optUrl = optUrl;
    }

    public String getOptReq() {
        return optReq;
    }

    public void setOptReq(String optReq) {
        this.optReq = optReq;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setOptOrder(Integer optOrder) {
        this.optOrder = optOrder;
    }
    @Override
    public Integer getOptOrder() {
        return optOrder;
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

      public Date getLastModifyDate() {
          return updateDate;
    }

      public void setLastModifyDate(Date lastModifyDate) {
          this.updateDate = lastModifyDate;
    }
    //结束
      @Override
      public boolean equals(Object obj){
          if(obj==null)
              return false;
          if(this==obj)
              return true;

          if(obj instanceof IOptMethod){
              return StringUtils.equals(optCode , ((IOptMethod)obj).getOptCode());
          }

          if(obj instanceof IOptDataScope){
              return StringUtils.equals(optCode , ((IOptDataScope)obj).getOptScopeCode());
          }
          if(obj instanceof String){
              return StringUtils.equals(optCode , (String)obj);
          }
          return false;
      }

      @Override
      public int hashCode(){
        return optCode==null?0:optCode.hashCode();
    }
}
