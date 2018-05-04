package com.centit.framework.system.dao.hibernateimpl;

import com.centit.framework.common.ObjectException;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.UserInfoDao;
import com.centit.framework.system.po.FVUserOptList;
import com.centit.framework.system.po.UserInfo;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userInfoDao")
public class UserInfoDaoImpl extends BaseDaoImpl<UserInfo, String> implements UserInfoDao {
    @Transactional
    public int checkIfUserExists(String userCode, String loginName) {
        long hasExist = 0L;
        String hql;

        if (StringUtils.isNotBlank(userCode)) {
            hql = "SELECT COUNT(*) FROM UserInfo WHERE userCode = " + QueryUtils.buildStringForQuery(userCode);
            hasExist = DatabaseOptUtils.getSingleIntByHql(this, hql);
        }

        hql = "SELECT COUNT(*) FROM UserInfo WHERE loginName = " + QueryUtils.buildStringForQuery(loginName);

        if (StringUtils.isNotBlank(userCode)) {
            hql += " AND userCode <> " + QueryUtils.buildStringForQuery(userCode);
        }
        long size = DatabaseOptUtils.getSingleIntByHql(this, hql);
        if (size >= 1) {
            throw new ObjectException("登录名：" + loginName + " 已存在!!!");
        }

        return (int)hasExist;
    }

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
                    "(select  id.userCode from UserUnit where id.unitCode = :byUnderUnit) ");

            filterField.put("queryByUnit", "userCode in " +
                    "(select  id.userCode from UserUnit where id.unitCode = :queryByUnit) ");
            filterField.put("roleCode", "userCode in " +
                  "(select  v.id.userCode from FVUserRoles v where v.id.roleCode = :roleCode) ");

            filterField.put("queryByGW", "userCode in " +
                    "(select  id.userCode from UserUnit where id.userStation = :queryByGW)");
            filterField.put("queryByXZ", "userCode in " +
                    "(select  id.userCode from UserUnit where id.userRank = :queryByXZ)");
            filterField.put("queryByRole", "userCode in " +
                    "(select r.id.userCode from UserRole r, RoleInfo i " +
                    "where r.id.roleCode = :queryByRole and r.id.roleCode = i.roleCode and i.isValid = 'T')");

            filterField.put(CodeBook.ORDER_BY_HQL_ID, "userOrder asc");

            filterField.put("unitCode", "userCode in (select userCode from UserUnit where unitCode in " +
                    "(select unitCode from UnitInfo where unitCode = :unitCode or parentUnit = :unitCode))");
        }
        return filterField;
    }

    @Transactional
    public String getNextKey() {
        return DatabaseOptUtils.getNextValueOfSequence(this, "S_USERCODE");
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserOptList> getAllOptMethodByUser(String userCode) {
        String[] params = null;
        String hql = "FROM FVUserOptList urv where urv.id.userCode=?";

        params = new String[]{userCode};
        List<FVUserOptList> ls = (List<FVUserOptList>) DatabaseOptUtils.findObjectsByHql
                (this,hql, (Object[]) params);
        return ls;
    }
    /*
     * public FUserinfo loginUser(String userName, String password) { return
     * (FUserinfo) getHibernateTemplate().find(
     * "FROM FUserinfo WHERE username = ? AND userpin = ? ", new Object[] {
     * userName, password }).get(0); }
     */

    @Transactional
    public List<UserInfo> listUnderUnit(Map<String, Object> filterMap) {
        return this.listObjects(filterMap);
       /* String shql = "from f_userinfo where 1=1 ";

        HqlAndNamedParams hql = builderHqlAndNamedParams(shql, filterMap);
        String hql1 = "select *  " + hql.getHql();
        System.out.println(1);
        List<UserInfo> l = null;
        try {
            l = (List<UserInfo>)getHibernateTemplate().executeFind(
                    new SQLWithNamedParamsCallBack(hql1, hql.getParams(), UserInfo.class));
        } catch (Exception e) {
            log.error(e.getMessage());
            // return null;
        }

        return l;*/
    }

    @Transactional
    public List<UserInfo> listUnderUnit(Map<String, Object> filterMap, PageDesc pageDesc) {
        return this.listObjects(filterMap, pageDesc);
    }

    @Transactional
    public UserInfo getUserByCode(String userCode) {
        return getObjectById(userCode);
    }

    @Transactional
    public UserInfo getUserByLoginName(String loginName) {
        return super.getObjectByProperty("loginName",loginName);
    }

    @Transactional
    public UserInfo getUserByRegEmail(String regEmail) {
        return super.getObjectByProperty("regEmail", regEmail);
    }

    @Transactional
    public UserInfo getUserByRegCellPhone(String regCellPhone) {
        return super.getObjectByProperty("regCellPhone", regCellPhone);
    }

    @Transactional
    public UserInfo getUserByTag(String userTag) {
        return super.getObjectByProperty("userTag", userTag);
    }

    @Transactional
    public UserInfo getUserByUserWord(String userWord) {
        return super.getObjectByProperty("userWord", userWord);
    }

    @Transactional
    public UserInfo getUserByIdCardNo(String idCardNo){
        return super.getObjectByProperty("idCardNo", idCardNo);
    }

    /**
     * 批量添加或更新
     *
     * @param userinfos List
     */
    @Transactional
    public void batchSave(List<UserInfo> userinfos) {
        for (int i = 0; i < userinfos.size(); i++) {
            this.saveObject(userinfos.get(i));

            if (19 == i % 20) {
                DatabaseOptUtils.flush(this.getCurrentSession());
            }
        }
    }
    @Transactional
    public void batchMerge(List<UserInfo> userinfos) {
        for (int i = 0; i < userinfos.size(); i++) {
            mergeObject(userinfos.get(i));

            if (19 == i % 20) {
                DatabaseOptUtils.flush(this.getCurrentSession());
            }
        }
    }

  @Override
  public List<UserInfo> listUsersByRoleCode(String roleCode) {
    return  this.listObjects(QueryUtils.createSqlParamsMap("roleCode",roleCode));
  }

  public int isLoginNameExist(String userCode, String loginName){
        String sql = "select count(*) as usersCount from F_USERINFO t " +
                "where t.USERCODE <> ? and t.LOGINNAME = ?";
        Object obj  = DatabaseOptUtils.getSingleObjectBySql(this, sql,
                new Object[]{userCode, loginName} );
        Integer uc = NumberBaseOpt.castObjectToInteger(obj);
        return uc;
    }
    public int isCellPhoneExist(String userCode, String loginName){
        String sql = "select count(*) as usersCount from F_USERINFO t " +
                "where t.USERCODE <> ? and t.REGCELLPHONE = ?";
        Object obj  = DatabaseOptUtils.getSingleObjectBySql(this, sql,
                new Object[]{userCode, loginName} );
        Integer uc = NumberBaseOpt.castObjectToInteger(obj);
        return uc;
    }
    public int isEmailExist(String userCode, String loginName){
        String sql = "select count(*) as usersCount from F_USERINFO t " +
                "where t.USERCODE <> ? and t.REGEMAIL = ?";
        Object obj  = DatabaseOptUtils.getSingleObjectBySql(this, sql,
                new Object[]{userCode, loginName} );
        Integer uc = NumberBaseOpt.castObjectToInteger(obj);
        return uc;
    }

    public int isAnyOneExist(String userCode, String loginName,String regPhone,String regEmail) {
        String sql = "select count(*) as usersCount from F_USERINFO t " +
                "where t.USERCODE != ? and " +
                "(t.LOGINNAME = ? or t.REGCELLPHONE= ? or t.REGEMAIL = ?)";
        Object obj = DatabaseOptUtils.getSingleObjectBySql(this, sql,
                new Object[]{StringUtils.isBlank(userCode) ? "null" : userCode,
                        StringUtils.isBlank(loginName) ? "null" : loginName,
                        StringUtils.isBlank(regPhone) ? "null" : regPhone,
                        StringUtils.isBlank(regEmail) ? "null" : regEmail});
        return NumberBaseOpt.castObjectToInteger(obj);
    }

    @Override
    public void updateUser(UserInfo userInfo){
      super.updateObject(userInfo);
    }
}
