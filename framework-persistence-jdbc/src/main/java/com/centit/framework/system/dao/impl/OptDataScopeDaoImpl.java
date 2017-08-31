package com.centit.framework.system.dao.impl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.OptDataScopeDao;
import com.centit.framework.system.po.OptDataScope;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository("optDataScopeDao")
public class OptDataScopeDaoImpl extends BaseDaoImpl<OptDataScope, String> implements OptDataScopeDao {

    @Transactional
    public List<OptDataScope> getDataScopeByOptID(String sOptID) {
        return listObjects("optId", sOptID);
    }

    @Transactional
    public int getOptDataScopeSumByOptID(String sOptID) {
        //return pageCount(QueryUtils.createSqlParamsMap("optId", sOptID));
        return getOrmDaoSupport().fetchObjectsCount(
                QueryUtils.createSqlParamsMap("optId", sOptID),getPoClass());
    }


    @Transactional
    public void deleteDataScopeOfOptID(String sOptID) {
        getOrmDaoSupport().deleteObjectByProperties(
                QueryUtils.createSqlParamsMap("optId", sOptID),getPoClass());
    }


    @Transactional
    public String getNextOptCode() {

    	return DatabaseOptUtils.getNextValueOfSequence(this, "S_OPTDEFCODE");
    }

    
    @Transactional
    public List<String> listDataFiltersByIds(Collection<String> scopeCodes) {
    	List<OptDataScope> objs	= listObjects("FROM OptDataScope WHERE optId in (?)", scopeCodes);
    	if(objs==null)
    		return null;
    	List<String> filters = new ArrayList<String>();
    	for(OptDataScope scope:objs)
    		filters.add(scope.getFilterCondition());
    	return filters;
    }

}
