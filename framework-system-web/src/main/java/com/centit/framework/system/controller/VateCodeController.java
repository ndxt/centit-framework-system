package com.centit.framework.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.framework.system.dao.UserInfoDao;
import com.centit.framework.system.utils.VotaCode;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.security.SecurityOptUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证码接口
 * @author tian_y
 */
@Controller
@RequestMapping("/vateCode")
@Api(value = "邮箱、手机号验证码接口", tags = "邮箱、手机号验证码接口")
public class VateCodeController extends BaseController {

    @Value("${third.services.aliyun.access.key:}")
    private String accessKeyId;
    @Value("${third.services.aliyun.access.secret:}")
    private String accessKeySecret;

    @Autowired
    private NotificationCenter notificationCenter;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private PlatformEnvironment platformEnvironment;

    @Autowired(required = false)
    private RedisTemplate<String, JSONObject> redisTemplate;

    private static Pattern pattern = Pattern.compile("[0-9]*");

    private ConcurrentHashMap
            <String, VotaCode> votaCodeMap = new ConcurrentHashMap<>();

    private VotaCode fetchVotaCode(String key){
        if (redisTemplate == null){
            return votaCodeMap.get(key);
        } else {
            JSONObject jsonObject = redisTemplate.boundValueOps(key).get();
            return jsonObject.toJavaObject(VotaCode.class);
        }
    }

    private void saveVotaCode(String key, VotaCode votaCode){
        if (redisTemplate == null){
            votaCodeMap.put(key, votaCode);
        } else {
            redisTemplate.boundValueOps(key).set(JSONObject.from(votaCode));
        }
    }

    private void deleteVotaCode(String key){
        if (redisTemplate == null){
            votaCodeMap.remove(key);
        } else {
            redisTemplate.delete(key);
        }
    }

    @ApiOperation(
        value = "验证唯一性",
        notes = "验证唯一性"
    )
    @RequestMapping(value = "/checkOnly", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData checkOnly(@RequestParam(value = "loginname") String loginname,
                                  HttpServletRequest request) throws Exception{
        UserInfo userInfo;
        String msg = "";
        Matcher isNum = pattern.matcher(loginname);
        if(loginname.indexOf('@')>0){
            msg = "邮件";
            userInfo = userInfoDao.getUserByRegEmail(loginname);
        }else if(loginname.length() == 11 && isNum.matches()){
            msg = "手机号";
            userInfo = userInfoDao.getUserByRegCellPhone(loginname);
        }else{
            msg = "账号";
            userInfo = userInfoDao.getUserByLoginName(loginname);
        }
        if(userInfo != null){
            return ResponseData.makeErrorMessage("此" + msg + "已被使用！");
        }
        return ResponseData.successResponse;
    }

    @ApiOperation(
        value = "获取Email验证码",
        notes = "获取Email验证码"
    )
    @RequestMapping(value = "/getEmailCode", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData getEmailCode(@RequestParam("email") String email) {
        VotaCode votaCode = fetchVotaCode(email);
        if(votaCode != null){
            if ((System.currentTimeMillis() - votaCode.getCreateTime()) < 1000 * 60) {
                return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "验证码发送时间小于1分钟，请稍后再试。");
            }else{
                //重新发送则删除之前存入redis中的数据
                deleteVotaCode(email);
            }
        }
        UserInfo userInfo = userInfoDao.getUserByRegEmail(email);
        if (userInfo != null) {
            return ResponseData.makeErrorMessage("此邮箱已被使用！");
        }
        return sendEmail(userInfo.getUserCode(), email);
    }

    @ApiOperation(
        value = "获取手机验证码",
        notes = "获取手机验证码"
    )
    @RequestMapping(value = "/getPhoneCode", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData getPhoneCode(@RequestParam(value = "userCode", required = false) String userCode,
                                            @RequestParam("phone") String phone,
                                            HttpServletRequest request) throws Exception {
        VotaCode votaCode = fetchVotaCode(phone);
        if(votaCode != null){
            if ((System.currentTimeMillis() - votaCode.getCreateTime()) < 1000 * 60) {
                return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "验证码发送时间小于1分钟，请稍后再试。");
            }else{
                //重新发送则删除之前存入redis中的数据
                deleteVotaCode(phone);
            }
        }
        if(phone != null && !phone.equals("")){
            UserInfo userInfo = userInfoDao.getUserByRegCellPhone(phone);
            if (userInfo != null) {
                return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "此手机号已被使用");
            }
        }
        SendSmsResponseBody s = sendPhone(phone, userCode);
        if(s != null && s.getCode() != null && s.getCode().equals("OK")){
            s.setCode("0");
        }
        ResponseMapData result = ResponseData.makeResponseData(
            CollectionsOpt.createHashMap("x-auth-token", request.getSession().getId()));
        result.setCode(NumberBaseOpt.castObjectToInteger(s.getCode(), 500));
        result.setMessage(s.getMessage());
        return result;
    }

