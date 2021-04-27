package com.hgcw.wiki.resp;

import lombok.Data;

/**
 * 用户登录成功返回数据的实体类
 */
@Data
public class EuserLoginResp {
    private Long id;

    private String loginName;

    private String name;

    @Override
    public String toString() {
        return "EuserLoginResp{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}