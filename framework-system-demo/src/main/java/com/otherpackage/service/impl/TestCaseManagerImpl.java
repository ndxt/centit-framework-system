package com.otherpackage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.system.service.GeneralService;
import com.centit.support.database.utils.PageDesc;
import com.otherpackage.dao.TestCaseDao;
import com.otherpackage.po.TestCase;
import com.otherpackage.service.TestCaseManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    @Resource
    protected GeneralService generalService;
    // 业务对应的数据库访问到
    @Resource
    protected TestCaseDao testCaseDao;
    // 具体的业务操作，每一个业务操作有一个唯一的操作代码optCode
    @Override
    public JSONArray listObjectsAsJson(CentitUserDetails ud,
                                Map<String, Object> filterMap, PageDesc pageDesc){
        // 创建当权用户信息相关的上下文
        DataPowerFilter dataPowerFilter = generalService.createUserDataPowerFilter(ud);
        // 将当前查询参数（一般是前台输入的参数）添加到这个用户信息中
        dataPowerFilter.addSourceData(filterMap);
        // 获取用户权限范围过滤条件，getOptId() 获取当前业务ID，  query为当前业务操作代码optCode
        List<String> filters = generalService.listUserDataFiltersByOptIdAndMethod(
            ud.getUserCode(), getOptId(), "query");
        return DictionaryMapUtils.objectsToJSONArray( // 数据字典翻译工作
            this.testCaseDao.listObjectByProperties(//) .listObjectsAsJson( // 执行数据驱动查询
                //dataPowerFilter.getSourceData(),// 当前用户相关参数（已包括查询信息）
                filterMap));
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
        DataPowerFilter dataPowerFilter = generalService.createUserDataPowerFilter(ud);
        // 获取用户权限范围过滤条件，getOptId() 获取当前业务ID，  saveNew 为当前业务操作代码optCode
        List<String> filters = generalService.listUserDataFiltersByOptIdAndMethod(
            ud.getUserCode(), getOptId(), "saveNew");
        boolean passed = dataPowerFilter.checkObject(testCase, filters);
        if(passed){
            this.testCaseDao.saveNewObject(testCase);
        }
        return passed;
    }
}
