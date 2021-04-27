package com.hgcw.wiki.resp;

import lombok.Data;

@Data
public class EuserQueryResp {
    private Long id;

    private String loginName;

    private String name;

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