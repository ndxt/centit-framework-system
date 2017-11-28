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

    /**
     * 获取下一个序列值
     * @return
     */
    String getNextKey();

    /**
     * 根据机构Id获取用户列表
     * @param unitCode unitCode
     * @return List<UserInfo>
     */
    List<UserInfo> listUnitUsers(String unitCode);

    /**
     * 根据名称查新机构
     * @param name 机构名称
     * @return UnitInfo
     */
    UnitInfo getUnitByName(String name);

    /**
     * 根据Tag查新机构
     * @param unitTag Tag
     * @return UnitInfo
     */
    UnitInfo getUnitByTag(String unitTag);

    /**
     * 根据unitWord查新机构
     * @param unitWord unitWord
     * @return UnitInfo
     */
    UnitInfo getUnitByWord(String unitWord);

    /**
     * 根据UnitPath查询子机构
     * @param unitPath 机构层级
     * @return List<UnitInfo>
     */
    List<UnitInfo> listSubUnitsByUnitPaht(String unitPath);

    /**
     * 查询所有非叶子机构的Id
     * @return ID列表
     */
    List<String> getAllParentUnit();

    /**
     * 根据名称获取同级机构
     * @param unitName 机构名称
     * @param parentCode 父机构ID
     * @param unitCode 机构ID
     * @return UnitInfo
     */
    UnitInfo getPeerUnitByName(String unitName, String parentCode, String unitCode);

    /**
     * 根据UNIT_ORDER获取同级机构 数量
     * @param parentUnit 父机构ID
     * @param unitOrder 排序号
     * @return 数量
     */
    Integer isExistsUnitByParentAndOrder(String parentUnit, long unitOrder);
}
