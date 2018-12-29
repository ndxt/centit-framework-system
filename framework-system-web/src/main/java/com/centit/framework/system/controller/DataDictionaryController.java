package com.centit.framework.system.controller;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.centit.framework.common.ObjectException;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.po.DataCatalog;
import com.centit.framework.system.po.DataDictionary;
import com.centit.framework.system.po.DataDictionaryId;
import com.centit.framework.system.service.DataDictionaryManager;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 数据字典
 *
 * @author god
 */
@Controller
@RequestMapping("/dictionary")
@Api(tags = "框架数据字典操作接口", value = "框架数据字典接口")
public class DataDictionaryController extends BaseController {

    public static final String F = "F";
    public static final String S = "S";
    public static final String U = "U";
    public static final String T = "T";

    @Value("${app.local.multiLang:false}")
    private boolean multiLang;

    @Resource
    private DataDictionaryManager dataDictionaryManager;

    //private String optId = "DICTSET";
    public String getOptId() {
        return "DICTSET";
    }

    /**
     * 查询所有字典目录列表
     *
     * @param field    指需要的属性名
     * @param pageDesc 分页信息
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @ApiOperation(value = "查询所有字典目录列表", notes = "查询所有字典目录列表。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "field", value = "指需要显示的属性名",
            allowMultiple = true, paramType = "query", dataType = "array"),
        @ApiImplicitParam(
            name = "pageDesc", value = "分页对象",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);

        List<DataCatalog> listObjects = dataDictionaryManager.listObjects(searchColumn, pageDesc);

        SimplePropertyPreFilter simplePropertyPreFilter = null;
        if (ArrayUtils.isNotEmpty(field)) {
            simplePropertyPreFilter = new SimplePropertyPreFilter(DataCatalog.class, field);
        }

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, DictionaryMapUtils.objectsToJSONArray(listObjects));
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);
        resData.toJSONString(simplePropertyPreFilter);

        return resData;
    }

    /**
     * 查询单个字典目录
     *
     * @param catalogCode DataCatalog主键
     */
    @ApiOperation(value = "查询单个字典目录", notes = "根据数据字典类别代码查询单个字典目录。")
    @ApiImplicitParam(
        name = "catalogCode", value = "数据字典的类别代码",
        required = true, paramType = "path", dataType = "String"
    )
    @RequestMapping(value = "/{catalogCode}", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData getCatalog(@PathVariable String catalogCode) {

        DataCatalog dbDataCatalog = dataDictionaryManager.getCatalogIncludeDataPiece(catalogCode);

        return ResponseData.makeResponseData(dbDataCatalog);
    }

    /**
     * catalogCode是否不存在
     *
     * @param catalogCode catalogCode
     */
    @ApiOperation(value = "根据数据字典类别代码查询字典是否不存在", notes = "根据数据字典类别代码查询字典是否不存在。")
    @ApiImplicitParam(
        name = "catalogCode", value = "数据字典的类别代码",
        required = true, paramType = "path", dataType = "String"
    )
    @RequestMapping(value = "/notexists/{catalogCode}", method = {RequestMethod.GET})
    @WrapUpResponseBody(contentType = WrapUpContentType.RAW)
    public boolean isNotExistsCatalogCode(@PathVariable String catalogCode) {
        DataCatalog dbDataCatalog = dataDictionaryManager.getObjectById(catalogCode);
        return null == dbDataCatalog;
    }

    /**
     * catalogName是否已存在
     *
     * @param catalogName catalogName
     */
    @ApiOperation(value = "根据数据字典名字代码查询字典是否存在", notes = "根据数据字典名字代码查询字典是否存在。")
    @ApiImplicitParam(
        name = "catalogName", value = "数据字典的类别名字",
        required = true, paramType = "path", dataType = "String"
    )
    @RequestMapping(value = "/existcatalogname/{catalogName}", method = {RequestMethod.GET})
    @WrapUpResponseBody(contentType = WrapUpContentType.RAW)
    public int isExistsCatalogName(@PathVariable String catalogName) {
        return dataDictionaryManager.existCatalogName(catalogName);
    }

    /**
     * dataCode是否已存在
     *
     * @param catalogCode catalogCode
     * @param dataCode    dataCode
     */
    @ApiOperation(value = "校验数据代码是否存在", notes = "根据数据字典类别代码和数据代码判断字典是否存在")
    @ApiImplicitParams({@ApiImplicitParam(
        name = "catalogCode", value = "数据字典的类别代码",
        required = true, paramType = "path", dataType = "String"
    ), @ApiImplicitParam(
        name = "dataCode", value = "数据字典的数据代码",
        required = true, paramType = "path", dataType = "String"
    )})
    @RequestMapping(value = "/notexists/dictionary/{catalogCode}/{dataCode}", method = {RequestMethod.GET})
    @WrapUpResponseBody(contentType = WrapUpContentType.RAW)
    public boolean isNotExistsDataCode(@PathVariable String catalogCode, @PathVariable String dataCode) {
        DataDictionary dbDataDictionary = dataDictionaryManager.getDataDictionaryPiece(
            new DataDictionaryId(catalogCode, dataCode));

        return null == dbDataDictionary;
    }

    /**
     * 新增字典类别
     *
     * @param dataCatalog {@link DataCatalog}
     * @param request     {@link HttpServletRequest}
     */
    @ApiOperation(value = "新增字典类别", notes = "新增字典类别")
    @ApiParam(name = "dataCatalog", value = "新增字典类别,输入框有提示的都是必填项", required = true)
    @RequestMapping(method = {RequestMethod.POST})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增字典类别")
    @WrapUpResponseBody
    public void createCatalog(@Valid DataCatalog dataCatalog, HttpServletRequest request) {
        boolean isAdmin = isLoginAsAdmin(request);
        if (isAdmin) {
            dataCatalog.setCatalogStyle("S");
        } else {
            dataCatalog.setCatalogStyle("U");
        }
        dataDictionaryManager.saveNewObject(dataCatalog);
        if(dataCatalog.getDataDictionaries() != null && dataCatalog.getDataDictionaries().size() > 0){
            for (DataDictionary d : dataCatalog.getDataDictionaries()) {
                if (StringUtils.isBlank(d.getDataStyle())) {
                    d.setDataStyle(isAdmin ? "S" : "U");
                    d.setCatalogCode(dataCatalog.getCatalogCode());
                }
            }
            dataDictionaryManager.saveCatalogIncludeDataPiece(dataCatalog, isAdmin);
        }
    }

    /**
     * 更新字典类别
     *
     * @param catalogCode DataCatalog主键
     * @param dataCatalog {@link DataCatalog}
     */
    @ApiOperation(value = "更新字典类别", notes = "更新字典类别信息")
    @ApiImplicitParam(
        name = "catalogCode", value = "数据字典的类别代码",
        required = true, paramType = "path", dataType = "String"
    )
    @ApiParam(name = "dataCatalog", value = "更新字典类别新的对象信息", required = true)
    @RequestMapping(value = "/{catalogCode}", method = {RequestMethod.PUT})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新字典类别")
    @WrapUpResponseBody
    public ResponseData updateCatalog(@PathVariable String catalogCode, @Valid DataCatalog dataCatalog) {

        DataCatalog dbDataCatalog = dataDictionaryManager.getObjectById(catalogCode);

        if (null == dbDataCatalog) {
            return ResponseData.makeErrorMessage("当前对象不存在");
        }

        DataCatalog oldValue = new DataCatalog();
        BeanUtils.copyProperties(dbDataCatalog, oldValue);

        BeanUtils.copyProperties(dataCatalog, dbDataCatalog, "catalogStyle", "catalogCode", "dataDictionaries");
//        boolean isAdmin = isLoginAsAdmin(request);
//        String datastyle = isAdmin?"S":"U";
//        for(DataDictionary d : dataCatalog.getDataDictionaries()){
//            d.setDataStyle(datastyle);
//        }
//        dbDataCatalog.addAllDataPiece(dataCatalog.getDataDictionaries());

//        dataDictionaryManager.saveCatalogIncludeDataPiece(dbDataCatalog,isAdmin);
        dataDictionaryManager.updateCatalog(dbDataCatalog);

        return ResponseData.makeSuccessResponse();

        /***********************log*****************************/
//        OperationLogCenter.logUpdateObject(request, optId, catalogCode, OperationLog.P_OPT_LOG_METHOD_U,
//                "更新数据字典目录", dbDataCatalog, oldValue);
        /***********************log*****************************/
    }

    private boolean isLoginAsAdmin(HttpServletRequest request) {
        Object obj = request.getSession().getAttribute(MainFrameController.ENTRANCE_TYPE);
        return obj != null && MainFrameController.DEPLOY_LOGIN.equals(obj.toString());
    }

    /**
     * 更新字典目录明细
     *
     * @param catalogCode DataCatalog主键
     * @param dataCatalog {@link DataCatalog}
     * @param request     {@link HttpServletRequest}
     * @param response    {@link HttpServletResponse}
     */
    @ApiOperation(value = "更新字典目录明细", notes = "更新字典目录明细，目录明显对象就是dataDictionaries[]里面的属性组成")
    @ApiImplicitParam(
        name = "catalogCode", value = "数据字典的类别代码",
        required = true, paramType = "path", dataType = "String"
    )
    @ApiParam(name = "dataCatalog", value = "更新字典类别新的对象信息", required = true)
    @RequestMapping(value = "update/{catalogCode}", method = {RequestMethod.PUT})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新字典目录明细")
    @WrapUpResponseBody
    public ResponseData updateDictionary(@PathVariable String catalogCode, @Valid DataCatalog dataCatalog,
                                         HttpServletRequest request, HttpServletResponse response) {

        DataCatalog dbDataCatalog = dataDictionaryManager.getObjectById(catalogCode);

        if (null == dbDataCatalog) {
            return ResponseData.makeErrorMessage("当前对象不存在");
        }
        DataCatalog oldValue = new DataCatalog();
        BeanUtils.copyProperties(dbDataCatalog, oldValue);
        boolean isAdmin = isLoginAsAdmin(request);
        String datastyle = isAdmin ? "S" : "U";
        for (DataDictionary d : dataCatalog.getDataDictionaries()) {
            if (StringUtils.isBlank(d.getDataStyle())) {
                d.setDataStyle(datastyle);
            }
        }
        dbDataCatalog.addAllDataPiece(dataCatalog.getDataDictionaries());
        List<DataDictionary> oldDictionaries = dataDictionaryManager.saveCatalogIncludeDataPiece(dbDataCatalog, isAdmin);
        oldValue.setDataDictionaries(oldDictionaries);

        return ResponseData.makeSuccessResponse();

        /***********************log*****************************/
//        OperationLogCenter.logUpdateObject(request, optId, catalogCode, OperationLog.P_OPT_LOG_METHOD_U,
//                "更新数据字典明细", dbDataCatalog, oldValue);
        /***********************log*****************************/
    }

    /**
     * 新增数据字典
     *
     * @param catalogCode    DataCatalog主键
     * @param dataCode       DataDictionary主键
     * @param dataDictionary {@link DataDictionary}
     * @param request        {@link HttpServletRequest}
     */
    @ApiOperation(value = "新增某个字典目录里的字典明细", notes = "新增某个字典目录里的字典明细")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "catalogCode", value = "数据字典的类别代码",
            required = true, paramType = "path", dataType = "String"
        ),
        @ApiImplicitParam(
            name = "dataCode", value = "数据字典的明细代码",
            required = true, paramType = "path", dataType = "String"
        )
    })
    @ApiParam(name = "dataDictionary", value = "字典明细的对象信息", required = true)
    @RequestMapping(value = "/dictionary/{catalogCode}/{dataCode}", method = {RequestMethod.PUT})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增数据字典")
    @WrapUpResponseBody
    public ResponseData createDictionary(@PathVariable String catalogCode, @PathVariable String dataCode,
                                         @Valid DataDictionary dataDictionary,
                                         HttpServletRequest request) {

        DataDictionary dbDataDictionary = dataDictionaryManager.getDataDictionaryPiece(new DataDictionaryId(catalogCode,
            dataCode));

        DataDictionary oldValue = new DataDictionary();
        oldValue.copy(dbDataDictionary);

        DataCatalog dbDataCatalog = dataDictionaryManager.getObjectById(catalogCode);

        dictionaryPreHandler(dbDataCatalog, dataDictionary);

        dictionaryPreInsertHandler(dbDataCatalog, dataDictionary, request);
        dataDictionaryManager.saveDataDictionaryPiece(dataDictionary);
        /**************************log***************************/
//            OperationLogCenter.logNewObject(request, optId, catalogCode+"-"+dataCode, OperationLog.P_OPT_LOG_METHOD_C,
//                    "新增数据字典明细", dataDictionary);
        /**************************log***************************/

        return ResponseData.makeSuccessResponse();
    }

    /**
     * 更新数据字典
     *
     * @param catalogCode    DataCatalog主键
     * @param dataCode       DataDictionary主键
     * @param dataDictionary {@link DataDictionary}
     * @param request        {@link HttpServletRequest}
     * @param response       {@link HttpServletResponse}
     */
    @ApiOperation(value = "更新某个字典目录里的字典明细", notes = "更新某个字典目录里的字典明细")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "catalogCode", value = "数据字典的类别代码",
            required = true, paramType = "path", dataType = "String"
        ),
        @ApiImplicitParam(
            name = "dataCode", value = "数据字典的明细代码",
            required = true, paramType = "path", dataType = "String"
        )
    })
    @ApiParam(name = "dataDictionary", value = "字典明细的对象信息", required = true)
    @RequestMapping(value = "/dictionary/{catalogCode}/{dataCode}", method = {RequestMethod.POST})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新数据字典")
    @WrapUpResponseBody
    public ResponseData editDictionary(@PathVariable String catalogCode, @PathVariable String dataCode,
                                       @Valid DataDictionary dataDictionary,
                                       HttpServletRequest request, HttpServletResponse response) {

        DataDictionary dbDataDictionary = dataDictionaryManager.getDataDictionaryPiece(new DataDictionaryId(catalogCode,
            dataCode));

        DataDictionary oldValue = new DataDictionary();
        oldValue.copy(dbDataDictionary);

        DataCatalog dbDataCatalog = dataDictionaryManager.getObjectById(catalogCode);

        dictionaryPreHandler(dbDataCatalog, dataDictionary);

        dictionaryPreUpdateHandler(dbDataCatalog, dbDataDictionary, request);
        BeanUtils.copyProperties(dataDictionary, dbDataDictionary, "id", "dataStyle");
        dictionaryPreUpdateHandler(dbDataCatalog, dbDataDictionary, request);
        dataDictionaryManager.saveDataDictionaryPiece(dbDataDictionary);
        /**************************log***************************/
//        OperationLogCenter.logUpdateObject(request, optId, catalogCode+"-"+dataCode, OperationLog.P_OPT_LOG_METHOD_U,
//                "更新数据字典明细", dbDataDictionary, oldValue);
        /**************************log***************************/

        return ResponseData.makeSuccessResponse();
    }

    /**
     * 数据字典公共验证
     *
     * @param dataCatalog    DataCatalog
     * @param dataDictionary DataDictionary
     */
    protected void dictionaryPreHandler(DataCatalog dataCatalog, DataDictionary dataDictionary) {
        //附加代码 EXTRACODE  字段
        //这是一个自解释字段，业务系统可以自行解释这个字段的意义，单作为树形结构的数据字典时，这个字段必需为上级字典的代码。

        if (T.equalsIgnoreCase(dataCatalog.getCatalogType())) {
            String extraCode = dataDictionary.getExtraCode();
            if (StringUtils.isBlank(extraCode)) {
                throw new ObjectException("extraCode 字段不可为空");
            }

            if (extraCode.equals(dataDictionary.getDataCode())) {
                throw new ObjectException("extraCode 与 dataCode 不能一致");
            }
            DataDictionary dd = dataDictionaryManager.getDataDictionaryPiece(
                new DataDictionaryId(dataDictionary.getCatalogCode(), extraCode));
            if (null == dd) {
                throw new ObjectException("当前父节点不存在");
            }
        }
    }

    /**
     * 数据字典的新增权限进行业务数据判断
     *
     * @param dataCatalog    DataCatalog
     * @param dataDictionary DataDictionary
     * @param request        HttpServletRequest
     */
    protected void dictionaryPreInsertHandler(DataCatalog dataCatalog, DataDictionary dataDictionary,
                                              HttpServletRequest request) {
        if (isLoginAsAdmin(request)) {
            dataDictionary.setDataStyle(S);
        } else {
            if (!S.equalsIgnoreCase(dataCatalog.getCatalogStyle()) && !U.equalsIgnoreCase(dataCatalog.getCatalogStyle())) {
                throw new ObjectException("catalogStyle 字段只可填写 S 或 U");
            }
            dataDictionary.setDataStyle(U);
            if (!U.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException("dataStyle 字段只可填写 U");
            }
        }
    }

    /**
     * 数据字典的删除权限进行业务数据判断
     *
     * @param request        HttpServletRequest
     * @param dataCatalog    DataCatalog
     * @param dataDictionary DataDictionary
     */
    protected void dictionaryPreDeleteHandler(DataCatalog dataCatalog, DataDictionary dataDictionary,
                                              HttpServletRequest request) {
        if (isLoginAsAdmin(request)) {
            if (!S.equalsIgnoreCase(dataDictionary.getDataStyle()) && !U.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException("只能删除 catalogStyle为 S 或 U 的字典目录");
            }
        } else {
            if (!U.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException("dataStyle 字段只可填写 U");
            }
        }
    }

    /**
     * 数据字典的编辑权限进行业务数据判断
     *
     * @param dataCatalog    DataCatalog
     * @param dataDictionary DataDictionary
     * @param request        HttpServletRequest
     */
    protected void dictionaryPreUpdateHandler(DataCatalog dataCatalog, DataDictionary dataDictionary,
                                              HttpServletRequest request) {
        if (isLoginAsAdmin(request)) {

            if (F.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException("dataStyle 为 F 类型的数据字典，任何地方都不允许编辑，只能有开发人员给出更新脚本添加、更改和删除");
            }

            if (F.equalsIgnoreCase(dataCatalog.getCatalogStyle()) && !S.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException("只能修改 dataStyle 为 S 的数据字典");
            }
            if (!S.equalsIgnoreCase(dataCatalog.getCatalogStyle()) && !U.equalsIgnoreCase(dataCatalog.getCatalogStyle())) {
                throw new ObjectException("catalogStyle 字段只可填写 S 或 U");
            }
            if (!S.equalsIgnoreCase(dataDictionary.getDataStyle()) && !U.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException("dataStyle 字段只可填写 S 或 U");
            }
        } else {
            if (!U.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException("dataStyle 字段只可填写 U");
            }
        }
    }

    protected void catalogPrDeleteHandler(DataCatalog dataCatalog, HttpServletRequest request) {
        if (isLoginAsAdmin(request)) {
            if (!S.equalsIgnoreCase(dataCatalog.getCatalogStyle()) && !U.equalsIgnoreCase(dataCatalog.getCatalogStyle())) {
                throw new ObjectException("只能删除 catalogStyle为 S 或 U 的字典目录");
            }
        } else {
            if (!U.equalsIgnoreCase(dataCatalog.getCatalogStyle())) {
                throw new ObjectException("只可删除 catalogStyle 为 U 的字典目录");
            }
        }
    }

    /**
     * 删除字典目录
     *
     * @param catalogCode DataCatalog主键
     * @param request     {@link HttpServletRequest}
     * @param response    {@link HttpServletResponse}
     */
    @ApiOperation(value = "根据数据字典目录代码删除字典目录", notes = "根据数据字典目录代码删除字典目录。")
    @ApiImplicitParam(
        name = "catalogCode", value = "数据字典目录代码",
        required = true, paramType = "path", dataType = "String"
    )
    @RequestMapping(value = "/{catalogCode}", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除字典目录")
    @WrapUpResponseBody
    public ResponseData deleteCatalog(@PathVariable String catalogCode,
                                      HttpServletRequest request, HttpServletResponse response) {
        DataCatalog dataCatalog = dataDictionaryManager.getObjectById(catalogCode);
        catalogPrDeleteHandler(dataCatalog, request);

        dataDictionaryManager.deleteDataDictionary(catalogCode);
        return ResponseData.makeSuccessResponse();

        /*****************log************************/
//        OperationLogCenter.logDeleteObject(request, optId, catalogCode, OperationLog.P_OPT_LOG_METHOD_D,
//                "删除数据字典目录", dataCatalog);
        /*****************log************************/
    }

    /**
     * 删除数据字典
     *
     * @param catalogCode DataCatalog主键
     * @param dataCode    dataCode
     * @param request     {@link HttpServletRequest}
     * @param response    {@link HttpServletResponse}
     */
    @ApiOperation(value = "删除字典明细", notes = "根据字典类别和字典明细代码删除字典明细")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "catalogCode", value = "数据字典的类别代码",
            required = true, paramType = "path", dataType = "String"
        ),
        @ApiImplicitParam(
            name = "dataCode", value = "数据字典的明细代码",
            required = true, paramType = "path", dataType = "String"
        )
    })
    @RequestMapping(value = "/dictionary/{catalogCode}/{dataCode}", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除数据字典")
    @WrapUpResponseBody
    public ResponseData deleteDictionary(@PathVariable String catalogCode, @PathVariable String dataCode,
                                         HttpServletRequest request, HttpServletResponse response) {
        DataCatalog dataCatalog = dataDictionaryManager.getObjectById(catalogCode);
        DataDictionary dataDictionary = dataDictionaryManager.getDataDictionaryPiece(new DataDictionaryId(catalogCode, dataCode));

        dictionaryPreDeleteHandler(dataCatalog, dataDictionary, request);

        dataDictionaryManager.deleteDataDictionaryPiece(dataDictionary.getId());

        return ResponseData.makeSuccessResponse();

        /*****************log************************/
//        OperationLogCenter.logDeleteObject(request, optId, catalogCode+"-"+dataCode, OperationLog.P_OPT_LOG_METHOD_D,
//                "删除数据字典明细", dataDictionary);
        /*****************log************************/
    }

    /**
     * 获取字典的所有明细信息
     *
     * @param catalogCode 数据字典的类别代码
     */
    @ApiOperation(value = "获取字典的所有明细信息", notes = "根据字典类别代码获取字典的所以明细信息")
    @ApiImplicitParam(
        name = "catalogCode", value = "数据字典的类别代码",
        required = true, paramType = "path", dataType = "String"
    )
    @RequestMapping(value = "/dictionaryPiece/{catalogCode}", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData getDataDictionary(@PathVariable String catalogCode) {
        List<DataDictionary> datas = dataDictionaryManager.getDataDictionary(catalogCode);
        return ResponseData.makeResponseData(datas);
    }

    /**
     * 获取字典的详细信息
     *
     * @param catalogCode 数据字典的类别代码
     */
    @ApiOperation(value = "获取字典的详细信息", notes = "根据字典类别代码获取字典的详细信息")
    @ApiImplicitParam(
        name = "catalogCode", value = "数据字典的类别代码",
        required = true, paramType = "path", dataType = "String"
    )
    @RequestMapping(value = "/editDictionary/{catalogCode}", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData getDataDictionaryDetail(@PathVariable String catalogCode) {
        List<DataDictionary> datas = dataDictionaryManager.getDataDictionary(catalogCode);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("dataDictionary", datas);
        resData.addResponseData("multiLang", multiLang);
        resData.addResponseData("langs", CodeRepositoryUtil.getLabelValueMap("SUPPORT_LANG"));
        return resData;
    }

    /**
     * 获取所以字典目录信息
     */
    @ApiOperation(value = "获取所以字典目录信息", notes = "获取所以字典目录信息")
    @RequestMapping(value = "/allCatalog", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData getAllCatalog() {
        List<DataCatalog> catalogs = dataDictionaryManager.listAllDataCatalog();
        return ResponseData.makeResponseData(catalogs);
    }

    /**
     * 获取所以字典目录信息及对应的所以字典明细信息
     *
     * @param response HttpServletResponse
     */
    @ApiOperation(value = "获取所以字典目录信息及对应的所以字典明细信息", notes = "获取所以字典目录信息及对应的所以字典明细信息")
    @RequestMapping(value = "/wholeDictionary", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData getWholeDictionary(HttpServletResponse response) {
        List<DataCatalog> catalogs = dataDictionaryManager.listAllDataCatalog();
        List<DataDictionary> dictionarys = dataDictionaryManager.getWholeDictionary();

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("catalog", catalogs);
        resData.addResponseData("dictionary", dictionarys);
        return resData;
    }

    /**
     * 将字典明细信息导入到Properties文件
     */
    @ApiOperation(value = "将字典明细信息导入到Properties文件", notes = "将字典明细信息导入到Properties文件")
    @GetMapping("/dictionaryprop")
    public ResponseEntity<byte[]> downloadProperties() throws IOException {
        List<DataDictionary> dictionarys = dataDictionaryManager.getWholeDictionary();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write("#dictionaryprop_zh_CN.Properties\r\n".getBytes());

        for (DataDictionary dict : dictionarys) {
            out.write((dict.getCatalogCode() + "." + dict.getDataCode() +
                "=" + dict.getDataValue() + "\r\n").getBytes());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "dictionaryprop_zh_CN.Properties");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<byte[]>(out.toByteArray(), headers, HttpStatus.CREATED);
    }
}
