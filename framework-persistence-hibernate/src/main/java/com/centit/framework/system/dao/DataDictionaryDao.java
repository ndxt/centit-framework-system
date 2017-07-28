package com.centit.framework.system.dao;

import com.centit.framework.system.po.DataDictionary;
import com.centit.framework.system.po.DataDictionaryId;

import java.util.List;
import java.util.Map;

public interface DataDictionaryDao {

	List<DataDictionary> listObjects(Map<String, Object> filterDescMap);
	
	DataDictionary getObjectById(DataDictionaryId dd);

    DataDictionaryId saveNewObject(DataDictionary dataDictionary);
	
	void deleteObject(DataDictionary dataDictionary);
	
	void deleteObjectById(DataDictionaryId dd);

    DataDictionary mergeObject(DataDictionary dataDictionary);
	//listObjectsAll("FROM DataDictionary ORDER BY id.catalogCode, dataOrder");
    List<DataDictionary> getWholeDictionary();
    
    //listObjectsAll("FROM DataDictionary WHERE id.catalogCode = ? ORDER BY dataOrder", catalogCode);
    List<DataDictionary> listDataDictionary(String catalogCode);
    
    //用序列生成
    String getNextPrimarykey();

    //批量删除 DatabaseOptUtils.doExecuteHql(this, "delete from DataDictionary where id.catalogCode =?", catalog);
    void deleteDictionary(String catalog);

}
