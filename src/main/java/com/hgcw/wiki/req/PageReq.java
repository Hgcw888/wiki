package com.hgcw.wiki.req;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @author hgcw
 * @date 2021/4/22 21:46
 */
@Data
// @Max参数校验
public class PageReq {
    @NotNull(message = "【页码】不能为空")
    private int page;
    @NotNull(message = "【每页天数】不能为空")

    @Max(value = 1000, message = "【每页页数】不能超过1000")
    private int Size;

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", Size=" + Size +
                '}';
    }
}
