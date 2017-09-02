package com.centit.framework.system.dao.impl;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.dao.DataDictionaryDao;
import com.centit.framework.system.po.DataDictionary;
import com.centit.framework.system.po.DataDictionaryId;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.PersistenceException;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository("dataDictionaryDao")
public class DataDictionaryDaoImpl extends BaseDaoImpl<DataDictionary, DataDictionaryId>
        implements DataDictionaryDao {

    @Override
    public String getDaoEmbeddedFilter() {
        return  "[:datacode | and DATA_CODE = :datacode ]" +
                "[:catalogcode | and CATALOG_CODE = :catalogcode ]" +
                "[NP_system | and DATA_STYLE = 'S' ]" +
                "[:(like)dataValue | and DATA_VALUE like :dataValue ]";
    }


    @Override
    public List<DataDictionary> listObjects(Map<String, Object> filterDescMap) {
        return this.listObjectsByProperties(filterDescMap);
    }

    @Override
    public DataDictionary getObjectById(DataDictionaryId dd) {
        return super.getObjectById(dd);
    }

    @Override
    public void deleteObjectById(DataDictionaryId dd) {
        super.deleteObjectById(dd);
    }

    public List<DataDictionary> getWholeDictionary(){
        return listObjects();
       
    }
    
    @Transactional
    public List<DataDictionary> listDataDictionary(String catalogCode) {
        return listObjectsByProperty("catalogCode", catalogCode);
    }

    
    @Transactional
    public String getNextPrimarykey() {
        try {
            Object obj = DatabaseAccess.getScalarObjectQuery(this.getConnection(),
                    "select max(DATA_CODE) from F_DATADICTIONARY where length(DATA_CODE)=12 ");
            return StringBaseOpt.objectToString(StringBaseOpt.objectToString(obj));
        } catch (IOException e){
            throw  new PersistenceException(PersistenceException.DATABASE_IO_EXCEPTION,e);
        }catch (SQLException e){
            throw  new PersistenceException(PersistenceException.DATABASE_SQL_EXCEPTION,e);
        }
    }

    @Transactional
    public void deleteDictionary(String catalog) {
        deleteObjectsByProperties(
                QueryUtils.createSqlParamsMap( "catalogCode", catalog));
    }

}
