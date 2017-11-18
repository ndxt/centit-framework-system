package com.centit.framework.system.dao;

import com.centit.framework.system.po.FVUserOptList;
import com.centit.framework.system.po.UserInfo;
import java.util.List;
import java.util.Map;

public interface UserInfoDao {

    void mergeObject(UserInfo userInfo);

    void saveNewObject(UserInfo userInfo);

    void deleteObjectById(String userCode);

    List<UserInfo> listObjects();

    List<UserInfo> listObjects(Map<String, Object> filterMap);

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

    //return this.listObjectsAll(filterMap);
    List<UserInfo> listUnderUnit(Map<String, Object> filterMap);

    // return this.listObjectsAll(filterMap, pageDesc);
    //List<UserInfo> listUnderUnit(Map<String, Object> filterMap, PageDesc pageDesc);

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

<<<<<<< HEAD
//    List<UserInfo> listUserinfoByUsercodes(List<String> userCodes);

=======

//    List<UserInfo> listUserinfoByUsercodes(List<String> userCodes);


>>>>>>> 67d46959175df85f7c02b9c76e1458ecef7a5b38
//    List<UserInfo> listUserinfoByLoginname(List<String> loginnames);

    //add by zhuxw
    void restPwd(UserInfo user);

}
