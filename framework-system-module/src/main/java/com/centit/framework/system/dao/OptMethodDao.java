package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.model.basedata.OptMethod;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("optMethodDao")
public class OptMethodDao extends BaseDaoImpl<OptMethod, String> {

    public String getNextOptCode() {
        return UuidOpt.getUuidAsString22();
    }

    /**
     * 查询全部操作
     *
     * @return List&lt;OptMethod&gt;
     */
    public List<OptMethod> listObjectsAll() {
        return super.listObjects();
    }

    public OptMethod getObjectById(String optCode) {
        return super.getObjectById(optCode);
    }

    public void deleteObjectById(String optCode) {
        super.deleteObjectById(optCode);
    }

    @Transactional
    public List<OptMethod> listOptMethodByOptID(String sOptID) {
        return listObjectsByProperties(CollectionsOpt.createHashMap("optId", sOptID));
    }

    @Transactional
    public List<OptMethod> listOptMethodByRoleCode(String roleCode) {
        return listObjectsByFilter(" WHERE OPT_CODE in "
            + "(select rp.OPT_CODE from F_ROLEPOWER rp where rp.ROLE_CODE = ?)"
            + " order by OPT_ID", new Object[]{roleCode});
    }

    @Transactional
    public void deleteOptMethodsByOptID(String sOptID) {
        deleteObjectsByProperties(CollectionsOpt.createHashMap("optId", sOptID));
    }


    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("optId", CodeBook.EQUAL_HQL_ID);
        filterField.put("optCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("isInWorkflow", CodeBook.EQUAL_HQL_ID);
        filterField.put("optReq", CodeBook.EQUAL_HQL_ID);
        filterField.put("optMethod", CodeBook.EQUAL_HQL_ID);
        filterField.put("optName", CodeBook.LIKE_HQL_ID);
        return filterField;
    }

    @Transactional
    public List<OptMethod> listAllOptMethodByUnit(String topUnit) {
        String sql = "select o.* " +
            "from F_OPTDEF o join F_OPTINFO a on ( o.OPT_ID = a.OPT_ID )" +
            "join F_OS_INFO b on(a.TOP_OPT_ID=b.os_id) " +
            "where b.TOP_UNIT = ?";

        return getJdbcTemplate().execute(
            (ConnectionCallback<List<OptMethod>>) conn ->
                OrmDaoUtils.queryObjectsByParamsSql(conn, sql,
                    new Object[]{topUnit}, OptMethod.class));
    }

    public List<OptMethod> listPublicOptMethodByUnit(String topUnit) {
        String sql = "select o.* " +
            "from F_OPTDEF o join F_OPTINFO a on ( o.OPT_ID = a.OPT_ID ) " +
            "join f_rolepower c on o.opt_code=c.opt_code and c.role_code='public' " +
            "join F_OS_INFO b on(a.TOP_OPT_ID=b.REL_OPT_ID) " +
            "where b.TOP_UNIT = ?";
        return getJdbcTemplate().execute(
            (ConnectionCallback<List<OptMethod>>) conn ->
                OrmDaoUtils.queryObjectsByParamsSql(conn, sql,
                    new Object[]{topUnit}, OptMethod.class));
    }

    public List<OptMethod> listUserOptMethodByRoleCode(String[] roleCodes) {
        String sql = "select o.* " +
            "from F_OPTDEF o join F_OPTINFO a on ( o.OPT_ID = a.OPT_ID ) " +
            "join f_rolepower c on o.opt_code=c.opt_code " +
            "where c.role_code in (:roleCodes)";
        return getJdbcTemplate().execute(
            (ConnectionCallback<List<OptMethod>>) conn ->
                OrmDaoUtils.queryObjectsByNamedParamsSql(conn, sql,
                    CollectionsOpt.createHashMap("roleCodes",roleCodes), OptMethod.class));
    }

    public void updateOptMethod(OptMethod optMethod) {
        super.updateObject(optMethod);
    }

    public int[] updateOptIdByOptCodes(String optId, List<String> optCodes) {
        String sql = "UPDATE f_optdef SET OPT_ID=? WHERE OPT_CODE = ? ";
        return super.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, optId);
                preparedStatement.setString(2, optCodes.get(i));
            }

            @Override
            public int getBatchSize() {
                return optCodes.size();
            }
        });
    }
}
