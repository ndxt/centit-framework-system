package com.centit.framework.system.dao.hibernateimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.UserUnitDao;
import com.centit.framework.system.po.UserUnit;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userUnitDao")
public class UserUnitDaoImpl extends BaseDaoImpl<UserUnit, String> implements UserUnitDao {

    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put("userCode_isValid",
                    "userCode in (select userCode from UserInfo where isValid = :userCode_isValid)");
//            filterField.put("unitCode",
//                    "(unitCode = :unitCode or unitCode in (select unitCode from UnitInfo where parentUnit = :unitCode))");
            filterField.put("unitCode", CodeBook.EQUAL_HQL_ID);
            filterField.put("userStation", "userStation = :userStation");
            filterField.put("unitRank", "userRank = :unitRank");
            filterField.put("userCode", "userCode = :userCode");
            filterField.put("isPrimary", CodeBook.EQUAL_HQL_ID);
            filterField.put("unitName", CodeBook.LIKE_HQL_ID);
            filterField.put("(like)userName", "userCode in (select userCode from UserInfo where userName like :userName)");
            filterField.put("isValid", "userCode in (select userCode from UserInfo where isValid = :isValid)");

            filterField.put(CodeBook.ORDER_BY_HQL_ID, "userOrder asc");

        }
        return filterField;
    }

    @Override
    @Transactional
    public List<UserUnit> listUserUnitsByUserCode(String userId) {
        List<UserUnit> ls = listObjects(
                "FROM UserUnit where userCode=?",
                userId);
        /*
         * for (FUserunit usun : ls) {
         * usun.setUnitname(CodeRepositoryUtil.getValue
         * ("unitCode",usun.getId().getUnitcode() )); }
         */
        return ls;
    }

    @Override
    @Transactional
    public List<UserUnit> listObjectByUserUnit(String userCode,String unitCode){
        List<UserUnit> ls = listObjects(
                "FROM UserUnit where userCode=? and unitCode=?",
                new Object[]{userCode,unitCode});
        return ls;
    }

    @Override
    @Transactional
    public String getNextKey() {
        return DatabaseOptUtils.getNextValueOfSequence(this, "S_USER_UNIT_ID");

    }

    @Override
    @Transactional
    public void deleteUserUnitByUser(String userCode) {
        DatabaseOptUtils
                .doExecuteHql(
                        this,
                        "delete UserUnit  where userCode = ? ",
                        userCode);

    }

    @Override
    @Transactional
    public void deleteUserUnitByUnit(String unitCode) {
        DatabaseOptUtils
                .doExecuteHql(
                        this,
                        "delete UserUnit  where unitCode = ? ",
                        unitCode);
    }

    @Override
    @Transactional
    public UserUnit getPrimaryUnitByUserId(String userId) {
        List<UserUnit> list = listObjects(
                "FROM UserUnit where userCode=? and isPrimary='T'",
                userId);
        if (list != null && list.size()>0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public List<UserUnit> listUnitUsersByUnitCode(String unitCode) {
        List<UserUnit> ls =listObjects(
                "FROM UserUnit where unitCode=?",
                unitCode);
        return ls;
    }

    /**
     * unitcode不为null就是某个处室的某个角色，为NULL就是所有处室的某个角色
     *
     * @param roleType roleType
     * @param roleCode roleCode
     * @param unitCode unitCode
     * @return List
     */

    @Transactional
    public List<UserUnit> listUserUnitsByRoleAndUnit(String roleType,
                                                   String roleCode, String unitCode) {
        List<UserUnit> ls = null;
        if (unitCode != null && !"".equals(unitCode)) {
            if ("gw".equals(roleType)) {
              ls = listObjects("FROM UserUnit where unitCode=? and userStation=? ",
                new Object[]{unitCode, roleCode});
            }else if ("xz".equals(roleType)) {
              ls = listObjects("FROM UserUnit where unitCode=? and userRank=? ",
                new Object[]{unitCode, roleCode});
            }
        } else {
            if ("gw".equals(roleType)) {
              ls = listObjects("FROM UserUnit where userStation=? ",
                roleCode);
            }else if ("xz".equals(roleType)) {
              ls = listObjects("FROM UserUnit where userRank=? ",
                roleCode);
            }
        }
        return ls;
    }



    @Transactional
    public List<UserUnit> listUnitUsersByUnitCodeAndFilter(String unitCode, PageDesc pageDesc,
            Map<String, Object> filterMap) {
        String hql = "FROM UserUnit where unitCode=? ";

        if (null != filterMap && null != filterMap.get("ORDER_BY")) {
          hql += "order by " + filterMap.get("ORDER_BY");
        }
        return super.listObjects(hql, unitCode, pageDesc);

    }



    /**
     * 批量添加或更新
     *
     * @param userunits List
     */
    @Transactional
    public void batchSave(List<UserUnit> userunits) {
        for (int i = 0; i < userunits.size(); i++) {
            super.saveObject(userunits.get(i));

            if (0 == i % 20) {
                DatabaseOptUtils.flush(this.getCurrentSession());
            }
        }
    }


    @Transactional
    public void batchMerge(List<UserUnit> userunits) {
        for (int i = 0; i < userunits.size(); i++) {
            mergeObject(userunits.get(i));

            if (19 == i % 20) {
                DatabaseOptUtils.flush(this.getCurrentSession());
            }
        }
    }

    @Override
    public int countSubUserUnits(Map<String, Object> filterDescMap){
        String hql = "from UserUnit where 1=1 " +
          "[:(STARTWITH)unitPath | and unitCode in (select unitCode from UnitInfo where unitPath like :unitPath)]"+
          "[:(like)userName | and userCode in (select userCode from UserInfo where userName like :userName)]";
        QueryAndNamedParams qap = QueryUtils.translateQuery(hql, filterDescMap);
        Query q =  getCurrentSession().createQuery("SELECT COUNT(*) "
          + QueryUtils.removeOrderBy(qap.getQuery()));

        Map<String, Object> params = qap.getParams();
        DatabaseOptUtils.setQueryParameters(q,params);
        Integer total = NumberBaseOpt.castObjectToInteger(q.uniqueResult());
        return total==null?0:total;
    }

    @Override
    public List<UserUnit> querySubUserUnits(Map<String, Object> pageQueryMap) {
        String hql = "from UserUnit where 1=1 " +
          "[:(STARTWITH)unitPath | and unitCode in (select unitCode from UnitInfo where unitPath like :unitPath)]"+
          "[:(like)userName | and userCode in (select userCode from UserInfo where userName like :userName)]";

        int startPos = 0;
      int maxSize = 0;
      if(pageQueryMap!=null){
        startPos = NumberBaseOpt.castObjectToInteger(pageQueryMap.get("startRow"));
        maxSize = NumberBaseOpt.castObjectToInteger(pageQueryMap.get("maxSize"));
      }
      QueryAndNamedParams qap = QueryUtils.translateQuery(hql, pageQueryMap);

      return listObjectsByNamedHql(qap.getQuery(), qap.getParams(),
        startPos, maxSize);
    }
}
