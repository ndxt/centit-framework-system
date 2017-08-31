package com.centit.framework.system.dao.impl;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.dao.DataCatalogDao;
import com.centit.framework.system.po.DataCatalog;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("dataCatalogDao")
public class DataCatalogDaoImpl extends BaseDaoImpl<DataCatalog, String> implements DataCatalogDao{

    @Transactional
    public List<DataCatalog> listFixCatalog() {
        return listObjects("catalogStyle","F");
    }

    @Transactional
    public List<DataCatalog> listUserCatalog() {
        return listObjects("catalogStyle","U");
    }

    @Transactional
    public List<DataCatalog> listSysCatalog() {
        return listObjects("catalogStyle","S");
    }

}
