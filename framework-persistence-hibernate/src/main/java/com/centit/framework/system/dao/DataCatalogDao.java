package com.centit.framework.system.dao;

import com.centit.framework.hibernate.dao.BaseDao;
import com.centit.framework.system.po.DataCatalog;

import java.util.List;

public interface DataCatalogDao extends BaseDao<DataCatalog, String>{


    List<DataCatalog> listFixCatalog();

    List<DataCatalog> listUserCatalog();

    List<DataCatalog> listSysCatalog();

}
