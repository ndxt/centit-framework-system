package com.centit.framework.system.dao.jdbcimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.UserQueryFilterDao;
import com.centit.framework.system.po.UserQueryFilter;
import com.centit.support.database.utils.QueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * UserQueryFilterDao  Repository.
 * create by scaffold 2016-02-29
 * @author codefan@sina.com
 * 用户自定义过滤条件表null
*/

@Repository("userQueryFilterDao")
public class UserQueryFilterDaoImpl extends BaseDaoImpl<UserQueryFilter,Long>
    implements UserQueryFilterDao {
    public static final Logger logger = LoggerFactory.getLogger(UserQueryFilterDaoImpl.class);

    @Override
    public Map<String, String> getFilterField() {
        if( filterField == null){
            filterField = new HashMap<>();
            filterField.put("filterNo" , CodeBook.EQUAL_HQL_ID);
            filterField.put("userCode" , CodeBook.EQUAL_HQL_ID);
            filterField.put("modleCode" , CodeBook.EQUAL_HQL_ID);
            filterField.put("filterName" , CodeBook.EQUAL_HQL_ID);
            filterField.put("filterValue" , CodeBook.EQUAL_HQL_ID);
        }
        return filterField;
    }

    @Override
    public void mergeUserFilter(UserQueryFilter userQueryFilter) {
        this.mergeObject(userQueryFilter);
    }

    @Override
    public UserQueryFilter getObjectById(Long filterNo) {
        return super.getObjectById(filterNo);
    }

    @Transactional
    public List<UserQueryFilter> listUserQueryFilterByModle(String userCode,String modelCode){
        return super.listObjectsByFilter(" where USER_CODE = ? and MODLE_CODE = ? "
                + "order by IS_DEFAULT desc , CREATE_DATE desc",
                new Object[]{userCode,modelCode});
    }

    @Transactional
    public List<UserQueryFilter> listUserDefaultFilterByModle(String userCode,String modelCode){
        return super.listObjectsByFilter(" where USER_CODE = ? and MODLE_CODE = ? "
                + "and IS_DEFAULT = 'T' order by CREATE_DATE desc",
                new Object[]{userCode,modelCode});
    }

    @Transactional
    public UserQueryFilter getUserDefaultFilterByModle(String userCode,String modelCode){
        return super.getObjectByProperties(
                QueryUtils.createSqlParamsMap("userCode",userCode,"modelCode",modelCode ));

    }

    @Override
    public void saveNewObject(UserQueryFilter userQueryFilter) {
        super.saveNewObject(userQueryFilter);
    }

    @Transactional
    public Long getNextKey() {
        return DatabaseOptUtils.getSequenceNextValue(this, "S_FILTER_NO");
    }
}
