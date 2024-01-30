package com.centit.framework.system.service;

import com.centit.framework.model.basedata.UserPlat;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * @author zfg
 */
public interface UserPlatService {

    UserPlat getObjectById(String userPlatId);

    UserPlat getUserPlatByProperties(Map<String,Object> paramsMap);

    List<UserPlat> listPlatUsersByPlatId(String platId);

    List<UserPlat> listObjects(Map<String, Object> filterMap, PageDesc pageDesc);

    void mergeObject(UserPlat userPlat);

    void deleteObjectById(String userPlatId);

    void saveUserPlat(UserPlat userPlat);

    void updateUserPlat(UserPlat userPlat);

    void deleteObject(UserPlat userPlat);
}
