package com.centit.framework.users.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.users.config.AppConfig;
import com.centit.framework.users.dingtalk.DingTalkEncryptException;
import com.centit.framework.users.dingtalk.DingTalkEncryptor;
import com.centit.framework.users.po.DingTalkSuite;
import com.centit.framework.users.service.DingTalkSuiteService;
import com.centit.framework.users.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zfg
 */
@Controller
@RequestMapping("/callback")
public class CallBackController extends BaseController {

    @Autowired
    private DingTalkSuiteService dingTalkSuiteService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AppConfig appConfig;

    @PostMapping(value = "/dingCallback")
    @ResponseBody
    public Object dingCallback(@RequestParam(value = "suitetbId") String suitetbId,
                               @RequestParam(value = "signature") String signature,
                               @RequestParam(value = "timestamp") Long timestamp,
                               @RequestParam(value = "nonce") String nonce,
                               @RequestBody(required = false) JSONObject json,
                               HttpServletRequest request) {
        try {
            String encrypt = json.getString("encrypt");
            String plainText = decodeEncrypt(suitetbId, signature, timestamp.toString(), nonce, encrypt);
            logger.info("plainText内容: " + plainText);
            JSONObject callBackContent = JSON.parseObject(plainText);

            // 根据回调事件类型做不同的业务处理
            String eventType = callBackContent.getString("EventType");
            String suiteTicket = callBackContent.getString("SuiteTicket");
            logger.info("suiteTicket内容: " + suiteTicket);
            String res = "success";
            switch (eventType) {
                case "suite_ticket":
                    // suite_ticket用于用签名形式生成accessToken(访问钉钉服务端的凭证)，需要保存到应用的db。
                    // 钉钉会定期向本callback url推送suite_ticket新值用以提升安全性。
                    // 应用在获取到新的时值时，保存db成功后，返回给钉钉success加密串（如本demo的return）
                    logger.info("应用suite_ticket数据推送: " + plainText);
                    suiteTicket(callBackContent, suitetbId);
                    break;
                case "check_create_suite_url":
                    logger.info("验证新创建的回调URL有效性: " + plainText);
                    createSuiteCheck(callBackContent);
                    break;
                case "check_update_suite_url":
                    logger.info("验证更新回调URL有效性: " + plainText);
                    updateSuiteCheck(callBackContent);
                    break;
                case "tmp_auth_code":
                    // 本事件应用应该异步进行授权开通企业的初始化，目的是尽最大努力快速返回给钉钉服务端。用以提升企业管理员开通应用体验
                    // 即使本接口没有收到数据或者收到事件后处理初始化失败都可以后续再用户试用应用时从前端获取到corpId并拉取授权企业信息，进而初始化开通及企业。
                    logger.info("企业授权开通应用事件: " + plainText);
                    //tempAuthCode(callBackContent, suitetbId);
                    break;
                default:
                    break;
            }
            // 返回success的加密信息表示回调处理成功
            return codeEncrypt(suitetbId, res, timestamp.toString(), nonce);
        } catch (Exception e) {
            //失败的情况，应用的开发者应该通过告警感知，并干预修复
            logger.error("process callback fail.", e);
            return "fail";
        }
    }

    private void suiteTicket(JSONObject decodeEncryptJson, String suitetbId) {
        try {
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("suiteid", suitetbId);
            //查询钉钉套件suite信息
            DingTalkSuite suite = dingTalkSuiteService.getDingTalkSuiteByProperty(paramsMap);
            if (null != suite) {
                String suiteTicket = decodeEncryptJson.getString("SuiteTicket");
                //通过suiteTicket获取之后需要换取suiteToken
                //JSONObject suiteToken = DingTalkUtil.getSuiteAccessToken(suite.getSuiteKey(), suite.getSuiteSecret(), suiteTicket);
                suite.setSuitTicket(suiteTicket);
                //suite.setSuiteAccessToken(suiteToken.getString("suite_access_token"));
                //有效期7200秒，每20分钟（1200秒）推送一次就会更新一次（更新时存储数据库），所以不需要额外定时获取
                dingTalkSuiteService.updateDingTalkSuiteInfo(suite);
            }
        } catch (Exception e) {
            logger.error("异常:", e);
        }
    }

    /**
     * encrypt解密
     *
     * @param suitetbId
     * @param msgSignature
     * @param timeStamp
     * @param nonce
     * @param encrypt
     * @return
     */
    private String decodeEncrypt(String suitetbId, String msgSignature, String timeStamp, String nonce, String encrypt) {
        String decodeEncrypt = null;
        try {
            //encrypt解密
            DingTalkEncryptor dingTalkEncryptor = createDingTalkEncryptor(suitetbId);
            if (null != dingTalkEncryptor) {
                decodeEncrypt = dingTalkEncryptor.getDecryptMsg(msgSignature, timeStamp, nonce, encrypt);
            }
        } catch (DingTalkEncryptException e) {
            logger.error("异常:", e);
        }
        return decodeEncrypt;
    }

    /**
     * 对返回信息进行加密
     *
     * @param suitetbId
     * @param res
     * @param timeStamp
     * @param nonce
     * @return
     */
    private JSONObject codeEncrypt(String suitetbId, String res, String timeStamp, String nonce) {
        long timeStampLong = Long.parseLong(timeStamp);
        Map<String, String> jsonMap = null;
        try {
            //jsonMap是需要返回给钉钉服务器的加密数据包
            DingTalkEncryptor dingTalkEncryptor = createDingTalkEncryptor(suitetbId);
            if (null != dingTalkEncryptor) {
                jsonMap = dingTalkEncryptor.getEncryptedMap(res, timeStampLong, nonce);
            }
        } catch (DingTalkEncryptException e) {
            logger.error("异常:", e);
        }
        JSONObject json = new JSONObject();
        json.putAll(jsonMap);
        return json;
    }

    /**
     * 创建加密/解密 类
     *
     * @param suitetbId
     * @return
     */
    private DingTalkEncryptor createDingTalkEncryptor(String suitetbId) {
        DingTalkEncryptor dingTalkEncryptor = null;  //加密方法类
        try {
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("suiteid", suitetbId);
            //查询钉钉套件suite信息
            DingTalkSuite suite = dingTalkSuiteService.getDingTalkSuiteByProperty(paramsMap);
            String suiteKey = "";
            if (null != suite) {
                //套件注册成功后生成的suite_key
                suiteKey = suite.getSuiteKey();
                if (!"".equals(suiteKey) && suiteKey != null) { //当suite_key存在时，即套件创建成功
                    dingTalkEncryptor = new DingTalkEncryptor(suite.getToken(), suite.getEncodingAesKey(), suiteKey);  //创建加解密类
                } else {  //当suite_key不存在时，第一次创建套件
                    dingTalkEncryptor = new DingTalkEncryptor(suite.getToken(), suite.getEncodingAesKey(),
                        appConfig.getAppKey());  //创建加解密类
                }
            }

        } catch (DingTalkEncryptException e) {
            logger.error("异常:", e);
        }
        return dingTalkEncryptor;
    }

    private String createSuiteCheck(JSONObject decodeEncryptJson) {
        //此事件需要返回的"Random"字段，
        String res = decodeEncryptJson.getString("Random");
        String testSuiteKey = decodeEncryptJson.getString("TestSuiteKey");
        return res;
    }

    private String updateSuiteCheck(JSONObject decodeEncryptJson) {
        //此事件需要返回的"Random"字段，
        return decodeEncryptJson.getString("Random");
    }
}
