package com.centit.framework.system.service;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.model.basedata.DataCatalog;
import com.centit.framework.model.basedata.DataDictionary;
import com.centit.framework.model.basedata.DataDictionaryId;
import com.centit.support.database.utils.PageDesc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DataDictionaryManager {

    DataCatalog getObjectById(String catalogCode);

    int existCatalogName(String catalogName);

    void saveNewObject(DataCatalog dataCatalog);

    void updateCatalog(DataCatalog dataCatalog);

    @Deprecated
    List<DataCatalog> listSysDataCatalog();

    List<DataCatalog> listUserDataCatalog();

    List<DataCatalog> listFixDataCatalog();

    List<DataCatalog> listAllDataCatalog(Map<String, Object> filterMap);

    JSONArray appendRelativeOsInfo(List<DataCatalog> catalogList);

    List<DataDictionary> listDataDictionarys(Map<String, Object> filterDescMap);

    List<DataCatalog> listObjects(Map<String, Object> filterDescMap,PageDesc pageDesc);

    DataCatalog getCatalogIncludeDataPiece(String catalogCode);

    List<DataDictionary> saveCatalogIncludeDataPiece(DataCatalog dataCatalog);

    void deleteDataDictionary(String catalogCode);

    void deleteDataDictionaryPiece(DataDictionaryId id);

    void saveDataDictionaryPiece(DataDictionary dd);

    DataDictionary getDataDictionaryPiece(DataDictionaryId id);

    String[] getFieldsDesc(String sDesc, String sType);

    List<DataDictionary> getDataDictionary(String catalogCode);

    List<DataDictionary> getWholeDictionary(Collection<String> catalogCodes);
}
