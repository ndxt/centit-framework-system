package com.centit.framework.system.dao;

import com.centit.framework.mybatis.dao.BaseDao;
import com.centit.framework.system.po.FVUserOptMoudleList;
import com.centit.framework.system.po.OptInfo;
import com.centit.framework.system.po.OptMethod;
import com.centit.framework.system.po.OptMethodUrlMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OptInfoDao extends BaseDao {

    List<OptInfo> listObjects(Map<String, Object> filterMap);

    List<OptInfo> listObjectsByRoleCode(String roleCode);

    List<OptInfo> listObjectsByCon(@Param(value="condition")String condition);

    List<OptInfo> listObjectsAll();

    List<OptInfo> listMenuByTypes(@Param("optType1") String optType1, @Param("optType2") String optType2);

    void deleteObject(OptInfo optMethod);

    void mergeObject(OptInfo optMethod);

    void deleteObjectById(String optId);

    void saveNewObject(OptInfo optMethod);

    //"select count(1) as hasChildren from OptInfo where preOptId = ?",optId
    int countChildrenSum(String optId);


    OptInfo getObjectById(String optId);

    //"from OptInfo opt where opt.isInToolbar = 'T'";
    List<OptInfo> listValidObjects();

    // String hql = "FROM FVUserOptMoudleList where userCode=?";
    List<OptInfo> getFunctionsByUserID(String userID);



    //"FROM OptInfo where optUrl='...' order by orderInd ";
    //"FROM FVUserOptMoudleList where isintoolbar='Y' and userCode=? and optType = " +
    //(isAdmin ? "'S'" : "'O'") + " ORDER BY orderind";
    //return getMenuFuncs(preOpts, ls);
    List<FVUserOptMoudleList> getMenuFuncByUserID(@Param("userCode") String userCode, @Param("optType") String optType);

    List<OptInfo> getMenuFuncByOptUrl();

//     List<FVUserOptMoudleList> getMenuFuncByUserID(Map map);


    List<String> listUserDataPowerByOptMethod(@Param("userCode") String userCode,
                                               @Param("optId") String optid,
                                               @Param("optMethod") String optMethod);//zou_wy

    //"FROM FVUserOptMoudleList  where userCode=? and topoptid=?" + " ORDER BY preoptid, orderind";
    //参数  String userID, String superFunctionId
    List<OptInfo> getFunctionsByUserAndSuperFunctionId(Map map);

    // String hql = "FROM FVUserOptList urv where urv.id.userCode=? and optid= ?";
    //参数String userCode, String optid
    List<OptMethod> getMethodByUserAndOptid(Map map);

    // DatabaseOptUtils.findObjectsByHql(this, "from OptMethodUrlMap");
    List<OptMethodUrlMap> listAllOptMethodUrlMap();

    List<OptInfo> listObjectByParentOptid(@Param("optId") String optId);

  /**
   * 根据菜单类型获取菜单
   * @param types 类型数组
   * @return 菜单列表
   */
    List<OptInfo> listMenuByTypes(@Param("types") String... types);

}
