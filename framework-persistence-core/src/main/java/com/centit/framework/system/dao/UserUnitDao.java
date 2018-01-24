package com.centit.framework.system.dao;

import com.centit.framework.system.po.UserUnit;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * 用户机构Dao
 * @author god
 * updated by zou_wy@centit.com
 */
public interface UserUnitDao {

    /**
    * 根据条件查询
    * @param filterMap 过滤条件
    * @return List &lt;UserUnit&gt;
    */
    List<UserUnit> listObjects(Map<String, Object> filterMap);

    /**
    * 查询数量 用于分页
    * @param filterDescMap 过滤条件
    * @return int
    */
    int pageCount(Map<String, Object> filterDescMap);

    /**
    * 分页查询
    * @param pageQueryMap 过滤条件
    * @return List &lt;UserUnit&gt;
    */
    List<UserUnit> pageQuery(Map<String, Object> pageQueryMap);

    /**
    * 查询全部
    * @return List &lt;UserUnit&gt;
    */
    List<UserUnit> listObjectsAll();

    /**
    * 根据Id查询
    * @param userUnitId 用户机构Id
    * @return UserUnit
    */
    UserUnit getObjectById(String userUnitId);

    /**
    * 新增
    * @param userUnit 用户机构
    */
    void saveNewObject(UserUnit userUnit);

    /**
    * 更新
    * @param userUnit 用户机构
    */
    void updateObject(UserUnit userUnit);

    /**
    * 根据Id删除
    * @param userUnitId 用户机构Id
    */
    void deleteObjectById(String userUnitId);

    /**
    * 强制删除
    * @param id 用户机构Id
    */
    void deleteObjectForceById(String id);

    /**
    * 删除
    * @param object 用户机构
    */
    void deleteObject(UserUnit object);

    /**
    * 根据用户代码查询
    * @param userId 用户代码
    * @return List &lt;UserUnit&gt;
    */
    List<UserUnit> listUserUnitsByUserCode(String userId);

    /**
    * 根据用户和机构代码查询
    * @param userCode 用户代码
    * @param unitCode 机构代码
    * @return List &lt;UserUnit&gt;
    */
    List<UserUnit> listObjectByUserUnit(String userCode, String unitCode);

    /**
    * 获取下一个序列
    * @return String
    */
    String getNextKey();

    /**
    * 根据用户代码删除
    * @param userCode 用户代码
    */
    void deleteUserUnitByUser(String userCode);

    /**
    * 根据机构代码删除
    * @param unitCode 机构代码
    */
    void deleteUserUnitByUnit(String unitCode);

    /**
    * 根据用户代码获取主机构关系
    * @param userId 用户代码
    * @return UserUnit
    */
    UserUnit getPrimaryUnitByUserId(String userId);

    /**
    * 根据机构代码查询
    * @param unitCode 机构代码
    * @return List &lt;UserUnit&gt;
    */
    List<UserUnit> listUnitUsersByUnitCode(String unitCode);

    /**
    * 查询用户组数量 用于分页
    * @param filterDescMap 过滤条件
    * @return 条数
    */
    int countSubUserUnits(Map<String, Object> filterDescMap);

    /**
    * 分页查询 用户组
    * @param pageQueryMap 包含分页信息 过滤条件
    * @return 用户组列表
    */
    List<UserUnit> querySubUserUnits(Map<String, Object> pageQueryMap);
}
