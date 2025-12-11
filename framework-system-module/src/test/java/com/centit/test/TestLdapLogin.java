package com.centit.test;

import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.model.basedata.UserSyncDirectory;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.json.JSONOpt;
import com.centit.support.security.SecurityOptUtils;
import org.apache.commons.lang3.StringUtils;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TestLdapLogin {
    public static String getAttributeString(Attribute attr){
        if(attr==null) {
            return null;
        }
        try {
            return StringBaseOpt.objectToString(attr.get());
        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static Map<String, String> fetchAttributeMap(Attributes attr, Map<String, Object> fieldMap){
        Map<String, String> valueMap = new HashMap<>(fieldMap.size()+1);
        for(Map.Entry<String, Object> ent : fieldMap.entrySet()){
            valueMap.put(ent.getKey(),
                getAttributeString(attr.get(StringBaseOpt.castObjectToString(ent.getValue()))));
        }
        return valueMap ;
    }
    private static Map<String, String> checkUserPasswordByDn(UserSyncDirectory directory, String loginName, String password) {
        JSONObject searchParams = JSONObject.parseObject(directory.getSearchBase());
        String userURIFormat = searchParams.getString("userURIFormat");
        if(StringUtils.isBlank(userURIFormat)){
            userURIFormat = "{loginName}";
        }
        String userURI = Pretreatment.mapTemplateString(userURIFormat,
            CollectionsOpt.createHashMap("loginName", loginName, "topUnit", directory.getTopUnit()));

        final Properties env = getProperties(directory.getUrl(), password, userURI);
        LdapContext ctx = null;
        try {
            ctx = new InitialLdapContext(env, null);
            Attributes attr = ctx.getAttributes(userURI);
            if (attr != null) {
                Map<String, Object> userFields = CollectionsOpt.objectToMap(searchParams.get("userFieldMap"));
                Map<String, String> userFieldMap = fetchAttributeMap(attr, userFields);
                String userUnitField = StringBaseOpt.castObjectToString(searchParams.getString("userUnitField"),
                    "memberOf");
                Attribute members = attr.get(userUnitField);
                StringBuilder memberOf = new StringBuilder();
                if (members != null) {
                    NamingEnumeration<?> ms = members.getAll();
                    while (ms.hasMoreElements()) {
                        Object member = ms.next();
                        String groupName = StringBaseOpt.objectToString(member);
                        memberOf.append(groupName).append(";");
                    }
                }
                userFieldMap.put("memberOf", memberOf.toString());
                return userFieldMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static Properties getProperties(String directoryUrl, String password, String userURI) {
        Properties env = new Properties();
        //String ldapURL = "LDAP://192.168.128.5:389";//ip:port ldap://192.168.128.5:389/CN=Users,DC=centit,DC=com
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//"none","simple","strong"
        env.put(Context.SECURITY_PRINCIPAL, userURI);
        env.put(Context.SECURITY_CREDENTIALS, password);
        //"LDAP://192.168.128.5:389"
        env.put(Context.PROVIDER_URL, directoryUrl);
        return env;
    }

    public static void main(String[] args) {
        JSONOpt.fastjsonGlobalConfig();
        UserSyncDirectory directory = new UserSyncDirectory();
        directory.setTopUnit("centit");
        directory.setUrl("LDAP://192.168.128.5:389");
        directory.setUser("accountcentit");
        directory.setUserPwd(SecurityOptUtils.decodeSecurityString("cipher:py0HcYKZIkjd8AergvlE65oD4q27dMoqgOiXjj6M7Yw="));
        directory.setSearchBase("{ " +
            " userSearchBase : \"CN=Users,DC=centit,DC=com\", "+
            " userSearchFilter : \"(&(objectCategory=person)(objectClass=user))\", "+
            " unitSearchBase : \"CN=Users,DC=centit,DC=com\", "+
            " unitSearchFilter : \"(objectCategory=group)\", "+
            " userUnitField : \"memberOf\", "+
            " userFieldMap : { "+
            " userName : \"displayName\", "+
            " loginName : \"sAMAccountName\", "+
            " userTag : \"distinguishedName\", "+
            " regEmail : \"mail\", "+
            " regCellPhone : \"mobilePhone\", "+
            " userDesc : \"description\", " +
            " userValid : \"userAccountControl\" "+
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
        checkUserPasswordByDn(directory, "codefan", "abc$A123");
        //System.out.println(JSON.toJSONString(pass));
    }
}
