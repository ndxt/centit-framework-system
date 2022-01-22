package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.po.DataCatalog;
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
        return listObjectsByProperty("catalogStyle","F");
    }

    @Transactional
    public List<DataCatalog> listUserCatalog() {
        return listObjectsByProperty("catalogStyle","U");
    }

    @Transactional
    public List<DataCatalog> listSysCatalog() {
        return listObjectsByProperty("catalogStyle","S");
    }

    @Transactional
    public void updateCatalog(DataCatalog dataCatalog){
        super.updateObject(dataCatalog);
    }

    @Transactional
    public List<DataCatalog> listDataCatalogByUnit(String topUnit){
        return super.listObjectsByProperty("topUnit", topUnit);
    }

    @Transactional
    public void deletetReferences(String catalogCode){
        this.getJdbcTemplate().update("DELETE FROM m_application_dictionary WHERE dictionary_id = ? ", new Object[]{catalogCode});
    }
}
