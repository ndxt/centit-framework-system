package com.otherpackage.po;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "TEST_CASE")
@Data
public class TestCase implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 类别代码
     */
    @Id
    @Column(name = "CASE_ID")
    private String caseId;

    @Size(max = 20)
    private String name;

    @Range(max = 120)
    private Integer age;
}
