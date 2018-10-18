package com.otherpackage.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TEST_CASE")
public class TestCase implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 类别代码
     */
    @Id
    @Column(name = "CASE_ID")
    private String caseId;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }
}
