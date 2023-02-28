package com.centit.sys.test;

import com.alibaba.fastjson2.JSON;
import com.centit.framework.security.model.JsonCentitUserDetails;

public class TestUserDetails {

    @org.junit.Test
    public void testUserDetailJson() throws Exception {
        String userJson="{\"accountNonExpired\":true,\"accountNonLocked\":true,\"authenticated\":true,\"credentialsNonExpired\":true,\"currentStationId\":\"U00002\",\"enabled\":true,\"name\":\"anonymoususer\",\"userInfo\":{\"createDate\":1522133499181,\"enabled\":true,\"isValid\":\"T\",\"lastModifyDate\":1522133499183,\"loginName\":\"anonymoususer\",\"userCode\":\"anonymousUser\",\"userName\":\"anonymousUser\",\"userOrder\":1000,\"userUnits\":[{\"isPrimary\":\"F\",\"unitCode\":\"U00001\",\"userCode\":\"anonymousUser\",\"userRank\":\"zz\",\"userStation\":\"nq\",\"userUnitId\":\"00001\"},{\"isPrimary\":\"T\",\"unitCode\":\"U00002\",\"userCode\":\"anonymousUser\",\"userRank\":\"zr\",\"userStation\":\"gl\",\"userUnitId\":\"00002\"}]},\"userOptList\":{},\"userRoles\":[{\"isValid\":\"T\",\"roleCode\":\"anonymous\",\"roleDesc\":\"匿名用户角色\",\"roleName\":\"匿名用户角色\",\"roleOwner\":\"U00001\",\"rolePowers\":[],\"roleType\":\"G\",\"unitCode\":\"U00001\"},{\"isValid\":\"T\",\"roleCode\":\"admin\",\"roleDesc\":\"管理员角色\",\"roleName\":\"管理员角色\",\"roleOwner\":\"U00002\",\"rolePowers\":[],\"roleType\":\"G\",\"unitCode\":\"U00002\"}],\"userSettings\":{},\"username\":\"anonymoususer\"}";

        JsonCentitUserDetails userDetails =
            JSON.parseObject(userJson, JsonCentitUserDetails.class);
        String s = JSON.toJSONString(userDetails);

        System.out.println(s);
    }
}
