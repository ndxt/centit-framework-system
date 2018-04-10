package com.centit.framework.system.dao;

import com.centit.framework.system.po.FVUserOptMoudleList;
import com.centit.framework.system.po.OptInfo;
import com.centit.framework.system.po.OptMethodUrlMap;

import java.util.List;
import java.util.Map;

/**
 * 菜单信息Dao
 * @author zou_wy@centit.com
 */
public interface OptInfoDao {

    /**
     * 查询所有机构列表
     * @return List &lt;UserInfo&gt;
     */
    List<OptInfo> listObjectsAll();

    /**
     * 根据条件查询机构列表
     * @param filterMap 过滤条件Map
     * @return List&lt;UserInfo&gt;
     */
    List<OptInfo> listObjects(Map<String, Object> filterMap);

    /**
     * 新增菜单
     * @param optInfo 菜单对象
     */
    void saveNewObject(OptInfo optInfo);

    /**
     * 删除菜单
     * @param optInfo 菜单对象
     */
     void deleteObject(OptInfo optInfo);

    /**
     * 根据Id删除菜单
     * @param optId 菜单Id
     */
    void deleteObjectById(String optId);

    /**
     * 更新菜单
     * @param optInfo 菜单对象
     */
    void updateOptInfo(OptInfo optInfo);

    /**
     * 查询下级菜单数量
     * @param optId 菜单ID
     * @return 菜单数量
     */
     int countChildrenSum(String optId);

    /**
     * 根据Id查询菜单
     * @param optId 菜单对象
     * @return OptInfo
     */
     OptInfo getObjectById(String optId);

    /**
     * 查询用户拥有的叶子菜单
      * @param userCode 用户ID
     * @param optType 菜单类型
     * @return List&lt;FVUserOptMoudleList&gt;
     */
     List<FVUserOptMoudleList> getMenuFuncByUserID(String userCode, String optType);

    /**
     * 查询用户拥有的所有叶子菜单
     * @param userCode 用户代码
     * @param optType 菜单类型
     */
    List<FVUserOptMoudleList> listUserAllSubMenu(String userCode, String optType);

    /**
     * 查询有子菜单 的菜单（opt_url=...）
     * @return 菜单列表
     */
    List<OptInfo> getMenuFuncByOptUrl();

  /**
   * 查询用户数据范围
   * @param userCode 用户Id
   * @param optId 菜单Id
   * @param optMethod 操作定义
   * @return List&lt;String&gt;
   */
     List<String> listUserDataPowerByOptMethod(String userCode, String optId, String optMethod);

    /**
     * 查询全部OptMethodUrlMap
     * @return List&lt;OptMethodUrlMap&gt;
     */
    List<OptMethodUrlMap> listAllOptMethodUrlMap();

    /**
     * 根据父Id查询下级菜单
     * @param optId 父Id
     * @return List&lt;OptInfo&gt;
     */
    List<OptInfo> listObjectByParentOptid(String optId);

    /**
     * 根据菜单类型获取菜单
     * @param types 类型数组
     * @return 菜单列表
     */
    List<OptInfo> listMenuByTypes(String... types);

}
