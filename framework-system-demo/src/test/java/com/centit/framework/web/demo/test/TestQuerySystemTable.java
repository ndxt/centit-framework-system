package com.centit.framework.web.demo.test;

import com.centit.framework.common.SysParametersUtils;
import com.centit.prodcts.demoapp.config.ServiceConfig;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.utils.DataSourceDescription;

import java.lang.annotation.Annotation;
import java.util.Properties;

/**
 * Created by codefan on 17-9-11.
 */
public class TestQuerySystemTable {


    public static DataSourceDescription getDataSource() {
        DataSourceDescription dataSource = new DataSourceDescription();
        Properties env = SysParametersUtils.loadProperties();
        //dataSource.setDriverClassName(env.getProperty("jdbc.driver"));
        dataSource.setConnUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.password"));
        dataSource.setMaxTotal(NumberBaseOpt.castObjectToInteger(env.getProperty("jdbc.maxActive")));
        dataSource.setMaxIdle(NumberBaseOpt.castObjectToInteger(env.getProperty("jdbc.maxIdle")));
        dataSource.setMaxWaitMillis(NumberBaseOpt.castObjectToInteger(env.getProperty("jdbc.maxWait")));
        //dataSource.setDefaultAutoCommit(StringRegularOpt.isTrue(env.getProperty("jdbc.defaultAutoCommit")));
        //dataSource.setRemoveAbandonedTimeout(NumberBaseOpt.castObjectToInteger(env.getProperty("jdbc.removeAbandonedTimeout")));
        return dataSource;
    }

    public static void main(String[] args) {
        Annotation[] annotations = ServiceConfig.class.getAnnotations();
        System.out.println(annotations.length);
//        UserInfoDaoImpl userInfoDao = new UserInfoDaoImpl();
//
//        userInfoDao.setDataSource(getDataSource());
//        Map<String,Object> filterMap = Collections.createHashMap("unitCode","U00001");
//        PageDesc page = new PageDesc();//1,20,userInfoDao.pageCount(filterMap ) );
//
//        Map<String, Object> pageQureyMap =
//                QueryParameterPrepare.prepPageParams(filterMap, page , 1 /* userInfoDao.pageCount(filterMap )*/);
//
//        List<UserInfo> users = userInfoDao.pageQuery(pageQureyMap);
//        System.out.println(page.getTotalRows());
//        System.out.println(users.size());
//        if(users.size()>0) {
//            System.out.println(users.get(0).getUserName());
//        }
    }
}
