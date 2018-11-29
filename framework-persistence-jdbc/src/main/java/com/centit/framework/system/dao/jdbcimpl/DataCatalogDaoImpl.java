package com.centit.framework.system.dao.jdbcimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.dao.DataCatalogDao;
import com.centit.framework.system.po.DataCatalog;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("dataCatalogDao")
public class DataCatalogDaoImpl extends BaseDaoImpl<DataCatalog, String> implements DataCatalogDao{


    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put("catalogCode", CodeBook.LIKE_HQL_ID);
            filterField.put("catalogName", CodeBook.LIKE_HQL_ID);
            filterField.put("catalogStyle", CodeBook.EQUAL_HQL_ID);
            filterField.put("catalogType", CodeBook.EQUAL_HQL_ID);
            filterField.put("optId", CodeBook.EQUAL_HQL_ID);
        }
        return filterField;
    }
    @Override
    public DataCatalog getObjectById(String catalogCode) {
        return super.getObjectById(catalogCode);
    }

    @Override
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

    @Override
    public void updateCatalog(DataCatalog dataCatalog){
        super.updateObject(dataCatalog);
    }

    @Override
    public void saveNewObject(DataCatalog dataCatalog) {
        super.saveNewObject(dataCatalog);
    }
}
