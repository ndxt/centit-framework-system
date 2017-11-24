package com.centit.framework.system.dao.jdbcimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.UnitInfoDao;
import com.centit.framework.system.po.UnitInfo;
import com.centit.framework.system.po.UserInfo;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.QueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("unitInfoDao")
public class UnitInfoDaoImpl extends BaseDaoImpl<UnitInfo, String> implements UnitInfoDao {
    public static final Logger logger = LoggerFactory.getLogger(UnitInfoDaoImpl.class);

    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>(10);
            filterField.put("unitCode", CodeBook.EQUAL_HQL_ID);
            filterField.put("unitName", CodeBook.LIKE_HQL_ID);
            filterField.put("ISVALID", CodeBook.EQUAL_HQL_ID);
            filterField.put("UNITTAG", CodeBook.EQUAL_HQL_ID);
            filterField.put("UNITWORD", CodeBook.EQUAL_HQL_ID);
            filterField.put("parentUnit", CodeBook.EQUAL_HQL_ID);
            filterField.put("NP_TOPUnit", "(parentUnit is null or parentUnit='0')");
            filterField.put(CodeBook.ORDER_BY_HQL_ID, " unitOrder, unitCode ");
            filterField.put("unitPath", CodeBook.LIKE_HQL_ID);
        }
        return filterField;
    }

    @Override
    @Transactional
    public String getNextKey() {
        return StringBaseOpt.objectToString(
          DatabaseOptUtils.getSequenceNextValue(this, "S_UNITCODE"));
    }

    @Transactional
    public String getUnitCode(String depno) {
        String sSqlsen = "select UNIT_CODE " +
                "from f_unitinfo " +
                "where dep_no = ?";
        return this.getJdbcTemplate().queryForList(sSqlsen,
                new Object[]{depno} ,String.class).get(0);

    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation=Propagation.MANDATORY)
    public List<UserInfo> listUnitUsers(String unitCode) {
        String sql = "select a.* " +
                "from F_USERINFO a join F_USERUNIT b on(a.USERCODE=b.USERCODE) " +
                "where b.UNITCODE =?";

        return getJdbcTemplate().execute(
                (ConnectionCallback<List<UserInfo>>) conn ->
                        OrmDaoUtils.queryObjectsByParamsSql(conn, sql ,
                                new Object[]{unitCode}, UserInfo.class));
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation=Propagation.MANDATORY)
    public List<UserInfo> listRelationUsers(String unitCode) {
      String sSqlsen = "select ui.* FROM F_USERINFO ui " +
              "where exists (select uu.* from F_USERUNIT where uu.USER_CODE=ui.USER_CODE and uu.UNIT_CODE= ?) " +
              " or exists(select ur.* from F_V_USERROLES ur " +
              "where ur.USER_CODE=ui.USER_CODE and ur.ROLE_TYPE='D' " +
              "and ur.UNIT_CODE= ? )";
        return getJdbcTemplate().execute(
                (ConnectionCallback<List<UserInfo>>) conn ->
                        OrmDaoUtils.queryObjectsByParamsSql(conn, sSqlsen ,
                                new Object[]{unitCode, unitCode}, UserInfo.class));
    }

    @Transactional
    public String getUnitNameOfCode(String unitcode) {
        String sql = "select UNITNAME from F_UNITINFO where UNITCODE=?";
        return this.getJdbcTemplate().queryForList(sql,
               new Object[]{unitcode} ,String.class).get(0);
    }

    @Transactional
    public UnitInfo getUnitByName(String name) {
        String sql = "select u.UNIT_CODE, u.PARENT_UNIT, u.UNIT_TYPE, u.IS_VALID, u.UNIT_NAME, u.ENGLISH_NAME," +
                " u.UNIT_SHORT_NAME, u.UNIT_WORD, u.UNIT_TAG, u.UNIT_DESC, u.ADDRBOOK_ID, u.UNIT_ORDER, u.UNIT_GRADE," +
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
        return super.getObjectByProperties(QueryUtils.createSqlParamsMap("unitTag", unitTag));
    }

    @Transactional
    public UnitInfo getUnitByWord(String unitWord) {
        return super.getObjectByProperties(QueryUtils.createSqlParamsMap("unitWord", unitWord));
    }

    @Transactional
    public List<UnitInfo> listSubUnits(String unitCode){
        return super.listObjectsByProperty("parentUnit", unitCode);
    }

    @Override
    @Transactional(propagation=Propagation.MANDATORY)
    public List<UnitInfo> listSubUnitsByUnitPaht(String unitPath){
        return listObjects(QueryUtils.createSqlParamsMap("unitPath", unitPath+"%" ));
    }

    @Override
    public List<String> getAllParentUnit(){
        return this.getJdbcTemplate().queryForList(
          "select distinct t.parent_unit from f_unitinfo t ", String.class);
    }

    @Override
    public UnitInfo getObjectById(String unitCode) {
        return super.getObjectById(unitCode);
    }

    @Override
    public void deleteObjectById(String unitCode) {
        super.deleteObjectById(unitCode);
    }

    @Override
    public int countChildrenSum(String unitCode){
        return this.getJdbcTemplate().queryForObject(
          "select count(*) as subunits from F_UNITINFO where PARENT_UNIT = ?",
          Integer.class, unitCode);
    }

    /**
     * 根据名称获取同级机构
     * @param unitName 机构名称
     * @param parentCode 父类代码
     * @return UnitInfo 机构信息
     */
    @Override
    public UnitInfo getPeerUnitByName(String unitName, String parentCode, String unitCode) {
        String sql = "select u.UNIT_CODE, u.PARENT_UNIT, u.UNIT_TYPE, u.IS_VALID, u.UNIT_NAME, u.ENGLISH_NAME," +
                " u.UNIT_SHORT_NAME, u.UNIT_WORD, u.UNIT_TAG, u.UNIT_DESC, u.ADDRBOOK_ID, u.UNIT_ORDER, u.UNIT_GRADE," +
                " u.DEP_NO, u.UNIT_PATH, u.UNIT_MANAGER, u.CREATE_DATE, u.CREATOR, u.UPDATOR, u.UPDATE_DATE " +
                "from F_UNITINFO u " +
                "where u.UNIT_NAME = :unitName and u.PARENT_UNIT = :parentUnit and u.UNIT_CODE <> :unitCode";

        List<UnitInfo> list = listObjectsBySql(sql, QueryUtils.createSqlParamsMap(
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
    @Override
    public Integer isExistsUnitByParentAndOrder(String parentUnit, long unitOrder) {
        String sql = "select count(*) as existUnit " +
          "from F_UNITINFO u " +
          "where u.UNIT_ORDER = :unitOrder and u.PARENT_UNIT = :parentUnit ";

        Object object = DatabaseOptUtils.getScalarObjectQuery(this, sql, QueryUtils.createSqlParamsMap(
          "unitOrder", unitOrder, "parentUnit", parentUnit));


        return NumberBaseOpt.castObjectToInteger(object);
    }
}
