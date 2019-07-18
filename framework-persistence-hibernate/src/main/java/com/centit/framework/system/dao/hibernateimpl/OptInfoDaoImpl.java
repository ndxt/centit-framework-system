package com.centit.framework.system.dao.hibernateimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.OptInfoDao;
import com.centit.framework.system.po.FVUserOptMoudleList;
import com.centit.framework.system.po.OptInfo;
import com.centit.framework.system.po.OptMethodUrlMap;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("optInfoDao")
public class OptInfoDaoImpl extends BaseDaoImpl<OptInfo, String> implements OptInfoDao {

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("optId", CodeBook.EQUAL_HQL_ID);
            filterField.put("optUrl", CodeBook.EQUAL_HQL_ID);
            filterField.put("optName", CodeBook.LIKE_HQL_ID);
            filterField.put("preOptId", CodeBook.EQUAL_HQL_ID);
            filterField.put("NP_topOptId", "(preOptId is null or preOptId='0')");
            filterField.put("optType", CodeBook.EQUAL_HQL_ID);
            filterField.put("optTypes", "optType in :optTypes");
            filterField.put("topOptId", CodeBook.EQUAL_HQL_ID);
            filterField.put("isInToolbar", CodeBook.EQUAL_HQL_ID);
            filterField.put(CodeBook.ORDER_BY_HQL_ID, " preOptId, orderInd");
        }
        return filterField;
    }

    @Override
    @Transactional
    public List<OptInfo> listParentMenuFunc(){
        String hql1 = "FROM OptInfo where optUrl='...' order by orderInd ";
        List<OptInfo> preOpts = listObjects(hql1);
        return preOpts;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<OptInfo> getMenuFuncByUserID(String userCode, String optType) {

        String hql = "FROM FVUserOptMoudleList where isInToolbar='Y' and userCode=?0 and optType = ?1 ORDER BY orderInd";
        // + " ORDER BY preoptid, formcode";
        List<FVUserOptMoudleList> ls = (List<FVUserOptMoudleList>)DatabaseOptUtils.findObjectsByHql
                (this, hql,new Object[]{userCode, optType});
        return mapOptMoudleListToOptInfo(ls);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<String> listUserDataPowerByOptMethod(String userCode,String optId,String optMethod) {
        String sSqlsen = "select OPT_SCOPE_CODES " +
                 "from F_V_USEROPTDATASCOPES " +
                 "where USER_CODE = ?0 and OPT_ID = ?1 and OPT_METHOD = ?2";

        List<Object[]> l = (List<Object[]>) DatabaseOptUtils.findObjectsBySql
                 (this, sSqlsen,new Object[]{userCode, optId, optMethod});

        if(l==null)
             return null;
        List<String> scopeCodes = new ArrayList<String>();
         for(Object[] obj : l)
             scopeCodes.add(String.valueOf(obj[0]));
         return scopeCodes;
    }

    private List<OptInfo> mapOptMoudleListToOptInfo(List<FVUserOptMoudleList> ls ) {
        List<OptInfo> opts = new ArrayList<>(ls.size()+1);
        for (FVUserOptMoudleList opm : ls) {
            OptInfo opt = new OptInfo();
            opt.setFormCode(opm.getFormCode());
            opt.setImgIndex(opm.getImgIndex());
            opt.setIsInToolbar(opm.getIsInToolbar());
            opt.setMsgNo(opm.getMsgNo());
            opt.setOptType(opm.getOptType());
            opt.setMsgPrm(opm.getMsgPrm());
            opt.setOptId(opm.getOptId());
            opt.setOptName(opm.getOptName());
            opt.setOptUrl(opm.getOptUrl());
            opt.setOptRoute(opm.getOptRoute());
            opt.setPreOptId(opm.getPreOptId());
            opt.setTopOptId(opm.getTopOptId());
            opt.setPageType(opm.getPageType());
            opts.add(opt);
            //System.out.print(opt.getOptType());
        }
        return opts;
    }



    @SuppressWarnings("unchecked")
    @Transactional
    public List<OptMethodUrlMap> listAllOptMethodUrlMap() {
        List<?> listObjects = DatabaseOptUtils.findObjectsByHql
                (this, "from OptMethodUrlMap");

        return (List<OptMethodUrlMap>) listObjects;
    }

    public int countChildrenSum(String optId){
        return (int)DatabaseOptUtils.getSingleIntByHql(this,
                "select count(1) as hasChildren from OptInfo where preOptId = ?0",optId);
    }

 /*   public List<OptInfo> listObjectsByCon(String condition){
        return this.listObjectsAll("From OptInfo where "+condition);
    }*/

    public List<OptInfo> listObjectByParentOptid(String optId){
        return this.listObjects("From OptInfo where preOptId = ?0", optId);
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
        String hql = "FROM FVUserOptMoudleList where userCode=?0 and optType = ?1 ORDER BY orderInd";
        // + " ORDER BY preoptid, formcode";
        List<FVUserOptMoudleList> ls = (List<FVUserOptMoudleList>) DatabaseOptUtils.findObjectsByHql
            (this, hql,new Object[]{userCode, optType});
        return mapOptMoudleListToOptInfo(ls);
    }

}
