package com.centit.framework.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.system.dao.UnitInfoDao;
import com.centit.framework.system.po.UnitInfo;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import org.apache.commons.lang3.StringUtils;

public abstract class PersistenceUtils {

    public static String makeIdByFormat(String idno, String idFormate,
                                        String defaultPrefix, int defaultLen, String defaultPad){
        if(StringUtils.equalsIgnoreCase("uuid",idFormate)){
            return UuidOpt.getUuidAsString32();
        } else if(StringUtils.equalsIgnoreCase("uuid22",idFormate)){
            return UuidOpt.getUuidAsString22();
        } else {
            //{"prefix":"U","length":8,"pad":"0"}
            String prefix = defaultPrefix;
            String pad = defaultPad;
            int len = defaultLen;

            JSONObject idFormat = (JSONObject) JSON.parse(idFormate);
            if(idFormat!=null) {
                len =NumberBaseOpt.castObjectToInteger(idFormat.get("length"), 1);
                prefix = idFormat.getString("prefix");
                pad = idFormat.getString("pad");
            }
            return StringBaseOpt.midPad(idno, len, prefix, pad);
        }
    }

}
