package com.centit.framework.web.demo.test;

import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.system.dao.impl.UserInfoDaoImpl;
import com.centit.framework.system.po.UserInfo;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringRegularOpt;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.List;
import java.util.Properties;

/**
 * Created by codefan on 17-9-11.
 */
public class TestQuerySystemTable {


    public static BasicDataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        Properties env = SysParametersUtils.loadProperties();
        dataSource.setDriverClassName(env.getProperty("jdbc.driver"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.password"));
        dataSource.setMaxTotal(NumberBaseOpt.castObjectToInteger(env.getProperty("jdbc.maxActive")));
        dataSource.setMaxIdle(NumberBaseOpt.castObjectToInteger(env.getProperty("jdbc.maxIdle")));
        dataSource.setMaxWaitMillis(NumberBaseOpt.castObjectToInteger(env.getProperty("jdbc.maxWait")));
        dataSource.setDefaultAutoCommit(StringRegularOpt.isTrue(env.getProperty("jdbc.defaultAutoCommit")));
        dataSource.setRemoveAbandonedTimeout(NumberBaseOpt.castObjectToInteger(env.getProperty("jdbc.removeAbandonedTimeout")));
        return dataSource;
    }

    public static void main(String[] args) {
        UserInfoDaoImpl userInfoDao = new UserInfoDaoImpl();

        userInfoDao.setDataSource(getDataSource());

        List<UserInfo> users = userInfoDao.listObjects(QueryUtils.createSqlParamsMap("unitCode","U00001"));
        System.out.print(users.size());
    }
}
