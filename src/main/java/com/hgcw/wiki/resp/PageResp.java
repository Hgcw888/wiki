package com.hgcw.wiki.resp;

import lombok.Data;

import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/22 21:46
 */
@Data
public class PageResp<T>{
    //当前页数量
    private Long total;
    //当前页列表
    private List<T> list;

    public PageResp(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageResp{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
