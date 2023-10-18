package com.centit.framework.system.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.basedata.DataCatalog;
import com.centit.framework.model.basedata.DataDictionary;
import com.centit.framework.model.basedata.DataDictionaryId;
import com.centit.framework.system.dao.DataCatalogDao;
import com.centit.framework.system.dao.DataDictionaryDao;
import com.centit.framework.system.service.DataDictionaryManager;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;

@Service("dataDictionaryManager")
public class DataDictionaryManagerImpl implements
    DataDictionaryManager {

    protected Logger logger = LoggerFactory.getLogger(DataDictionaryManagerImpl.class);

    @Autowired
    @NotNull
    private DataDictionaryDao dictionaryDao;

    @Autowired
    @NotNull
    protected DataCatalogDao dataCatalogDao;


    @Override
    @Transactional
    public List<DataDictionary> getDataDictionary(String catalogCode) {
        logger.info("缓存数据字典  DataDictionary ：" + catalogCode + " ......");
        return dictionaryDao.listDataDictionary(catalogCode);
        //logger.info("loading DataDictionary end");
    }

    @Override
    @Transactional
    public DataCatalog getCatalogIncludeDataPiece(String catalogCode) {
        DataCatalog dc = dataCatalogDao.getObjectById(catalogCode);
        dc.addAllDataPiece(dictionaryDao.listDataDictionary(catalogCode));
        return dc;
    }

    @Override
    @Transactional
    public List<DataDictionary> saveCatalogIncludeDataPiece(DataCatalog dataCatalog) {
//        dataCatalogDao.updateOptMethod(dataCatalog);
        List<DataDictionary> oldData = dictionaryDao.listDataDictionary(dataCatalog.getCatalogCode());
        List<DataDictionary> newData = dataCatalog.getDataDictionaries();
        Triple<List<DataDictionary>, List<Pair<DataDictionary, DataDictionary>>, List<DataDictionary>>
            dbOptList = CollectionsOpt.compareTwoList(oldData, newData,
            Comparator.comparing(DataDictionary::getDataCode));

        if (dbOptList.getRight() != null) {
            for (DataDictionary dp : dbOptList.getRight()) {
                dictionaryDao.deleteObject(dp);
            }
        }
        if (dbOptList.getLeft() != null) {
            for (DataDictionary dp : dbOptList.getLeft()) {
                //if ((isAdmin || !"F".equals(dp.getDataStyle()))) {
                dictionaryDao.saveNewObject(dp);
                //}
            }
        }

        if (null != dbOptList.getMiddle()) {
            for (Pair<DataDictionary, DataDictionary> updateDp : dbOptList.getMiddle()) {
                DataDictionary oldD = updateDp.getLeft();
                DataDictionary newD = updateDp.getRight();
                dictionaryDao.updateDictionary(newD);
            }
        }
        CodeRepositoryCache.evictCache("DataCatalog");
        CodeRepositoryCache.evictCache("DataDictionary", dataCatalog.getCatalogCode());
        return oldData;
    }

    @Override
    @Transactional
    public void deleteDataDictionary(String catalogCode) {
        dictionaryDao.deleteDictionary(catalogCode);
        dataCatalogDao.deleteObjectById(catalogCode);
        CodeRepositoryCache.evictCache("DataDictionary", catalogCode);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDataDictionaryPiece(DataDictionaryId dd) {
        dictionaryDao.deleteObjectById(dd);
        CodeRepositoryCache.evictCache("DataDictionary", dd.getCatalogCode());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveDataDictionaryPiece(DataDictionary dd) {
        dictionaryDao.mergeObject(dd);
        CodeRepositoryCache.evictCache("DataDictionary", dd.getCatalogCode());
    }

    public String[] getFieldsDesc(String sDesc, String sType) {
        String[] nRes = {"数据代码", "扩展代码(父代码)", "扩展代码2(自定义)", "标记", "数值", "类型", "数据描述"};
        if ("T".equals(sType))
            nRes[1] = "上级代码";
        if (sDesc == null || "".equals(sDesc))
            return nRes;
        String[] s = StringUtils.split(sDesc, ';');
        if (s == null)
            return nRes;
        int n = s.length;

        for (int i = 0; i < n; i++) {
            int p = s[i].indexOf(':');
            if (p > 1)
                nRes[i] = s[i].substring(0, p);
            else
                nRes[i] = s[i];
        }
        return nRes;
    }

    @Transactional
    public DataDictionary getDataDictionaryPiece(DataDictionaryId id) {
        return dictionaryDao.getObjectById(id);
    }

    @Transactional
    public List<DataCatalog> listFixDataCatalog() {
        return dataCatalogDao.listFixCatalog();
    }

    @Transactional
    @Deprecated
    public List<DataCatalog> listSysDataCatalog() {
        return dataCatalogDao.listSysCatalog();
    }

    @Transactional
    public List<DataCatalog> listUserDataCatalog() {
        return dataCatalogDao.listUserCatalog();
    }

    @Transactional
    public List<DataCatalog> listAllDataCatalog(Map<String, Object> filterMap) {
        return dataCatalogDao.listObjectsByProperties(filterMap);
    }

    @Transactional
    public List<DataDictionary> listDataDictionarys(Map<String, Object> filterDescMap) {
        return dictionaryDao.listObjectsByProperties(filterDescMap);
    }

    @Transactional
    public List<DataDictionary> getWholeDictionary(Collection<String> catalogCodes) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("catalogcodes", CollectionsOpt.listToArray(catalogCodes));
        return dictionaryDao.getWholeDictionary(filterMap);
    }

    @Override
    @Transactional
    public DataCatalog getObjectById(String catalogCode) {
        return dataCatalogDao.getObjectById(catalogCode);
    }

    @Override
    @Transactional
    public int existCatalogName(String catalogName) {
        HashMap<String, Object> map = new HashMap();
        map.put("catalogName", catalogName);
        return dataCatalogDao.countObjectByProperties(map);
    }


    @Override
    @Transactional
    public void updateCatalog(DataCatalog dataCatalog) {
        dataCatalogDao.updateCatalog(dataCatalog);
    }

    @Override
    @Transactional
    public void saveNewObject(DataCatalog dataCatalog) {
        dataCatalogDao.saveNewObject(dataCatalog);
    }


    @Override
    @Transactional
    public List<DataCatalog> listObjects(Map<String, Object> filterDescMap, PageDesc pageDesc) {
        return dataCatalogDao.listObjectsByProperties(filterDescMap, pageDesc);
    }

    //select dictionary_id from m_application_dictionary where os_id = :osId
    @Override
    @Transactional
    public JSONArray appendRelativeOsInfo(List<DataCatalog> catalogList){
        if(catalogList==null || catalogList.size()==0)
            return null;
        JSONArray jsonArray = JSONArray.from(catalogList);
        for(Object obj : jsonArray){
            if(obj instanceof JSONObject){
                JSONObject jsonObject = (JSONObject) obj;
                String catalogId = jsonObject.getString("catalogCode");
                String sqlSen = "select a.OS_ID, a.OS_NAME "+
                "from m_application_dictionary b join F_OS_INFO a on (b.os_id = a.os_id) "+
                "where b.dictionary_id = ?";
                JSONArray osList = DatabaseOptUtils.listObjectsBySqlAsJson(dataCatalogDao, sqlSen, new Object[]{catalogId});
                if(osList!=null){
                    jsonObject.put("relativeOs", osList);
                }
            }
        }
        return jsonArray;
    }
}
