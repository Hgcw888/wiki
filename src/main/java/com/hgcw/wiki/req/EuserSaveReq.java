package com.hgcw.wiki.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 添加和编辑端口请求类
 */
@Data
public class EuserSaveReq {
    private Long id;
    @NotNull(message = "【登陆名】不能为空")
    private String loginName;
    @NotNull(message = "【昵称】不能为空")
    private String name;
    @NotNull(message = "【密码】不能为空")
//    @Length(min = 6, max = 20, message = "【密码】6~20位")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$", message = "【密码】至少包含数字和英文，长度为6-20")//长度校验
    private String password;

    @Override
    public String toString() {
        return "EuserQueryResp{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}