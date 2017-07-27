package com.centit.framework.system.dao;

import com.centit.framework.hibernate.dao.BaseDao;
import com.centit.framework.system.po.OptDataScope;

import java.util.Collection;
import java.util.List;

public interface OptDataScopeDao extends BaseDao<OptDataScope, String> {

    List<OptDataScope> getDataScopeByOptID(String sOptID) ;

    int getOptDataScopeSumByOptID(String sOptID);


    void deleteDataScopeOfOptID(String sOptID) ;

    String getNextOptCode();

    List<String> listDataFiltersByIds(Collection<String> scopeCodes);//zou_wy
}
