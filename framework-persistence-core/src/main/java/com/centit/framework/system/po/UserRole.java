package com.centit.framework.system.po;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.po.EntityWithTimestamp;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.model.basedata.IUserRole;
import com.centit.framework.model.basedata.IUserUnit;
import io.swagger.annotations.ApiModel;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

/**
 * FUserrole entity.
 *
 * @author MyEclipse Persistence Tools
 */
// 用户角色设定
@Entity
@Table(name = "F_USERROLE")
@ApiModel(value="系统用户角色关联信息对象",description="系统用户角色关联信息对象 UserInfo")
public class UserRole implements IUserRole, EntityWithTimestamp, java.io.Serializable {

    // Fields
    // public final SimpleDateFormat sdfDate = new
    // SimpleDateFormat("yyyy-MM-dd");

    private static final long serialVersionUID = 8079422314053320707L;

    @EmbeddedId
    private UserRoleId id; // 主键

    @Column(name = "OBTAIN_DATE")
    @Temporal(TemporalType.DATE)
    private Date obtainDate; // 获得角色时间

    @Column(name = "SECEDE_DATE")
    @Temporal(TemporalType.DATE)
    private Date secedeDate;

    @Column(name = "CHANGE_DESC")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String changeDesc; // 说明

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
    private Date  updateDate;
    //结束

    @Transient
    private String obtainType;

    @Transient
    private String inheritedFrom;

    @Override
    public String getObtainType() {
      return obtainType;
    }

    public void setObtainType(String obtainType) {
      this.obtainType = obtainType;
    }

    @Override
    public String getInheritedFrom() {
      return inheritedFrom;
    }

    public void setInheritedFrom(String inheritedFrom) {
      this.inheritedFrom = inheritedFrom;
    }
    // Constructors

    /**
     * default constructor
     */
    public UserRole() {
        obtainType = "D";
        this.id=new UserRoleId();
    }

    /**
     * minimal constructor
     * @param id UserRoleId
     */
    public UserRole(UserRoleId id) {
        obtainType = "D";
        this.id = id;
    }

    /**
     * full constructor
     *
     * @param id UserRoleId
     * @param obtainDate Date
     * @param changedesc String
     */
    public UserRole(UserRoleId id, Date obtainDate, String changedesc) {
        obtainType = "D";
        this.id = id;
        this.obtainDate = obtainDate;
        this.changeDesc = changedesc;
    }

    public String getLoginName() {
        String userCode = getUserCode();

        if (null != userCode) {
          IUserInfo user = CodeRepositoryUtil.getUserInfoByCode(userCode);

          if (null != user) {
            return user.getLoginName();
          }
        }

        return null;
    }
    public String getUserPrimaryUnit() {
        String userCode = getUserCode();

        if (null != userCode) {
          IUserUnit unit = CodeRepositoryUtil.getUserPrimaryUnit(userCode);

          if (null != unit) {
            return unit.getUnitCode();
          }
        }

        return null;
    }

    // Property accessors

    public UserRoleId getId() {
        return this.id;
    }

    public void setId(UserRoleId id) {
        this.id = id;
    }

    public String getUserCode() {
        if (this.id == null)
            this.id = new UserRoleId();
        return this.id.getUserCode();
    }

    public void setUserCode(String userCode) {
        if (this.id == null)
            this.id = new UserRoleId();
        this.id.setUserCode(userCode);
    }

    public String getRoleCode() {
        if (this.id == null)
            this.id = new UserRoleId();
        return this.id.getRoleCode();
    }

    public void setRoleCode(String rolecode) {
        if (this.id == null)
            this.id = new UserRoleId();
        this.id.setRoleCode(rolecode);
    }

    public Date getObtainDate() {
        return this.obtainDate;
    }

    public void setObtainDate(Date obtaindate) {

        this.obtainDate =obtaindate;
    }

    public Date getSecedeDate() {
        return secedeDate;
    }

    public void setSecedeDate(Date secedeDate) {
        this.secedeDate = secedeDate;
    }
    /*
     * public void setSecededate(String ssecededate) { try { this.secededate =
     * sdfDate.parse(ssecededate); } catch (ParseException e) {
     * e.printStackTrace(); } }
     */

    public String getChangeDesc() {
        return this.changeDesc;
    }

    public void setChangeDesc(String changedesc) {
        this.changeDesc = changedesc;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void copy(UserRole other) {

        this.setId(other.getId());
        this.obtainDate = other.getObtainDate();
        this.changeDesc = other.getChangeDesc();
        this.creator=other.creator;
        this.updator=other.updator;
        this.updateDate=other.updateDate;
        this.secedeDate =other.secedeDate;
        this.createDate = other.getCreateDate();
    }

    public void copyNotNullProperty(UserRole other) {
        if(other.getId()!=null)
            this.setId(other.getId());
        if (other.getObtainDate() != null)
            this.obtainDate = other.getObtainDate();
        if (other.getChangeDesc() != null)
            this.changeDesc = other.getChangeDesc();
        if (other.getCreator() != null)
            this.creator =other.getCreator();
        if (other.getUpdator() != null)
            this.updator =other.getUpdator();
        if (other.getUpdateDate() != null)
            this.updateDate =other.getUpdateDate();
        if (other.getCreateDate() != null)
            this.createDate = other.getCreateDate();

        this.secedeDate =other.secedeDate;
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

      @Override
      public Date getLastModifyDate() {
          return updateDate;
    }

      @Override
      public void setLastModifyDate(Date lastModifyDate) {
          this.updateDate = lastModifyDate;
    }
    //结束
}
