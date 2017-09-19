package com.centit.framework.system.dao.impl;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.OptFlowNoPoolDao;
import com.centit.framework.system.po.OptFlowNoPool;
import com.centit.framework.system.po.OptFlowNoPoolId;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.utils.PersistenceException;
import com.centit.support.database.utils.QueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository("optFlowNoPoolDao")
public class OptFlowNoPoolDaoImpl extends BaseDaoImpl<OptFlowNoPool, OptFlowNoPoolId> implements OptFlowNoPoolDao {


    public static final Logger logger = LoggerFactory.getLogger(OptFlowNoPoolDaoImpl.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put("ownerCode", "ownerCode = :ownerCode");
            filterField.put("(date)codeDate", "codeDate = :codeDate");
            filterField.put("codeCode", "codeCode = :codeCode");
            filterField.put("curNo", "ownerCode = :curNo");
        }
        return filterField;
    }

    @Override
    public OptFlowNoPool getObjectById(OptFlowNoPoolId cid) {
        return super.getObjectById(cid);
    }

    @Override
    public void deleteObjectById(OptFlowNoPoolId cid) {
        super.deleteObjectById(cid);
    }

    @Override
    public void saveObject(OptFlowNoPool optFlowNoPool) {
        super.saveNewObject(optFlowNoPool);
    }

    @Transactional
    public long fetchFirstLsh(String ownerCode, String codeCode,
                              Date codeBaseDate) {

        Long lsh = NumberBaseOpt.castObjectToLong(DatabaseOptUtils.getScalarObjectQuery(this,
                "select min(CURNO) as MinNo from F_OPTFLOWNOPOOL" +
                " where OWNERCODE = ? and CODECODE = ? and CODEDATE = ?",
                new Object[]{ownerCode,ownerCode,codeBaseDate }));
        return lsh == null ? 0l: lsh;
    }
}
