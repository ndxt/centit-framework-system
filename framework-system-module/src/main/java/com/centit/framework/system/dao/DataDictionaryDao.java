package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.po.DataDictionary;
import com.centit.framework.system.po.DataDictionaryId;
import com.centit.support.algorithm.CollectionsOpt;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("dataDictionaryDao")
public class DataDictionaryDao
    extends BaseDaoImpl<DataDictionary, DataDictionaryId>{

    // 转换主键中的 字段描述 对应关系
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("datacode", CodeBook.EQUAL_HQL_ID);
        filterField.put("catalogcode", CodeBook.EQUAL_HQL_ID);
        filterField.put("catalogCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("NP_system", "dataStyle = 'S'");
        filterField.put("dataValue", CodeBook.LIKE_HQL_ID);
        filterField.put("catalogcodes", "catalogcode in (:catalogcodes)");
        return filterField;
    }

    @Override
    public List<DataDictionary> listObjects(Map<String, Object> filterDescMap) {
        return this.listObjectsByProperties(filterDescMap);
    }

    public DataDictionary getObjectById(DataDictionaryId dd) {
        return super.getObjectById(dd);
    }

    public void deleteObjectById(DataDictionaryId dd) {
        super.deleteObjectById(dd);
    }

    public List<DataDictionary> getWholeDictionary(Map<String, Object> filterMap){
        return listObjects(filterMap);

    }

    @Transactional
    public List<DataDictionary> listDataDictionary(String catalogCode) {
        return listObjectsByProperty("catalogCode", catalogCode);
    }

    @Transactional
    public void deleteDictionary(String catalog) {
        deleteObjectsByProperties(
                CollectionsOpt.createHashMap( "catalogCode", catalog));
    }

    public void updateDictionary(DataDictionary dataDictionary){
        super.updateObject(dataDictionary);
    }

}
