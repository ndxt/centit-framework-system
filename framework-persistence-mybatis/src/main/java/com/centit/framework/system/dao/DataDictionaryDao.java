package com.centit.framework.system.dao;

import java.util.List;
import java.util.Map;

import com.centit.framework.mybatis.dao.BaseDao;
import org.springframework.stereotype.Repository;
import com.centit.framework.system.po.DataDictionary;
import com.centit.framework.system.po.DataDictionaryId;

@Repository
public interface DataDictionaryDao extends BaseDao {

    List<DataDictionary> listObjects(Map<String, Object> filterDescMap);

    DataDictionary getObjectById(DataDictionaryId dd);

    void saveNewObject(DataDictionary dataDictionary);

    void deleteObject(DataDictionary dataDictionary);

    void deleteObjectById(DataDictionaryId dd);

    void mergeObject(DataDictionary dataDictionary);
    //listObjectsAll("FROM DataDictionary ORDER BY id.catalogCode, dataOrder");
    List<DataDictionary> getWholeDictionary();
    
    //listObjectsAll("FROM DataDictionary WHERE id.catalogCode = ? ORDER BY dataOrder", catalogCode);
    List<DataDictionary> listDataDictionary(String catalogCode);
    
    //用序列生成
    String getNextPrimarykey();

    //批量删除 DatabaseOptUtils.doExecuteHql(this, "delete from DataDictionary where id.catalogCode =?", catalog);
    void deleteDictionary(String catalog);

}
