package com.centit.framework.system.dao.hibernateimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.DataDictionaryDao;
import com.centit.framework.system.po.DataDictionary;
import com.centit.framework.system.po.DataDictionaryId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("dataDictionaryDao")
public class DataDictionaryDaoImpl extends BaseDaoImpl<DataDictionary, DataDictionaryId> implements DataDictionaryDao {


    // 转换主键中的 字段描述 对应关系
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("datacode", "id.dataCode = :datacode");
            filterField.put("catalogcode", "id.catalogCode = :catalogcode");
            filterField.put("catalogCode", "id.catalogCode = :catalogCode");
            filterField.put("NP_system", "dataStyle = 'S'");
            filterField.put("dataValue", CodeBook.LIKE_HQL_ID);
            filterField.put(CodeBook.ORDER_BY_HQL_ID, "dataOrder");
        }
        return filterField;
    }

    public List<DataDictionary> getWholeDictionary(){
        return listObjects("FROM DataDictionary ORDER BY id.catalogCode, dataOrder");

    }

    @Transactional
    public List<DataDictionary> listDataDictionary(String catalogCode) {
        return listObjects("FROM DataDictionary WHERE id.catalogCode = ?0 ORDER BY dataOrder", catalogCode);
    }

    @Transactional
    public void deleteDictionary(String catalog) {
        try {
            DatabaseOptUtils.doExecuteHql(this, "delete from DataDictionary where id.catalogCode =?0", catalog);
            logger.debug("delete DataDictionary successful");
        } catch (RuntimeException re) {
            logger.error("delete DataDictionary failed", re);
            throw re;
        }
    }

    @Override
    public void updateDictionary(DataDictionary dataDictionary){
        super.updateObject(dataDictionary);
    }

}
