package com.centit.framework.system.dao.impl;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.dao.DataCatalogDao;
import com.centit.framework.system.po.DataCatalog;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("dataCatalogDao")
public class DataCatalogDaoImpl extends BaseDaoImpl<DataCatalog, String> implements DataCatalogDao{

    /**
     * 每个dao都需要重载这个函数已获得自定义的查询条件，否则listObjects、pageQuery就等价与listObjectsByProperties
     *
     * @return FilterQuery
     */
    @Override
    public String getDaoEmbeddedFilter() {
        return  "[:(like)catalogCode | and CATALOG_CODE like :catalogCode ]" +
                "[:(like)catalogName | and CATALOG_NAME like :catalogName ]" +
                "[:catalogStyle | and CATALOG_STYLE = :catalogStyle ]" +
                "[:catalogType | and CATALOG_TYPE = :catalogType ]" +
                "[:optId | and OPT_ID = : optId ]" ;
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


}
