package com.centit.framework.system.dao.jdbcimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.dao.OptInfoDao;
import com.centit.framework.system.po.FVUserOptMoudleList;
import com.centit.framework.system.po.OptInfo;
import com.centit.framework.system.po.OptMethod;
import com.centit.framework.system.po.OptMethodUrlMap;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("optInfoDao")
public class OptInfoDaoImpl extends BaseDaoImpl<OptInfo, String> implements OptInfoDao {

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put("optId", CodeBook.EQUAL_HQL_ID);
            filterField.put("optUrl", CodeBook.EQUAL_HQL_ID);
            filterField.put("optName", CodeBook.LIKE_HQL_ID);
            filterField.put("preOptId", CodeBook.EQUAL_HQL_ID);
            filterField.put("NP_TOPOPT", "(preOptId is null or preOptId='0')");
            filterField.put("optType", CodeBook.EQUAL_HQL_ID);
            filterField.put("optTypes", "optType in (:optTypes)");
            filterField.put("topOptId", CodeBook.EQUAL_HQL_ID);
            filterField.put("isInToolbar", CodeBook.EQUAL_HQL_ID);
        }
        return filterField;
    }

    @Override
    @Transactional
    public List<OptInfo> listValidObjects() {
        return listObjectsByProperty("isInToolbar","T");
    }

    @SuppressWarnings("unchecked")
    public List<OptInfo> getFunctionsByUserID(String userID) {
        String sql = "select OPT_ID, PRE_OPT_ID, OPT_NAME, OPT_TYPE, FORM_CODE, " +
                "OPT_ROUTE, OPT_URL, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, " +
                "TOP_OPT_ID, PAGE_TYPE,ORDER_IND " +
                "from F_V_USEROPTMOUDLELIST " +
                "where USERCODE= :userId";
        return super.listObjectsBySql(sql, QueryUtils.createSqlParamsMap("userId",userID));
    }

    @Override
    @Transactional
    public List<OptInfo> getMenuFuncByOptUrl(){
        String hql1 = "where OPT_URL='...' order by ORDER_IND ";
        return super.listObjectsByFilter(hql1,(Object[]) null);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserOptMoudleList> getMenuFuncByUserID(String userCode, String optType) {

        String querySql = "select OPT_ID, USER_CODE, OPT_NAME, PRE_OPT_ID, FORM_CODE,"+
                "OPT_URL, OPT_ROUTE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, " +
                "TOP_OPT_ID, ORDER_IND, PAGE_TYPE "+
                "from F_V_USEROPTMOUDLELIST "+
                "where IS_IN_TOOLBAR = 'Y' "+
                "and USER_CODE = ? "+
                "and OPT_TYPE = ? "+
                "order by ORDER_IND ";

        return getJdbcTemplate().execute(
                (ConnectionCallback<List<FVUserOptMoudleList>>) conn ->
                        OrmDaoUtils.queryObjectsByParamsSql(conn, querySql ,
                                new Object[]{userCode, optType}, FVUserOptMoudleList.class));

    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<String> listUserDataPowerByOptMethod(String userCode,String optid,String optMethod) {

        String sql = "select OPT_SCOPE_CODES " +
            "from F_V_USEROPTDATASCOPES " +
            "where USER_CODE = ? and OPT_ID = ? and OPT_METHOD = ?";
        return this.getJdbcTemplate().queryForList(sql,
                new Object[]{userCode, optid, optMethod} ,String.class);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<OptInfo> getFunctionsByUserAndSuperFunctionId(String userID, String superFunctionId) {

        String sql = "select OPT_ID, PRE_OPT_ID, OPT_NAME, OPT_TYPE, FORM_CODE, " +
                "OPT_ROUTE, OPT_URL, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, " +
                "TOP_OPT_ID, PAGE_TYPE,ORDER_IND " +
                "from F_V_USEROPTMOUDLELIST " +
                "where USERCODE= :userCode and" +
                " TOP_OPT_ID=:topOptId ORDER BY PRE_OPT_ID, ORDER_IND";
        return super.listObjectsBySql(sql,
                QueryUtils.createSqlParamsMap("userCode",userID,"topOptId",superFunctionId));
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<OptMethod> getMethodByUserAndOptid(String userCode, String optid) {
        String querySql = "select USER_CODE, OPT_CODE, OPT_NAME, OPT_ID, OPT_METHOD " +
                "from F_V_USEROPTLIST " +
                "where USER_CODE= ? and OPT_ID = ?";

        return getJdbcTemplate().execute(
                (ConnectionCallback<List<OptMethod>>) conn ->
                        OrmDaoUtils.queryObjectsByParamsSql(conn, querySql ,
                                new Object[]{userCode, optid}, OptMethod.class));

    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<OptMethodUrlMap> listAllOptMethodUrlMap() {
        return getJdbcTemplate().execute(
                (ConnectionCallback<List<OptMethodUrlMap>>) conn ->
                        OrmDaoUtils.listAllObjects(conn, OptMethodUrlMap.class));
    }

    @Override
    public List<OptInfo> listObjectByParentOptid(String optId){
        return this.listObjectsByProperty("preOptId", optId);
    }


    public int countChildrenSum(String optId){
        return pageCount(QueryUtils.createSqlParamsMap("preOptId",optId) );
    }

    @Override
    public OptInfo getObjectById(String optId) {
        return super.getObjectById(optId);
    }

   /* public List<OptInfo> listObjectsByCon(String condition){
        return this.listObjectsByFilter(" where "+condition, (Object[]) null);
    }*/

    @Override
    public List<OptInfo> listObjectsAll() {
        return super.listObjects();
    }

    @Override
    public void deleteObjectById(String optId) {
        super.deleteObjectById(optId);
    }

    @Override
    public List<OptInfo> listMenuByTypes(String... types){
        Map<String, Object> map = new HashMap<>(2);
        if(types.length == 1){
            map.put("optType", types);
        }else {
            map.put("optTypes", types);
        }
        return listObjects(map);
    }

}
