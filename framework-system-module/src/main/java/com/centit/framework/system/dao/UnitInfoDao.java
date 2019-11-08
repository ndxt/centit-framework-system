package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.po.UnitInfo;
import com.centit.framework.system.po.UserInfo;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.orm.OrmDaoUtils;
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
        if (filterField == null) {
            filterField = new HashMap<>(10);
            filterField.put("unitCode", CodeBook.EQUAL_HQL_ID);
            filterField.put("unitName", CodeBook.LIKE_HQL_ID);
            filterField.put("isValid", CodeBook.EQUAL_HQL_ID);
            filterField.put("UNITTAG", CodeBook.EQUAL_HQL_ID);
            filterField.put("unitWord", CodeBook.EQUAL_HQL_ID);
            filterField.put("parentUnit", CodeBook.EQUAL_HQL_ID);
            filterField.put("NP_TOPUnit", "(parentUnit is null or parentUnit='0' or parentUnit='')");
            filterField.put("depNo", CodeBook.EQUAL_HQL_ID);
            filterField.put(CodeBook.ORDER_BY_HQL_ID, " unitOrder, unitCode ");
            filterField.put("(STARTWITH)unitPath", CodeBook.LIKE_HQL_ID);
        }
        return filterField;
    }

    @Transactional
    public String getNextKey() {
        return StringBaseOpt.objectToString(
          DatabaseOptUtils.getSequenceNextValue(this, "S_UNITCODE"));
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<UserInfo> listUnitUsers(String unitCode) {
        String sql = "select a.* " +
                "from F_USERINFO a join F_USERUNIT b on(a.USERCODE=b.USERCODE) " +
                "where b.UNITCODE =?";

        return getJdbcTemplate().execute(
                (ConnectionCallback<List<UserInfo>>) conn ->
                        OrmDaoUtils.queryObjectsByParamsSql(conn, sql ,
                                new Object[]{unitCode}, UserInfo.class));
    }

    @Transactional
    public UnitInfo getUnitByName(String name) {
        String sql = "select u.UNIT_CODE, u.PARENT_UNIT, u.UNIT_TYPE, u.IS_VALID, u.UNIT_NAME, u.ENGLISH_NAME," +
                " u.UNIT_SHORT_NAME, u.UNIT_WORD, u.UNIT_TAG, u.UNIT_DESC, u.UNIT_ORDER, u.UNIT_GRADE," +
                " u.DEP_NO, u.UNIT_PATH, u.UNIT_MANAGER, u.CREATE_DATE, u.CREATOR, u.UPDATOR, u.UPDATE_DATE " +
                "from F_UNITINFO u " +
                "where u.UNIT_NAME = ? or u.UNIT_SHORT_NAME = ?"
                + " order by unitOrder asc";

        return getJdbcTemplate().execute(
                (ConnectionCallback<List<UnitInfo>>) conn ->
                        OrmDaoUtils.queryObjectsByParamsSql(conn, sql ,
                                new Object[]{name, name}, UnitInfo.class)).get(0);
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
        return super.listObjectsByProperty("parentUnit", unitCode);
    }

    @Transactional
    public List<UnitInfo> listSubUnitsByUnitPaht(String unitPath){
        return listObjects(CollectionsOpt.createHashMap("unitPath", unitPath+"%" ));
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
    public UnitInfo getPeerUnitByName(String unitName, String parentCode, String unitCode) {
        String sql = "select u.UNIT_CODE, u.PARENT_UNIT, u.UNIT_TYPE, u.IS_VALID, u.UNIT_NAME, u.ENGLISH_NAME," +
                " u.UNIT_SHORT_NAME, u.UNIT_WORD, u.UNIT_TAG, u.UNIT_DESC, u.UNIT_ORDER, u.UNIT_GRADE," +
                " u.DEP_NO, u.UNIT_PATH, u.UNIT_MANAGER, u.CREATE_DATE, u.CREATOR, u.UPDATOR, u.UPDATE_DATE " +
                "from F_UNITINFO u " +
                "where u.UNIT_NAME = :unitName and u.PARENT_UNIT = :parentUnit and u.UNIT_CODE <> :unitCode";

        List<UnitInfo> list = listObjectsBySql(sql, CollectionsOpt.createHashMap(
                "unitName", unitName, "parentUnit", parentCode, "unitCode", unitCode));

        if(list == null || list.size() == 0){
          return null;
        }
        return list.get(0);
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

}
