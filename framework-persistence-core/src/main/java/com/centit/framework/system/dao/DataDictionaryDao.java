package com.centit.framework.system.dao;

import com.centit.framework.system.po.DataDictionary;
import com.centit.framework.system.po.DataDictionaryId;

import java.util.List;
import java.util.Map;

/**
 * 数据字典Dao
 * @author god
 * update by zou_wy@centit.com
 */
public interface DataDictionaryDao {

    /**
    * 查询数据字典
    * @param filterDescMap 过滤条件
    * @return List<DataDictionary>
    */
    List<DataDictionary> listObjects(Map<String, Object> filterDescMap);

    /**
    * 根据Id查询数据字典
    * @param dataDictionaryId 字典Id
    * @return DataDictionary
    */
    DataDictionary getObjectById(DataDictionaryId dataDictionaryId);

    /**
    * 新增数据自定
    * @param dataDictionary 字典对象
    */
    void saveNewObject(DataDictionary dataDictionary);

    /**
     * 删除字典
     * @param dataDictionary 字典对象
     */
    void deleteObject(DataDictionary dataDictionary);

    /**
    * 根据Id删除字典
    * @param dataDictionaryId 字典Id
    */
    void deleteObjectById(DataDictionaryId dataDictionaryId);

    /**
    * 更新字典
    * @param dataDictionary 字典对象
    */
    void updateDictionary(DataDictionary dataDictionary);

    /**
    * 查询全部
    * @return List<DataDictionary>
    */
    List<DataDictionary> getWholeDictionary();

    /**
    * 根据类别Id查询数据字典
    * @param catalogCode 类别Id
    * @return List<DataDictionary>
    */
    List<DataDictionary> listDataDictionary(String catalogCode);

    /**
    * 根据类别Id删除字典
    * @param catalogCode 类别ID
    */
    void deleteDictionary(String catalogCode);

}
