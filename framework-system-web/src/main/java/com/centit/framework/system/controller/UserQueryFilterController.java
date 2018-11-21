package com.centit.framework.system.controller;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.system.po.UserQueryFilter;
import com.centit.framework.system.service.UserQueryFilterManager;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * UserQueryFilter  Controller.
 * create by scaffold 2016-02-29
 * @author codefan@sina.com
 * 用户自定义过滤条件表null
*/


@Controller
@RequestMapping("/userqueryfilter")
@Api(value="用户自定义过滤条件操作。", tags= "用户自定义过滤条件操作接口")
public class UserQueryFilterController  extends BaseController {
    //private static final Logger logger = LoggerFactory.getLogger(UserQueryFilterController.class);
    public String getOptId() {
        return  "UserQueryFilter";
    }
    @Resource
    private UserQueryFilterManager userQueryFilterMag;
    /*public void setUserQueryFilterMag(UserQueryFilterManager basemgr)
    {
        userQueryFilterMag = basemgr;
        //this.setBaseEntityManager(userQueryFilterMag);
    }*/

    /**
     * 查询所有   用户自定义过滤条件表  列表
     *
     * @param field    json中只保存需要的属性名
     * @param pageDesc    pageDesc
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @ApiOperation(value="查询所有 用户自定义过滤条件",notes="查询所有 用户自定义过滤条件")
    @ApiImplicitParams({@ApiImplicitParam(
        name = "field", value="过滤返回的字段信息",
        allowMultiple= true, paramType = "query", dataType= "String"
    ),@ApiImplicitParam(
        name = "pageDesc", value="json格式的分页信息",
        required=true, paramType = "body", dataTypeClass = PageDesc.class
    )})
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);
        List<UserQueryFilter> listObjects = userQueryFilterMag.listObjects(searchColumn, pageDesc);

        SimplePropertyPreFilter simplePropertyPreFilter = null;
        if (ArrayUtils.isNotEmpty(field)) {
            simplePropertyPreFilter = new SimplePropertyPreFilter(UserQueryFilter.class, field);
        }

        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response, simplePropertyPreFilter);
            return;
        }
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, listObjects);
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 查找用户某个模块的所有过滤器
     * @param modelCode 按照模块列出用户所有的过滤器
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @ApiOperation(value="查找用户某个模块的所有过滤器",notes="查找用户某个模块的所有过滤器")
    @ApiImplicitParam(
        name = "modelCode", value="模块代码",
        required=true, paramType = "path", dataType= "String")
    @RequestMapping(value = "/list/{modelCode}", method = {RequestMethod.GET})
    public void listUserQueryFilter(@PathVariable String modelCode, HttpServletRequest request, HttpServletResponse response) {

        List<UserQueryFilter> userFilters =
                userQueryFilterMag.listUserQueryFilterByModle(
                        super.getLoginUserCode(request), modelCode);

        JsonResultUtils.writeSingleDataJson(userFilters, response);
    }
    /**
     * 查询单个 用户自定义过滤条件

     * @param filterNo  过滤条件序号
     * @param response    {@link HttpServletResponse}
     */
    @ApiOperation(value="查询单个用户自定义过滤条件",notes="查询单个用户自定义过滤条件")
    @ApiImplicitParam(
        name = "filterNo", value="过滤条件序号",
        required=true, paramType = "path", dataType= "String")
    @RequestMapping(value = "/{filterNo}", method = {RequestMethod.GET})
    public void getUserQueryFilter(@PathVariable Long filterNo, HttpServletResponse response) {

        UserQueryFilter userQueryFilter =
                userQueryFilterMag.getUserQueryFilter( filterNo);

        JsonResultUtils.writeSingleDataJson(userQueryFilter, response);
    }

