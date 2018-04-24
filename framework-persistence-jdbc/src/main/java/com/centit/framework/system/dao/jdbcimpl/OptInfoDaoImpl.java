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
    public List<OptInfo> getMenuFuncByOptUrl(){
        String hql1 = "where OPT_URL='...' order by ORDER_IND ";
        return super.listObjectsByFilter(hql1,(Object[]) null);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public List<OptInfo> getMenuFuncByUserID(String userCode, String optType) {

        String querySql = "SELECT DISTINCT a.USER_CODE,d.Opt_ID,d.Opt_Name,d.Pre_Opt_ID,d.Form_Code,d.opt_url,d.opt_Route," +
            "d.Msg_No,d.Msg_Prm,d.Is_In_ToolBar,d.Img_Index,d.Top_Opt_ID,d.Order_Ind,d.Page_Type,d.Opt_Type,d.flow_code," +
            "d.icon,d.height,d.width,d.update_date,d.create_date,d.creator,d.updator " +
            "FROM f_v_userroles a " +
            "JOIN f_rolepower b ON a.ROLE_CODE = b.ROLE_CODE " +
            "JOIN f_optdef c ON b.OPT_CODE = c.OPT_CODE " +
            "JOIN f_optinfo d ON c.Opt_ID = d.Opt_ID " +
            "WHERE d.opt_url <> '...' " +
            "and d.IS_IN_TOOLBAR = 'Y' " +
            "and a.USER_CODE = ? "+
            "and d.OPT_TYPE = ? "+
            "order by d.ORDER_IND ";



      /*  String querySql = "select OPT_ID, USER_CODE, OPT_NAME, PRE_OPT_ID, FORM_CODE,"+
                "OPT_URL, OPT_ROUTE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, " +
                "TOP_OPT_ID, ORDER_IND, PAGE_TYPE "+
                "from F_V_USEROPTMOUDLELIST "+
                "where IS_IN_TOOLBAR = 'Y' "+
                "and USER_CODE = ? "+
                "and OPT_TYPE = ? "+
                "order by ORDER_IND ";*/

        return getJdbcTemplate().execute(
                (ConnectionCallback<List<OptInfo>>) conn ->
                        OrmDaoUtils.queryObjectsByParamsSql(conn, querySql ,
                                new Object[]{userCode, optType}, OptInfo.class));

    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<String> listUserDataPowerByOptMethod(String userCode,String optId,String optMethod) {

        String sql = "select OPT_SCOPE_CODES " +
            "from F_V_USEROPTDATASCOPES " +
            "where USER_CODE = ? and OPT_ID = ? and OPT_METHOD = ?";
        return this.getJdbcTemplate().queryForList(sql,
                new Object[]{userCode, optId, optMethod} ,String.class);
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

    @Override
    public void updateOptInfo(OptInfo optInfo){
        super.updateObject(optInfo);
    }

    @Override
    public List<OptInfo> listUserAllSubMenu(String userCode, String optType){
        String querySql = "SELECT DISTINCT a.USER_CODE,d.Opt_ID,d.Opt_Name,d.Pre_Opt_ID,d.Form_Code,d.opt_url,d.opt_Route," +
            "d.Msg_No,d.Msg_Prm,d.Is_In_ToolBar,d.Img_Index,d.Top_Opt_ID,d.Order_Ind,d.Page_Type,d.Opt_Type,d.flow_code," +
            "d.icon,d.height,d.width,d.update_date,d.create_date,d.creator,d.updator " +
            "FROM f_v_userroles a " +
            "JOIN f_rolepower b ON a.ROLE_CODE = b.ROLE_CODE " +
            "JOIN f_optdef c ON b.OPT_CODE = c.OPT_CODE " +
            "JOIN f_optinfo d ON c.Opt_ID = d.Opt_ID " +
            "WHERE d.opt_url <> '...' " +
            "and a.USER_CODE = ? "+
            "and d.OPT_TYPE = ? "+
            "order by d.ORDER_IND ";


       /* String querySql = "select OPT_ID, USER_CODE, OPT_NAME, PRE_OPT_ID, FORM_CODE,"+
            "OPT_URL, OPT_ROUTE, OPT_TYPE, MSG_NO, MSG_PRM, IS_IN_TOOLBAR, IMG_INDEX, " +
            "TOP_OPT_ID, ORDER_IND, PAGE_TYPE "+
            "from F_V_USEROPTMOUDLELIST "+
            "where USER_CODE = ? "+
            "and OPT_TYPE = ? "+
            "order by ORDER_IND ";
*/
        return getJdbcTemplate().execute(
            (ConnectionCallback<List<OptInfo>>) conn ->
                OrmDaoUtils.queryObjectsByParamsSql(conn, querySql ,
                    new Object[]{userCode, optType}, OptInfo.class));
    }

}
