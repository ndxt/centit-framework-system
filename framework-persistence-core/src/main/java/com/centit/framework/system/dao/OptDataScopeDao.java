package com.centit.framework.system.dao;

import com.centit.framework.system.po.OptDataScope;

import java.util.Collection;
import java.util.List;

public interface OptDataScopeDao {

    void mergeObject(OptDataScope optDataScope);

    void deleteObject(OptDataScope optDataScope);

    //return listObjectsAll("FROM OptDataScope WHERE optId =?", sOptID);
    List<OptDataScope> getDataScopeByOptID(String sOptID) ;

    //"SELECT count(optScopeCode) FROM OptDataScope WHERE optId = ?", sOptID)));
    int getOptDataScopeSumByOptID(String sOptID);


    //"DELETE FROM OptDataScope WHERE optId = ?", sOptID);
    void deleteDataScopeOfOptID(String sOptID) ;

  
    //return DatabaseOptUtils.getNextValueOfSequence(this, "S_OPTDEFCODE");
    String getNextOptCode();

    
       // listObjectsAll("FROM OptDataScope WHERE optId in ?", scopeCodes)
    List<String> listDataFiltersByIds(Collection<String> scopeCodes);//zou_wy
}
