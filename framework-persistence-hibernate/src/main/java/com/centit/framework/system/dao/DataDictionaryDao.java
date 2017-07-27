package com.centit.framework.system.dao;

import com.centit.framework.hibernate.dao.BaseDao;
import com.centit.framework.system.po.DataDictionary;
import com.centit.framework.system.po.DataDictionaryId;

import java.util.List;

public interface DataDictionaryDao extends BaseDao<DataDictionary, DataDictionaryId> {

    List<DataDictionary> getWholeDictionary();
    
    List<DataDictionary> listDataDictionary(String catalogCode);
    
    String getNextPrimarykey();

    void deleteDictionary(String catalog);

}
