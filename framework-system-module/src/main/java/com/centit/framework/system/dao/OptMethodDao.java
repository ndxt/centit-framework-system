package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.po.OptMethod;
import com.centit.support.algorithm.CollectionsOpt;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("optMethodDao")
public class OptMethodDao extends BaseDaoImpl<OptMethod, String>{

    /**
     * 查询全部操作
     *
     * @return List&lt;OptMethod&gt;
     */
    public List<OptMethod> listObjectsAll() {
        return super.listObjects();
    }

    public OptMethod getObjectById(String optCode) {
        return super.getObjectById(optCode);
    }

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
    public void deleteOptMethodsByOptID(String sOptID) {
        deleteObjectsByProperties(CollectionsOpt.createHashMap("optId", sOptID));
    }


    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("optId", CodeBook.EQUAL_HQL_ID);
        filterField.put("optCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("isInWorkflow", CodeBook.EQUAL_HQL_ID);
        filterField.put("optReq", CodeBook.EQUAL_HQL_ID);
        filterField.put("optMethod", CodeBook.EQUAL_HQL_ID);
        filterField.put("optName", CodeBook.LIKE_HQL_ID);
        return filterField;
    }

    @Transactional
    public String getNextOptCode() {
        return String.valueOf(DatabaseOptUtils.getSequenceNextValue(this, "S_OPTDEFCODE"));
    }

    public void updateOptMethod(OptMethod optMethod){
        super.updateObject(optMethod);
    }
}
