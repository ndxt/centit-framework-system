package com.centit.framework.system.dao;

import com.centit.framework.hibernate.dao.BaseDao;
import com.centit.framework.system.po.FVUserOptList;
import com.centit.framework.system.po.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserInfoDao extends BaseDao<UserInfo, String> {

    /**
     * 这个方法迁移到 ManagerImpl类中
     * hql = "SELECT COUNT(*) FROM UserInfo WHERE userCode = " + QueryUtils.buildStringForQuery(user.getUserCode());
     * hql = "SELECT COUNT(*) FROM UserInfo WHERE loginName = " + QueryUtils.buildStringForQuery(user.getLoginName());
     * 			" AND userCode  " + QueryUtils.buildStringForQuery(user.getUserCode());
     * 
     *  放到impl中去了
     * @return int
     */
    //boolean checkIfUserExists(UserInfo user);
    
	 int isLoginNameExist(String userCode, String loginName);
	 int isCellPhoneExist(String userCode, String regCellPhone);
	 int isEmailExist(String userCode, String regEmail);
	 int isAnyOneExist(String userCode,  String loginName,
                       String regCellPhone, String regEmail);

    //"U"+ DatabaseOptUtils.getNextKeyBySequence(this, "S_USERCODE", 7);
    String getNextKey();

    //hql = "FROM FVUserOptList urv where urv.id.userCode=?";
    List<FVUserOptList> getAllOptMethodByUser(String userCode);
	/*
     * FUserinfo loginUser(String userName, String password) { return
	 * (FUserinfo) getHibernateTemplate().find(
	 * "FROM FUserinfo WHERE username = ? AND userpin = ? ", new Object[] {
	 * userName, password }).get(0); }
	 */
   
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
    UserInfo getUserByWord(String userWord);
    
  
//    List<UserInfo> listUserinfoByUsercodes(List<String> userCodes);

  
//    List<UserInfo> listUserinfoByLoginname(List<String> loginnames);
    
    //add by zhuxw
    void restPwd(UserInfo user);
    
}