    /**
     * 校验验证码并且更新用户信息
     * @param userCode 用户编码
     * @param key 手机号或者邮箱
     * @param code 验证码
     * @param request
     * @return
     */
    @ApiOperation(
        value = "校验和更新",
        notes = "校验和更新"
    )
    @RequestMapping(value = "/checkCode", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData checkCode(@RequestParam(value = "userCode", required=false) String userCode,
                                       @RequestParam("key") String key,
                                       @RequestParam("code") String code,
                                       HttpServletRequest request){
        try {
            if (code == null) {
                return ResponseData.makeErrorMessage(500, "请输入验证码！");
            }
            //从Redis中获取验证码和部分信息
            VotaCode votaCode = fetchVotaCode(key);
            if(votaCode == null){
                return ResponseData.makeErrorMessage(500, "未发送验证码！");
            }
            if (!StringUtils.equals(votaCode.getVerifyCode(), (code))) {
                return ResponseData.makeErrorMessage(500, "验证码错误！");
            }
            if ((System.currentTimeMillis() - votaCode.getCreateTime()) > 1000 * 60 * 5) {
                return ResponseData.makeErrorMessage(500, "验证码已过期！");
            }
            if(StringUtils.isNotBlank(userCode)){
                UserInfo user = userInfoDao.getUserByCode(userCode);
                if (user != null) {
                    if(StringUtils.isNotBlank(votaCode.getEmail())){
                        user.setRegEmail(votaCode.getEmail());
                        logger.info("用户:{}修改用户信息邮箱",userCode);
                    }else if(StringUtils.isNotBlank(votaCode.getPhone())){
                        user.setRegCellPhone(votaCode.getPhone());
                        logger.info("用户:{}修改用户信息手机",userCode);
                    }
                    userInfoDao.updateUser(user);
                    //刷新缓存中的人员信息
                    reloadAuthentication(user.getUserCode(), request);
                    //人员新增更新成功后刷新缓存
                    CodeRepositoryCache.evictCache("UserInfo");
                }
            }
            deleteVotaCode(key);
            return ResponseData.makeSuccessResponse();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.errorResponse;
    }

    /**
     *
     * @param loginname  手机号或者邮箱
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(
        value = "找回密码(发送验证码 手机/邮箱)",
        notes = "找回密码(发送验证码 手机/邮箱)"
    )
    @RequestMapping(value = "/findPwd", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData findPwd(@RequestParam(value = "loginname") String loginname,
                                HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            UserInfo userInfo;
            if(loginname.indexOf('@')>0){
                userInfo = userInfoDao.getUserByRegEmail(loginname);
                if(userInfo == null){
                    return ResponseData.makeErrorMessage("用户不存在");
                }
                sendEmail(userInfo.getUserCode(), loginname);
            }else{
                userInfo = userInfoDao.getUserByRegCellPhone(loginname);
                if(userInfo == null){
                    return ResponseData.makeErrorMessage("用户不存在");
                }
                sendPhone(loginname, "");
            }
            result.put("x-auth-token", request.getSession().getId());
            return ResponseData.makeResponseData(result);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseData.errorResponse;
        }


    }

    @ApiOperation(
        value = "校验并返回用户信息",
        notes = "校验并返回用户信息"
    )
    @RequestMapping(value = "/checkCodeUser", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData checkCodeUser(@RequestParam("key") String key,
                                       @RequestParam("code") String code,
                                       HttpServletRequest request){
        try {
            if (code == null) {
                return ResponseData.makeErrorMessage(500, "请输入验证码！");
            }
            //从Redis中获取验证码和部分信息
            VotaCode votaCode = fetchVotaCode(key);
            /*if(json == null){
                json = JSONObject.parseObject(request.getHeader("verifyCode"));
            }*/
            if(votaCode == null){
                return ResponseData.makeErrorMessage(500, "未发送验证码！");
            }

            if (!StringUtils.equals(votaCode.getVerifyCode(),code)) {
                return ResponseData.makeErrorMessage(500, "验证码错误！");
            }
            if ((System.currentTimeMillis() - votaCode.getCreateTime()) > 1000 * 60 * 5) {
                return ResponseData.makeErrorMessage(500, "验证码已过期！");
            }
            UserInfo userInfo = new UserInfo();
            if(StringUtils.isNotBlank(votaCode.getEmail())){
                userInfo = userInfoDao.getUserByRegEmail(votaCode.getEmail());
            }else if(StringUtils.isNotBlank(votaCode.getPhone())){
                userInfo = userInfoDao.getUserByRegCellPhone(votaCode.getPhone());
            }
            deleteVotaCode(key);
            return ResponseData.makeResponseData(userInfo);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.errorResponse;
    }

    public ResponseData sendEmail(String userCode, String email){
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String message = "您的验证码为:" + verifyCode + "，该码有效期为5分钟，该码只能使用一次!";


        ResponseData result = notificationCenter.sendMessageAppointedType("email",
            "system", userCode,
            NoticeMessage.create().operation("system").method("post").subject("您有新邮件")
                .content(message));
        if(result.getCode() == 0){
            //发送成功则将JSON保存到session和Redis中
            VotaCode votaCode = new VotaCode();
            votaCode.setVerifyCode(verifyCode);
            votaCode.setEmail(email);
            votaCode.setCreateTime(System.currentTimeMillis());
            saveVotaCode(email, votaCode);
        }
        return result;
    }

    public SendSmsResponseBody sendPhone(String phone, String userCode) {
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("code", verifyCode);
        if(userCode != null && !userCode.equals("")){
            UserInfo userInfo = userInfoDao.getUserByCode(userCode);
            if(userInfo != null){
                jSONObject.put("product", "用户"+userInfo.getUserName());
            }else{
                jSONObject.put("product", "用户");
            }
        }else{
            jSONObject.put("product", "用户");
        }
        try {
            com.aliyun.dysmsapi20170525.Client client = this.createClient();

            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName("身份验证")
                .setTemplateCode("SMS_65920066")
                .setPhoneNumbers(phone)
                .setTemplateParam(jSONObject.toString());


            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponseBody result = client.sendSms(sendSmsRequest).getBody();
            if(result.getCode().equals("OK")){
                //发送成功
                //将验证码存入到Redis中
                VotaCode votaCode = new VotaCode();
                votaCode.setVerifyCode(verifyCode);
                votaCode.setPhone(phone);
                votaCode.setCreateTime(System.currentTimeMillis());
                saveVotaCode(phone, votaCode);
            }
            return result;
        } catch (Exception e) {
            throw new ObjectException(ResponseData.ERROR_PROCESS_ERROR,"发送短信异常："+ e.getMessage(), e);
        }
    }

    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception 异常
     */
    private com.aliyun.dysmsapi20170525.Client createClient() throws Exception {

        Config config = new Config()
            // 您的AccessKey ID
            .setAccessKeyId(SecurityOptUtils.decodeSecurityString(accessKeyId))
            // 您的AccessKey Secret
            .setAccessKeySecret(SecurityOptUtils.decodeSecurityString(accessKeySecret));
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    private void reloadAuthentication(String userCode, HttpServletRequest request) {
        CentitUserDetails centitUserDetails = platformEnvironment.loadUserDetailsByUserCode(userCode);
        centitUserDetails.setLoginIp(WebOptUtils.getRequestAddr(request));
        SecurityContextHolder.getContext().setAuthentication(centitUserDetails);
    }

}
