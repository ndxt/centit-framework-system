package com.centit.framework.system.po;

import com.centit.framework.core.po.EntityWithTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name = "F_OPTFLOWNOPOOL")
public class OptFlowNoPool implements EntityWithTimestamp, java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private OptFlowNoPoolId cid;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createDate;

    @Column(name = "LAST_MODIFY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifyDate;

    // Constructors

    /**
     * default constructor
     */
    public OptFlowNoPool() {
    }

    /**
     * minimal constructor
     * @param id OptFlowNoPoolId
     */
    public OptFlowNoPool(OptFlowNoPoolId id) {
        this.cid = id;
    }

    public OptFlowNoPool(OptFlowNoPoolId id
            , Date lastCodeDate) {
        this.cid = id;
        this.createDate = lastCodeDate;
    }

    public OptFlowNoPoolId getCid() {
        return this.cid;
    }

    public void setCid(OptFlowNoPoolId id) {
        this.cid = id;
    }

    public String getOwnerCode() {
        if (this.cid == null)
            this.cid = new OptFlowNoPoolId();
        return this.cid.getOwnerCode();
    }

    public void setOwnerCode(String ownerCode) {
        if (this.cid == null)
            this.cid = new OptFlowNoPoolId();
        this.cid.setOwnerCode(ownerCode);
    }

    public Date getCodeDate() {
        if (this.cid == null)
            this.cid = new OptFlowNoPoolId();
        return this.cid.getCodeDate();
    }

    public void setCodeDate(Date codeDate) {
        if (this.cid == null)
            this.cid = new OptFlowNoPoolId();
        this.cid.setCodeDate(codeDate);
    }

    public String getCodeCode() {
        if (this.cid == null)
            this.cid = new OptFlowNoPoolId();
        return this.cid.getCodeCode();
    }

    public void setCodeCode(String codeCode) {
        if (this.cid == null)
            this.cid = new OptFlowNoPoolId();
        this.cid.setCodeCode(codeCode);
    }

    public Long getCurNo() {
        if (this.cid == null)
            this.cid = new OptFlowNoPoolId();
        return this.cid.getCurNo();
    }

    public void setCurNo(Long curNo) {
        if (this.cid == null)
            this.cid = new OptFlowNoPoolId();
        this.cid.setCurNo(curNo);
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	@Override
    public Date getLastModifyDate() {
        return lastModifyDate;
    }

	@Override
    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }
	

    public void copy(OptFlowNoPool other) {

        this.setOwnerCode(other.getOwnerCode());
        this.setCodeDate(other.getCodeDate());
        this.setCodeCode(other.getCodeCode());
        this.setCurNo(other.getCurNo());

        this.createDate = other.getCreateDate();

    }

    public void copyNotNullProperty(OptFlowNoPool other) {

        if (other.getOwnerCode() != null)
            this.setOwnerCode(other.getOwnerCode());
        if (other.getCodeDate() != null)
            this.setCodeDate(other.getCodeDate());
        if (other.getCodeCode() != null)
            this.setCodeCode(other.getCodeCode());

        if (other.getCurNo() != null)
            this.setCurNo(other.getCurNo());

        if (other.getCreateDate() != null)
            this.createDate = other.getCreateDate();

    }

    public void clearProperties() {

        this.createDate = null;

    }
}
