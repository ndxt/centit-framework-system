package com.centit.framework.system.dao.impl;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.dao.CodeBook;
import com.centit.support.database.utils.PageDesc;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.UserInfoDao;
import com.centit.framework.system.po.FVUserOptList;
import com.centit.framework.system.po.UserInfo;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userInfoDao")
public class UserInfoDaoImpl extends BaseDaoImpl<UserInfo, String> implements UserInfoDao {

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put(CodeRepositoryUtil.USER_CODE, CodeBook.LIKE_HQL_ID);
            filterField.put("USERCODE_EQ", CodeBook.EQUAL_HQL_ID);
            filterField.put("USERNAME", CodeBook.LIKE_HQL_ID);
            filterField.put("ISVALID", CodeBook.EQUAL_HQL_ID);
            filterField.put("LOGINNAME", CodeBook.LIKE_HQL_ID);
            filterField.put("USERSTATE", CodeBook.EQUAL_HQL_ID);
            filterField.put("USERORDER", CodeBook.EQUAL_HQL_ID);
            filterField.put("USERTAG", CodeBook.EQUAL_HQL_ID);
            filterField.put("USERWORD", CodeBook.EQUAL_HQL_ID);

            filterField.put("byUnderUnit", "userCode in " +
                    "(select  us.USER_CODE from f_userunit us where us.UNIT_CODE = :byUnderUnit ) ");

            filterField.put("queryByUnit", "userCode in " +
                    "(select  us.USER_CODE from f_userunit us where us.UNIT_CODE = :queryByUnit ) ");
            filterField.put("queryByGW", "userCode in " +
                    "(select  us.USER_CODE from f_userunit us where us.User_Station = :queryByGW )");
            filterField.put("queryByXZ", "userCode in " +
                    "(select  us.USER_CODE from f_userunit us where us.USER_RANK = :queryByXZ )");
            filterField.put("queryByRole", "userCode in " +
                    "(select r.USER_CODE from f_userrole r join f_roleinfo i on r.ROLE_CODE = i.ROLE_CODE " +
                    "where r.ROLE_CODE = :queryByRole and i.IS_VALID = 'T')");

            filterField.put(CodeBook.ORDER_BY_HQL_ID, "userOrder asc");

            filterField.put("unitCode", "userCode in (select us.USER_CODE from f_userunit us where us.UNIT_CODE in " +
                    "(select un.UNIT_CODE from f_unitinfo un where un.UNIT_CODE = :unitCode or un.PARENT_UNIT = :unitCode))");
        }
        return filterField;
    }

    @Transactional
    public String getNextKey() {
        return "U"+ StringBaseOpt.fillZeroForString(
                String.valueOf(DatabaseOptUtils.getSequenceNextValue(this, "S_USERCODE")), 7);
    }

    @Override
    @Transactional
    public void saveObject(UserInfo o) {
        if (!StringUtils.isNotBlank(o.getUserCode())) {
            // o.setUsercode("u" + this.getNextKey());
            o.setUserCode(this.getNextKey());
        }

        // 无密码，初始化密码
        if (!StringUtils.isNotBlank(o.getUserPin())) {
            o.setUserPin(new Md5PasswordEncoder().encodePassword("000000", o.getUserCode()));
        }

        super.updateObject(o);
    }
    
    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserOptList> getAllOptMethodByUser(String userCode) {
        String sql = "select USER_CODE,OPT_CODE,OPT_NAME,OPT_ID,OPT_METHOD " +
                "from F_V_USEROPTLIST " +
                "where USER_CODE=?";

        return getJdbcTemplate().execute(
                (ConnectionCallback<List<FVUserOptList>>) conn ->
                        OrmDaoUtils.queryObjectsByParamsSql(conn, sql ,
                                new Object[]{userCode}, FVUserOptList.class));
    }

    @Transactional
    public List<UserInfo> listUnderUnit(Map<String, Object> filterMap) {
        return this.listObjects(filterMap);
    }

    @Transactional
    public List<UserInfo> listUnderUnit(Map<String, Object> filterMap, PageDesc pageDesc) {
        return this.listObjectsByProperties(filterMap, pageDesc);
    }

    @Transactional
    public UserInfo getUserByCode(String userCode) {
        return getObjectById(userCode);
    }
    
    @Transactional
    public UserInfo getUserByLoginName(String loginName) {
        return super.getObjectByProperties(QueryUtils.createSqlParamsMap(
                "loginName",loginName.toLowerCase()));
    }
    
    @Transactional
    public UserInfo getUserByRegEmail(String regEmail) {
        return super.getObjectByProperties(QueryUtils.createSqlParamsMap("regEmail", regEmail));
    }
    
    @Transactional
    public UserInfo getUserByRegCellPhone(String regCellPhone) {
        return super.getObjectByProperties(QueryUtils.createSqlParamsMap("regCellPhone", regCellPhone));
    }
    
    @Transactional
    public UserInfo getUserByTag(String userTag) {
        return super.getObjectByProperties(QueryUtils.createSqlParamsMap("userTag", userTag));
    }
    
    @Transactional
    public UserInfo getUserByUserWord(String userWord) {
        return super.getObjectByProperties(QueryUtils.createSqlParamsMap("userWord", userWord));
    }

    @Transactional
    public UserInfo getUserByIdCardNo(String idCardNo){
        return super.getObjectByProperties(QueryUtils.createSqlParamsMap("idCardNo", idCardNo));
    }
    
    public void restPwd(UserInfo user){
        saveObject(user);
    }

    @Override
    public void deleteObjectById(String userCode) {
        super.deleteObjectById(userCode);
    }

    @Override
    public UserInfo getObjectById(String userCode) {
        return super.getObjectById(userCode);
    }

    public int isLoginNameExist(String userCode, String loginName){
        String sql = "select count(*) as usersCount from F_USERINFO t " +
                "where t.USERCODE <> ? and t.LOGINNAME = ?";
        try {
            return DatabaseOptUtils.doExecuteSql(this, sql,
                    new Object[]{userCode, loginName});
        }catch(SQLException e){
            logger.error(e.getMessage(), e);
        }
        return -1;
    }
    public int isCellPhoneExist(String userCode, String loginName){
        String sql = "select count(*) as usersCount from F_USERINFO t " +
                "where t.USERCODE <> ? and t.REGCELLPHONE = ?";
        try {
            return DatabaseOptUtils.doExecuteSql(this, sql,
                    new Object[]{userCode, loginName});
        }catch(SQLException e){
            logger.error(e.getMessage(), e);
        }
        return -1;
    }
    public int isEmailExist(String userCode, String loginName){
        String sql = "select count(*) as usersCount from F_USERINFO t " +
                "where t.USERCODE <> ? and t.REGEMAIL = ?";
        try {
            return DatabaseOptUtils.doExecuteSql(this, sql,
                    new Object[]{userCode, loginName});
        }catch(SQLException e){
            logger.error(e.getMessage(), e);
        }
        return -1;
    }

    public int isAnyOneExist(String userCode, String loginName,String regPhone,String regEmail) {
        String sql = "select count(*) as usersCount from F_USERINFO t " +
                "where t.USER_CODE != ? and " +
                "(t.LOGIN_NAME = ? or t.REG_CELL_PHONE= ? or t.Reg_Email = ?)";
        try {
            return DatabaseOptUtils.doExecuteSql(this, sql,
                    new Object[]{userCode, loginName, regPhone, regEmail});
        }catch(SQLException e){
            logger.error(e.getMessage(), e);
        }
        return -1;
    }
}
