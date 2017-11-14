package com.centit.framework.system.dao.jdbcimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.OptMethodDao;
import com.centit.framework.system.po.OptMethod;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("optMethodDao")
public class OptMethodDaoImpl extends BaseDaoImpl<OptMethod, String> implements OptMethodDao {

    @Override
    public OptMethod getObjectById(String optCode) {
        return super.getObjectById(optCode);
    }

    @Override
    public void deleteObjectById(String optCode) {
        super.deleteObjectById(optCode);
    }

    @Transactional
    public List<OptMethod> listOptMethodByOptID(String sOptID) {
        return listObjectsByProperty("optId", sOptID);
    }

    @Transactional
    public List<OptMethod> listOptMethodByRoleCode(String roleCode) {
        return listObjectsByFilter(" WHERE OPT_CODE in "
                + "(select rp.OPT_CODE from F_ROLEPOWER rp where rp.ROLE_CODE = ?)"
                + " order by OPT_ID", new Object[]{roleCode});
    }

    @Transactional
    public int getOptMethodSumByOptID(String sOptID) {
        return pageCount(QueryUtils.createSqlParamsMap("optId", sOptID));
    }

    @Transactional
    public void deleteOptMethodsByOptID(String sOptID) {
        deleteObjectsByProperties(QueryUtils.createSqlParamsMap("optId", sOptID));
    }


    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put("optId", CodeBook.EQUAL_HQL_ID);
            filterField.put("optCode", CodeBook.EQUAL_HQL_ID);
            filterField.put("isInWorkflow", CodeBook.EQUAL_HQL_ID);
            filterField.put("optReq", CodeBook.EQUAL_HQL_ID);
            filterField.put("optMethod", CodeBook.EQUAL_HQL_ID);
            filterField.put("optName", CodeBook.LIKE_HQL_ID);
        }
        return filterField;
    }

    @Transactional
    public String getNextOptCode() {
        return String.valueOf(DatabaseOptUtils.getSequenceNextValue(this, "S_OPTDEFCODE"));
    }

}
