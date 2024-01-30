package com.centit.framework.system.dao;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.basedata.TenantInfo;
import com.centit.framework.system.vo.PageListTenantInfoQo;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TenantInfoDao extends BaseDaoImpl<TenantInfo,String> {


    public List<TenantInfo> listObjectsByProperties(PageListTenantInfoQo pageListTenantInfoQo, PageDesc pageDesc) {

        HashMap<String, Object> filterMap = getQoFilterMap(pageListTenantInfoQo);
        return super.listObjectsByProperties(filterMap,pageDesc);
    }

    /**
     * 查询租户信息和租户所有者对应的用户名
     * @param filterMap 过滤条件
     * @param pageDesc 分页
     * @return JSONArray
     */
    public JSONArray listTenantInfoWithOwnUserName(Map<String,Object> filterMap,PageDesc pageDesc){
        String sql = " SELECT A.TOP_UNIT, A.UNIT_NAME, A.OWN_USER, B.USER_NAME " +
            " FROM F_TENANT_INFO A JOIN  F_USERINFO B ON A.OWN_USER = B.USER_CODE " +
            " WHERE   A.IS_AVAILABLE = 'T' [ :(like)unitName | AND A.UNIT_NAME like :unitName ] " +
            "[ :otherTenant | AND A.top_unit not in ( SELECT DISTINCT b_1.TOP_UNIT FROM f_userunit a_1 join f_unitinfo b_1  on a_1.TOP_UNIT = b_1.TOP_UNIT WHERE a_1.USER_CODE = :userCode  ) ] order by A.UNIT_NAME ";
        QueryAndNamedParams qap = QueryUtils.translateQuery(sql, filterMap);
        if (StringUtils.isNotBlank(MapUtils.getString(filterMap,"otherTenant"))){
            qap.addParam("userCode",MapUtils.getString(filterMap,"userCode"));
        }
        return DatabaseOptUtils.listObjectsByNamedSqlAsJson(this, qap.getQuery(), qap.getParams(), pageDesc);
    }


    /**
     * 获取用户所在租户
     * @param filterMap 过滤
     * @return List
     */
    public List<TenantInfo> listUserTenant(Map<String,Object> filterMap){
        String sql = " SELECT " +
            "DISTINCT " +
            "A.TOP_UNIT, A.UNIT_NAME,A.SOURCE_URL,A.OWN_USER,a.pic_id,a.logo_file_id " +
            "FROM F_TENANT_INFO A  JOIN F_USERUNIT B ON A.TOP_UNIT = B.TOP_UNIT WHERE A.IS_AVAILABLE = 'T'   AND B.USER_CODE = :userCode " ;
        return getJdbcTemplate().execute((ConnectionCallback<List<TenantInfo>>) conn ->OrmDaoUtils.queryObjectsByNamedParamsSql(conn, sql ,
            filterMap, TenantInfo.class));
    }

    private HashMap<String, Object> getQoFilterMap(PageListTenantInfoQo pageListTenantInfoQo) {
        HashMap<String, Object> filterMap = new HashMap<>();

        if (StringUtils.isNotBlank(pageListTenantInfoQo.getUnitName())){
            filterMap.put("unitName_lk",StringUtils.join("%",pageListTenantInfoQo.getUnitName(),"%"));
        }

        if (StringUtils.isNotBlank(pageListTenantInfoQo.getCheckState()) && pageListTenantInfoQo.getCheckState().equals("1")){
            //passTime_nv is null
            filterMap.put("passTime_nv","");
        }
        if (StringUtils.isNotBlank(pageListTenantInfoQo.getCheckState()) && pageListTenantInfoQo.getCheckState().equals("2")){
            //passTime is not null
            filterMap.put("passTime_nn","");
        }

        if (null != pageListTenantInfoQo.getStartApplyTime()){
            filterMap.put("applyTime_ge",pageListTenantInfoQo.getStartApplyTime());
        }
        if (null != pageListTenantInfoQo.getEndApplyTime()){
            filterMap.put("applyTime_le",pageListTenantInfoQo.getEndApplyTime());
        }

        if (null != pageListTenantInfoQo.getStartUseLimittime()){
            filterMap.put("useLimittime_ge",pageListTenantInfoQo.getStartUseLimittime());
        }
        if (null != pageListTenantInfoQo.getEndUseLimittime()){
            filterMap.put("useLimittime_le",pageListTenantInfoQo.getEndUseLimittime());
        }

        if (null != pageListTenantInfoQo.getStartPassTime()){
            filterMap.put("passTime_ge",pageListTenantInfoQo.getStartPassTime());
        }
        if (null != pageListTenantInfoQo.getEndPassTime()){
            filterMap.put("passTime_le",pageListTenantInfoQo.getEndPassTime());
        }

        if (StringUtils.isNotBlank(pageListTenantInfoQo.getIsAvailable())){
            filterMap.put("isAvailable",pageListTenantInfoQo.getIsAvailable());
        }

        if (StringUtils.isNotBlank(pageListTenantInfoQo.getOwnUser())){
            filterMap.put("ownUser",pageListTenantInfoQo.getOwnUser());
        }
        return filterMap;
    }

    /**
     * 校验用户是否为租户所有者
     * @param topUnit 租户
     * @param userCode 用户代码
     * @return boolean
     */
    public boolean userIsOwner(String topUnit,String userCode) {
        String sql = " SELECT COUNT(1) FROM F_TENANT_INFO WHERE TOP_UNIT = ? AND OWN_USER = ? ";
        return NumberBaseOpt.castObjectToInteger(
            DatabaseOptUtils.getScalarObjectQuery(this, sql,new Object[]{topUnit, userCode}))> 0;
    }
}
