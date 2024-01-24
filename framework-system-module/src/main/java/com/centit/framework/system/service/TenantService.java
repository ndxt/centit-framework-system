package com.centit.framework.system.service;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.basedata.UnitInfo;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.model.basedata.UserUnit;
import com.centit.framework.system.po.TenantBusinessLog;
import com.centit.framework.model.basedata.TenantInfo;
import com.centit.framework.system.po.TenantMemberApply;
import com.centit.framework.system.vo.PageListTenantInfoQo;
import com.centit.framework.system.vo.TenantMemberApplyVo;
import com.centit.framework.system.vo.TenantMemberQo;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface TenantService {


    /**
     * 用户注册
     * @param userinfo 用户信息
     * @return ResponseData
     */
    ResponseData registerUserAccount(UserInfo userinfo) ;

    /**
     * 用户新建租户
     * 1.新建租户
     * 2.根据租户信息创建租户单位
     * 3.把租户所有人与机构信息绑定
     * 4.给租户所有人创建机构管理的最高权限（最高权限怎么设置）
     * @param tenantInfo 租户信息
     * @return ResponseData
     */
    ResponseData applyAddTenant(String userCode, TenantInfo tenantInfo);

    /**
     * 申请加入租户
     * 可以是用户主动申请，也可以是管理员邀请
     * @param tenantMemberApply 租户成员
     * @return ResponseData
     */
    ResponseData applyJoinTenant(TenantMemberApply tenantMemberApply);

    /**
     * 用户申请加入租户
     * @param tenantMemberApply 租户成员
     * @return ResponseData
     */
    ResponseData userApplyJoinTenant(TenantMemberApply tenantMemberApply);

    /**
     * 管理员邀请用户加入租户
     * @param tenantMemberApply 租户成员
     * @return ResponseData
     */
    ResponseData adminApplyUserJoinTenant(TenantMemberApply tenantMemberApply);


    /**
     * 列出申请信息
     * 可以是管理员邀请的信息，也可以是用户主动申请的信息
     * @param parameters 需要包含key
     * code（用户代码或机构代码）
     * codeType（代码类型，1：用户代码2：机构代码）
     * approveType（审批类型， 1待审批 2已审批）
     * @param pageDesc 分页
     * @return PageQueryResult
     */
    PageQueryResult listApplyInfo(Map<String,Object> parameters, PageDesc pageDesc);

    /**
     * 平台管理员审核租户
     * @param tenantInfo 租户信息
     * @return ResponseData
     */
    ResponseData adminCheckTenant(String userCode, TenantInfo tenantInfo);

    /**
     * 同意加入
     * 可以是平台管理员审核用户的加入
     * 也可以是普通用户同意管理员的邀请
     * @param tenantMemberApplyVo 租户成员
     * @return ResponseData
     */
    ResponseData agreeJoin(TenantMemberApplyVo tenantMemberApplyVo);

    /**
     * 更新用户当前机构信息
     * @param userInfo 用户信息
     * @return ResponseData
     */
    ResponseData updateUserCurrentUnit(UserInfo userInfo);

    /**
     * 退出租户
     * @param topUnit 租户
     * @return ResponseData
     */
    ResponseData quitTenant(String topUnit,String userCode);

    /**
     * 移除租户内的成员
     * @param topUnit 租户
     * @param userCode 用户代码
     * @return ResponseData
     */
    ResponseData removeTenantMember(String topUnit,String userCode);

    /**
     * 转让租户
     * @param tenantBusinessLog 转让日志
     * @return ResponseData
     */
    ResponseData businessTenant(String userCode, TenantBusinessLog tenantBusinessLog);


    /**
     * 平台审核租户转让
     * 租户所有者交易租户，不需要平台审核
     * @param tenantBusinessLog 转让日志
     * @return ResponseData
     */
    @Deprecated
    ResponseData adminCheckTenantBusiness(TenantBusinessLog tenantBusinessLog);

    /**
     * 分页列出租户列表
     * 1.可以是已经审核通过的
     * 2.也可以是待审核的
     * 3.也可以是审核不通过的
     * @param tenantInfo 租户信息
     * @return PageQueryResult
     */
    PageQueryResult pageListTenantApply(String userCode, PageListTenantInfoQo tenantInfo, PageDesc pageDesc);


    /**
     * 展示该机构下的人员
     * @param params 请求参数
     * @param pageDesc 分页参数
     * @return PageQueryResult
     */
    PageQueryResult pageListTenantMember(Map<String,Object> params, PageDesc pageDesc);

    /**
     * 租户所有者或平台管理员分配管理员权限
     * @param tenantMemberQo 租户成员
     * @return ResponseData
     */
    ResponseData assignTenantRole(String userCode, TenantMemberQo tenantMemberQo);


    /**
     * 删除租户成员角色
     * @param tenantMemberQo 租户成员
     * @return ResponseData
     */
    ResponseData deleteTenantRole(String userCode, TenantMemberQo tenantMemberQo);


    /**
     * 用户所在租户
     * @param userCode 用户code
     * @return JSONArray
     */
    JSONArray userTenants(String userCode);


    /**
     * 根据unitName 模糊查询租户信息
     * @param filterMap 过滤条件
     * @param pageDesc 分页
     * @return PageQueryResult
     */
    PageQueryResult pageListTenants(Map<String,Object> filterMap , PageDesc pageDesc);

    /**
     * 精确查找用户
     * @param paramMap 用户信息
     *                 只能根据userCode，userName，regCellPhone精确查找
     *                 unitCode:必要 当前用户所在租户code
     * @return ResponseData 查找userinfo结果
     */
    ResponseData findUsers(Map<String,Object> paramMap);


    List<UserInfo> searchUsers(Map<String,Object> paramMap);

    /**
     *取消加入租户申请
     * @param parameters userCode topUnit 必传
     * @return ResponseData
     */
    ResponseData cancelApply(Map<String, Object> parameters);

    /**
     * 注销租户申请
     * @param parameters topUnit必传
     * @return ResponseData
     */
    ResponseData deleteTenant(String userCode, Map<String, Object> parameters);

    /**
     * 更新租户信息，为系统管理员和租户所有者，租户管理员使用
     * @param tenantInfo 租户信息
     * @return ResponseData
     */
    ResponseData updateTenant(String userCode, TenantInfo tenantInfo);

    /**
     * 创建单位 是对 com.centit.framework.system.service.SysUnitManager#saveNewUnitInfo(com.centit.framework.model.basedata.UnitInfo)
     * 接口的再次封装
     * @param unitInfo 机构信息
     * @return ResponseData
     */
    ResponseData addTenantUnit(UnitInfo unitInfo);

    /**
     * 新增用户 是对 com.centit.framework.system.controller.UserInfoController#create(com.centit.framework.model.basedata.UserInfo, com.centit.framework.model.basedata.UserUnit, javax.servlet.http.HttpServletRequest)
     * 接口的再次封装
     * @param userInfo 用户信息
     * @param userUnit 用户单位信息
     * @return ResponseData
     */
    ResponseData addTenantUser(UserInfo userInfo, UserUnit userUnit);


}
