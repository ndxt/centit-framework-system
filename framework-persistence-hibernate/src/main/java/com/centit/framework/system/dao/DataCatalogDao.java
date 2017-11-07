package com.centit.framework.system.dao;

import com.centit.framework.system.po.DataCatalog;

import java.util.List;
import java.util.Map;

public interface DataCatalogDao{

    List<DataCatalog> listObjects();

    DataCatalog getObjectById(String catalogCode);

    void saveNewObject(DataCatalog dataCatalog);

    void mergeObject(DataCatalog dataCatalog);

    void deleteObjectById(String catalogCode);

    //listObjectsAll("FROM DataCatalog WHERE catalogStyle='F'");
    List<DataCatalog> listFixCatalog();

    //listObjectsAll("FROM DataCatalog WHERE catalogStyle='U'");
    List<DataCatalog> listUserCatalog();

    //listObjectsAll("FROM DataCatalog WHERE catalogStyle='S'");
    List<DataCatalog> listSysCatalog();

    //分页  //startRow  startRow
    int  pageCount(Map<String, Object> filterDescMap);

    List<DataCatalog>  pageQuery(Map<String, Object> pageQureyMap);
}
