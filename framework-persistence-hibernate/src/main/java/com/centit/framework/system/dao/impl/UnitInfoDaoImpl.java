package com.centit.framework.system.dao.impl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.UnitInfoDao;
import com.centit.framework.system.po.UnitInfo;
import com.centit.framework.system.po.UserInfo;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("unitInfoDao")
public class UnitInfoDaoImpl extends BaseDaoImpl<UnitInfo, String> implements UnitInfoDao {
    public static final Logger logger = LoggerFactory.getLogger(UnitInfoDaoImpl.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put("unitCode", CodeBook.EQUAL_HQL_ID);
            filterField.put("UNITNAME", CodeBook.LIKE_HQL_ID);
            filterField.put("ISVALID", CodeBook.EQUAL_HQL_ID);
            filterField.put("UNITTAG", CodeBook.EQUAL_HQL_ID);
            filterField.put("UNITWORD", CodeBook.EQUAL_HQL_ID);
            filterField.put("parentUnit", CodeBook.EQUAL_HQL_ID);
            filterField.put("NP_TOPUnit", "(parentUnit is null or parentUnit='0')");
            filterField.put(CodeBook.ORDER_BY_HQL_ID, " unitOrder, unitCode ");
        }
        return filterField;
    }

    @Transactional
    public String getNextKey() {
    /*    return getNextKeyByHqlStrOfMax("unitCode",
                        "FUnitinfo WHERE unitCode !='99999999'",6);*/
        return DatabaseOptUtils.getNextKeyBySequence(this, "S_UNITCODE", 6);
    }

    @Transactional
    public String getUnitCode(String depno) {
        List<UnitInfo> ls = listObjects("FROM UnitInfo where depNo=?", depno);
        if (ls != null) {
            return ls.get(0).getUnitCode();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation=Propagation.MANDATORY)
    public List<UserInfo> listUnitUsers(String unitCode) {
        String sSqlsen = "select a.* " +
                "from F_USERINFO a join F_USERUNIT b on(a.USERCODE=b.USERCODE) " +
                "where b.UNITCODE =?";

        return DatabaseOptUtils.findObjectsBySql(
                this, sSqlsen, new Object[]{unitCode} ,UserInfo.class);
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation=Propagation.MANDATORY)
    public List<UserInfo> listRelationUsers(String unitCode) {
        String sSqlsen = "select * FROM F_USERINFO ui where ui.USERCODE in " +
                "(select USERCODE from F_USERUNIT where UNITCODE= ? ) or " +
                "ui.USERCODE in (select USERCODE from F_USERROLE where ROLECODE like ? ";

        return DatabaseOptUtils.findObjectsBySql(
                this, sSqlsen,new Object[]{unitCode,unitCode+ "-%"}, UserInfo.class);
    }

    @Transactional
    public String getUnitNameOfCode(String unitcode) {
       return String.valueOf( DatabaseOptUtils.getSingleObjectBySql(this,
                "select UNITNAME from F_UNITINFO where UNITCODE=?", unitcode ));
    }

    /**
     * 批量添加或更新
     *
     * @param unitinfos List
     */
    @Transactional
    public void batchSave(List<UnitInfo> unitinfos) {
        for (int i = 0; i < unitinfos.size(); i++) {
            saveObject(unitinfos.get(i));
        }
    }

    @Transactional
    public void batchMerge(List<UnitInfo> unitinfos) {
        for (int i = 0; i < unitinfos.size(); i++) {
            this.mergeObject(unitinfos.get(i));

            if (19 == i % 20) {
                DatabaseOptUtils.flush(this.getCurrentSession());
            }
        }
    }

    @Transactional
    public UnitInfo getUnitByName(String name) {
        if (StringUtils.isNotBlank(name)) {
            String hql = "from UnitInfo where unitName = ? or unitShortName = ?"
                        + " order by unitOrder asc";
            List<UnitInfo> list = listObjects(hql,
                    new Object[]{name,name});
            if (list !=null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    @Transactional
    public UnitInfo getUnitByTag(String unitTag) {
        return super.getObjectByProperty("unitTag", unitTag);
    }

    @Transactional
    public UnitInfo getUnitByWord(String unitWord) {
        return super.getObjectByProperty("unitWord", unitWord);
    }

    @Transactional
    public List<UnitInfo> listSubUnits(String unitCode){
        return super.listObjectByProperty("parentUnit", unitCode);
        /*String hql = "from UnitInfo where parentUnit = ?";
        return listObjects(hql,
            new Object[]{unitCode,unitCode});*/
    }

    @Transactional(propagation=Propagation.MANDATORY)
    public List<UnitInfo> listAllSubUnits(String unitCode){
        UnitInfo unitInfo = this.getObjectById(unitCode);
        if(unitInfo != null) {
            return listSubUnitsByUnitPaht(unitInfo.getUnitPath());
        }
        return null;
    }

    @Transactional(propagation=Propagation.MANDATORY)
    public List<UnitInfo> listSubUnitsByUnitPaht(String unitPath){
        String hql = "from UnitInfo where unitPath like ?";
        return listObjects(hql,
            new Object[]{unitPath+"%"});
    }

    public List<String> getAllParentUnit(){
        return (List<String>)DatabaseOptUtils.findObjectsBySql(this,
                "select distinct t.parent_unit from f_unitinfo t ");
    }

    public int countChildrenSum(String unitCode){
        return NumberBaseOpt.castObjectToInteger(DatabaseOptUtils.getSingleObjectBySql(this,
          "select count(1) as subunits from F_UNITINFO where PARENT_UNIT = ?",  unitCode));
    }

    /**
     * 根据名称获取同级机构
     * @param unitName 单位 机构名称
     * @param parentCode 父类代码
     * @return 单位信息
     */
    @Override
    public UnitInfo getPeerUnitByName(String unitName, String parentCode, String unitCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("unitName", unitName);
        map.put("parentUnit", parentCode);
        map.put("unitCode", unitCode);
        StringBuilder sql = new StringBuilder();
        sql.append("from UnitInfo u where u.unitName = :unitName and u.parentUnit = :parentUnit and u.unitCode <> :unitCode");
        List<UnitInfo> unitInfos = listObjectsByNamedHql(sql.toString(), map, -1, -1);
        if(unitInfos==null || unitInfos.size()==0)
            return null;
        return unitInfos.get(0);
    }

    /**
     * 根据PARENT_UNIT和UNIT_ORDER获取同级机构
     * @param parentUnit 机构名称
     * @param unitOrder 父类代码
     * @return UnitInfo 机构信息
     */
    @Override
    public Integer isExistsUnitByParentAndOrder(String parentUnit, long unitOrder) {
      String sql = "select count(*) as existUnit " +
          "from UnitInfo u  " +
          "where u.unitOrder = :unitOrder and u.parentUnit = :parentUnit ";

       Object object = DatabaseOptUtils.getSingleObjectByHql( this, sql, QueryUtils.createSqlParamsMap(
          "unitOrder", unitOrder, "parentUnit", parentUnit));


      return NumberBaseOpt.castObjectToInteger(object);
    }
}
