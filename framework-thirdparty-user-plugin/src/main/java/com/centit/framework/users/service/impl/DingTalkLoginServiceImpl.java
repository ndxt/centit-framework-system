package com.centit.framework.users.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.basedata.*;
import com.centit.framework.system.dao.UnitInfoDao;
import com.centit.framework.system.dao.UserInfoDao;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.dao.UserUnitDao;
import com.centit.framework.users.config.AppConfig;
import com.centit.framework.users.config.UrlConstant;
import com.centit.framework.users.dao.SocialDeptAuthDao;
import com.centit.framework.users.dto.DingUnitDTO;
import com.centit.framework.users.dto.DingUserDTO;
import com.centit.framework.users.po.SocialDeptAuth;
import com.centit.framework.users.service.DingTalkLoginService;
import com.centit.framework.users.service.TokenService;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.UuidOpt;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zfg
 */
@Service("dingTalkLoginService")
public class DingTalkLoginServiceImpl implements DingTalkLoginService {

    private static final Logger logger = LoggerFactory.getLogger(DingTalkLoginServiceImpl.class);

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private SocialDeptAuthDao socialDeptAuthDao;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UnitInfoDao unitInfoDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserUnitDao userUnitDao;

    @Autowired
    private UserRoleDao userRoleDao;

