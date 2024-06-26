package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.model.basedata.OptInfo;
import com.centit.framework.model.basedata.OptMethodUrlMap;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("optInfoDao")
public class OptInfoDao extends BaseDaoImpl<OptInfo, String> {

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("optId", CodeBook.EQUAL_HQL_ID);
        filterField.put("optUrl", CodeBook.EQUAL_HQL_ID);
        filterField.put("optName", CodeBook.LIKE_HQL_ID);
        filterField.put("preOptId", CodeBook.EQUAL_HQL_ID);
        filterField.put("NP_TOPOPT", "(PRE_OPT_ID is null or PRE_OPT_ID='0')");
        filterField.put("optType", CodeBook.EQUAL_HQL_ID);
        filterField.put("optTypes", "OPT_TYPE in (:optTypes)");
        filterField.put("topOptId", CodeBook.EQUAL_HQL_ID);
        filterField.put("isInToolbar", CodeBook.EQUAL_HQL_ID);
        filterField.put("topUnit", CodeBook.EQUAL_HQL_ID);
        return filterField;
    }

    @Transactional
    public List<OptInfo> listParentMenuFunc(){
//        String hql1 = "where OPT_URL='...' order by ORDER_IND ";
        String sql = "where Opt_ID in (select PRE_OPT_ID from f_optinfo group by Pre_Opt_ID) order by order_ind";
        return super.listObjectsByFilter(sql,(Object[]) null);
    }
    @Transactional
    public List<OptInfo> listParentMenuFunc(String topOptid){
        String sql = "where Opt_ID in (select PRE_OPT_ID from f_optinfo group by Pre_Opt_ID) and top_opt_id=? order by order_ind";
        return super.listObjectsByFilter(sql,new Object[]{topOptid});
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<OptInfo> getMenuFuncByUserID(String userCode, String optType,String topOptId) {

        String querySql = "SELECT DISTINCT a.USER_CODE,d.Opt_ID,d.Opt_Name,d.Pre_Opt_ID,d.Form_Code,d.opt_url,d.opt_Route," +
            "d.Msg_No,d.Msg_Prm,d.Is_In_ToolBar,d.Img_Index,d.Top_Opt_ID,d.Order_Ind,d.Page_Type,d.Opt_Type,d.flow_code," +
            "d.icon,d.height,d.width,d.update_date,d.create_date,d.creator,d.updator " +
            "FROM f_v_userroles a " +
            "JOIN f_rolepower b ON a.ROLE_CODE = b.ROLE_CODE " +
            "JOIN f_optdef c ON b.OPT_CODE = c.OPT_CODE " +
            "JOIN f_optinfo d ON c.Opt_ID = d.Opt_ID " +
            "WHERE " +
//            "d.opt_url <> '...' " +
            "d.Opt_ID not in (select Pre_Opt_ID from f_optinfo group by Pre_Opt_ID) " +
            "and d.IS_IN_TOOLBAR = 'Y' " +
            "and a.USER_CODE = ? "+
            "and d.OPT_TYPE = ? "+
            "and d.top_opt_id = ? "+
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
                                new Object[]{userCode, optType,topOptId}, OptInfo.class));

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

    public List<OptInfo> listObjectByParentOptid(String optId){
        return this.listObjectsByProperties(CollectionsOpt.createHashMap("preOptId", optId));
    }


    public OptInfo getObjectById(String optId) {
        return super.getObjectById(optId);
    }

   /* public List<OptInfo> listObjectsByCon(String condition){
        return this.listObjectsByFilter(" where "+condition, (Object[]) null);
    }*/

    public List<OptInfo> listObjectsAll() {
        return super.listObjects();
    }

    @Transactional
    public void deleteObjectById(String optId) {
        super.deleteObjectById(optId);
    }


    public List<OptInfo> listMenuByTypes(String... types){
        Map<String, Object> map = new HashMap<>(2);
        if(types.length == 1){
            map.put("optType", types);
        }else {
            map.put("optTypes", types);
        }
        return listObjectsByProperties(map);
    }

    @Transactional
    public void updateOptInfo(OptInfo optInfo){
        //optInfo.setTopOptId(optInfo.getOsId());
        super.updateObject(optInfo);
    }


    public List<OptInfo> listUserAllSubMenu(String userCode, String optType){
        String querySql = "SELECT DISTINCT a.USER_CODE,d.Opt_ID,d.Opt_Name,d.Pre_Opt_ID,d.Form_Code,d.opt_url,d.opt_Route," +
            "d.Msg_No,d.Msg_Prm,d.Is_In_ToolBar,d.Img_Index,d.Top_Opt_ID,d.Order_Ind,d.Page_Type,d.Opt_Type,d.flow_code," +
            "d.icon,d.height,d.width,d.update_date,d.create_date,d.creator,d.updator " +
            "FROM f_v_userroles a " +
            "JOIN f_rolepower b ON a.ROLE_CODE = b.ROLE_CODE " +
            "JOIN f_optdef c ON b.OPT_CODE = c.OPT_CODE " +
            "JOIN f_optinfo d ON c.Opt_ID = d.Opt_ID " +
            "WHERE " +
//            "d.opt_url <> '...' " +
            "d.Opt_ID not in (select Pre_Opt_ID from f_optinfo group by Pre_Opt_ID) " +
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


    @Transactional
    public List<OptInfo> listAllOptInfoByTopOpt(String topOptId){
       return super.listObjectsByFilter("where TOP_OPT_ID = ? or OS_ID = ?",
           new Object[]{topOptId, topOptId});

    }

    @Transactional
    public List<OptInfo> listAllOptInfoByUnit(String topUnit){
        String sql = "select a.* " +
            "from F_OPTINFO a join F_OS_INFO b on(a.TOP_OPT_ID=b.os_id) " +
            "where b.TOP_UNIT = ?";

        return getJdbcTemplate().execute(
            (ConnectionCallback<List<OptInfo>>) conn ->
                OrmDaoUtils.queryObjectsByParamsSql(conn, sql ,
                    new Object[]{topUnit}, OptInfo.class));
    }

    @Transactional
    public List<OptInfo> listOptInfoByRoleCode(String roleCode){
        String sql = " SELECT " +
            " A.OPT_ID, " +
            " A.OPT_NAME, " +
            " A.PRE_OPT_ID, " +
            " A.OPT_ROUTE, " +
            " A.OPT_URL, " +
            " A.FORM_CODE, " +
            " A.OPT_TYPE, " +
            " A.MSG_NO, " +
            " A.MSG_PRM, " +
            " A.IS_IN_TOOLBAR, " +
            " A.IMG_INDEX, " +
            " A.TOP_OPT_ID, " +
            " A.ORDER_IND, " +
            " A.FLOW_CODE, " +
            " A.PAGE_TYPE, " +
            " A.ICON, " +
            " A.HEIGHT, " +
            " A.WIDTH, " +
            " A.DOC_ID, " +
            " A.OS_ID, " +
            " A.SOURCE_ID " +
            " FROM f_optinfo A JOIN f_optdef b on a.OPT_ID = b.OPT_ID   " +
            " WHERE b.OPT_CODE in (select rp.OPT_CODE from F_ROLEPOWER rp where rp.ROLE_CODE = ? ) ";
        return getJdbcTemplate().execute(
            (ConnectionCallback<List<OptInfo>>) conn ->
                OrmDaoUtils.queryObjectsByParamsSql(conn, sql ,new Object[]{roleCode}, OptInfo.class));
    }

    @Transactional
    public List<OptInfo> listFromParent(Map<String, Object> filterMap){
        String sql = "select a.* " +
            "from F_OPTINFO a join F_OS_INFO b on(a.TOP_OPT_ID=b.os_id) " +
            "where 1=1 [:topUnit | and b.TOP_UNIT = :topUnit] " +
            "[:preOptId | and a.PRE_OPT_ID =:preOptId]" +
            "[:NP_TOPOPT | and (a.PRE_OPT_ID is null or a.PRE_OPT_ID='0')]";
        QueryAndNamedParams qap = QueryUtils.translateQuery(sql, filterMap);
        return getJdbcTemplate().execute(
            (ConnectionCallback<List<OptInfo>>) conn ->
                OrmDaoUtils.queryObjectsByNamedParamsSql(conn, qap.getQuery() ,
                    qap.getParams(), OptInfo.class));
    }

    @Transactional
    public List<OptInfo> listUserOptinfos(String topUnit, String userCode) {
        String sql = "SELECT DISTINCT a.USER_CODE,d.Opt_ID,d.Opt_Name,d.Pre_Opt_ID,d.Form_Code,d.opt_url,d.opt_Route," +
            "d.Msg_No,d.Msg_Prm,d.Is_In_ToolBar,d.Img_Index,d.Top_Opt_ID,d.Order_Ind,d.Page_Type,d.Opt_Type,d.flow_code," +
            "d.icon,d.height,d.width,d.update_date,d.create_date,d.creator,d.updator " +
            "from F_V_USERROLES a " +
            "JOIN F_ROLEPOWER b ON a.Role_Code = b.Role_Code " +
            "JOIN F_OPTDEF c ON b.OPT_CODE = c.OPT_CODE " +
            "JOIN F_OPTINFO d ON c.OPT_ID = d.OPT_ID " +
            "where USER_CODE= :userCode and d.IS_IN_TOOLBAR = 'Y' " +
            "and (a.ROLE_TYPE='G' or (a.ROLE_TYPE='D' and a.UNIT_CODE = :unitCode ) )" +
            " order by d.PRE_OPT_ID,d.ORDER_IND ";
        Map<String, Object> map = CollectionsOpt.createHashMap("userCode", userCode,
            "unitCode", topUnit);
        return jdbcTemplate.execute(
            (ConnectionCallback<List<OptInfo>>) conn -> OrmDaoUtils
                .queryObjectsByNamedParamsSql(conn, sql, map, OptInfo.class));
    }
}
