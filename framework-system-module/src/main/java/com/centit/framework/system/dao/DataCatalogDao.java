package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.model.basedata.DataCatalog;
import com.centit.support.algorithm.CollectionsOpt;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("dataCatalogDao")
public class DataCatalogDao extends BaseDaoImpl<DataCatalog, String>{

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("catalogCode", CodeBook.LIKE_HQL_ID);
        filterField.put("catalogName", CodeBook.LIKE_HQL_ID);
        filterField.put("catalogStyle", CodeBook.EQUAL_HQL_ID);
        filterField.put("catalogType", CodeBook.EQUAL_HQL_ID);
        filterField.put("optId", CodeBook.EQUAL_HQL_ID);
        filterField.put("topUnit", CodeBook.EQUAL_HQL_ID);
        filterField.put("osId",
            "CATALOG_CODE in (select dictionary_id from m_application_dictionary where os_id = :osId)");

        return filterField;
    }

    @Transactional
    public DataCatalog getObjectById(String catalogCode) {
        return super.getObjectById(catalogCode);
    }

    @Transactional
    public void deleteObjectById(String catalogCode) {
        super.deleteObjectById(catalogCode);
    }

    @Transactional
    public List<DataCatalog> listFixCatalog() {
        return listObjectsByProperties(CollectionsOpt.createHashMap("catalogStyle","F"));
    }

    @Transactional
    public List<DataCatalog> listUserCatalog() {
        return listObjectsByProperties(CollectionsOpt.createHashMap("catalogStyle","U"));
    }

    @Transactional
    public List<DataCatalog> listSysCatalog() {
        return listObjectsByProperties(CollectionsOpt.createHashMap("catalogStyle","S"));
    }

    @Transactional
    public void updateCatalog(DataCatalog dataCatalog){
        super.updateObject(dataCatalog);
    }

    @Transactional
    public List<DataCatalog> listDataCatalogByUnit(String topUnit){
        return super.listObjectsByProperties(CollectionsOpt.createHashMap("topUnit", topUnit));
    }
}
