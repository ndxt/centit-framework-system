package com.centit.framework.system.po;

import com.centit.framework.core.dao.DictionaryMap;
import com.centit.framework.core.po.EntityWithTimestamp;
import com.centit.framework.model.basedata.IDataCatalog;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.HtmlUtils;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * FAddressBook entity.
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name = "F_DATACATALOG")
public class DataCatalog implements EntityWithTimestamp,IDataCatalog, java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CATALOG_CODE")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private String catalogCode; // 类别代码

    @Column(name = "CATALOG_NAME")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String catalogName;// 类别名称

    @Column(name = "CATALOG_STYLE")
    @NotBlank(message = "字段不能为空")
    @Length(max = 1, message = "字段长度必须为{max}")
    @Pattern(regexp = "[SUF]", message = "字段只能填写F,S,U")
    private String catalogStyle;// 类别状态

    @Column(name = "CATALOG_TYPE")
    @NotBlank(message = "字段不能为空")
    @Length(max = 1, message = "字段长度必须为{max}")
    @Pattern(regexp = "[LT]", message = "字段只能填写L或T")
    private String catalogType;// 类别形式

    @Column(name = "CATALOG_DESC")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String catalogDesc;// 类别描述

    @Column(name = "FIELD_DESC")
    @Length(max = 1024, message = "字段长度不能大于{max}")
    private String fieldDesc; // 字典字段描述

    @Column(name = "NEED_CACHE")
    @NotBlank(message = "字段不能为空")
    @Length(max = 1, message = "字段长度必须为{max}")
    // 默认值为1如何设置？
    private String needCache; // 是否需要缓存

    @Column(name = "OPT_ID")
    @Length(max = 16, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName="optName", value="optId")
    private String optId;

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
    private List<DataDictionary> dataDictionaries;

    // Constructors
    /** default constructor */
    /**
     * minimal constructor
     *
     * @param catalogcode catalogcode
     * @param catalogname catalogname
     * @param catalogstyle catalogstyle
     * @param catalogtype catalogtype
     * @param isupload isupload
     */
    public DataCatalog(String catalogcode, String catalogname, String catalogstyle, String catalogtype, String isupload) {

        this.catalogCode = catalogcode;

        this.catalogName = catalogname;
        this.catalogStyle = catalogstyle;
        this.catalogType = catalogtype;
        this.needCache = "1";
    }

    /**
     * full constructor
     * @param catalogcode String
     * @param catalogname String
     * @param catalogstyle String
     * @param catalogtype String
     * @param catalogdesc String
     * @param isupload String
     * @param needCache String
     * @param fielddesc String
     */
    public DataCatalog(String catalogcode, String catalogname, String catalogstyle, String catalogtype,
                       String catalogdesc, String isupload, String needCache, String fielddesc) {

        this.catalogCode = catalogcode;

        this.catalogName = catalogname;
        this.catalogStyle = catalogstyle;
        this.catalogType = catalogtype;
        this.catalogDesc = catalogdesc;
        this.fieldDesc = fielddesc;
        this.needCache = needCache;
    }

 
    public String getCatalogCode() {
        return this.catalogCode;
    }

    public void setCatalogCode(String catalogcode) {
        this.catalogCode = catalogcode;
    }

    // Property accessors

    public String getOptId() {
        return optId;
    }

    public void setOptId(String dictionarytype) {
        this.optId = dictionarytype;
    }

    public String getCatalogName() {
        return this.catalogName;
    }

    public void setCatalogName(String catalogname) {
        this.catalogName = catalogname;
    }

    public String getCatalogStyle() {
        return this.catalogStyle;
    }

    public void setCatalogStyle(String catalogstyle) {
        this.catalogStyle = catalogstyle;
    }

    public String getCatalogType() {
        return this.catalogType;
    }

    public void setCatalogType(String catalogtype) {
        this.catalogType = catalogtype;
    }

    public String getCatalogDesc() {
        return this.catalogDesc;
    }

    public void setCatalogDesc(String catalogdesc) {
        this.catalogDesc = catalogdesc;
    }

    public String getFieldDesc() {
        return this.fieldDesc;
    }

    public void setFieldDesc(String fielddesc) {
    	
    	if (StringUtils.isNotBlank(fielddesc)) {
    		fielddesc = HtmlUtils.htmlUnescape(fielddesc);
    	}
    	
        this.fieldDesc = fielddesc;
    }

    public void setIsUpload(String isupload) {
    }

    /**
     * @return the needCache
     */
    public String getNeedCache() {
        return needCache;
    }

    /**
     * @param needCache the needCache to set
     */
    public void setNeedCache(String needCache) {
        this.needCache = needCache;
    }
    
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
    public void copy(DataCatalog other) {

        this.catalogName = other.getCatalogName();
        this.catalogStyle = other.getCatalogStyle();
        this.catalogType = other.getCatalogType();
        this.catalogDesc = other.getCatalogDesc();
        this.fieldDesc = other.getFieldDesc();
        this.optId = other.getOptId();
        this.needCache = other.getNeedCache();
        this.creator=other.creator;
        this.updator=other.updator;
        this.updateDate=other.updateDate;
    }

    public void copyNotNullProperty(DataCatalog other) {

        if (other.getCatalogName() != null)
            this.catalogName = other.getCatalogName();
        if (other.getCatalogStyle() != null)
            this.catalogStyle = other.getCatalogStyle();
        if (other.getCatalogType() != null)
            this.catalogType = other.getCatalogType();
        if (other.getCatalogDesc() != null)
            this.catalogDesc = other.getCatalogDesc();
        if (other.getFieldDesc() != null)
            this.fieldDesc = other.getFieldDesc();
        if (other.getOptId() != null) {
            this.optId = other.getOptId();
            this.needCache = other.getOptId() == null ? "1" : other.getNeedCache();
        }
        if (other.getCreator() != null)
        	this.creator =other.getCreator();
        if (other.getUpdator() != null)
        	this.updator =other.getUpdator();
        if (other.getUpdateDate() != null)
        	this.updateDate =other.getUpdateDate();
    }


    public void addAllDataPiece(List<DataDictionary> dataDictionaries) {
        getDataDictionaries().clear();

        if (CollectionUtils.isEmpty(dataDictionaries)) {
            return;
        }

        for (DataDictionary dataDictionary : dataDictionaries) {
            dataDictionary.setCatalogCode(this.catalogCode);
            getDataDictionaries().add(dataDictionary);
        }
    }

    public List<DataDictionary> getDataDictionaries() {
        if (null == dataDictionaries) {
            dataDictionaries = new ArrayList<>();
        }
        return dataDictionaries;
    }

    public void setDataDictionaries(List<DataDictionary> dataDictionaries) {
        this.dataDictionaries = dataDictionaries;
    }

    public DataCatalog() {
    }

}
