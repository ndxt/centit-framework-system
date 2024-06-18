package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.basedata.UnitInfo;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.QueryAndParams;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("unitInfoDao")
public class UnitInfoDao extends BaseDaoImpl<UnitInfo, String> {
    public static final Logger logger = LoggerFactory.getLogger(UnitInfoDao.class);

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>(10);
        filterField.put("unitCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("unitName", CodeBook.LIKE_HQL_ID);
        filterField.put("isValid", CodeBook.EQUAL_HQL_ID);
        filterField.put("UNITTAG", CodeBook.EQUAL_HQL_ID);
        filterField.put("unitWord", CodeBook.EQUAL_HQL_ID);
        filterField.put("parentUnit", CodeBook.EQUAL_HQL_ID);
        filterField.put("NP_TOPUnit", "(parentUnit is null or parentUnit='0' or parentUnit='')");
        filterField.put("depNo", CodeBook.EQUAL_HQL_ID);
        filterField.put(CodeBook.SELF_ORDER_BY, " UNIT_ORDER, UNIT_CODE ");
        filterField.put("(STARTWITH)unitPath", CodeBook.LIKE_HQL_ID);
        filterField.put("topUnit", CodeBook.EQUAL_HQL_ID);
        return filterField;
    }


    @SuppressWarnings("unchecked")
    @Transactional
    public List<UserInfo> listUnitUsers(String unitCode) {
        String sql = "select a.* " +
                "from F_USERINFO a join F_USERUNIT b on(a.USER_CODE=b.USER_CODE) " +
                "where b.UNIT_CODE =?";

        return getJdbcTemplate().execute(
                (ConnectionCallback<List<UserInfo>>) conn ->
                        OrmDaoUtils.queryObjectsByParamsSql(conn, sql ,
                                new Object[]{unitCode}, UserInfo.class));
    }


    @Transactional
    public List<UnitInfo> listUserTopUnits(String userCode){
        String sql = "select a.* " +
            "from F_UNITINFO a join F_USERUNIT b on (a.UNIT_CODE = b.UNIT_CODE) " +
            "where b.USER_CODE =? and CONCAT('/',a.UNIT_CODE) = a.UNIT_PATH";
        return getJdbcTemplate().execute(
            (ConnectionCallback<List<UnitInfo>>) conn ->
                OrmDaoUtils.queryObjectsByParamsSql(conn, sql ,
                    new Object[]{userCode}, UnitInfo.class));
    }

    @Transactional
    public List<UnitInfo> listAllTopUnits() {
        String sql = "select a.* from F_UNITINFO a where CONCAT('/',a.UNIT_CODE) = a.UNIT_PATH";
        return getJdbcTemplate().execute(
            (ConnectionCallback<List<UnitInfo>>) conn ->
                OrmDaoUtils.queryObjectsByParamsSql(conn, sql, null, UnitInfo.class));
    }

    @Transactional
    public UnitInfo getUnitByName(String name) {
        String sql = "select u.UNIT_CODE, u.PARENT_UNIT, u.UNIT_TYPE, u.IS_VALID, u.UNIT_NAME, u.ENGLISH_NAME," +
                " u.UNIT_SHORT_NAME, u.UNIT_WORD, u.UNIT_TAG, u.UNIT_DESC, u.UNIT_ORDER, u.UNIT_GRADE," +
                " u.DEP_NO, u.UNIT_PATH, u.UNIT_MANAGER, u.CREATE_DATE, u.CREATOR, u.UPDATOR, u.UPDATE_DATE " +
                "from F_UNITINFO u " +
                "where u.UNIT_NAME = ? or u.UNIT_SHORT_NAME = ?";
                //+ " order by UNIT_ORDER asc";
        List<UnitInfo> unitInfos = getJdbcTemplate().execute(
                (ConnectionCallback<List<UnitInfo>>) conn ->
                        OrmDaoUtils.queryObjectsByParamsSql(conn, sql ,
                                new Object[]{name, name}, UnitInfo.class));
        if(unitInfos!=null && unitInfos.size()>0){
            return unitInfos.get(0);
        }
        return null;
    }

