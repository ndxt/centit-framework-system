package com.centit.framework.system.dao;

import com.centit.framework.system.po.UnitInfo;
import com.centit.framework.system.po.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * 机构信息Dao
 * @author zou_wy@centit.com
 */
public interface UnitInfoDao {

    /**
     * 根据Id获取机构信息
     * @param unitCode 机构ID
     * @return UnitInfo
     */
    UnitInfo getObjectById(String unitCode);

    /**
     * 新增机构信息
     * @param unitInfo 机构对象信息
     */
    void saveNewObject(UnitInfo unitInfo);

    /**
     * 更新机构信息
     * @param unitInfo 机构对象信息
     */
    void updateUnit(UnitInfo unitInfo);

    /**
     * 根据Id删除机构
     * @param unitCode 机构Id
     */
    void deleteObjectById(String unitCode);

    /**
     * 查询所有机构列表
     * @return List<UserInfo>
     */
    List<UnitInfo> listObjects();

    /**
     * 根据条件查询机构列表
     * @param filterMap 过滤条件Map
     * @return List<UserInfo>
     */
    List<UnitInfo> listObjects(Map<String, Object> filterMap);


    /**
     * 根据过滤条件查询总行数
     * @param filterDescMap 过滤条件Map
     * @return 总行数
     */
    int pageCount(Map<String, Object> filterDescMap);

    /**
     * 分页查询
     * @param pageQueryMap 过滤条件Map
     * @return List<UserInfo>
     */
    List<UnitInfo> pageQuery(Map<String, Object> pageQueryMap);

     int countChildrenSum(String unitCode);

    // DatabaseOptUtils.getNextKeyBySequence(this, "S_UNITCODE", 6);
     String getNextKey();

    //listObjectsAll("FROM UnitInfo where depNo=?", depno);
     String getUnitCode(String depno);

    /**
     * "select a.* " +
                "from f_Userinfo a join f_userunit b on(a.userCode=b.userCode) " +
                "where b.unitcode =?"
     * @param unitCode unitCode
     * @return List UserInfo
     */
     List<UserInfo> listUnitUsers(String unitCode);

    /**
     * "select * FROM F_Userinfo ui where ui.userCode in " +
                "(select userCode from f_userunit where unitcode='" + unitCode + "') or " +
                "ui.userCode in (select userCode from f_userrole where rolecode like ? "
     * @param unitCode unitCode
     * @return List UserInfo
     */
     List<UserInfo> listRelationUsers(String unitCode);

    // "select unitname from f_unitinfo where unitcode=?", unitcode ));
     String getUnitNameOfCode(String unitcode);

    /**
     * "from UnitInfo where unitName = ? or unitShortName = ?"
                        + " order by unitOrder asc";
     * @param name name
     * @return UnitInfo
     */
    UnitInfo getUnitByName(String name);

    //return super.getObjectByProperty("unitTag", unitTag);
    UnitInfo getUnitByTag(String unitTag);

    //return super.getObjectByProperty("unitWord", unitWord);
    UnitInfo getUnitByWord(String unitWord);

    //String hql = "from UnitInfo where unitPath like ?";{unitPath+"/%"});
    List<UnitInfo> listSubUnitsByUnitPaht(String unitPath);

    List<String> getAllParentUnit();

    UnitInfo getPeerUnitByName(String unitName, String parentCode, String unitCode);

    Integer isExistsUnitByParentAndOrder(String parentUnit, long unitOrder);
}
