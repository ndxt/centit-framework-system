package com.centit.framework.system.dao;

import com.centit.framework.hibernate.dao.BaseDao;
import com.centit.framework.system.po.RolePower;
import com.centit.framework.system.po.RolePowerId;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-10-29
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */

public interface RolePowerDao extends BaseDao<RolePower, RolePowerId> {

	//"DELETE FROM RolePower rp where rp.id.roleCode=?", rolecode
    void deleteRolePowersByRoleCode(String rolecode);
    
    //"DELETE FROM RolePower rp where rp.id.optCode=?", optecode
    void deleteRolePowersByOptCode(String optecode);
    
    //"FROM RolePower rp where rp.id.roleCode=?", rolecode
    List<RolePower> listRolePowersByRoleCode(String roleCode);
    
}
