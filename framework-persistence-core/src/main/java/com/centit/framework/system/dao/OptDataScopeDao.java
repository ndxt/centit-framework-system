package com.centit.framework.system.dao;

import com.centit.framework.system.po.OptDataScope;

import java.util.Collection;
import java.util.List;

/**
 * 数据范围Dao
 * @author god
 * updated by zou_wy@centit.com
 */
public interface OptDataScopeDao {

    /**
     * 新增
     */
    void saveNewOPtDataScope(OptDataScope optDataScope);

  /**
     * 更新数据范围
     * @param optDataScope 数据范围对象
     */
    void updateOptDataScope(OptDataScope optDataScope);

    /**
     * 删除
     * @param optDataScope 数据范围对象
     */
    void deleteObject(OptDataScope optDataScope);

    /**
     * 根据菜单ID查询数据范围
     * @param sOptID 菜单Id
     * @return List<OptDataScope>
     */
    List<OptDataScope> getDataScopeByOptID(String sOptID);

    /**
     * 根据菜单Id查询数据范围数量
     * @param sOptID 菜单ID
     * @return
     */
    int getOptDataScopeSumByOptID(String sOptID);


    /**
     * 根据菜单Id删除数据范围
     * @param sOptID 菜单ID
     */
    void deleteDataScopeOfOptID(String sOptID) ;

    /**
     * 获取下一个序列
     * @return String
     */
    String getNextOptCode();

    /**
     * 根据Id查询过滤条件
     * @param scopeCodes
     * @return
     */
    List<String> listDataFiltersByIds(Collection<String> scopeCodes);//zou_wy
}
