package com.hgcw.wiki.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author hgcw
 * @date 2021/4/27 22:28
 */
@Data
public class EuserResetPasswordReq {
    private Long id;

    @NotNull(message = "【密码】不能为空")
//    @Length(min = 6, max = 20, message = "【密码】6~32位")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "【密码】至少包含数字和英文，长度为6-32")//长度校验
    private String password;

    @Override
    public String toString() {
        return "EuserResetPasswordReq{" +
                "password='" + password + '\'' +
                '}';
    }
}
