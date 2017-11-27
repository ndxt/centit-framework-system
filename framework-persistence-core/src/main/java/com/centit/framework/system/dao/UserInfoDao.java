package com.centit.framework.system.dao;

import com.centit.framework.system.po.FVUserOptList;
import com.centit.framework.system.po.UserInfo;
import java.util.List;
import java.util.Map;

public interface UserInfoDao {

    void mergeObject(UserInfo userInfo);

    /**
     * 更新用户信息
     * @param userInfo 用户对象信息
     */
    void updateUser(UserInfo userInfo);

    /**
     * 新增用户
     * @param userInfo 用户对象信息
     */
    void saveNewObject(UserInfo userInfo);

    /**
     * 根据ID删除用户
     * @param userCode 用户ID
     */
    void deleteObjectById(String userCode);

    List<UserInfo> listObjects();

    List<UserInfo> listObjects(Map<String, Object> filterMap);

  /**
   * 根据角色获取 相关用户  这个需要从视图中获取，包括继承来的角色
   * @param roleCode 角色代码
   * @return 返回相关用户
   */
    List<UserInfo> listUsersByRoleCode(String roleCode);

    int  pageCount(Map<String, Object> filterDescMap);

    List<UserInfo>  pageQuery(Map<String, Object> pageQureyMap);

    UserInfo getObjectById(String userCode);

    /*
     * 这个方法迁移到 ManagerImpl类中
     * hql = "SELECT COUNT(*) FROM UserInfo WHERE userCode = " + QueryUtils.buildStringForQuery(user.getUserCode());
     * hql = "SELECT COUNT(*) FROM UserInfo WHERE loginName = " + QueryUtils.buildStringForQuery(user.getLoginName());
     *             " AND userCode  " + QueryUtils.buildStringForQuery(user.getUserCode());
     *  放到impl中去了
     * @return int
     */
    //boolean checkIfUserExists(UserInfo user);

     int isLoginNameExist(String userCode, String loginName);
     int isCellPhoneExist(String userCode, String regCellPhone);
     int isEmailExist(String userCode, String regEmail);
     int isAnyOneExist(String userCode, String loginName,
                       String regCellPhone, String regEmail);

    //"U"+ DatabaseOptUtils.getNextKeyBySequence(this, "S_USERCODE", 7);
    String getNextKey();

  //hql = "FROM FVUserOptList urv where urv.id.userCode=?";
    List<FVUserOptList> getAllOptMethodByUser(String userCode);

    //return this.listObjectsAll(filterMap);
    List<UserInfo> listUnderUnit(Map<String, Object> filterMap);


    //return getObjectById(userCode);
    UserInfo getUserByCode(String userCode);


    // return super.getObjectByProperty("loginName",loginName.toLowerCase());
    UserInfo getUserByLoginName(String loginName);

    // return super.getObjectByProperty("regEmail", regEmail);
    UserInfo getUserByRegEmail(String regEmail);

    //return super.getObjectByProperty("regCellPhone", regCellPhone);
    UserInfo getUserByRegCellPhone(String regCellPhone);

    //return super.getObjectByProperty("userTag", userTag);
    UserInfo getUserByTag(String userTag);

    //return super.getObjectByProperty("userWord", userWord);
    UserInfo getUserByUserWord(String userWord);
    UserInfo getUserByIdCardNo(String idCardNo);

    //add by zhuxw
    void restPwd(UserInfo user);

}
