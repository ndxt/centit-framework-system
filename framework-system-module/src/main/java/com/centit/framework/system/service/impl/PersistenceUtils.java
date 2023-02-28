package com.centit.framework.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import org.apache.commons.lang3.StringUtils;

abstract class PersistenceUtils {

    static String makeIdByFormat(String idno, String idFormat,
                                        String defaultPrefix, int defaultLen, String defaultPad){
        if(StringUtils.equalsIgnoreCase("uuid",idFormat)){
            return UuidOpt.getUuidAsString32();
        } else if(StringUtils.equalsIgnoreCase("uuid22",idFormat)){
            return UuidOpt.getUuidAsString22();
        } else {
            //{"prefix":"U","length":8,"pad":"0"}
            String prefix = defaultPrefix;
            String pad = defaultPad;
            int len = defaultLen;
            if(StringUtils.isNotBlank(idFormat)) {
                JSONObject idJsonFormat = (JSONObject) JSON.parse(idFormat);
                if (idJsonFormat != null) {
                    len = NumberBaseOpt.castObjectToInteger(idJsonFormat.get("length"), 1);
                    prefix = idJsonFormat.getString("prefix");
                    pad = idJsonFormat.getString("pad");
                }
            }
            return StringBaseOpt.midPad(idno, len, prefix, pad);
        }
    }

}
