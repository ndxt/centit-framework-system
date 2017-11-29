package com.centit.framework.system.dao;

import com.centit.framework.system.po.DataCatalog;

import java.util.List;
import java.util.Map;

/**
 * 字典类别Dao
 * @author god
 * update by zou_wy@centit.com
 * @date 2017-11-29
 */
public interface DataCatalogDao{

    /**
     * 查询所有类别
     * @return List<DataCatalog>
     */
    List<DataCatalog> listObjects();

    /**
     * 根据Id查询类别
     * @param catalogCode 类别Id
     * @return DataCatalog
     */
    DataCatalog getObjectById(String catalogCode);

    /**
     * 新增类别
     * @param dataCatalog 类别对象
     */
    void saveNewObject(DataCatalog dataCatalog);

    /**
     * 更新类别
     * @param dataCatalog 类别对象
     */
    void updateCatalog(DataCatalog dataCatalog);

    /**
     * 根据Id删除类别
     * @param catalogCode 类别Id
     */
    void deleteObjectById(String catalogCode);

    /**
     * 查询所有框架固有的类别
     * @return List<DataCatalog>
     */
    List<DataCatalog> listFixCatalog();

    /**
     * 查询所有用户定义的类别
     * @return List<DataCatalog>
     */
    List<DataCatalog> listUserCatalog();

    /**
     * 查询所有系统类别
     * @return List<DataCatalog>
     */
    List<DataCatalog> listSysCatalog();

    /**
     * 查询条数
     * @param filterDescMap 过滤条件
     * @return int
     */
    int pageCount(Map<String, Object> filterDescMap);

    /**
     *  分页查询
     * @param pageQueryMap 过滤条件
     * @return List<DataCatalog>
     */
    List<DataCatalog>  pageQuery(Map<String, Object> pageQueryMap);
}
