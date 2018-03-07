package com.centit.framework.system.dao;

import com.centit.framework.system.po.OptMethod;

import java.util.List;

/**
 * 用户操作Dao
 * @author god
 * update by zou_wy@centit.com
 * date 2017-11-29
 */
public interface OptMethodDao {

    /**
     * 查询全部操作
     * @return List&lt;OptMethod&gt;
     */
    List<OptMethod> listObjects();

    /**
     * 根据Id查询操作
     * @param optCode 操作Id
     * @return OptMethod
     */
    OptMethod getObjectById(String optCode);

    /**
     * 更新操作定义
     * @param optMethod 操作对象
     */
    void updateOptMethod(OptMethod optMethod);

    /**
     * 删除操作
     * @param optMethod 操作对象
     */
    void deleteObject(OptMethod optMethod);

    /**
     * 根据Id删除操作
     * @param optCode 操作Id
     */
    void deleteObjectById(String optCode);

    /**
     * 新增操作
     * @param optMethod 操作对象
     */
    void saveNewObject(OptMethod optMethod);

    /**
     * 根据菜单Id查询操作
     * @param sOptID 菜单Id
     * @return List&lt;OptMethod&gt;
     */
    List<OptMethod> listOptMethodByOptID(String sOptID);

    /**
     *  根据角色Id查询操作
     * @param roleCode 角色Id
     * @return List&lt;OptMethod&gt;
     */
    List<OptMethod> listOptMethodByRoleCode(String roleCode);

    /**
    * 根据菜单Id删除操作
    * @param sOptID 菜单Id
    */
    void deleteOptMethodsByOptID(String sOptID);

    /**
     * 查询下一个序列值
     * @return String
     */
    String getNextOptCode();

}
