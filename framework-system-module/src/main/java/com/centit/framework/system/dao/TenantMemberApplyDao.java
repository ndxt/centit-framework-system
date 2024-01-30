package com.centit.framework.system.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.po.TenantMemberApply;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class TenantMemberApplyDao extends BaseDaoImpl<TenantMemberApply,String> {


    /**
     * 还未审批条件
     */
    public static final String[] NOT_APPROVE_ARRAY = {"1","2"};
    /**
     * 已经审批列表条件
     */
    public static final String[] HAS_APPROVE_ARRAY= {"3","4"};

    /**
     * 同意条件
     */
    public static final String[] HAS_AGREE = {"3"};
    /**
     * 不同意条件
     */
    public static final String[] NOT_AGREE = {"4"};

    /**
     * 用户主动申请
     */
    public static final String APPLYTYPE_APPLY = "1";

    /**
     * 租户邀请
     */
    public static  final String APPLYTYPE_INVITED = "2";


    /**
     * 保存数据前查看数据是否存在，如果存在把applyState修改为重新申请
     * @param tenantMemberApply
     */
    @Transactional
    public void saveTenantMemberApply(TenantMemberApply tenantMemberApply) {
        TenantMemberApply oldTenantMemberApply = super.getObjectById(tenantMemberApply);
        if (null == oldTenantMemberApply){
            tenantMemberApply.setApplyState("1");
            super.saveNewObject(tenantMemberApply);
        }else {
            //更新字段为空的数据
            tenantMemberApply.setApplyState("2");
            String[] fields = new String[]{"inviterUserCode","applyType","applyTime","applyState",
                "applyRemark","approveRemark","unitCode"};
            super.updateObject(fields,tenantMemberApply);
        }
    }


}
