package com.hgcw.wiki.req;

import lombok.Data;

/**
 * @author hgcw
 * @date 2021/4/18 22:11
 *查询端口请求类
 */
@Data
public class EuserQueryReq extends PageReq {


    private String loginName;

    @Override
    public String toString() {
        return "EuserQueryReq{" +
                "loginName='" + loginName + '\'' +
                "} " + super.toString();
    }
}
