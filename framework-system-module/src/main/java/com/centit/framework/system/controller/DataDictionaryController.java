package com.centit.framework.system.controller;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ObjectException;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.*;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.support.database.utils.PageDesc;
import com.centit.framework.model.basedata.OperationLog;
import com.centit.framework.system.po.DataCatalog;
import com.centit.framework.system.po.DataDictionary;
import com.centit.framework.system.po.DataDictionaryId;
import com.centit.framework.system.service.DataDictionaryManager;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
 */
@Controller
@RequestMapping("/dictionary")
public class DataDictionaryController extends BaseController {

    public static final String F = "F";
    public static final String S = "S";
    public static final String U = "U";
    public static final String T = "T";

    @Value("${sys.multi_lang}")
    private boolean multiLang;

    @Resource
    private DataDictionaryManager dataDictionaryManager;

    private String optId = "DICTSET";

    /**
     * 查询所有数据目录列表
     *
     * @param field    只需要的属性名
     * @param pageDesc 分页信息
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);

        List<DataCatalog> listObjects = dataDictionaryManager.listObjects(searchColumn, pageDesc);

        SimplePropertyPreFilter simplePropertyPreFilter = null;
        if (ArrayUtils.isNotEmpty(field)) {
            simplePropertyPreFilter = new SimplePropertyPreFilter(DataCatalog.class, field);
        }

        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response, simplePropertyPreFilter);

            return;
        }

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, DictionaryMapUtils.objectsToJSONArray(listObjects));
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response, simplePropertyPreFilter);
    }

    /**
     * 查询单个数据目录
     *
     * @param catalogCode DataCatalog主键
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{catalogCode}", method = {RequestMethod.GET})
    public void getCatalog(@PathVariable String catalogCode, HttpServletResponse response) {

        DataCatalog dbDataCatalog = dataDictionaryManager.getCatalogIncludeDataPiece(catalogCode);

        JsonResultUtils.writeSingleDataJson(dbDataCatalog, response);
    }

    /**
     * catalogCode是否已存在
     *
     * @param catalogCode catalogCode
     * @param response {@link HttpServletResponse}
     */
    @RequestMapping(value = "/notexists/{catalogCode}", method = {RequestMethod.GET})
    public void isNotExistsCatalogCode(@PathVariable String catalogCode, HttpServletResponse response) {
        DataCatalog dbDataCatalog = dataDictionaryManager.getObjectById(catalogCode);
        JsonResultUtils.writeOriginalObject(null == dbDataCatalog, response);
    }

    /**
     * dataCode是否已存在
     *
     * @param catalogCode catalogCode
     * @param dataCode dataCode
     * @param response {@link HttpServletResponse}
     */
    @RequestMapping(value = "/notexists/dictionary/{catalogCode}/{dataCode}", method = {RequestMethod.GET})
    public void isNotExistsDataCode(@PathVariable String catalogCode, @PathVariable String dataCode,
                                    HttpServletResponse response) {
        DataDictionary dbDataDictionary = dataDictionaryManager.getDataDictionaryPiece(
                new DataDictionaryId(catalogCode, dataCode));

        JsonResultUtils.writeOriginalObject(null == dbDataDictionary, response);
    }

