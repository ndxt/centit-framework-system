package com.centit.framework.system.dao;

import com.centit.framework.hibernate.dao.BaseDao;
import com.centit.framework.system.po.RoleInfo;
import com.centit.framework.system.po.VOptTree;

import java.util.List;

public interface RoleInfoDao extends BaseDao<RoleInfo, String> {
	
    //DatabaseOptUtils.findObjectsByHql(this,"FROM VOptTree");
     List<VOptTree> getVOptTreeList();
    
    /**
     *         String hql = "select new map(def.optName as def_optname, def.optCode as def_optcode) "
                + "from OptMethod def, RolePower pow where def.optCode = pow.id.optCode and pow.id.roleCode = ?";

     * @param rolecode rolecode
     * @return List
     */
     List<Object> listRoleOptMethods(String rolecode);

    /**
     * select count(1) from f_userrole where rolecode=?
     * @param roleCode roleCode
     * @return int
     */
     int countRoleUserSum(String roleCode);
    
}