    @Transactional
    public UnitInfo getUnitByTag(String unitTag) {
        return super.getObjectByProperties(CollectionsOpt.createHashMap("unitTag", unitTag));
    }

    @Transactional
    public UnitInfo getUnitByWord(String unitWord) {
        return super.getObjectByProperties(CollectionsOpt.createHashMap("unitWord", unitWord));
    }

    @Transactional
    public List<UnitInfo> listSubUnits(String unitCode){
        return super.listObjectsByProperties(CollectionsOpt.createHashMap("parentUnit", unitCode));
    }

    @Transactional
    public List<UnitInfo> listSubUnitsByUnitPaht(String unitPath){
        return listObjectsByProperties(CollectionsOpt.createHashMap("unitPath", unitPath+"/%" ));
    }

    public List<String> getAllParentUnit(){
        return this.getJdbcTemplate().queryForList(
          "select distinct t.parent_unit from f_unitinfo t ", String.class);
    }

    public UnitInfo getObjectById(String unitCode) {
        return super.getObjectById(unitCode);
    }

    @Transactional
    public void deleteObjectById(String unitCode) {
        super.deleteObjectById(unitCode);
    }

    /**
     * 根据名称获取同级机构
     * @param unitName 机构名称
     * @param parentCode 父类代码
     * @param unitCode 结构代码
     * @return UnitInfo 机构信息
     */
    public boolean isUniqueName(String unitName, String parentCode, String unitCode) {
        String sql = "select count(*) as hasSameNameUnit from F_UNITINFO u " +
                "where  u.UNIT_NAME = :unitName and" +
            " u.PARENT_UNIT = :parentUnit [ :unitCode | and u.UNIT_CODE <> :unitCode ]";
        Map<String, Object> filerMap = CollectionsOpt.createHashMap("unitName", unitName, "parentUnit", parentCode, "unitCode", unitCode);
        QueryAndParams queryAndParams = QueryAndParams.createFromQueryAndNamedParams(QueryUtils.translateQuery(sql, filerMap));
        logger.info("sql: {},参数：unitName={},parentCode={},unitCode={}",queryAndParams.getQuery(),unitName,parentCode,unitCode);
        return NumberBaseOpt.castObjectToInteger(
            DatabaseOptUtils.getScalarObjectQuery(this, queryAndParams.getQuery(),queryAndParams.getParams()))>0;
    }

    /**
     * 根据PARENT_UNIT和UNIT_ORDER获取同级机构
     * @param parentUnit 机构名称
     * @param unitOrder 父类代码
     * @return UnitInfo 机构信息
     */
    public Integer isExistsUnitByParentAndOrder(String parentUnit, long unitOrder) {
        String sql = "select count(*) as existUnit " +
          "from F_UNITINFO u " +
          "where u.UNIT_ORDER = :unitOrder and u.PARENT_UNIT = :parentUnit ";
        Object object = DatabaseOptUtils.getScalarObjectQuery(this, sql, CollectionsOpt.createHashMap(
          "unitOrder", unitOrder, "parentUnit", parentUnit));


        return NumberBaseOpt.castObjectToInteger(object);
    }

    @Transactional
    public void updateUnit(UnitInfo unitInfo){
        super.updateObject(unitInfo);
    }

    /**
     * 统计租户下机构个数
     * @param topUnit 用户租户
     * @return 机构个数
     */
    public int countUnitByTopUnit(String topUnit) {
        String sql = " SELECT COUNT(1) COUNT FROM F_UNITINFO WHERE 1=1 AND IS_VALID = 'T'  [  :topUnit | AND TOP_UNIT = :topUnit ] ";
        Map<String, Object> params  = StringUtils.isBlank(topUnit)? new HashMap<>():CollectionsOpt.createHashMap("topUnit",topUnit);
        QueryAndParams queryAndParams = QueryAndParams.createFromQueryAndNamedParams(QueryUtils.translateQuery(sql, params));
        logger.info("sql: {},参数：{}",queryAndParams.getQuery(),topUnit);
        return NumberBaseOpt.castObjectToInteger(DatabaseOptUtils.getScalarObjectQuery(this, queryAndParams.getQuery(),queryAndParams.getParams()));
    }

}
