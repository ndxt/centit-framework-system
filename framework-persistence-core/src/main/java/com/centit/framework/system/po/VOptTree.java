package com.centit.framework.system.po;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Table(name = "V_OPT_TREE")
public class VOptTree {

    @Id
    @Column(name = "MENU_ID")
    @GeneratedValue(generator = "assignedGenerator")
    private String menu_id;

    @Column(name = "PARENT_ID")
    @Length(max = 8, message = "字段长度不能大于{max}")
    private String parent_id;

    @Column(name = "MENU_NAME")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String menu_name;

    @Column(name = "ORDER_IND")
    @Length(max = 4, message = "字段长度不能大于{max}")
    private Long orderInd; // 业务顺序

    public VOptTree() {

    }

    public Long getOrderInd() {
        return orderInd;
    }

    public void setOrderInd(Long orderInd) {
        this.orderInd = orderInd;
    }


    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }
}
