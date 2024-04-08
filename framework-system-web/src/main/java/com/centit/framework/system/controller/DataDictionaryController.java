package com.centit.framework.system.controller;

import com.centit.fileserver.utils.UploadDownloadUtils;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.basedata.DataCatalog;
import com.centit.framework.model.basedata.DataDictionary;
import com.centit.framework.model.basedata.DataDictionaryId;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.service.DataDictionaryManager;
import com.centit.framework.system.service.impl.DBPlatformEnvironment;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.JavaBeanMetaData;
import com.centit.support.common.ObjectException;
import com.centit.support.common.ParamName;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.report.ExcelImportUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    @Autowired
    private DataDictionaryManager dataDictionaryManager;

    //private String optId = "DICTIONARY";
    public String getOptId() {
        return "DICTIONARY";
    }

    /**
     * 查询所有字典目录列表
     *
     * @param field    指需要的属性名
     * @param pageDesc 分页信息
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return PageQueryResult
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
    public PageQueryResult<Object> list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        if (WebOptUtils.isTenantTopUnit(request)) {
            searchColumn.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        }
        List<DataCatalog> listObjects = dataDictionaryManager.listObjects(searchColumn, pageDesc);

        return PageQueryResult.createJSONArrayResult(
            dataDictionaryManager.appendRelativeOsInfo(listObjects), pageDesc, field);
    }

    /**
     * 查询单个字典目录
     *
     * @param catalogCode DataCatalog主键
     * @return DataCatalog
     */
    @ApiOperation(value = "查询单个字典目录", notes = "根据数据字典类别代码查询单个字典目录。")
    @ApiImplicitParam(
        name = "catalogCode", value = "数据字典的类别代码",
        required = true, paramType = "path", dataType = "String"
    )
    @RequestMapping(value = "/{catalogCode}", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public DataCatalog getCatalog(@PathVariable String catalogCode) {
        return dataDictionaryManager.getCatalogIncludeDataPiece(catalogCode);
    }

    /**
     * catalogCode是否不存在
     *
     * @param catalogCode catalogCode
     * @return result
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
     * @return result
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
     * @return result
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

    @ApiOperation(value = "新增字典类别", notes = "新增字典类别")
    @ApiParam(name = "dataCatalog", value = "新增字典类别,输入框有提示的都是必填项", required = true)
    @RequestMapping(method = {RequestMethod.POST})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增字典类别",
        tag = "{loginUser.userCode}")
    @WrapUpResponseBody
    public void createCatalog(@Valid DataCatalog dataCatalog, HttpServletRequest request, HttpServletResponse response) {
        boolean isAdmin = isLoginAsAdmin(request);
        if (isAdmin) {
            dataCatalog.setCatalogStyle("S");
        } else {
            dataCatalog.setCatalogStyle("U");
        }
        dataDictionaryManager.saveNewObject(dataCatalog);
        if (dataCatalog.getDataDictionaries() != null && dataCatalog.getDataDictionaries().size() > 0) {
            for (DataDictionary d : dataCatalog.getDataDictionaries()) {
                d.setDataValue(StringEscapeUtils.unescapeHtml4(d.getDataValue()));
                d.setDataDesc(StringEscapeUtils.unescapeHtml4(d.getDataDesc()));
                if (StringUtils.isBlank(d.getDataStyle())) {
                    d.setDataStyle(isAdmin ? "S" : "U");
                }
                d.setCatalogCode(dataCatalog.getCatalogCode());
            }
            dataDictionaryManager.saveCatalogIncludeDataPiece(dataCatalog);
        }
        JsonResultUtils.writeSingleDataJson(dataCatalog.getCatalogCode(), response);
    }

    /**
     * 更新字典类别
     *
     * @param catalogCode DataCatalog主键
     * @param dataCatalog {@link DataCatalog}
     * @return result
     */
    @ApiOperation(value = "更新字典类别", notes = "更新字典类别信息")
    @ApiImplicitParam(
        name = "catalogCode", value = "数据字典的类别代码",
        required = true, paramType = "path", dataType = "String"
    )
    @ApiParam(name = "dataCatalog", value = "更新字典类别新的对象信息", required = true)
    @RequestMapping(value = "/{catalogCode}", method = {RequestMethod.PUT})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新字典类别",
        tag = "{catalogCode}")
    @WrapUpResponseBody
    public ResponseData updateCatalog(@ParamName("catalogCode") @PathVariable String catalogCode,
                                      @Valid DataCatalog dataCatalog, HttpServletRequest request) {
        DataCatalog dbDataCatalog = dataDictionaryManager.getObjectById(catalogCode);
        if (null == dbDataCatalog) {
            return ResponseData.makeErrorMessage(604,
                getI18nMessage("error.604.object_not_found", request,
                    "DataCatalog", catalogCode));
        }

        DataCatalog oldValue = new DataCatalog();
        BeanUtils.copyProperties(dbDataCatalog, oldValue);
        BeanUtils.copyProperties(dataCatalog, dbDataCatalog, "catalogStyle", "catalogCode", "dataDictionaries");
        dataDictionaryManager.updateCatalog(dbDataCatalog);
        return ResponseData.successResponse;
    }

    private boolean isLoginAsAdmin(HttpServletRequest request) {
        return WebOptUtils.getCurrentTopUnit(request).equals(DBPlatformEnvironment.SYSTEM);
    }

    /**
     * 更新字典目录及明细
     *
     * @param catalogCode DataCatalog主键
     * @param dataCatalog {@link DataCatalog}
     * @param request     {@link HttpServletRequest}
     * @return result
     */
    @ApiOperation(value = "更新字典目录明细", notes = "更新字典目录明细，目录明显对象就是dataDictionaries[]里面的属性组成")
    @ApiImplicitParam(
        name = "catalogCode", value = "数据字典的类别代码",
        required = true, paramType = "path", dataType = "String"
    )
    @ApiParam(name = "dataCatalog", value = "更新字典类别新的对象信息", required = true)
    @RequestMapping(value = "/update/{catalogCode}", method = {RequestMethod.PUT})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新字典目录明细",
        tag = "{catalogCode}")
    @WrapUpResponseBody
    public ResponseData updateDictionary(@ParamName("catalogCode") @PathVariable String catalogCode, @Valid DataCatalog dataCatalog,
                                         HttpServletRequest request) {
        DataCatalog dbDataCatalog = dataDictionaryManager.getObjectById(catalogCode);
        if (null == dbDataCatalog) {
            return ResponseData.makeErrorMessage(604,
                getI18nMessage("error.604.object_not_found", request,
                    "DataCatalog", catalogCode));
        }
        boolean isAdmin = isLoginAsAdmin(request);
        String dataStyle = isAdmin ? "S" : "U";
        for (DataDictionary d : dataCatalog.getDataDictionaries()) {
            d.setDataValue(StringEscapeUtils.unescapeHtml4(d.getDataValue()));
            d.setDataDesc(StringEscapeUtils.unescapeHtml4(d.getDataDesc()));
            if (StringUtils.isBlank(d.getDataStyle())) {
                d.setDataStyle(dataStyle);
            }
        }
        dbDataCatalog.addAllDataPiece(dataCatalog.getDataDictionaries());
        dataDictionaryManager.updateCatalog(dataCatalog);
        dataDictionaryManager.saveCatalogIncludeDataPiece(dbDataCatalog);
        return ResponseData.successResponse;
    }

    /*
     * 新增数据字典
     *
     * @param catalogCode    DataCatalog主键
     * @param dataDictionary {@link DataDictionary}
     * @param request        {@link HttpServletRequest}
     * @return result
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
    @RequestMapping(value = "/dictionaryPiece/{catalogCode}/{dataCode}", method = {RequestMethod.POST})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增数据字典",
        tag = "{catalogCode}:{dataCode}")
    @WrapUpResponseBody
    public DataDictionary createDictionary(@ParamName("catalogCode") @PathVariable String catalogCode,
                                         @PathVariable String dataCode,
                                         @Valid DataDictionary dataDictionary,
                                         HttpServletRequest request) {
        DataCatalog dbDataCatalog = dataDictionaryManager.getObjectById(catalogCode);
        dataDictionary.setCatalogCode(catalogCode);
        dataDictionary.setDataCode(dataCode);
        dataDictionary.setDataValue(StringEscapeUtils.unescapeHtml4(dataDictionary.getDataValue()));
        dataDictionary.setDataDesc(StringEscapeUtils.unescapeHtml4(dataDictionary.getDataDesc()));
        dictionaryPreHandler(dbDataCatalog, dataDictionary, request);
        dictionaryPreInsertHandler(dbDataCatalog, dataDictionary, request);
        dataDictionaryManager.saveDataDictionaryPiece(dataDictionary);
        return dataDictionary;
    }

    /*
     * 更新数据字典
     *
     * @param catalogCode    DataCatalog主键
     * @param dataDictionary {@link DataDictionary}
     * @param request        {@link HttpServletRequest}
     * @return result
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
    @RequestMapping(value = "/dictionaryPiece/{catalogCode}/{dataCode}", method = {RequestMethod.PUT})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新数据字典",
        tag = "{catalogCode}:{dataCode}")
    @WrapUpResponseBody
    public DataDictionary editDictionary(@ParamName("catalogCode") @PathVariable String catalogCode,
                                       @PathVariable String dataCode,
                                       @Valid DataDictionary dataDictionary,
                                       HttpServletRequest request) {

        DataDictionary dbDataDictionary = dataDictionaryManager.getDataDictionaryPiece(new DataDictionaryId(catalogCode,
           dataCode));
        DataCatalog dbDataCatalog = dataDictionaryManager.getObjectById(catalogCode);
        dataDictionary.setDataCode(dataCode);
        dataDictionary.setCatalogCode(catalogCode);
        dataDictionary.setDataValue(StringEscapeUtils.unescapeHtml4(dataDictionary.getDataValue()));
        dataDictionary.setDataDesc(StringEscapeUtils.unescapeHtml4(dataDictionary.getDataDesc()));
        dictionaryPreHandler(dbDataCatalog, dataDictionary, request);
        dictionaryPreUpdateHandler(dbDataCatalog, dbDataDictionary, request);
        BeanUtils.copyProperties(dataDictionary, dbDataDictionary, "id", "dataStyle");
        dictionaryPreUpdateHandler(dbDataCatalog, dbDataDictionary, request);
        dataDictionaryManager.saveDataDictionaryPiece(dbDataDictionary);
        return dbDataDictionary;
    }

    /**
     * 数据字典公共验证
     *
     * @param dataCatalog    DataCatalog
     * @param dataDictionary DataDictionary
     */
    private void dictionaryPreHandler(DataCatalog dataCatalog, DataDictionary dataDictionary, HttpServletRequest request) {
        //附加代码 EXTRACODE  字段
        //这是一个自解释字段，业务系统可以自行解释这个字段的意义，单作为树形结构的数据字典时，这个字段必需为上级字典的代码。
        if (T.equalsIgnoreCase(dataCatalog.getCatalogType()) && !StringBaseOpt.isNvl(dataDictionary.getExtraCode())) {
            String extraCode = dataDictionary.getExtraCode();
            if (dataDictionary.getDataCode().equals(extraCode)) {
                throw new ObjectException(dataDictionary, 701,
                    getI18nMessage("error.701.cycle_in_tree", request));
            }
            DataDictionary dd = dataDictionaryManager.getDataDictionaryPiece(
                new DataDictionaryId(dataDictionary.getCatalogCode(), extraCode));
            if (null == dd) {
                throw new ObjectException(dataDictionary, 604,
                    getI18nMessage("error.604.object_not_found", request, "DataDictionary", dd.getDataCode()));
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
    private void dictionaryPreInsertHandler(DataCatalog dataCatalog, DataDictionary dataDictionary,
                                            HttpServletRequest request) {
        if (isLoginAsAdmin(request)) {
            dataDictionary.setDataStyle(S);
        } else {
            if (!S.equalsIgnoreCase(dataCatalog.getCatalogStyle()) && !U.equalsIgnoreCase(dataCatalog.getCatalogStyle())) {
                throw new ObjectException(dataCatalog, 701,
                    getI18nMessage("error.701.field_must_be", request, "catalogStyle", "[SU]"));
            }
            dataDictionary.setDataStyle(U);
            /*if (!U.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException(dataDictionary, 701,
                    getI18nMessage("error.701.field_must_be", request, "dataStyle", "U"));
            }*/
        }
    }

    /**
     * 数据字典的删除权限进行业务数据判断
     *
     * @param request        HttpServletRequest
     * @param dataDictionary DataDictionary
     */
    private void dictionaryPreDeleteHandler(DataDictionary dataDictionary,
                                            HttpServletRequest request) {
        if (isLoginAsAdmin(request)) {
            if (!S.equalsIgnoreCase(dataDictionary.getDataStyle()) && !U.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException(dataDictionary, ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    getI18nMessage("error.701.field_must_be", request, "catalogStyle", "[SU]"));
            }
        } else {
            if (!U.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException(dataDictionary, 701,
                    getI18nMessage("error.701.field_must_be", request, "dataStyle", "U"));
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
        if (isLoginAsAdmin(request)) { //F,S,U
            if (F.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException(dataDictionary, 701,
                    getI18nMessage("error.701.field_must_be", request, "dataStyle", "[SU]"));
                //"dataStyle 为 F 类型的数据字典，任何地方都不允许编辑，只能有开发人员给出更新脚本添加、更改和删除"
            }

            if (F.equalsIgnoreCase(dataCatalog.getCatalogStyle()) && !S.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException(dataDictionary, 701,
                    getI18nMessage("error.701.field_must_be", request, "dataStyle", "U"));
                //"只能修改 dataStyle 为 S 的数据字典");
            }
            if (!S.equalsIgnoreCase(dataCatalog.getCatalogStyle()) && !U.equalsIgnoreCase(dataCatalog.getCatalogStyle())) {
                throw new ObjectException(dataCatalog, 701,
                    getI18nMessage("error.701.field_must_be", request, "catalogStyle", "[SU]"));
                //"catalogStyle 字段只可填写 S 或 U");
            }
            if (!S.equalsIgnoreCase(dataDictionary.getDataStyle()) && !U.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException(dataDictionary, 701,
                    getI18nMessage("error.701.field_must_be", request, "dataStyle", "[SU]"));
                //"dataStyle 字段只可填写 S 或 U");
            }
        } else {
            if (!U.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException(dataDictionary, 701,
                    getI18nMessage("error.701.field_must_be", request, "dataStyle", "U"));
                //"dataStyle 字段只可填写 U");
            }
        }
    }

    private void catalogPrDeleteHandler(DataCatalog dataCatalog, HttpServletRequest request) {
        if (isLoginAsAdmin(request)) {
            if (!S.equalsIgnoreCase(dataCatalog.getCatalogStyle()) && !U.equalsIgnoreCase(dataCatalog.getCatalogStyle())) {
                throw new ObjectException(dataCatalog, 701,
                    getI18nMessage("error.701.field_must_be", request, "catalogStyle", "[SU]"));
                //"只能删除 catalogStyle为 S 或 U 的字典目录");
            }
        } else {
            if (!U.equalsIgnoreCase(dataCatalog.getCatalogStyle())) {
                throw new ObjectException(dataCatalog, 701,
                    getI18nMessage("error.701.field_must_be", request, "catalogStyle", "U"));
                //"只可删除 catalogStyle 为 U 的字典目录");
            }
        }
    }

    /**
     * 删除字典目录
     *
     * @param catalogCode DataCatalog主键
     * @param request     {@link HttpServletRequest}
     * @return result
     */
    @ApiOperation(value = "根据数据字典目录代码删除字典目录", notes = "根据数据字典目录代码删除字典目录。")
    @ApiImplicitParam(
        name = "catalogCode", value = "数据字典目录代码",
        required = true, paramType = "path", dataType = "String"
    )
    @RequestMapping(value = "/{catalogCode}", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除字典目录",
        tag = "{catalogCode}")
    @WrapUpResponseBody
    public ResponseData deleteCatalog(@ParamName("catalogCode") @PathVariable String catalogCode,
                                      HttpServletRequest request) {
        DataCatalog dataCatalog = dataDictionaryManager.getObjectById(catalogCode);
        catalogPrDeleteHandler(dataCatalog, request);
        dataDictionaryManager.deleteDataDictionary(catalogCode);
        return ResponseData.successResponse;

    }

    /**
     * 删除数据字典
     *
     * @param catalogCode DataCatalog主键
     * @param dataCode    dataCode
     * @param request     {@link HttpServletRequest}
     * @return result
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
    @RequestMapping(value = "/dictionaryPiece/{catalogCode}/{dataCode}", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除数据字典",
        tag = "{catalogCode}:{dataCode}")
    @WrapUpResponseBody
    public ResponseData deleteDictionary(@ParamName("catalogCode") @PathVariable String catalogCode,
                                         @ParamName("dataCode") @PathVariable String dataCode,
                                         HttpServletRequest request) {
        DataDictionary dataDictionary = dataDictionaryManager.getDataDictionaryPiece(new DataDictionaryId(catalogCode, dataCode));
        dictionaryPreDeleteHandler(dataDictionary, request);
        dataDictionaryManager.deleteDataDictionaryPiece(dataDictionary.getId());
        return ResponseData.successResponse;
    }

    /**
     * 获取字典的所有明细信息
     *
     * @param catalogCode 数据字典的类别代码
     * @return result
     */
    @ApiOperation(value = "获取字典的所有明细信息", notes = "根据字典类别代码获取字典的所有明细信息")
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

    /*
     * 获取字典的详细信息
     * @param catalogCode 数据字典的类别代码
     * @return result
     */
    @ApiOperation(value = "获取字典的详细信息", notes = "根据字典类别代码获取字典的详细信息")
    @ApiImplicitParam(
        name = "catalogCode", value = "数据字典的类别代码",
        required = true, paramType = "path", dataType = "String"
    )
    @RequestMapping(value = "/editDictionary/{catalogCode}", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData getDataDictionaryDetail(@PathVariable String catalogCode, HttpServletRequest request) {
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        searchColumn.put("catalogCode", catalogCode);
        List<DataDictionary> datas = dataDictionaryManager.listDataDictionarys(searchColumn);
        DataCatalog catalog = dataDictionaryManager.getObjectById(catalogCode);
        if ("T".equals(catalog.getCatalogType())) {
            CollectionsOpt.sortAsTree(datas,
                (p, c) -> StringUtils.equals(p.getDataCode(), c.getExtraCode()));
        }
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("dataDictionary", datas);
        resData.addResponseData("multiLang", multiLang);
        resData.addResponseData("langs", CodeRepositoryUtil.getLabelValueMap("SUPPORT_LANG"));
        return resData;
    }
    @ApiOperation(value = "获取字典的详细信息", notes = "根据字典类别代码获取字典的详细信息")
    @ApiImplicitParam(
        name = "catalogCode", value = "数据字典的类别代码",
        required = true, paramType = "path", dataType = "String"
    )
    @RequestMapping(value = "/dictionarys", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData getDataDictionarys(HttpServletRequest request) {
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        List<DataDictionary> datas = dataDictionaryManager.listDataDictionarys(searchColumn);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("dataDictionary", datas);
        resData.addResponseData("multiLang", multiLang);
        resData.addResponseData("langs", CodeRepositoryUtil.getLabelValueMap("SUPPORT_LANG"));
        return resData;
    }

    /* 获取所有字典目录信息
     * @param request 请求体
     * @return result
     */
    @ApiOperation(value = "获取所有字典目录信息", notes = "获取所有字典目录信息")
    @RequestMapping(value = "/allCatalog", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData getAllCatalog(HttpServletRequest request) {
        Map<String, Object> searchColumn = new HashMap<>();
        if (WebOptUtils.isTenantTopUnit(request)) {
            searchColumn.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        }
        List<DataCatalog> catalogs = dataDictionaryManager.listAllDataCatalog(searchColumn);
        return ResponseData.makeResponseData(catalogs);
    }

    /**
     * 获取所有字典目录信息及对应的所有字典明细信息
     *
     * @param request HttpServletResponse
     * @return result
     */
    @ApiOperation(value = "获取所有字典目录信息及对应的所有字典明细信息", notes = "获取所有字典目录信息及对应的所有字典明细信息")
    @RequestMapping(value = "/wholeDictionary", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData getWholeDictionary(HttpServletRequest request) {
        Map<String, Object> searchColumn = new HashMap<>();
        if (WebOptUtils.isTenantTopUnit(request)) {
            searchColumn.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        }
        List<DataCatalog> catalogs = dataDictionaryManager.listAllDataCatalog(searchColumn);
        List<String> catalogCodes = new ArrayList<>();
        catalogs.forEach(ca -> catalogCodes.add(ca.getCatalogCode()));
        List<DataDictionary> dictionarys = dataDictionaryManager.getWholeDictionary(catalogCodes);

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("catalog", catalogs);
        resData.addResponseData("dictionary", dictionarys);
        return resData;
    }

    /**
     * 将字典明细信息导入到Properties文件
     *
     * @param request 请求体
     * @return result
     * @throws IOException 异常
     */
    @ApiOperation(value = "将字典明细信息导入到Properties文件", notes = "将字典明细信息导入到Properties文件")
    @GetMapping("/dictionaryprop")
    @ResponseBody
    public ResponseEntity<byte[]> downloadProperties(HttpServletRequest request) throws IOException {
        Map<String, Object> searchColumn = new HashMap<>();
        if (WebOptUtils.isTenantTopUnit(request)) {
            searchColumn.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        }
        List<DataCatalog> catalogs = dataDictionaryManager.listAllDataCatalog(searchColumn);
        List<String> catalogCodes = new ArrayList<>();
        catalogs.forEach(ca -> catalogCodes.add(ca.getCatalogCode()));
        List<DataDictionary> dictionarys = dataDictionaryManager.getWholeDictionary(catalogCodes);
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

    @ApiOperation(value = "导入excel到数据字典明细")
    @CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 86400, methods = RequestMethod.POST)
    @RequestMapping(value = "/excelData/{catalogCode}", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public DataCatalog importDataFromExcel(@PathVariable String catalogCode,
                                           HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        try {
            InputStream fileInfo = UploadDownloadUtils.fetchInputStreamFromMultipartResolver(request).getRight();
            List<Map<String, Object>> excelList = ExcelImportUtil.loadMapFromExcelSheet(fileInfo, 0);
            List<DataDictionary> object = new ArrayList<>();
            JavaBeanMetaData javaBeanMetaData = JavaBeanMetaData.createBeanMetaDataFromType(DataDictionary.class);
            for (Map<String, Object> map : excelList) {
                DataDictionary dataDictionary = (DataDictionary) javaBeanMetaData.createBeanObjectFromMap(map);
                dataDictionary.setCatalogCode(catalogCode);
                dataDictionary.setDataStyle("U");
                dataDictionary.setDataTag("T");
                dataDictionary.setCreateDate(new Date());
                object.add(dataDictionary);
            }
            DataCatalog dataCatalog = dataDictionaryManager.getObjectById(catalogCode);
            if (dataCatalog == null) {
                return null;
            }
            dataCatalog.getDataDictionaries().addAll(object);
            dataDictionaryManager.saveCatalogIncludeDataPiece(dataCatalog);
            return dataCatalog;
        } catch (ObjectException | IllegalAccessException | InstantiationException e) {
            JsonResultUtils.writeMessageJson(e.getMessage(), response);
        }
        return null;
    }
}
