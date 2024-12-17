package com.centit.framework.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.model.basedata.*;
import com.centit.framework.model.security.CentitPasswordEncoder;
import com.centit.framework.system.dao.UnitInfoDao;
import com.centit.framework.system.dao.UserInfoDao;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.dao.UserUnitDao;
import com.centit.framework.system.service.UserDirectory;
import com.centit.support.algorithm.*;
import com.centit.support.compiler.Pretreatment;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.validation.constraints.NotNull;
import java.util.*;

@Service("activeDirectoryUserDirectory")
public class ActiveDirectoryUserDirectoryImpl implements UserDirectory{

    private static Logger logger = LoggerFactory.getLogger(ActiveDirectoryUserDirectoryImpl.class);

    @Autowired
    @NotNull
    private UserUnitDao userUnitDao;

    @Autowired
    @NotNull
    private UnitInfoDao unitInfoDao;

    @Autowired
    @NotNull
    private UserRoleDao userRoleDao;

    @Autowired
    @Qualifier("userInfoDao")
    private UserInfoDao userInfoDao;

    @Autowired
    @NotNull
    private CentitPasswordEncoder passwordEncoder;

    @Value("${framework.password.default.generator:}")
    protected String defaultPasswordFormat;


    public static String getAttributeString(Attribute attr){
        if(attr==null) {
            return null;
        }
        try {
            return StringBaseOpt.objectToString(attr.get());
        } catch (NamingException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static Map<String, String> fetchAttributeMap(Attributes attr, Map<String, Object> fieldMap){
        Map<String, String> valueMap = new HashMap<>(fieldMap.size()+1);
        for(Map.Entry<String, Object> ent : fieldMap.entrySet()){
            valueMap.put(ent.getKey(),
                getAttributeString(attr.get(StringBaseOpt.castObjectToString(ent.getValue()))));
        }
        return valueMap ;
    }

    /**
     * LDAP登录Controller

{
     userSearchBase : "CN=Users,DC=centit,DC=com",
     userSearchFilter : "(&amp;(objectCategory=person)(objectClass=user))",
     unitSearchBase : "CN=Users,DC=centit,DC=com",
     unitSearchFilter : "(objectCategory=group)",
     userUnitField : "memberOf",
     userFieldMap : {
         userName : "displayName",
         loginName : "sAMAccountName",
         regEmail : "mail",
         regCellPhone : "mobilePhone",
         userDesc : "description",
     userValid : "userAccountControl"
     },
     unitFieldMap : {
         unitTag : "distinguishedName",
         unitName : "description",
         unitDesc : "name",
     },
     userURIFormat : "{loginName}@centit.com"
}

     * @param directory LDAP用户目录信息； 需要根据配置信息自动适配
     * @return 同步的数量
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public int synchroniseUserDirectory(UserSyncDirectory directory) {
        //String ldapURL = "LDAP://192.168.128.5:389";//ip:port ldap://192.168.128.5:389/CN=Users,DC=centit,DC=com
        if(StringUtils.isBlank(directory.getUrl())){
            return -2;
        }

        JSONObject searchParams = JSON.parseObject(directory.getSearchBase());
        String unitSearchBase =StringBaseOpt.castObjectToString(searchParams.getString("unitSearchBase"),
             "CN=Users,DC=com");
        String unitSearchFilter =StringBaseOpt.castObjectToString(searchParams.getString("unitSearchFilter"),
            "(objectCategory=group)");
        String userSearchBase =StringBaseOpt.castObjectToString(searchParams.getString("userSearchBase"),
            "CN=Users,DC=com");
        String userSearchFilter =StringBaseOpt.castObjectToString(searchParams.getString("userSearchFilter"),
            "(&(objectCategory=person)(objectClass=user))");
        // {"displayName", "name", "sAMAccountName",
        //                "mail", "distinguishedName", "jobNo", "idCard", "mobilePhone", "description", "memberOf"}
        Map<String, Object> userFields = CollectionsOpt.objectToMap(searchParams.get("userFieldMap"));
        String [] userFieldNames = new String[userFields.size()];
        int i = 0;
        for(Object obj : userFields.values()){
            userFieldNames[i++] = StringBaseOpt.castObjectToString(obj);
        }

        Map<String, Object> unitFields = CollectionsOpt.objectToMap(searchParams.get("unitFieldMap"));
        String [] unitFieldNames = new String[unitFields.size()];
        i = 0;
        for(Object obj : unitFields.values()){
            unitFieldNames[i++] = StringBaseOpt.castObjectToString(obj);
        }
        String userUnitField =StringBaseOpt.castObjectToString(searchParams.getString("userUnitField"),
            "memberOf");

        String userURIFormat = searchParams.getString("userURIFormat");
        if(StringUtils.isBlank(userURIFormat)){
            userURIFormat = "{loginName}";
        }
        String userURI = Pretreatment.mapTemplateString(userURIFormat,
            CollectionsOpt.createHashMap("loginName", directory.getUser(), "topUnit", directory.getTopUnit()));

        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//"none","simple","strong"
        env.put(Context.SECURITY_PRINCIPAL, userURI);
        env.put(Context.SECURITY_CREDENTIALS, directory.getClearPassword());
        env.put(Context.PROVIDER_URL, directory.getUrl());
        Date now = DatetimeOpt.currentUtilDate();
        try {
            LdapContext ctx = new InitialLdapContext(env, null);
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            Map<String,UnitInfo> allUnits = new HashMap<>();

            searchCtls.setReturningAttributes(unitFieldNames);
            NamingEnumeration<SearchResult> answer = ctx.search(unitSearchBase, unitSearchFilter, searchCtls);
            while (answer.hasMoreElements()) {
                SearchResult sr = answer.next();
                Attributes attrs = sr.getAttributes();
                Map<String, String> unitMap = fetchAttributeMap( attrs, unitFields);
                if(StringUtils.isBlank(unitMap.get("unitName"))|| StringUtils.isBlank(unitMap.get("unitTag"))){
                    continue;
                }

                UnitInfo unitInfo = unitInfoDao.getUnitByTag(unitMap.get("unitTag"));
                if(unitInfo==null){
                    unitInfo = new UnitInfo();

                    unitInfo.setIsValid("T");
                    unitInfo.setUnitType("A");

                    for(Map.Entry<String, String> ent : unitMap.entrySet()){
                        ReflectionOpt.setFieldValue(unitInfo, ent.getKey(), ent.getValue(), String.class);
                    }

                    unitInfo.setTopUnit(directory.getTopUnit());
                    //saveNewObject 会自动生成 unitCode
                    unitInfoDao.saveNewObject(unitInfo);
                    unitInfo.setUnitPath("/"+unitInfo.getUnitCode());
                    unitInfoDao.updateUnit(unitInfo);
                } else { //机构名称变换才更新，减少不必要的更新操作
                    if(StringUtils.isNotBlank(unitMap.get("unitName")) &&
                         !StringUtils.equals(unitInfo.getUnitName(), unitMap.get("unitName"))) {
                        for(Map.Entry<String, String> ent : unitMap.entrySet()){
                            ReflectionOpt.setFieldValue(unitInfo, ent.getKey(), ent.getValue(), String.class);
                        }
                        unitInfoDao.updateUnit(unitInfo);
                    }
                }
                allUnits.put(unitMap.get("unitTag"), unitInfo);
            }

            searchCtls.setReturningAttributes(userFieldNames);
            answer = ctx.search(userSearchBase, userSearchFilter, searchCtls);
            while (answer.hasMoreElements()) {
                SearchResult sr = answer.next();
                Attributes attrs = sr.getAttributes();

                Map<String, String> userMap = fetchAttributeMap(attrs, userFields);
                if (StringUtils.isBlank(userMap.get("userName")) || StringUtils.isBlank(userMap.get("loginName"))) {
                    continue;
                }

                boolean createUser = false;
                UserInfo userInfo = userInfoDao.getUserByLoginName(userMap.get("loginName"));
                if (userInfo == null) {
                    userInfo = new UserInfo();
//                    userInfo.setUserCode(userInfoDao.getNextKey());
                    boolean isDisable = StringUtils.equalsAny(userMap.get("userValid"),
                        "F", "false", "514", "546", "66050", "66080", "66082");
                    userInfo.setIsValid(isDisable ? "F" : "T");
                    userInfo.setLoginName(userMap.get("loginName"));
                    userInfo.setCreateDate(now);
                    userInfo.setUserPin(getDefaultPassword());
                    createUser = true;
                }
                for (Map.Entry<String, String> ent : userMap.entrySet()) {
                    String fieldKey = ent.getKey();
                    if ("loginName".equals(fieldKey)) continue;
                    if ("regEmail".equals(fieldKey)) {
                        String regEmail = ent.getValue();
                        if (StringUtils.isNotBlank(regEmail) && regEmail.length() < 60 &&
                            !regEmail.equals(userInfo.getRegEmail()) &&
                            userInfoDao.getUserByRegEmail(regEmail) == null) {
                            userInfo.setRegEmail(regEmail);
                        }
                        continue;
                    }
                    if ("regCellPhone".equals(fieldKey)) {
                        String regCellPhone = ent.getValue();
                        if (StringUtils.isNotBlank(regCellPhone) && regCellPhone.length() <= 15 &&
                            !regCellPhone.equals(userInfo.getRegCellPhone()) &&
                            userInfoDao.getUserByRegCellPhone(regCellPhone) == null) {
                            userInfo.setRegCellPhone(regCellPhone);
                        }
                        continue;
                    }
                    ReflectionOpt.setFieldValue(userInfo, fieldKey, ent.getValue(), String.class);
                }

                userInfo.setUpdateDate(now);
                if (createUser) {
                    userInfoDao.saveNewObject(userInfo);
                } else {
                    userInfoDao.updateUser(userInfo);
                }
                // 如果已经是禁用的用户，不再同步用户的其他信息
                if ("T".equals(userInfo.getIsValid())) { // 新建用户 设置角色
                    if (createUser && StringUtils.isNotBlank(directory.getDefaultUserRole())) {
                        UserRole role = new UserRole(
                            new UserRoleId(userInfo.getUserCode(), directory.getDefaultUserRole()));
                        role.setObtainDate(now);
                        role.setCreateDate(now);
                        role.setChangeDesc("LDAP同步时默认设置。");
                        userRoleDao.mergeUserRole(role);
                    }
                    // 同步机构
                    Attribute members = attrs.get(userUnitField);
                    if (members != null) {
                        NamingEnumeration<?> ms = members.getAll();
                        while (ms.hasMoreElements()) {
                            Object member = ms.next();
                            String groupName = StringBaseOpt.objectToString(member);
                            UnitInfo u = allUnits.get(groupName);

                            if (u != null && "T".equals(u.getIsValid())) {
                                if ((StringUtils.isNotBlank(u.getUnitCode())) && (StringUtils.isBlank(userInfo.getPrimaryUnit()))) {
                                    userInfo.setPrimaryUnit(u.getUnitCode());
                                    userInfoDao.updateUser(userInfo);
                                    UnitInfo unitInfo = unitInfoDao.getObjectById(userInfo.getPrimaryUnit());
                                    if (null != unitInfo && StringUtils.isNotBlank(unitInfo.getTopUnit())) {
                                        userInfo.setTopUnit(unitInfo.getTopUnit());
                                    }
                                    if (null != unitInfo && StringUtils.isBlank(userInfo.getTopUnit()) && StringUtils.isNotBlank(unitInfo.getUnitPath())) {
                                        String[] unitCodeArray = unitInfo.getUnitPath().split("/");
                                        if (ArrayUtils.isNotEmpty(unitCodeArray) && unitCodeArray.length > 1) {
                                            userInfo.setTopUnit(unitCodeArray[1]);
                                        }
                                    }
                                    userInfoDao.updateUser(userInfo);
                                }
                                List<UserUnit> uus = userUnitDao.listObjectByUserUnit(
                                    userInfo.getUserCode(), u.getUnitCode());
                                if (CollectionUtils.isEmpty(uus)) {
                                    UserUnit uu = new UserUnit();
                                    uu.setUserUnitId(UuidOpt.getUuidAsString());
                                    uu.setUnitCode(u.getUnitCode());
                                    uu.setUserCode(userInfo.getUserCode());
                                    uu.setCreateDate(now);
                                    if (u.getUnitCode().equals(userInfo.getPrimaryUnit())) {
                                        uu.setRelType("T");
                                    } else {
                                        uu.setRelType("F");
                                    }
                                    uu.setUserRank(directory.getDefaultRank());
                                    uu.setUserStation(directory.getDefaultStation());
                                    userUnitDao.saveNewObject(uu);
                                }
                            }
                        }
                    }
                }
            }

            ctx.close();
            return 0;
        }catch (NamingException e) {
            logger.error(e.getMessage(),e);
            return -1;
        }
    }

    private String getDefaultPassword() {
        String rawPass = UuidOpt.randomString(12);
        return passwordEncoder.createPassword(rawPass, "salt");
    }
}