    /**
     * 新增数据目录
     *
     * @param dataCatalog {@link DataCatalog}
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createCatalog(@Valid DataCatalog dataCatalog, HttpServletRequest request,HttpServletResponse response) {
        if(isLoginAsAdmin(request)){
            dataCatalog.setCatalogStyle("S");
        }else{
            dataCatalog.setCatalogStyle("U");
        }
        dataDictionaryManager.saveNewObject(dataCatalog);
        JsonResultUtils.writeBlankJson(response);

        /*******************log****************************/
        OperationLogCenter.logNewObject(request, optId, dataCatalog.getCatalogCode(), OperationLog.P_OPT_LOG_METHOD_C,
                "新增数据字典目录", dataCatalog);
        /*******************log****************************/
    }

    /**
     * 更新数据目录
     *
     * @param catalogCode DataCatalog主键
     * @param dataCatalog {@link DataCatalog}
     * @param request    {@link HttpServletRequest}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{catalogCode}", method = {RequestMethod.PUT})
    public void updateCatalog(@PathVariable String catalogCode, @Valid DataCatalog dataCatalog,
                              HttpServletRequest request,HttpServletResponse response) {

        DataCatalog dbDataCatalog = dataDictionaryManager.getObjectById(catalogCode);

        if (null == dbDataCatalog) {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        DataCatalog oldValue = new DataCatalog();
        BeanUtils.copyProperties(dbDataCatalog, oldValue);

        BeanUtils.copyProperties(dataCatalog, dbDataCatalog, "catalogStyle","catalogCode", "dataDictionaries");
//        boolean isAdmin = isLoginAsAdmin(request);
//        String datastyle = isAdmin?"S":"U";
//        for(DataDictionary d : dataCatalog.getDataDictionaries()){
//            d.setDataStyle(datastyle);
//        }
//        dbDataCatalog.addAllDataPiece(dataCatalog.getDataDictionaries());

//        dataDictionaryManager.saveCatalogIncludeDataPiece(dbDataCatalog,isAdmin);
        dataDictionaryManager.updateCatalog(dbDataCatalog);

        JsonResultUtils.writeBlankJson(response);

        /***********************log*****************************/
        OperationLogCenter.logUpdateObject(request, optId, catalogCode, OperationLog.P_OPT_LOG_METHOD_U,
                "更新数据字典目录", dbDataCatalog, oldValue);
        /***********************log*****************************/
    }

    private boolean isLoginAsAdmin(HttpServletRequest request){
         Object obj = request.getSession().getAttribute(MainFrameController.ENTRANCE_TYPE);
         return obj!=null && MainFrameController.DEPLOY_LOGIN.equals(obj.toString());
    }

    /**
     * 更新数据目录明细
     *
     * @param catalogCode DataCatalog主键
     * @param dataCatalog {@link DataCatalog}
     * @param request    {@link HttpServletRequest}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "update/{catalogCode}", method = {RequestMethod.PUT})
    public void updateDictionary(@PathVariable String catalogCode, @Valid DataCatalog dataCatalog,
                              HttpServletRequest request,HttpServletResponse response) {

        DataCatalog dbDataCatalog = dataDictionaryManager.getObjectById(catalogCode);

        if (null == dbDataCatalog) {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        DataCatalog oldValue = new DataCatalog();
        BeanUtils.copyProperties(dbDataCatalog, oldValue);

        boolean isAdmin = isLoginAsAdmin(request);
        String datastyle = isAdmin?"S":"U";
        for(DataDictionary d : dataCatalog.getDataDictionaries()){
            d.setDataStyle(datastyle);
        }
        dbDataCatalog.addAllDataPiece(dataCatalog.getDataDictionaries());

        List<DataDictionary> oldDictionaries = dataDictionaryManager.saveCatalogIncludeDataPiece(dbDataCatalog,isAdmin);
        oldValue.setDataDictionaries(oldDictionaries);

        JsonResultUtils.writeBlankJson(response);

        /***********************log*****************************/
        OperationLogCenter.logUpdateObject(request, optId, catalogCode, OperationLog.P_OPT_LOG_METHOD_U,
                "更新数据字典明细", dbDataCatalog, oldValue);
        /***********************log*****************************/
    }

    /**
     * 新增或保存数据字典
     *
     * @param catalogCode    DataCatalog主键
     * @param dataCode       DataDictionary主键
     * @param dataDictionary {@link DataDictionary}
     * @param request {@link HttpServletRequest}
     * @param response       {@link HttpServletResponse}
     */
    @RequestMapping(value = "/dictionary/{catalogCode}/{dataCode}",
            method = {RequestMethod.POST, RequestMethod.PUT})
    public void editDictionary(@PathVariable String catalogCode, @PathVariable String dataCode,
                               @Valid DataDictionary dataDictionary,
                               HttpServletRequest request,HttpServletResponse response) {

        DataDictionary dbDataDictionary = dataDictionaryManager.getDataDictionaryPiece(new DataDictionaryId(catalogCode,
                dataCode));

        DataDictionary oldValue = new DataDictionary();
        oldValue.copy(dbDataDictionary);

        DataCatalog dbDataCatalog = dataDictionaryManager.getObjectById(catalogCode);

        dictionaryPreHander(dbDataCatalog, dataDictionary);

        if (null != dbDataDictionary) { // update
            dictionaryPreUpdateHander(dbDataCatalog, dbDataDictionary,request);
            BeanUtils.copyProperties(dataDictionary, dbDataDictionary, "id","dataStyle");
            dictionaryPreUpdateHander(dbDataCatalog, dbDataDictionary,request);
            dataDictionaryManager.saveDataDictionaryPiece(dbDataDictionary);
            /**************************log***************************/
            OperationLogCenter.logUpdateObject(request, optId, catalogCode+"-"+dataCode, OperationLog.P_OPT_LOG_METHOD_U,
                    "更新数据字典明细", dbDataDictionary, oldValue);
            /**************************log***************************/
        } else { // insert
            dictionaryPreInsertHander(dbDataCatalog, dataDictionary,request);
            dataDictionaryManager.saveDataDictionaryPiece(dataDictionary);
            /**************************log***************************/
            OperationLogCenter.logNewObject(request, optId, catalogCode+"-"+dataCode, OperationLog.P_OPT_LOG_METHOD_C,
                    "新增数据字典明细", dataDictionary);
            /**************************log***************************/
        }

        JsonResultUtils.writeBlankJson(response);
    }

  /**
     * 数据字典公共验证
     * @param dataCatalog DataCatalog
     * @param dataDictionary DataDictionary
     */
    protected void dictionaryPreHander(DataCatalog dataCatalog, DataDictionary dataDictionary) {
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

            DataDictionary dd = dataDictionaryManager.getDataDictionaryPiece(new DataDictionaryId(dataDictionary.getCatalogCode(), extraCode));
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
     * @param request HttpServletRequest
     */
    protected void dictionaryPreInsertHander(DataCatalog dataCatalog, DataDictionary dataDictionary,
            HttpServletRequest request) {
        if(isLoginAsAdmin(request)){
            dataDictionary.setDataStyle(S);
        }else{
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
     * @param request HttpServletRequest
     * @param dataCatalog    DataCatalog
     * @param dataDictionary DataDictionary
     */
    protected void dictionaryPreDeleteHander(DataCatalog dataCatalog, DataDictionary dataDictionary,
            HttpServletRequest request) {
        if(isLoginAsAdmin(request)){
          if (!S.equalsIgnoreCase(dataDictionary.getDataStyle()) && !U.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException("只能删除 catalogStyle为 S 或 U 的数据目录");
            }
        }else{
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
     * @param request HttpServletRequest
     */
    protected void dictionaryPreUpdateHander(DataCatalog dataCatalog, DataDictionary dataDictionary,
            HttpServletRequest request) {
        if(isLoginAsAdmin(request)){

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
        }else {
            if (!U.equalsIgnoreCase(dataDictionary.getDataStyle())) {
                throw new ObjectException("dataStyle 字段只可填写 U");
            }
        }
    }

    protected void catalogPrDeleteHander(DataCatalog dataCatalog,HttpServletRequest request) {
        if(isLoginAsAdmin(request)){
            if (!S.equalsIgnoreCase(dataCatalog.getCatalogStyle()) && !U.equalsIgnoreCase(dataCatalog.getCatalogStyle())) {
                throw new ObjectException("只能删除 catalogStyle为 S 或 U 的数据目录");
            }
        }else{
              if (!U.equalsIgnoreCase(dataCatalog.getCatalogStyle())) {
                throw new ObjectException("只可删除 catalogStyle 为 U 的数据目录");
              }
        }
    }

    /**
     * 删除数据目录
     *
     * @param catalogCode DataCatalog主键
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{catalogCode}", method = RequestMethod.DELETE)
    public void deleteCatalog(@PathVariable String catalogCode,
            HttpServletRequest request,HttpServletResponse response) {
        DataCatalog dataCatalog = dataDictionaryManager.getObjectById(catalogCode);
        catalogPrDeleteHander(dataCatalog,request);

        dataDictionaryManager.deleteDataDictionary(catalogCode);
        JsonResultUtils.writeBlankJson(response);

        /*****************log************************/
        OperationLogCenter.logDeleteObject(request, optId, catalogCode, OperationLog.P_OPT_LOG_METHOD_D,
                "删除数据字典目录", dataCatalog);
        /*****************log************************/
    }

    /**
     * 删除数据字典
     *
     * @param catalogCode DataCatalog主键
     * @param dataCode  dataCode
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @RequestMapping(value = "/dictionary/{catalogCode}/{dataCode}", method = RequestMethod.DELETE)
    public void deleteDictionary(@PathVariable String catalogCode, @PathVariable String dataCode,
            HttpServletRequest request,HttpServletResponse response) {
        DataCatalog dataCatalog = dataDictionaryManager.getObjectById(catalogCode);
        DataDictionary dataDictionary = dataDictionaryManager.getDataDictionaryPiece(new DataDictionaryId(catalogCode, dataCode));

        dictionaryPreDeleteHander(dataCatalog, dataDictionary,request);

        dataDictionaryManager.deleteDataDictionaryPiece(dataDictionary.getId());

        JsonResultUtils.writeBlankJson(response);

        /*****************log************************/
        OperationLogCenter.logDeleteObject(request, optId, catalogCode+"-"+dataCode, OperationLog.P_OPT_LOG_METHOD_D,
                "删除数据字典明细", dataDictionary);
        /*****************log************************/
    }


    @RequestMapping(value = "/dictionaryPiece/{catalogCode}", method = {RequestMethod.GET})
    public void getDataDictionary(@PathVariable String catalogCode, HttpServletResponse response) {
        List<DataDictionary> datas = dataDictionaryManager.getDataDictionary(catalogCode);
        JsonResultUtils.writeSingleDataJson(datas, response);
    }

    @RequestMapping(value = "/editDictionary/{catalogCode}", method = {RequestMethod.GET})
    public void getDataDictionaryDetail(@PathVariable String catalogCode, HttpServletResponse response) {
        List<DataDictionary> datas = dataDictionaryManager.getDataDictionary(catalogCode);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("dataDictionary", datas);
        resData.addResponseData("multiLang", multiLang);
        resData.addResponseData("langs", CodeRepositoryUtil.getLabelValueMap("SUPPORT_LANG"));
        JsonResultUtils.writeSingleDataJson(datas, response);
    }

    @RequestMapping(value = "/allCatalog", method = {RequestMethod.GET})
    public void getAllCatalog(HttpServletResponse response) {
        List<DataCatalog> catalogs = dataDictionaryManager.listAllDataCatalog();
        JsonResultUtils.writeSingleDataJson(catalogs, response);
    }

    @RequestMapping(value = "/wholeDictionary", method = {RequestMethod.GET})
    public void getWholeDictionary(HttpServletResponse response) {
        List<DataCatalog> catalogs = dataDictionaryManager.listAllDataCatalog();
        List<DataDictionary> dictionarys = dataDictionaryManager.getWholeDictionary();

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("catalog", catalogs);
        resData.addResponseData("dictionary", dictionarys);
        JsonResultUtils.writeResponseDataAsJson(resData,response);
    }

    @RequestMapping("/dictionaryprop")
    public ResponseEntity<byte[]> downloadProperties() throws IOException{
        List<DataDictionary> dictionarys = dataDictionaryManager.getWholeDictionary();
        ByteArrayOutputStream out = new  ByteArrayOutputStream();
        out.write("#dictionaryprop_zh_CN.Properties\r\n".getBytes());

        for(DataDictionary dict : dictionarys){
            out.write((dict.getCatalogCode()+"."+dict.getDataCode()+
                     "=" +dict.getDataValue() +"\r\n") .getBytes());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "dictionaryprop_zh_CN.Properties");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<byte[]>(out.toByteArray(),
                                          headers, HttpStatus.CREATED);
    }
}
