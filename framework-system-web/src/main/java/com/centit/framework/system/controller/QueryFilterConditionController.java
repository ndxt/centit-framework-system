package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.system.po.QueryFilterCondition;
import com.centit.framework.system.service.QueryFilterConditionManager;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.Map;

/**
 * QueryFilterCondition  Controller.
 * create by scaffold 2016-03-01
 *
 * @author codefan@sina.com
 * 系统内置查询方式null
 */


@Controller
@Api(value = "系统内置查询方式接口", tags = "系统内置查询方式操作接口")
@RequestMapping("/queryfiltercondition")
public class QueryFilterConditionController extends BaseController {
    //private static final Logger logger = LoggerFactory.getLogger(QueryFilterConditionController.class);
    public String getOptId() {
        return "QueryFilter";
    }

    @Resource
    private QueryFilterConditionManager queryFilterConditionMag;
    /*public void setQueryFilterConditionMag(QueryFilterConditionManager basemgr)
    {
        queryFilterConditionMag = basemgr;
        //this.setBaseEntityManager(queryFilterConditionMag);
    }*/

    /**
     * 查询所有   系统内置查询方式  列表
     *
     * @param field    json中只保存需要的属性名
     * @param pageDesc 分页信息
     * @param request  {@link HttpServletRequest}
     */
    @ApiOperation(value = "查询所有", notes = "查询所有 系统内置查询方式  列表。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "field", value = "指需要显示的属性名",
            allowMultiple = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value = "分页对象",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData list(String[] field, PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);

        JSONArray listObjects = queryFilterConditionMag.listQueryFilterConditionsAsJson(field, searchColumn, pageDesc);

        if (null == pageDesc) {
            return ResponseData.makeResponseData(listObjects);
        }

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, listObjects);
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);
        return resData;
    }

    /**
     * 查询单个  系统内置查询方式
     *
     * @param conditionNo CONDITION_NO
     */
    @ApiOperation(value = "查询单个", notes = "查询单个 系统内置查询方式。")
    @ApiImplicitParam(
        name = "conditionNo", value = "条件编号",
        paramType = "path", dataType = "Long")
    @RequestMapping(value = "/{conditionNo}", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData getQueryFilterCondition(@PathVariable Long conditionNo) {

        QueryFilterCondition queryFilterCondition =
            queryFilterConditionMag.getObjectById(conditionNo);
        return ResponseData.makeResponseData(queryFilterCondition);
    }

    /**
     * 新增 系统内置查询方式
     *
     * @param queryFilterCondition {@link QueryFilterCondition}
     */
    @ApiOperation(value = "新增 系统内置查询方式", notes = "新增 系统内置查询方式。")
    @ApiImplicitParam(
        name = "queryFilterCondition", value = "查询对象", required = true,
        paramType = "body", dataTypeClass = QueryFilterCondition.class)
    @RequestMapping(method = {RequestMethod.POST})
    @WrapUpResponseBody
    public ResponseData createQueryFilterCondition(@Valid QueryFilterCondition queryFilterCondition) {
        Serializable pk = queryFilterConditionMag.saveNewObject(queryFilterCondition);
        return ResponseData.makeResponseData(pk);
    }

    /**
     * 删除单个  系统内置查询方式
     *
     * @param conditionNo CONDITION_NO
     */
    @ApiOperation(value = "删除单个  系统内置查询方式", notes = "删除单个  系统内置查询方式。")
    @ApiImplicitParam(
        name = "conditionNo", value = "条件编号", required = true,
        paramType = "path", dataType = "Long")
    @RequestMapping(value = "/{conditionNo}", method = {RequestMethod.DELETE})
    @WrapUpResponseBody
    public ResponseData deleteQueryFilterCondition(@PathVariable Long conditionNo) {

        queryFilterConditionMag.deleteObjectById(conditionNo);
        return ResponseData.makeSuccessResponse();
    }

    /**
     * 新增或保存 系统内置查询方式
     *
     * @param conditionNo          CONDITION_NO
     * @param queryFilterCondition {@link QueryFilterCondition}
     */
    @ApiOperation(value = "新增或保存 系统内置查询方式", notes = "新增或保存 系统内置查询方式。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "conditionNo", value = "条件编号",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "queryFilterCondition", value = "分页对象",
            paramType = "body", dataTypeClass = QueryFilterCondition.class)
    })
    @RequestMapping(value = "/{conditionNo}", method = {RequestMethod.PUT})
    @WrapUpResponseBody
    public ResponseData updateQueryFilterCondition(@PathVariable Long conditionNo,
                                                   @Valid QueryFilterCondition queryFilterCondition) {

        QueryFilterCondition dbQueryFilterCondition =
            queryFilterConditionMag.getObjectById(conditionNo);

        if (null != queryFilterCondition) {
            dbQueryFilterCondition.copy(queryFilterCondition);
            queryFilterConditionMag.mergeObject(dbQueryFilterCondition);
        } else {
            return ResponseData.makeErrorMessage("当前对象不存在");
        }

        return ResponseData.makeSuccessResponse();
    }
}
