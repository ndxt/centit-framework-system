package com.centit.framework.staticsystem.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.common.GlobalConstValue;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.core.dao.ExtendedQueryPool;
import com.centit.framework.model.basedata.*;
import com.centit.support.common.ListAppendMap;
import com.centit.support.database.utils.DataSourceDescription;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.DbcpConnectPools;
import com.centit.support.database.utils.TransactionHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcPlatformEnvironment extends AbstractStaticPlatformEnvironment {

    private static Log logger = LogFactory.getLog(JdbcPlatformEnvironment.class);

    private DataSourceDescription dataSource;

    public void setDataBaseConnectInfo(String connectURI, String username, String pswd){
        this.dataSource = new DataSourceDescription( connectURI,  username,  pswd);
    }

    private <T> List<T> jsonArrayToObjectList(JSONArray jsonArray, Class<T> clazz) {
        if(jsonArray==null)
            return new ArrayList<>();
        return jsonArray.toJavaList(clazz);
    }

    private void loadConfigFromJdbc() throws SQLException, IOException,DocumentException {

        ExtendedQueryPool.loadResourceExtendedSqlMap(dataSource.getDbType());

        try(Connection conn = DbcpConnectPools.getDbcpConnect(dataSource)) {
            JSONArray userJSONArray = DatabaseAccess.findObjectsAsJSON(conn,
                ExtendedQueryPool.getExtendedSql("LIST_ALL_USER"));
            List<UserInfo> userinfos = jsonArrayToObjectList(userJSONArray, UserInfo.class);
            CodeRepositoryCache.userInfoRepo.setFreshData(
                GlobalConstValue.NO_TENANT_TOP_UNIT, new ListAppendMap<>(userinfos,
                    UserInfo::getUserCode));
            JSONArray optInfoJSONArray = DatabaseAccess.findObjectsAsJSON(conn,
                ExtendedQueryPool.getExtendedSql("LIST_ALL_OPTINFO"));
            List<OptInfo> optinfos = jsonArrayToObjectList(optInfoJSONArray,  OptInfo.class);
            CodeRepositoryCache.optInfoRepo.setFreshData(GlobalConstValue.NO_TENANT_TOP_UNIT,
                optinfos);

            JSONArray optDataScopesJSONArray = DatabaseAccess.findObjectsAsJSON(conn,
                ExtendedQueryPool.getExtendedSql("LIST_ALL_OPTDATASCOPE"));
            List<OptDataScope> dataScopes = jsonArrayToObjectList(optDataScopesJSONArray, OptDataScope.class);
            optDataScopes.setFreshData(dataScopes);

            JSONArray optMethodsJSONArray = DatabaseAccess.findObjectsAsJSON(conn,
                ExtendedQueryPool.getExtendedSql("LIST_ALL_OPTMETHOD"));
            List<OptMethod> optmethods = jsonArrayToObjectList(optMethodsJSONArray,  OptMethod.class);
            allOptMethod.setFreshData(optmethods);

            JSONArray roleInfoJSONArray = DatabaseAccess.findObjectsAsJSON(conn,
                ExtendedQueryPool.getExtendedSql("LIST_ALL_ROLEINFO"));
            List<RoleInfo> roleinfos = jsonArrayToObjectList(roleInfoJSONArray,  RoleInfo.class);
            CodeRepositoryCache.roleInfoRepo.setFreshData(GlobalConstValue.NO_TENANT_TOP_UNIT,
                roleinfos);

            JSONArray rolePowerJSONArray = DatabaseAccess.findObjectsAsJSON(conn,
                ExtendedQueryPool.getExtendedSql("LIST_ALL_ROLEPOWER"));
            List<RolePower>  rolepowers = jsonArrayToObjectList(rolePowerJSONArray,  RolePower.class);
            allRolePower.setFreshData(rolepowers);

            JSONArray userRoleJSONArray = DatabaseAccess.findObjectsAsJSON(conn,
                ExtendedQueryPool.getExtendedSql("LIST_ALL_USERROLE"));
            List<UserRole> userroles = jsonArrayToObjectList(userRoleJSONArray, UserRole.class);
            allUserRoleRepo.setFreshData(userroles);

            JSONArray unitInfoJSONArray = DatabaseAccess.findObjectsAsJSON(conn,
                ExtendedQueryPool.getExtendedSql("LIST_ALL_UNITINFO"));
            List<UnitInfo> unitinfos = jsonArrayToObjectList(unitInfoJSONArray, UnitInfo.class);
            CodeRepositoryCache.unitInfoRepo.setFreshData(GlobalConstValue.NO_TENANT_TOP_UNIT,
                new ListAppendMap<>(unitinfos, UnitInfo::getUnitCode));

            JSONArray userUnitJSONArray = DatabaseAccess.findObjectsAsJSON(conn,
                ExtendedQueryPool.getExtendedSql("LIST_ALL_USERUNIT"));
            List<UserUnit> userunits = jsonArrayToObjectList(userUnitJSONArray, UserUnit.class);
            allUserUnitRepo.setFreshData(userunits);

            JSONArray dataCatalogsJSONArray = DatabaseAccess.findObjectsAsJSON(conn,
                ExtendedQueryPool.getExtendedSql("LIST_ALL_DATACATALOG"));
            List<DataCatalog> datacatalogs = jsonArrayToObjectList(dataCatalogsJSONArray, DataCatalog.class);

            JSONArray dataDictionaryJSONArray = DatabaseAccess.findObjectsAsJSON(conn,
                ExtendedQueryPool.getExtendedSql("LIST_ALL_DICTIONARY"));
            List<DataDictionary> datadictionaies = jsonArrayToObjectList(dataDictionaryJSONArray, DataDictionary.class);

            for (DataCatalog dd : datacatalogs) {
                List<DataDictionary> dictionaries = new ArrayList<>(20);
                for(DataDictionary data : datadictionaies){
                    if( StringUtils.equals(dd.getCatalogCode(), data.getCatalogCode())){
                        dictionaries.add(data);
                    }
                }
                dd.setDataDictionaries(dictionaries);
            }
            catalogRepo.setFreshData(datacatalogs);

            JSONArray osInfoJSONArray = DatabaseAccess.findObjectsAsJSON(conn,
                ExtendedQueryPool.getExtendedSql("LIST_ALL_OS"));
            List<OsInfo> osInfos = jsonArrayToObjectList(osInfoJSONArray, OsInfo.class);
            CodeRepositoryCache.osInfoCache.setFreshData(GlobalConstValue.NO_TENANT_TOP_UNIT,
                osInfos);
        }
    }
    /**
     * 刷新数据字典
     */
    @Override
    protected synchronized void reloadPlatformData() {
        try {
            CodeRepositoryCache.evictAllCache();
            loadConfigFromJdbc();
        } catch (IOException | SQLException | DocumentException e) {
           logger.error(e.getLocalizedMessage());
        }
        organizePlatformData();
    }

    /**
     * 修改用户密码
     * @param userCode userCode
     * @param userPassword userPassword
     */
    @Override
    public void changeUserPassword(String userCode, String userPassword) {
        UserInfo ui= (UserInfo)CodeRepositoryCache.userInfoRepo
            .getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT)
            .getAppendMap().get(userCode);
        if(ui==null)
            return;
        String userNewPassword = passwordEncoder.encodePassword(userPassword, userCode);
        try{
            TransactionHandler.executeQueryInTransaction(
                dataSource, (conn) -> DatabaseAccess.doExecuteSql(conn,
                    ExtendedQueryPool.getExtendedSql("UPDATE_USER_PASSWORD"),
                    new Object []{ userNewPassword, userCode })
            );
        }catch (Exception e){
            //conn.rollback();
        }
    }

}
