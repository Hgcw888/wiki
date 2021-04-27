package com.hgcw.wiki.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 用户登录请求实体类
 */
@Data
public class EuserLoginReq {

    @NotNull(message = "【登陆名】不能为空")
    private String loginName;

    @NotNull(message = "【密码】不能为空")
//    @Length(min = 6, max = 20, message = "【密码】6~32位")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "【密码】规则不正确")//长度校验
    private String password;

    @Override
    public String toString() {
        return "EuserQueryResp{" +

                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}