    /**
     * 新增 用户自定义过滤条件表
     *
     * @param userQueryFilter  {@link UserQueryFilter}
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @ApiOperation(value="查询单个用户自定义过滤条件新增 用户自定义过滤条件",notes="新增 用户自定义过滤条件")
    @ApiImplicitParam(
        name = "userQueryFilter", value="json格式的过滤条件对象",
        required=true, paramType = "body", dataTypeClass = UserQueryFilter.class)
    @RequestMapping(method = {RequestMethod.POST})
    public void createUserQueryFilter(
            @Valid UserQueryFilter userQueryFilter,
            HttpServletRequest request,HttpServletResponse response) {

        userQueryFilter.setFilterNo(userQueryFilterMag.getNextFilterKey());
        userQueryFilter.setCreateDate(DatetimeOpt.currentUtilDate());
        userQueryFilter.setIsDefault("F");

        if(StringBaseOpt.isNvl(userQueryFilter.getUserCode()))
            userQueryFilter.setUserCode(super.getLoginUserCode(request));
        userQueryFilterMag.saveNewObject(userQueryFilter);
        JsonResultUtils.writeSingleDataJson(userQueryFilter.getFilterNo(),response);
    }

    /**
    * 保存用户最新查看筛选器
    *
    * @param modelCode  所属模块编码
    * @param userQueryFilter  {@link UserQueryFilter}
    * @param request  {@link HttpServletRequest}
    * @param response  {@link HttpServletResponse}
    */
    @ApiOperation(value="保存用户最新查看筛选器",notes="保存用户最新查看筛选器")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "modelCode", value="所属模块编码",
            required=true, paramType = "path", dataType= "String"),
        @ApiImplicitParam(
            name = "userQueryFilter", value="json格式的过滤条件对象",
            required=true, paramType = "body", dataTypeClass = UserQueryFilter.class)
    })
   @RequestMapping(value = "/default/{modelCode}", method = {RequestMethod.POST,RequestMethod.PUT})
   public void createUserDefaultFilter(@PathVariable String modelCode,
           @Valid UserQueryFilter userQueryFilter,
           HttpServletRequest request,HttpServletResponse response) {

       userQueryFilter.setCreateDate(DatetimeOpt.currentUtilDate());
       userQueryFilter.setIsDefault("T");
       userQueryFilter.setModleCode(modelCode);
       if(StringBaseOpt.isNvl(userQueryFilter.getUserCode()))
           userQueryFilter.setUserCode(super.getLoginUserCode(request));

       Serializable pk = userQueryFilterMag.saveUserDefaultFilter(userQueryFilter);
       JsonResultUtils.writeSingleDataJson(pk,response);
   }

   /**
    * 保存用户最新查看筛选器
    *
    * @param modelCode  所属模块编码
    * @param request  {@link HttpServletRequest}
    * @param response  {@link HttpServletResponse}
    */
   @ApiOperation(value="保存用户最新查看筛选器",notes="保存用户最新查看筛选器")
   @ApiImplicitParam(
       name = "modelCode", value="所属模块编码",
       required=true, paramType = "path", dataType= "String")
   @RequestMapping(value = "/default/{modelCode}", method = {RequestMethod.GET})
   public void getUserDefaultFilter(@PathVariable String modelCode,
           HttpServletRequest request,HttpServletResponse response) {

       UserQueryFilter userQueryFilter =
               userQueryFilterMag.getUserDefaultFilter(super.getLoginUserCode(request), modelCode);
       JsonResultUtils.writeSingleDataJson(userQueryFilter,response);
   }


    /**
     * 删除单个 用户自定义过滤条件表

     * @param filterNo  FILTER_NO
     * @param response  {@link HttpServletResponse}
     */
    @ApiOperation(value="删除单个 用户自定义过滤条件表",notes="删除单个 用户自定义过滤条件表")
    @ApiImplicitParam(
        name = "filterNo", value="过滤条件序号",
        required=true, paramType = "path", dataType= "String")
    @RequestMapping(value = "/{filterNo}", method = {RequestMethod.DELETE})
    public void deleteUserQueryFilter(@PathVariable Long filterNo, HttpServletResponse response) {

        boolean b = userQueryFilterMag.deleteUserQueryFilter(filterNo);
        if(b)
            JsonResultUtils.writeBlankJson(response);
        else
            JsonResultUtils.writeErrorMessageJson("不能删除默认过滤条件！", response);
    }

    /**
     * 新增或保存 用户自定义过滤条件表

     * @param filterNo  FILTER_NO
     * @param userQueryFilter  {@link UserQueryFilter}
     * @param response    {@link HttpServletResponse}
     */
    @ApiOperation(value="新增或保存 用户自定义过滤条件表",notes="新增或保存 用户自定义过滤条件表")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "filterNo", value="过滤条件序号",
            required=true, paramType = "path", dataType= "String"),
        @ApiImplicitParam(
            name = "userQueryFilter", value="json格式的过滤条件对象",
            required=true, paramType = "body", dataTypeClass = UserQueryFilter.class)
    })
    @RequestMapping(value = "/{filterNo}", method = {RequestMethod.PUT})
    public void updateUserQueryFilter(@PathVariable Long filterNo,
        @Valid UserQueryFilter userQueryFilter, HttpServletResponse response) {


        UserQueryFilter dbUserQueryFilter  =
                userQueryFilterMag.getUserQueryFilter( filterNo);

        if (null != userQueryFilter) {
            dbUserQueryFilter .copy(userQueryFilter);
            dbUserQueryFilter.setCreateDate(DatetimeOpt.currentUtilDate());
            userQueryFilterMag.mergeObject(dbUserQueryFilter);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}
