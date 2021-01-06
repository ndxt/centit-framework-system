package com.centit.framework.system.service;

import com.centit.framework.system.po.UserUnit;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-10-28
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
public interface SysUserUnitManager{
    UserUnit getObjectById(String userUnitId);

    void deleteObject(UserUnit userUnit);

    List<UserUnit> listObjects(Map<String, Object> filterMap, PageDesc pageDesc);

    List<UserUnit> listObjectByUserUnit(String userCode, String unitCode);

    UserUnit getPrimaryUnitByUserCode(String userCode);

    String saveNewUserUnit(UserUnit userUnit);

    void updateUserUnit(UserUnit userunit);

    boolean hasUserStation(String stationCode,String userCode);

    List<UserUnit> listUnitUsersByUnitCode(String unitCode);
    List<UserUnit> listUserUnitssByUserCode(String userCode);

    void deletePrimaryUnitByUserCode(String userCode);

  /**
   * 获取 某机构及其子机构下 所有 用户组
   * @param unitCode 机构Code
   * @param map 过滤条件
   * @param pageDesc 分页信息
   * @return 用户组列表
   */
    List<UserUnit> listSubUsersByUnitCode(String unitCode, Map<String, Object> map, PageDesc pageDesc);

    List<UserUnit> listUserUnitsUnderUnitByUserCode(String userCode, String unitCode, PageDesc pageDesc);

}
