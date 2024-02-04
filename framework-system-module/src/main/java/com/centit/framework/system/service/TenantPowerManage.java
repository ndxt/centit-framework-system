package com.centit.framework.system.service;

import com.alibaba.fastjson2.JSONArray;
import com.centit.support.common.ObjectException;

/**
 * 判断用户在租户中具有的权限与角色
 * 如果用户登录后，且判断的是当前人员不需要传入参数userCode
 */
public interface TenantPowerManage {

    /**
     * 判断用户是否为租户所有者
     *
     * @param userCode 用户code
     * @param topUnit  租户id
     * @return true:是所有者
     */
    boolean userIsTenantOwner(String userCode, String topUnit);

    /**
     * 判断用户是否为租户管理员
     *
     * @param userCode 用户code
     * @param topUnit  租户id
     * @return true:是管理员
     */
    boolean userIsTenantAdmin(String userCode, String topUnit);


    /**
     * 获取租户的管理员列表
     * @param topUnit 租户代码
     * @return 管理员列表
     */
    JSONArray listTenantAdmin(String topUnit);
    /**
     * 租户是否为管理员或所有者
     * @param topUnit 租户
     * @return String
     */
    String userTenantRole(String userCode, String topUnit);

    /**
     * 判断用户是否为租户成员
     *
     * @param userCode 用户code
     * @param topUnit  租户id
     * @return true:是管理员
     */
    boolean userIsTenantMember(String userCode, String topUnit);

    /**
     * 判断用户是否为应用管理员
     *
     * @param userCode 用户code
     * @param osId     应用id
     * @return true:是管理员
     */
    boolean userIsApplicationAdmin(String userCode, String osId);

    /**
     * 判断用户是否为应用成员
     *
     * @param userCode 用户code
     * @param osId     应用id
     * @return true:是成员
     * @throws ObjectException 如果当前用户未登录抛出异常
     */
    boolean userIsApplicationMember(String userCode, String osId) throws ObjectException;

    /**
     * 校验用户是否为system租户成员
     * @param userCode 用户code
     * @return true：是 false：否
     */
    boolean userIsSystemMember(String userCode);

    /**
     * 校验当前用户是否为system租户管理员
     * @param userCode 用户code
     * @return true：是 false：否
     */
    boolean userIsSystemAdmin(String userCode);

    /**
     * 租户中用户数量是否达到限制
     * @param topUnit 租户
     * @return true:超出限制 false：未超出限制
     */
    boolean userNumberLimitIsOver(String topUnit);

    /**
     *  租户中单位数量是否达到限制
     * @param topUnit true:超出限制 false：未超出限制
     * @return boolean
     */
    boolean unitNumberLimitIsOver(String topUnit);
}
