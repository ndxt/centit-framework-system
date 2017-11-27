package com.centit.framework.system.dao;

import com.centit.framework.system.po.FVUserOptList;
import com.centit.framework.system.po.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * 用户信息DAO
 * @author zou_wy@centit.com
 */
public interface UserInfoDao {

    /**
     * 新增用户
     * @param userInfo 用户对象信息
     */
    void saveNewObject(UserInfo userInfo);

    /**
     * 更新用户信息
     * @param userInfo 用户对象信息
     */
    void updateUser(UserInfo userInfo);

    /**
     * 根据ID删除用户
     * @param userCode 用户ID
     */
    void deleteObjectById(String userCode);

    /**
     * 查询所有用户列表
     * @return List<UserInfo>
     */
    List<UserInfo> listObjects();

    /**
     * 根据条件查询用户列表
     * @param filterMap 过滤条件Map
     * @return List<UserInfo>
     */
    List<UserInfo> listObjects(Map<String, Object> filterMap);

    /**
     * 根据角色获取 相关用户  这个需要从视图中获取，包括继承来的角色
     * @param roleCode 角色代码
     * @return 返回相关用户
     */
    List<UserInfo> listUsersByRoleCode(String roleCode);

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
    List<UserInfo> pageQuery(Map<String, Object> pageQueryMap);

    /**
     * 判断登录名是否重复
     * @param userCode 用户ID
     * @param loginName 登录名
     * @return 用户记录数
     */
    int isLoginNameExist(String userCode, String loginName);

    /**
     * 判断手机号是否重复
     * @param userCode 用户ID
     * @param regCellPhone 手机号
     * @return 用户记录数
     */
    int isCellPhoneExist(String userCode, String regCellPhone);

    /**
     * 判断email是否重复
     * @param userCode 用户ID
     * @param regEmail email
     * @return 用户记录数
     */
    int isEmailExist(String userCode, String regEmail);

  /**
   * 判断登录名、手机号或者email是否重复
   * @param userCode 用户ID
   * @param loginName 登录名
   * @param regCellPhone 手机号
   * @param regEmail email
   * @return 用户记录数
   */
    int isAnyOneExist(String userCode, String loginName,
                      String regCellPhone, String regEmail);

    /**
     * 获取下一个序列
     * @return 序列值
     */
    String getNextKey();

    /**
     * 根据用户ID 获取操作定义
     * @param userCode 用户ID
     * @return List<FVUserOptList>
     */
    List<FVUserOptList> getAllOptMethodByUser(String userCode);

    /**
     * 根据Code查询用户信息
     * @param userCode 用户Id
     * @return UserInfo
     */
    UserInfo getUserByCode(String userCode);

    /**
     * 根据登录名查询用户信息
     * @param loginName 用户登录名
     * @return UserInfo
     */
    UserInfo getUserByLoginName(String loginName);

    /**
     * 根据email查询用户信息
     * @param regEmail email
     * @return UserInfo
     */
    UserInfo getUserByRegEmail(String regEmail);

    /**
     * 根据手机号查询用户信息
     * @param regCellPhone 手机号
     * @return UserInfo
     */
    UserInfo getUserByRegCellPhone(String regCellPhone);

    /**
     * 根据userTag查询用户信息
     * @param userTag userTag
     * @return UserInfo
     */
    UserInfo getUserByTag(String userTag);

    /**
     * 根据userWord查询用户信息
     * @param userWord userWord
     * @return UserInfo
     */
    UserInfo getUserByUserWord(String userWord);

    /**
     * 根据身份证号查询用户信息
     * @param idCardNo 身份证号
     * @return UserInfo
     */
    UserInfo getUserByIdCardNo(String idCardNo);

}
