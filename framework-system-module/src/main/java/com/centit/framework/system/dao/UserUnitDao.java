package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.po.UserUnit;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userUnitDao")
public class UserUnitDao extends BaseDaoImpl<UserUnit, String> {

    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>(20);
            filterField.put("userCode_isValid", "userCode in (select us.USER_CODE" +
                    " from f_userinfo us where us.IS_VALID = :userCode_isValid)");
//            filterField.put("unitCode","(unitCode = :unitCode or unitCode in " +
//                    "(select un.UNIT_CODE from f_unitinfo un  where un.PARENT_UNIT = :unitCode))");
            filterField.put("unitCode", CodeBook.EQUAL_HQL_ID);
            filterField.put("userStation", CodeBook.EQUAL_HQL_ID);
            filterField.put("userRank", CodeBook.EQUAL_HQL_ID);
            filterField.put("userCode", CodeBook.EQUAL_HQL_ID);
            filterField.put("isPrimary", CodeBook.EQUAL_HQL_ID);
            filterField.put("unitName", CodeBook.LIKE_HQL_ID);
            filterField.put("(like)userName", "userCode in (select us.USER_CODE from f_userinfo us where" +
                    " us.USER_NAME like :userName)");
            filterField.put("isValid", "userCode in (select us.USER_CODE from f_userinfo us where " +
                    "us.IS_VALID = :isValid)");

            filterField.put(CodeBook.ORDER_BY_HQL_ID, "userOrder asc");
        }
        return filterField;
    }

    public List<UserUnit> listObjectsAll() {
        return listObjects();
    }

    public UserUnit getObjectById(String userUnitId) {
        return super.getObjectById(userUnitId);
    }

    /**
     * 更新
     *
     * @param userUnit 用户机构
     */
    public void updateUserUnit(UserUnit userUnit) {
        super.updateObject(userUnit);
    }

    @Transactional
    public void deleteObjectById(String userUnitId) {
        super.deleteObjectById(userUnitId);
    }

    @Transactional
    public void deleteObjectForceById(String id) {
        super.deleteObjectForceById(id);
    }

    @Transactional
    public List<UserUnit> listUserUnitsByUserCode(String userId) {
        List<UserUnit> ls = listObjectsByProperty("userCode", userId);
        /*
         * for (FUserunit usun : ls) {
         * usun.setUnitname(CodeRepositoryUtil.getValue
         * ("unitCode",usun.getId().getUnitcode() )); }
         */
        return ls;
    }

    @Transactional
    public List<UserUnit> listObjectByUserUnit(String userCode,String unitCode){
        List<UserUnit> ls = listObjectsByProperties(CollectionsOpt.createHashMap(
                "userCode", userCode,"unitCode",unitCode));
        /*
         * for (FUserunit usun : ls) {
         * usun.setUnitname(CodeRepositoryUtil.getValue
         * ("unitCode",usun.getId().getUnitcode() )); }
         */
        return ls;
    }

    @Transactional
    public String getNextKey() {
        return "s" + StringBaseOpt.fillZeroForString(
                String.valueOf(DatabaseOptUtils.getSequenceNextValue(this, "S_USER_UNIT_ID")),9);
    }

    @Transactional
    public void deleteUserUnitByUser(String userCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("userCode", userCode);
        super.deleteObjectsByProperties(map);
    }

    @Transactional
    public void deleteUserUnitByUnit(String unitCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("unitCode", unitCode);
        super.deleteObjectsByProperties(map);
    }

    @Transactional
    public UserUnit getPrimaryUnitByUserId(String userId) {
        List<UserUnit> list = super.listObjectsByProperties(CollectionsOpt.createHashMap(
                "userCode", userId,"isPrimary","T"));
        if (list != null && list.size()>0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Transactional
    public List<UserUnit> listUnitUsersByUnitCode(String unitCode) {
        return listObjectsByProperty("unitCode", unitCode);
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
              ls = listObjectsByProperties(CollectionsOpt.createHashMap(
                "unitCode", unitCode, "userStation", roleCode));
            } else if ("xz".equals(roleType)) {
              ls = listObjectsByProperties(CollectionsOpt.createHashMap(
                "unitCode", unitCode, "userRank", roleCode));
            }
        } else {
            if ("gw".equals(roleType)) {
              ls = listObjectsByProperty("userStation", roleCode);
            } else if ("xz".equals(roleType)) {
              ls = listObjectsByProperty("userRank", roleCode);
            }
        }
        return ls;
    }

    public int countSubUserUnits(Map<String, Object> filterDescMap){
        String sql = "select USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_RANK, RANK_MEMO, USER_ORDER, " +
            "UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR " +
            "FROM F_USERUNIT WHERE 1=1 " +
            "[:(STARTWITH)unitPath | and UNIT_CODE IN (select UNIT_CODE from f_unitinfo where UNIT_PATH like :unitPath)]"+
            "[:(like)userName | and USER_CODE in (select USER_CODE from f_userinfo where USER_NAME like :userName)]";

        QueryAndNamedParams qap = QueryUtils.translateQuery(
          QueryUtils.buildGetCountSQLByReplaceFields(sql), filterDescMap);
        return jdbcTemplate.execute(
          (ConnectionCallback<Integer>) conn ->
            OrmDaoUtils.fetchObjectsCount(conn, qap.getQuery(), qap.getParams()));
    }

    public List<UserUnit> querySubUserUnits(Map<String, Object> pageQueryMap) {
        String sql = "select USER_UNIT_ID, UNIT_CODE, USER_CODE, IS_PRIMARY, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, " +
          "UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR " +
          "FROM F_USERUNIT WHERE 1=1 " +
          "[:(STARTWITH)unitPath | and UNIT_CODE IN (select UNIT_CODE from f_unitinfo where UNIT_PATH like :unitPath)]"+
          "[:(like)userName | and USER_CODE in (select USER_CODE from f_userinfo where USER_NAME like :userName or LOGIN_NAME like :userName)]" +
          "[:isValid | and USER_CODE in (select us.USER_CODE from f_userinfo us where us.IS_VALID = :isValid)]"+
          "[:userCode | and USER_CODE = :userCode]"+
          "[:unitIsValid | and UNIT_CODE IN (select UNIT_CODE from f_unitinfo where IS_VALID = :unitIsValid)]";
        PageDesc pageDesc = QueryParameterPrepare.fetchPageDescParams(pageQueryMap);

        String selfOrderBy = GeneralJsonObjectDao.fetchSelfOrderSql(sql, pageQueryMap);
        if (StringUtils.isNotBlank(selfOrderBy)) {
          sql = QueryUtils.removeOrderBy(sql) + " order by " + selfOrderBy;
        }
        QueryAndNamedParams qap = QueryUtils.translateQuery(sql, pageQueryMap);
        return jdbcTemplate.execute(
          /** 这个地方可以用replaceField 已提高效率
           *  pageDesc.setTotalRows(OrmDaoUtils.fetchObjectsCount(conn,
           QueryUtils.buildGetCountSQLByReplaceFields(qap.getSql()),qap.getParams()));
           * */
          (ConnectionCallback<List<UserUnit>>) conn -> OrmDaoUtils
            .queryObjectsByNamedParamsSql(conn, qap.getQuery(), qap.getParams(), (Class<UserUnit>) getPoClass(),
              pageDesc.getRowStart(), pageDesc.getPageSize()));
    }

}