    /**
     * 访问/sns/getuserinfo_bycode接口获取用户unionid
     *
     * @param authCode 临时授权码
     * @return 用户unionId或错误信息
     */
    @Override
    public ResponseData getUserByCode(String authCode) {
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_GET_USER_BYCODE);
        OapiSnsGetuserinfoBycodeRequest request = new OapiSnsGetuserinfoBycodeRequest();
        request.setTmpAuthCode(authCode);
        OapiSnsGetuserinfoBycodeResponse response;
        try {
            response = client.execute(request, appConfig.getAppKey(), appConfig.getAppSecret());
        } catch (ApiException e) {
            logger.error("Failed to {}", UrlConstant.URL_GET_USER_BYCODE, e);
            return ResponseData.makeErrorMessage("Failed to getUserByCode: " + e.getErrMsg());
        }
        if (!response.isSuccess()) {
            return ResponseData.makeErrorMessage(Integer.valueOf(response.getErrorCode()), response.getErrmsg());
        }
        return ResponseData.makeResponseData(response.getUserInfo().getUnionid());
    }

    /**
     * 访问/topapi/user/getbyunionid 获取userid
     *
     * @param accessToken Token
     * @param unionId 获取userid
     * @return ResponseData
     */
    @Override
    public ResponseData getUserByUnionId(String accessToken, String unionId) {
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_GET_USER_BYUNIONID);
        OapiUserGetbyunionidRequest request = new OapiUserGetbyunionidRequest();
        request.setUnionid(unionId);
        OapiUserGetbyunionidResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            logger.error("Failed to {}", UrlConstant.URL_USER_GET, e);
            return ResponseData.makeErrorMessage("Failed to getUserByUnionId: " + e.getErrMsg());
        }
        if (!response.isSuccess()) {
            return ResponseData.makeErrorMessage(Integer.valueOf(response.getErrorCode()), response.getErrmsg());
        }
        return ResponseData.makeResponseData(response.getResult().getUserid());
    }

    /**
     * 访问/topapi/v2/user/get 获取用户详情
     *
     * @param accessToken access_token
     * @param userId      用户userId
     * @return 用户详情或错误信息
     */
    @Override
    public ResponseData getUserInfo(String accessToken, String userId) {
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_GET_USER);
        OapiV2UserGetRequest request = new OapiV2UserGetRequest();
        request.setUserid(userId);
        request.setLanguage("zh_CN");
        OapiV2UserGetResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            logger.error("Failed to {}", UrlConstant.URL_GET_USER, e);
            return ResponseData.makeErrorMessage("Failed to getUserInfo: " + e.getErrMsg());
        }
        if (!response.isSuccess()) {
            return ResponseData.makeErrorMessage(Integer.valueOf(response.getErrorCode()), response.getErrmsg());
        }
        return ResponseData.makeResponseData(response.getBody());
    }

    /**
     * 访问/topapi/v2/user/create 创建用户
     *
     * @param accessToken Token
     * @param userInfo 创建用户
     * @return ResponseData
     */
    @Override
    public ResponseData userCreate(String accessToken, DingUserDTO userInfo) {
        ResponseData userByMobile = getUserByMobile(accessToken, userInfo.getRegCellPhone());
        if (userByMobile.getCode() == 0) {
            return ResponseData.makeResponseData("该手机用户已存在，无需同步");
        }
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.USER_CREATE);
        OapiV2UserCreateRequest request = new OapiV2UserCreateRequest();
        request.setName(userInfo.getUserName());
        request.setMobile(userInfo.getRegCellPhone());
        SocialDeptAuth socialDeptAuth = socialDeptAuthDao.getObjectById(userInfo.getPrimaryUnit());
        if (null != socialDeptAuth) {
            //需查询获取钉钉部门详情的id
            request.setDeptIdList(socialDeptAuth.getDeptId());
        } else {
            logger.error("Failed to {}", UrlConstant.USER_CREATE, "部门未同步到平台应用");
            return ResponseData.makeErrorMessage(500, "Failed to userCreate: 部门未同步到平台应用");
        }
        OapiV2UserCreateResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            logger.error("Failed to {}", UrlConstant.USER_CREATE, e);
            return ResponseData.makeErrorMessage("Failed to userCreate: " + e.getErrMsg());
        }
        if (!response.isSuccess()) {
            return ResponseData.makeErrorMessage(Integer.valueOf(response.getErrorCode()), response.getErrmsg());
        }
        //response.getResult().getUserid();
        return ResponseData.makeResponseData(response.getBody());
    }

    /**
     * 访问/topapi/v2/department/create 创建机构部门
     *
     * @param accessToken Token
     * @param unitInfo 机构部门
     * @return ResponseData
     */
    @Override
    public ResponseData unitCreate(String accessToken, DingUnitDTO unitInfo) {
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.DEPARTMENT_CREATE);
        OapiV2DepartmentCreateRequest request = new OapiV2DepartmentCreateRequest();
        request.setName(unitInfo.getUnitName());
        if ("".equals(unitInfo.getParentUnit())) {
            request.setParentId(1L);
        } else {
            //获取部门和平台部门的关联数据
            SocialDeptAuth socialDeptAuth = socialDeptAuthDao.getObjectById(unitInfo.getParentUnit());
            if (null != socialDeptAuth) {
                request.setParentId(Long.valueOf(socialDeptAuth.getDeptId()));
            } else {
                logger.error("Failed to {}", UrlConstant.DEPARTMENT_CREATE, "未查询到父节点机构");
                return ResponseData.makeErrorMessage(500, "Failed to unitCreate: 未查询到父节点机构");
            }
        }
        OapiV2DepartmentCreateResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            logger.error("Failed to {}", UrlConstant.DEPARTMENT_CREATE, e);
            return ResponseData.makeErrorMessage("Failed to unitCreate: " + e.getErrMsg());
        }
        if (!response.isSuccess()) {
            return ResponseData.makeErrorMessage(Integer.valueOf(response.getErrorCode()), response.getErrmsg());
        }
        //response.getResult().getDeptId();
        SocialDeptAuth socialDeptAuth = new SocialDeptAuth();
        socialDeptAuth.setUnitCode(unitInfo.getUnitCode());
        socialDeptAuth.setDeptId(response.getResult().getDeptId().toString());
        socialDeptAuthDao.saveNewObject(socialDeptAuth);
        return ResponseData.makeResponseData(response.getBody());
    }

    /**
     * 访问/topapi/v2/department/get 获取部门详情
     *
     * @param accessToken
     * @param deptId
     * @return
     */
    @Override
    public ResponseData getUnitInfo(String accessToken, String deptId) {
        OapiV2DepartmentGetResponse response = getDingUnitinfo(accessToken, deptId);
        if (response == null) {
            return ResponseData.makeErrorMessage("Failed to getDingUnitinfo");
        }
        if (!response.isSuccess()) {
            return ResponseData.makeErrorMessage(Integer.valueOf(response.getErrorCode()), response.getErrmsg());
        }
        return ResponseData.makeResponseData(response.getBody());
    }

    @Override
    public JSONArray getUnitList(String accessToken, String deptId) {
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.DEPARTMENTS_URL);
        OapiV2DepartmentListsubRequest request = new OapiV2DepartmentListsubRequest();
        request.setDeptId(Long.valueOf(deptId));
        request.setLanguage("zh_CN");
        OapiV2DepartmentListsubResponse response = null;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            logger.error("Failed to {}", UrlConstant.DEPARTMENT_CREATE, e);
        }
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        return jsonObject.getJSONArray("result");
    }

    @Override
    public JSONObject getUnitUserList(String accessToken, String deptId) {
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_USER_LIST);
        OapiV2UserListRequest request = new OapiV2UserListRequest();
        request.setDeptId(Long.valueOf(deptId));
        request.setCursor(Long.valueOf(0));
        request.setSize(Long.valueOf(100));
        OapiV2UserListResponse response = null;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            logger.error("Failed to {}", UrlConstant.URL_DEPARTMENT_GET, e);
        }
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        return jsonObject;
    }


    private OapiV2DepartmentGetResponse getDingUnitinfo(String accessToken, String deptId) {
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_DEPARTMENT_GET);
        OapiV2DepartmentGetRequest request = new OapiV2DepartmentGetRequest();
        request.setDeptId(Long.valueOf(deptId));
        request.setLanguage("zh_CN");
        OapiV2DepartmentGetResponse response = null;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            logger.error("Failed to {}", UrlConstant.URL_DEPARTMENT_GET, e);
        }
        return response;
    }

    private String getAccessToken() {
        String accessToken = "";
        ResponseData accessTokenData = tokenService.getAccessToken();
        if (accessTokenData.getCode() != 0) {
            return "";
        }
        accessToken = accessTokenData.getData().toString();
        return accessToken;
    }

    /**
     * 访问/topapi/v2/user/getbymobile 根据手机号查询用户
     * @param accessToken accessToken
     * @param mobile 手机号
     * @return ResponseData
     */
    @Override
    public ResponseData getUserByMobile(String accessToken, String mobile) {
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_GET_USER_BYMOBILE);
        OapiV2UserGetbymobileRequest request = new OapiV2UserGetbymobileRequest();
        request.setMobile(mobile);
        OapiV2UserGetbymobileResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            logger.error("Failed to {}", UrlConstant.URL_GET_USER_BYMOBILE, e);
            return ResponseData.makeErrorMessage("Failed to getUserByMobile: " + e.getErrMsg());
        }
        if (!response.isSuccess()) {
            return ResponseData.makeErrorMessage(Integer.valueOf(response.getErrorCode()), response.getErrmsg());
        }
        return ResponseData.makeResponseData(response.getBody());
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public int synchroniseUserDirectory(UserSyncDirectory directory){
        try{
            String accessToken = getAccessToken();
            Date now = DatetimeOpt.currentUtilDate();
            List<Object> list = getDeptList("1", new LinkedList<>(), accessToken);
            List<String> deptIdList = new ArrayList<>();
            deptIdList.add("1");
            if(list != null && list.size() > 0){
                for(Object obj : list){
                    if(obj == null){
                        continue;
                    }
                    JSONObject jsonObject = JSONObject.parseObject(obj+"");
                    deptIdList.add(jsonObject.getString("dept_id"));
                    String unitTag = jsonObject.getString("dept_id");
                    UnitInfo unitInfo = unitInfoDao.getUnitByTag(unitTag);
                    boolean createNew = unitInfo==null;
                    if(createNew){
                        unitInfo = new UnitInfo();
                        unitInfo.setUnitTag(unitTag);
                        unitInfo.setIsValid("T");
                        unitInfo.setUnitType("A");
                        unitInfo.setCreateDate(now);
                        unitInfo.setUnitWord("DING");
                    }
                    String parentId = jsonObject.getString("parent_id");
                    //获取父机构信息
                    UnitInfo parentUnit = unitInfoDao.getUnitByTag(parentId);
                    unitInfo.setUnitName(jsonObject.getString("name"));
                    unitInfo.setLastModifyDate(now);
                    unitInfo.setParentUnit(parentUnit.getUnitCode());
                    if(createNew){
                        unitInfoDao.saveNewObject(unitInfo);
                        unitInfo.setUnitPath("/"+unitInfo.getUnitCode());
                        UnitInfo pUnit = unitInfoDao.getObjectById(unitInfo.getParentUnit());
                        if (pUnit != null) {
                            unitInfo.setUnitPath(pUnit.getUnitPath() + "/" + unitInfo.getUnitCode());
                            //加上租户
                            unitInfo.setTopUnit(pUnit.getTopUnit());
                        }
                        if (StringUtils.isBlank(unitInfo.getTopUnit()) && StringUtils.isNotBlank(unitInfo.getUnitPath())) {
                            String[] unitCodeArray = unitInfo.getUnitPath().split("/");
                            if (ArrayUtils.isNotEmpty(unitCodeArray) && unitCodeArray.length > 1) {
                                //加上租户
                                unitInfo.setTopUnit(unitCodeArray[1]);
                            }
                        }
                        unitInfoDao.updateUnit(unitInfo);
                    }else {
                        unitInfoDao.updateUnit(unitInfo);
                    }
                }
            }
            List<JSONObject> userList = getUserList(deptIdList, new LinkedList<>(), accessToken);

            if(userList != null && userList.size() > 0){
                for(JSONObject jsonObject : userList){
                    String loginName = jsonObject.getString("userid");
                    String name = jsonObject.getString("name");
                    boolean createUser = false;
                    UserInfo userInfo = userInfoDao.getUserByTag(jsonObject.getString("unionid"));
                    if(userInfo == null) {
                        userInfo = new UserInfo();
                        //userInfo.setIsValid("T");
                        //钉钉中的userId为空时，登录名设置为用户名称
                        if(loginName != null && !"".equals(loginName)){
                            userInfo.setLoginName(loginName);
                        }else{
                            userInfo.setLoginName(name);
                        }
                        userInfo.setCreateDate(now);
                        userInfo.setUserName(name);
                        userInfo.setUserWord("DING");
                        createUser = true;
                    }
                    String regEmail = jsonObject.getString("email");
                    if (StringUtils.isNotBlank(regEmail) && regEmail.length() < 60 &&
                        userInfoDao.getUserByRegEmail(regEmail) == null) {
                        userInfo.setRegEmail(regEmail);
                    }
                    String regCellPhone = jsonObject.getString("mobile");
                    if (StringUtils.isNotBlank(regCellPhone) && regCellPhone.length() < 15 &&
                        userInfoDao.getUserByRegCellPhone(regCellPhone) == null) {
                        userInfo.setRegCellPhone(regCellPhone);
                    }
                    JSONArray unitArray = jsonObject.getJSONArray("dept_id_list");
                    if(unitArray != null && unitArray.size() > 0){
                        UnitInfo unitInfo = unitInfoDao.getUnitByTag(unitArray.getString(0));
                        if(unitInfo != null){
                            userInfo.setPrimaryUnit(unitInfo.getUnitCode());
                            if (null != unitInfo && StringUtils.isNotBlank(unitInfo.getTopUnit())) {
                                userInfo.setTopUnit(unitInfo.getTopUnit());
                            }
                            if (null != unitInfo && StringUtils.isBlank(userInfo.getTopUnit()) && StringUtils.isNotBlank(unitInfo.getUnitPath())) {
                                String[] unitCodeArray = unitInfo.getUnitPath().split("/");
                                if (ArrayUtils.isNotEmpty(unitCodeArray) && unitCodeArray.length > 1) {
                                    userInfo.setTopUnit(unitCodeArray[1]);
                                }
                            }
                        }
                    }
                    userInfo.setUpdateDate(now);
                    userInfo.setUserTag(jsonObject.getString("unionid"));
                    if(createUser){
                        userInfoDao.saveUserInfo(userInfo);
                    }else{
                        userInfoDao.updateUser(userInfo);
                    }

                    if(createUser && StringUtils.isNoneBlank(directory.getDefaultUserRole())){
                        UserRole role = new UserRole(
                            new UserRoleId(userInfo.getUserCode(), directory.getDefaultUserRole()));
                        role.setObtainDate(now);
                        role.setCreateDate(now);
                        role.setChangeDesc("钉钉同步时默认设置。");
                        userRoleDao.mergeUserRole(role);
                    }

                    if (CollectionUtils.isEmpty(unitArray)) {
                        for(Object obj : unitArray){
                            UnitInfo u = unitInfoDao.getUnitByTag(obj + "");
                            UserUnit uu = new UserUnit();
                            uu.setUserUnitId(UuidOpt.getUuidAsString());
                            uu.setUnitCode(u.getUnitCode());
                            uu.setUserCode(userInfo.getUserCode());
                            uu.setCreateDate(now);
                            if (u.getUnitCode().equals(userInfo.getPrimaryUnit())) {
                                uu.setRelType("T");
                            }else{
                                uu.setRelType("F");
                            }
                            uu.setUserRank(directory.getDefaultRank());
                            uu.setUserStation(directory.getDefaultStation());
                            uu.setRankMemo("DING");
                            userUnitDao.saveNewObject(uu);
                        }
                    }
                }
            }
            return 0;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public List getDeptList(String deptId, List<Object> list, String accessToken){
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }
        JSONArray jsonArray = this.getUnitList(accessToken, deptId);
        if(jsonArray != null && jsonArray.size() > 0){
            for(Object obj : jsonArray){
                list.add(obj);
                JSONObject jsonObject = JSONObject.parseObject(obj+"");
                getDeptList(jsonObject.getString("dept_id"), list, accessToken);
            }
        }
        return list;
    }

    public List getUserList(List<String> depts, List<JSONObject> list, String accessToken){
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }
        if(depts != null && depts.size() > 0){
            for(String dept : depts){
                JSONObject jsonObject = this.getUnitUserList(accessToken, dept);
                if(jsonObject != null) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    JSONArray jsonArray = new JSONArray();
                    if(jsonObject1 != null){
                        jsonArray = jsonObject1.getJSONArray("list");
                    }
                    for(Object obj : jsonArray){
                        list.add(JSONObject.parseObject(obj+""));
                    }
                }
            }
        }
        return list;
    }

}
