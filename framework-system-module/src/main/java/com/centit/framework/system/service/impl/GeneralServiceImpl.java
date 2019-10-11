package com.centit.framework.system.service.impl;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.model.basedata.IUserUnit;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.system.service.GeneralService;
import com.centit.framework.system.service.OptInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("generalService")
public  class GeneralServiceImpl implements GeneralService {

    @Autowired
    private OptInfoManager optInfoManager;

    /**
     * 获取用户数据权限过滤器
     *
     * @param sUserCode sUserCode
     * @param sOptId 业务名称
     * @param sOptMethod 对应的方法名称
     * @return 过滤条件列表，null或者空位不过来
     */
    @Override
    @Transactional
    public List<String> listUserDataFiltersByOptIdAndMethod(String sUserCode, String sOptId, String sOptMethod) {
        //CodeRepositoryUtil.listUserDataFiltersByOptIdAndMethod(sUserCode,sOptId,sOptMethod);
        return optInfoManager.listUserDataFiltersByOptIdAndMethod(sUserCode,sOptId,sOptMethod);
    }
    /**
     * 创建用户数据范围过滤器
     * @return  DataPowerFilter
     */
    @Override
    @Transactional
    public DataPowerFilter createUserDataPowerFilter(
            CentitUserDetails userDetails) {
        DataPowerFilter dpf = new DataPowerFilter();
        //当前用户信息
        dpf.addSourceData("currentUser", userDetails.getUserInfo());
        dpf.addSourceData("currentStation", userDetails.getCurrentStation());
        //当前用户主机构信息
        dpf.addSourceData("primaryUnit", CodeRepositoryUtil
                .getUnitInfoByCode(userDetails.getUserInfo().getString("primaryUnit")));
        //当前用户所有机构关联关系信息
        List<? extends IUserUnit>  userUnits = CodeRepositoryUtil
              .listUserUnits(userDetails.getUserCode());
        if(userUnits!=null) {
            dpf.addSourceData("userUnits", userUnits);
            Map<String, List<IUserUnit>> rankUnits = new HashMap<>(5);
            Map<String, List<IUserUnit>> stationUnits = new HashMap<>(5);
            for(IUserUnit uu : userUnits ){
                List<IUserUnit> rankUnit = rankUnits.get(uu.getUserRank());
                if(rankUnit==null){
                    rankUnit = new ArrayList<>(4);
                }
                rankUnit.add(uu);
                rankUnits.put(uu.getUserRank(),rankUnit);

                List<IUserUnit> stationUnit = stationUnits.get(uu.getUserStation());
                if(stationUnit==null){
                    stationUnit = new ArrayList<>(4);
                }
                stationUnit.add(uu);
                stationUnits.put(uu.getUserStation(),rankUnit);
            }
            dpf.addSourceData("rankUnits", rankUnits);
            dpf.addSourceData("stationUnits", stationUnits);
        }
        //当前用户的角色信息
        dpf.addSourceData("userRoles", userDetails.getUserRoles());
        return dpf;
    }
}
