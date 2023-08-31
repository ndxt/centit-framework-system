package com.otherpackage.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.database.utils.PageDesc;
import com.otherpackage.dao.TestCaseDao;
import com.otherpackage.po.TestCase;
import com.otherpackage.service.TestCaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service("testCaseManager")
public class TestCaseManagerImpl implements TestCaseManager {
    //业务代码optId
    public String getOptId(){
        return "testCase";
    }
    // 框架通用的服务，可以访问框架的配置信息
    @Autowired
    protected DataScopePowerManager dataScopePowerManager;
    // 业务对应的数据库访问到
    @Autowired
    protected TestCaseDao testCaseDao;
    // 具体的业务操作，每一个业务操作有一个唯一的操作代码optCode
    @Override
    public JSONArray listObjectsAsJson(CentitUserDetails ud,
                                Map<String, Object> filterMap, PageDesc pageDesc){
        // 创建当权用户信息相关的上下文
        DataPowerFilter dataPowerFilter = dataScopePowerManager.createUserDataPowerFilter(ud);
        // 将当前查询参数（一般是前台输入的参数）添加到这个用户信息中
        dataPowerFilter.addSourceData(filterMap);
        // 获取用户权限范围过滤条件，getOptId() 获取当前业务ID，  query为当前业务操作代码optCode
        List<String> filters = dataScopePowerManager.listUserDataFiltersByOptIdAndMethod(
            ud.getTopUnitCode(), ud.getUserCode(), getOptId(), "query");
        return DictionaryMapUtils.mapJsonArray( // 数据字典翻译工作
            this.testCaseDao.listObjectsByPropertiesAsJson(// 执行数据驱动查询
                dataPowerFilter.getSourceData(),// 当前用户相关参数（已包括查询信息）
                filters,
                dataPowerFilter.getPowerFilterTranslater(), // 用户数据范围权限过滤条件
                pageDesc), TestCase.class);
    }

    /**
     * 保存 新对象
     * @param ud 当前用户信息， 在Controller中可以通过 getLoginUser() 获取
     * @param testCase 新的对象
     * @return 返回是否保存成功
     */
    @Override
    public boolean saveNewCase(CentitUserDetails ud, TestCase testCase){
        // 创建当权用户信息相关的上下文
        DataPowerFilter dataPowerFilter = dataScopePowerManager.createUserDataPowerFilter(ud);
        // 获取用户权限范围过滤条件，getOptId() 获取当前业务ID，  saveNew 为当前业务操作代码optCode
        List<String> filters = dataScopePowerManager.listUserDataFiltersByOptIdAndMethod(
            ud.getTopUnitCode(), ud.getUserCode(), getOptId(), "saveNew");
        boolean passed = dataPowerFilter.checkObject(testCase, filters);
        if(passed){
            this.testCaseDao.saveNewObject(testCase);
        }
        return passed;
    }
}
