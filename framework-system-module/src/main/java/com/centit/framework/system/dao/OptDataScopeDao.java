package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.po.OptDataScope;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository("optDataScopeDao")
public class OptDataScopeDao extends BaseDaoImpl<OptDataScope, String> {

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("optId", CodeBook.EQUAL_HQL_ID);
        filterField.put("optScopeCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("scopeName", CodeBook.LIKE_HQL_ID);
        return filterField;
    }

    /**
     * @return 所有的数据范围定义
     */

    @Transactional
    public List<OptDataScope> listAllDataScope() {
        return listObjects();
    }

    @Transactional
    public List<OptDataScope> getDataScopeByOptID(String sOptID) {
        return this.listObjectsByProperty("optId", sOptID);
    }

    @Transactional
    public void deleteDataScopeOfOptID(String sOptID) {
        this.deleteObjectsByProperties(
                CollectionsOpt.createHashMap("optId", sOptID));
    }

    @Transactional
    public String getNextOptCode() {
        Long nextValue = DatabaseOptUtils.getSequenceNextValue(this, "S_OPTDEFCODE");
        return nextValue==null?"":String.valueOf(nextValue);
    }


    @Transactional
    public List<String> listDataFiltersByIds(Collection<String> scopeCodes) {
        List<OptDataScope> objs = this.listObjectsByFilter(
                "WHERE OPT_SCOPE_CODE in (:optScopeCode)",
                CollectionsOpt.createHashMap("optScopeCode", scopeCodes));
        if(objs==null)
            return null;
        List<String> filters = new ArrayList<>();
        for(OptDataScope scope:objs)
            filters.add(scope.getFilterCondition());
        return filters;
    }

    @Transactional
    public List<OptDataScope> listAllDataScopeByUnit(String topUnit){
        String sql = "select o.* " +
            "from F_OPTDATASCOPE o join F_OPTINFO a on ( o.OPT_ID = a.OPT_ID" +
            " join F_OS_INFO b on(a.TOP_OPT_ID=b.REL_OPT_ID) " +
            "where b.TOP_UNIT = ?";

        return getJdbcTemplate().execute(
            (ConnectionCallback<List<OptDataScope>>) conn ->
                OrmDaoUtils.queryObjectsByParamsSql(conn, sql ,
                    new Object[]{topUnit}, OptDataScope.class));
    }

    @Transactional
    public void updateOptDataScope(OptDataScope optDataScope){
        super.updateObject(optDataScope);
    }

    @Transactional
    public void saveNewOPtDataScope(OptDataScope optDataScope){
        super.saveNewObject(optDataScope);
    }

}
