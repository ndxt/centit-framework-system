package com.centit.framework.system.dao;

import com.centit.framework.hibernate.dao.BaseDao;
import com.centit.framework.system.po.UserUnit;

import java.util.List;

public interface UserUnitDao extends BaseDao<UserUnit, String> {
 
	//"FROM UserUnit where userCode=?", userId
     List<UserUnit> listUserUnitsByUserCode(String userId);
    
    //"FROM UserUnit where userCode=? and unitCode=?",new Object[]{userCode,unitCode});
    //参数 String userCode,String unitCode
     List<UserUnit> listObjectByUserUnit(String userCode,String unitCode);
    
    // return "s"+ DatabaseOptUtils.getNextKeyBySequence(this, "S_USER_UNIT_ID", 9);
     String getNextKey();
    
    //"update UserUnit set isPrimary='F',lastModifyDate= ?  where userCode = ? and (unitCode <> ? or userStation <> ? or userRank <> ?) and isPrimary='T'",
     void deleteOtherPrimaryUnit(UserUnit object);
    
    // "delete UserUnit  where userCode = ? ",userCode
     void deleteUserUnitByUser(String userCode);

    // "delete UserUnit  where unitCode = ? ",unitCode
     void deleteUserUnitByUnit(String unitCode);
    
    //"FROM UserUnit where userCode=? and isPrimary='T'", userId
     UserUnit getPrimaryUnitByUserId(String userId);
    
    //"FROM UserUnit where unitCode=?", unitCode
     List<UserUnit> listUnitUsersByUnitCode(String unitCode);

    /**
     * unitcode不为null就是某个处室的某个角色，为NULL就是所有处室的某个角色
     *if (unitCode != null &amp;&amp; !"".equals(unitCode)) {
            if ("gw".equals(roleType))
                ls =listObjectsAll("FROM UserUnit where unitCode=? and userStation=? ",
                               new Object[]{ unitCode, roleCode});
            else if ("xz".equals(roleType))
                ls = listObjectsAll("FROM UserUnit where unitCode=? and userRank=? ",
                        new Object[]{ unitCode, roleCode});
        } else {
            if ("gw".equals(roleType))
                ls = listObjectsAll("FROM UserUnit where userStation=? ",
                                roleCode);
            else if ("xz".equals(roleType))
                ls = listObjectsAll("FROM UserUnit where userRank=? ",
                                roleCode);
        }
     * @param roleType String
     * @param roleCode String
     * @param unitCode String
     * @return List UserUnit
     * 分页
     */
//     List<UserUnit> listUserUnitsByRoleAndUnitFilterPagination(String roleType,
//                                                               String roleCode, String unitCode);
    //"FROM UserUnit where unitCode=? "  hql.append("order by " + filterMap.get("ORDER_BY"));
    //分页
//     List<UserUnit> listUnitUsersByUnitCodeAndFilterPagination(String unitCode, PageDesc pageDesc,
//            Map<String, Object> filterMap);

}
