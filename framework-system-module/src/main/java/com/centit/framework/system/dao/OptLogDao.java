package com.centit.framework.system.dao;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.po.OptLog;
import com.centit.support.database.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository("optLogDao")
public class OptLogDao extends BaseDaoImpl<OptLog, Long> {

    public static final Logger logger = LoggerFactory.getLogger(OptLogDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put("logId", CodeBook.EQUAL_HQL_ID);
            filterField.put("logLevel", CodeBook.LIKE_HQL_ID);
            filterField.put(CodeRepositoryUtil.USER_CODE, CodeBook.EQUAL_HQL_ID);
            filterField.put("(date)optTimeBegin", "optTime >= :optTimeBegin ");
            filterField.put("(nextday)optTimeEnd", "optTime < :optTimeEnd");
            filterField.put("optId", CodeBook.LIKE_HQL_ID);
            filterField.put("optCode", CodeBook.LIKE_HQL_ID);
            filterField.put("optContent", CodeBook.LIKE_HQL_ID);
            filterField.put("oldValue", CodeBook.LIKE_HQL_ID);
            filterField.put("optMethod", CodeBook.EQUAL_HQL_ID);

        }
        return filterField;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<String> listOptIds() {
        final String hql = "select DISTINCT f.OPT_ID from F_OPT_LOG f";

        return this.getJdbcTemplate().queryForList(hql,String.class);
    }


    @Transactional
    public Long createNewLogId(){
        return DatabaseOptUtils.getSequenceNextValue(this, "S_SYS_LOG");
    }

    public OptLog getObjectById(Long logId) {
        return super.getObjectById(logId);
    }

    @Transactional
    public void deleteObjectById(Long logId) {
        super.deleteObjectById(logId);
    }

    @Transactional
    public void mergeOptLog(OptLog o) {
        if (null == o.getLogId()) {
            o.setLogId(DatabaseOptUtils.getSequenceNextValue(this, "S_SYS_LOG"));
        }
       /* return */super.mergeObject(o);
    }

    @Transactional
    public void delete(Date begin, Date end) {
        String hql = "delete from F_OPT_LOG o where 1=1 ";
        List<Object> objects = new ArrayList<>();
        if (null != begin) {
            hql += "and o.optTime > ?";
            objects.add(begin);
        }
        if (null != end) {
            hql += "and o.optTime < ?";
            objects.add(end);
        }

        try {
            DatabaseOptUtils.doExecuteSql(this, hql, objects.toArray(new Object[objects.size()]));
        } catch (DataAccessException e) {
            throw new PersistenceException(e);
        }

    }

}
