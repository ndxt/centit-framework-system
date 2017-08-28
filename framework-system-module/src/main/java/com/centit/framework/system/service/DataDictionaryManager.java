package com.centit.framework.system.service;

import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.system.po.DataCatalog;
import com.centit.framework.system.po.DataDictionary;
import com.centit.framework.system.po.DataDictionaryId;

import java.util.List;
import java.util.Map;

public interface DataDictionaryManager {
	
	DataCatalog getObjectById(String catalogCode);

    void saveNewObject(DataCatalog dataCatalog);
	
	void mergeObject(DataCatalog dataCatalog);
	
    List<DataCatalog> listSysDataCatalog();

    List<DataCatalog> listUserDataCatalog();

    List<DataCatalog> listFixDataCatalog();
    
    List<DataCatalog> listAllDataCatalog();
    
    List<DataDictionary> listDataDictionarys(Map<String, Object> filterDescMap);
    
    List<DataCatalog> listObjects(Map<String, Object> filterDescMap,PageDesc pageDesc);
    
    DataCatalog getCatalogIncludeDataPiece(String catalogCode);

    List<DataDictionary> saveCatalogIncludeDataPiece(DataCatalog dataCatalog,boolean isAdmin);
    
    void deleteDataDictionary(String catalogCode);

    void deleteDataDictionaryPiece(DataDictionaryId id);

    void saveDataDictionaryPiece(DataDictionary dd);

    DataDictionary getDataDictionaryPiece(DataDictionaryId id);

    String[] getFieldsDesc(String sDesc, String sType);

    List<DataDictionary> getDataDictionary(String catalogCode);
    
    List<DataDictionary> getWholeDictionary();
}
