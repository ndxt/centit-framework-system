package com.centit.framework.users.test;

import com.alibaba.fastjson2.JSON;
import com.centit.framework.model.basedata.UserSyncDirectory;
import com.centit.framework.users.controller.LdapLogin;

public class TestLdapSearch {

    public static void main(String[] args) {
        UserSyncDirectory directory = new UserSyncDirectory();
        directory.setTopUnit("centit");
        directory.setUrl("LDAP://192.168.128.5:389");
        directory.setUser("accountcentit@centit.com");
        directory.setUserPwd("*********");
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
        boolean pass = LdapLogin.checkUserPasswordByDn(directory, "codefan", "******");
        System.out.println(JSON.toJSONString(pass));
    }
}

