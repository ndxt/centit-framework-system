package com.centit.framework.system.dao.impl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.OptDataScopeDao;
import com.centit.framework.system.po.OptDataScope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository("optDataScopeDao")
public class OptDataScopeDaoImpl extends BaseDaoImpl<OptDataScope, String> implements OptDataScopeDao {

    @Transactional
    public List<OptDataScope> getDataScopeByOptID(String sOptID) {
        return listObjects("FROM OptDataScope WHERE optId =?", sOptID);
    }

    @Transactional
    public int getOptDataScopeSumByOptID(String sOptID) {
        return Integer.valueOf(String.valueOf(DatabaseOptUtils.getSingleObjectByHql(this,
                "SELECT count(optScopeCode) FROM OptDataScope WHERE optId = ?", sOptID)));
    }


    @Transactional
    public void deleteDataScopeOfOptID(String sOptID) {
        DatabaseOptUtils.doExecuteHql(this, "DELETE FROM OptDataScope WHERE optId = ?", sOptID);
    }

  
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("OPTID", CodeBook.EQUAL_HQL_ID);
            filterField.put("OPTSCOPECODE", CodeBook.EQUAL_HQL_ID);
            filterField.put("SCOPENAME", CodeBook.LIKE_HQL_ID);
        }
        return filterField;
    }

    @Transactional
    public String getNextOptCode() {
        return DatabaseOptUtils.getNextValueOfSequence(this, "S_OPTDEFCODE");
    }

    
    @Transactional
    public List<String> listDataFiltersByIds(Collection<String> scopeCodes) {
        List<OptDataScope> objs    = listObjects("FROM OptDataScope WHERE optId in ?", scopeCodes);
        if(objs==null)
            return null;
        List<String> filters = new ArrayList<String>();
        for(OptDataScope scope:objs)
            filters.add(scope.getFilterCondition());
        return filters;
    }

}
