package com.centit.framework.system.dao.impl;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.dao.DataDictionaryDao;
import com.centit.framework.system.po.DataDictionary;
import com.centit.framework.system.po.DataDictionaryId;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.orm.PersistenceException;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Repository("dataDictionaryDao")
public class DataDictionaryDaoImpl extends BaseDaoImpl<DataDictionary, DataDictionaryId> implements DataDictionaryDao {

    public List<DataDictionary> getWholeDictionary(){
        return listObjects();
       
    }
    
    @Transactional
    public List<DataDictionary> listDataDictionary(String catalogCode) {
        return listObjects("catalogCode", catalogCode);
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
        this.getOrmDaoSupport().deleteObjectByProperties(
                QueryUtils.createSqlParamsMap( "catalogCode", catalog), this.getPoClass()
        );
    }

}
