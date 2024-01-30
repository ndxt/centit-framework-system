package com.centit.framework.system.utils;

import lombok.Data;

@Data
public class VotaCode {
    Long createTime;
    String verifyCode;
    String email;
    String phone;
}
