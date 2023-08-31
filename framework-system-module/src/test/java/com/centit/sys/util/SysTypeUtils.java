package com.centit.sys.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import com.centit.framework.model.basedata.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysTypeUtils {

    public static final int sysType  = 1;

    public static void main(String[] args) {
        //JSONObject jsonObject = new JSONObject();


        List<UserInfo> userInfos = new ArrayList<UserInfo>();

        for (int i = 0; i < 10; i++) {
            UserInfo u = new UserInfo();

            u.setUserCode("usercode_" + i);
            u.setUserName("username_" + i);

            userInfos.add(u);
        }


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("errorcode", 1);
        params.put("userinfos", userInfos);

        SimplePropertyPreFilter p = new SimplePropertyPreFilter(UserInfo.class);
        p.getExcludes().add("loginName");


        String text = JSON.toJSONString(params, p);

        System.out.println(text);

    }
}

