package com.centit.framework.system.dao;

import com.centit.framework.system.po.RoleInfo;
import com.centit.framework.system.po.VOptTree;

import java.util.List;
import java.util.Map;

/**
 * 角色信息Dao
 * @author zou_wy@centit.com
 */
public interface RoleInfoDao {

    /**
     * 查询所有机构列表
     * @return List &lt;UserInfo&gt;
     */
    List<RoleInfo> listObjectsAll();

    /**
     * 根据条件查询机构列表
     * @param filterMap 过滤条件Map
     * @return List &lt;UserInfo&gt;
     */
    List<RoleInfo> listObjects(Map<String, Object> filterMap);


    /**
     * 根据过滤条件查询总行数
     * @param filterDescMap 过滤条件Map
     * @return 总行数
     */
    int pageCount(Map<String, Object> filterDescMap);

    /**
     * 分页查询
     * @param pageQueryMap 过滤条件Map
     * @return List &lt;UserInfo&gt;
     */
    List<RoleInfo> pageQuery(Map<String, Object> pageQueryMap);

    /**
     * 新增角色
     * @param roleInfo 角色对象
     */
    void saveNewObject(RoleInfo roleInfo);

    /**
     * 根据Id删除角色
     * @param roleCode 角色ID
     */
    void deleteObjectById(String roleCode);

    /**
     * 更新角色
     * @param roleInfo 角色对象
     */
    void updateRole(RoleInfo roleInfo);

    /**
     * 获取下一个序列值
     * @return String
     */
    String getNextKey();

    /**
     * 根据Id查询角色
     * @param roleCode 角色Id
     * @return RoleInfo
     */
    RoleInfo getObjectById(String roleCode);

    /**
     * 查询角色 根据代码 或者 名称（根据名称查询时经查询 G 和 P 类别的角色）
     * @param roleCodeOrName 角色代码或者名称
     * @return RoleInfo
     */
    RoleInfo getRoleByCodeOrName(String roleCodeOrName);

    /**
     * 查询菜单树
     * @return List &lt;VOptTree&gt;
     */
    List<VOptTree> getVOptTreeList();

    /**
     * 根据角色Id查询操作定义
     * @param roleCode 角色ID
     * @return List
     */
    List<Object> listRoleOptMethods(String roleCode);

    /**
     * 根据属性查询角色
     * @param propertyName 属性名称
     * @param propertyValue 属性值
     * @return RoleInfo
     */
    RoleInfo getObjectByProperty(String propertyName, Object propertyValue);
}
