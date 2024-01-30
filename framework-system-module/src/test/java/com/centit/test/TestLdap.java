package com.centit.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.system.service.impl.ActiveDirectoryUserDirectoryImpl;
import com.centit.framework.model.basedata.UnitInfo;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.model.basedata.UserSyncDirectory;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.Pretreatment;
import org.apache.commons.lang3.StringUtils;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TestLdap {

    private static int searchUserDirectory(UserSyncDirectory directory) {
        Properties env = new Properties();
        //String ldapURL = "LDAP://192.168.128.5:389";//ip:port ldap://192.168.128.5:389/CN=Users,DC=centit,DC=com


        JSONObject searchParams = JSON.parseObject(directory.getSearchBase());

        String unitSearchBase = StringBaseOpt.castObjectToString(searchParams.getString("unitSearchBase"),
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
            Map<String, UnitInfo> allUnits = new HashMap<>();

            searchCtls.setReturningAttributes(unitFieldNames);
            NamingEnumeration<SearchResult> answer = ctx.search(unitSearchBase, unitSearchFilter,searchCtls);
            while (answer.hasMoreElements()) {
                SearchResult sr = answer.next();
                Attributes attrs = sr.getAttributes();
                Map<String, String> unitMap = ActiveDirectoryUserDirectoryImpl.fetchAttributeMap( attrs, unitFields);
                if(StringUtils.isBlank(unitMap.get("unitName"))|| StringUtils.isBlank(unitMap.get("unitTag"))){
                    continue;
                }

                UnitInfo unitInfo = new UnitInfo();
//                    unitInfo.setUnitCode(unitInfoDao.getNextKey());
                unitInfo.setUnitTag(unitMap.get("unitTag"));
                unitInfo.setIsValid("T");
                unitInfo.setUnitType("A");
                unitInfo.setCreateDate(now);

                unitInfo.setUnitName(unitMap.get("unitName"));
                unitInfo.setUnitDesc(unitMap.get("unitDesc"));
                unitInfo.setLastModifyDate(now);

                allUnits.put(unitMap.get("unitTag"), unitInfo);
            }

            searchCtls.setReturningAttributes(userFieldNames);
            answer = ctx.search(userSearchBase, userSearchFilter,searchCtls);
            while (answer.hasMoreElements()) {
                SearchResult sr = answer.next();
                Attributes attrs = sr.getAttributes();

                Map<String, String> userMap = ActiveDirectoryUserDirectoryImpl.fetchAttributeMap( attrs, userFields);
                if(StringUtils.isBlank(userMap.get("userName"))|| StringUtils.isBlank(userMap.get("loginName"))){
                    continue;
                }

                UserInfo userInfo =  new UserInfo();
//                    userInfo.setUserCode(userInfoDao.getNextKey());
                userInfo.setIsValid("T");
                userInfo.setLoginName(userMap.get("loginName"));
                userInfo.setCreateDate(now);
                String regEmail = userMap.get("regEmail");


                Attribute members =  attrs.get(userUnitField);
                if(members!=null){
                    NamingEnumeration<?> ms = members.getAll();
                    while (ms.hasMoreElements()) {
                        Object member = ms.next();
                        String groupName = StringBaseOpt.objectToString(member);
                        UnitInfo u = allUnits.get(groupName);
                    }
                }

            }
            ctx.close();
            return 0;
        }catch (NamingException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static void main(String[] args) {
        UserSyncDirectory directory = new UserSyncDirectory();
        directory.setTopUnit("centit");
        directory.setUrl("LDAP://192.168.128.5:389");
        directory.setUser("accountcentit");
        directory.setUserPwd("centit.131511.cn");
        directory.setSearchBase("{ " +
            " userSearchBase : \"CN=Users,DC=centit,DC=com\", "+
            " userSearchFilter : \"(&(objectCategory=person)(objectClass=user))\", "+
            " unitSearchBase : \"CN=Users,DC=centit,DC=com\", "+
            " unitSearchFilter : \"(objectCategory=group)\", "+
            " userUnitField : \"memberOf\", "+
            " userFieldMap : { "+
            " userName : \"displayName\", "+
            " loginName : \"sAMAccountName\", "+
            " regEmail : \"mail\", "+
            " regCellPhone : \"mobilePhone\", "+
            " userDesc : \"description\" "+
            " }, "+
            " unitFieldMap : { "+
            " unitTag : \"distinguishedName\", "+
            " unitName : \"name\", "+
            " unitDesc : \"description\" "+
            " }, "+
            //" userURIFormat : \"distinguishedName=CN={name},CN=Users,DC=centit,DC=com\" "+
            " userURIFormat : \"{loginName}@centit.com\" "+
            "}");
        //Map<String, Object> userInfo = LdapLogin.searchLdapUserByloginName(directory, "codefan");
        //boolean pass = LdapLogin.checkUserPasswordByDn(directory, "codefan", "******");
        searchUserDirectory(directory);
        //System.out.println(JSON.toJSONString(pass));
    }
}
