package com.hgcw.wiki.req;

import lombok.Data;

/**
 * @author hgcw
 * @date 2021/4/18 22:11
 *首页查询端口请求类
 */
@Data
public class DocQueryReq extends PageReq {
    @Override
    public String toString() {
        return "DocQueryReq{} " + super.toString();
    }
}
