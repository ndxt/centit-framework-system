package com.centit.framework.users.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.basedata.UserSyncDirectory;
import com.centit.framework.users.dto.DingUnitDTO;
import com.centit.framework.users.dto.DingUserDTO;

/**
 * @author zfg
 */
public interface DingTalkLoginService {

    ResponseData getUserByCode(String authCode);

    ResponseData getUserByUnionId(String accessToken, String unionId);

    ResponseData getUserInfo(String accessToken, String userId);

    ResponseData userCreate(String accessToken, DingUserDTO userInfo);

    ResponseData unitCreate(String accessToken, DingUnitDTO unitInfo);

    ResponseData getUnitInfo(String accessToken, String deptId);

    JSONArray getUnitList(String accessToken, String deptId);

    JSONObject getUnitUserList(String accessToken, String deptId);

    int synchroniseUserDirectory(UserSyncDirectory directory);

    ResponseData getUserByMobile(String accessToken, String mobile);

}
