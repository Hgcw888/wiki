package com.hgcw.wiki.resp;

import lombok.Data;

/**
 * @author hgcw
 * @date 2021/4/18 21:39
 */
@Data
public class CommonResp<T>{
    /**
     * 返回请求
     */
    private boolean success=true;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T content;
}
