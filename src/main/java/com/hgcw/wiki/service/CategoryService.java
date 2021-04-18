package com.hgcw.wiki.service;

import com.hgcw.wiki.domin.Category;
import com.hgcw.wiki.req.CategoryQueryReq;
import com.hgcw.wiki.req.CategorySaveReq;
import com.hgcw.wiki.resp.CategoryQueryResp;
import com.hgcw.wiki.resp.PageResp;

import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 21:35
 */
public interface CategoryService {

    List<CategoryQueryResp> selectList();

    PageResp<CategoryQueryResp> dimSelect(CategoryQueryReq req);

    void delectCategory(Long id);

    void updateCategory(CategorySaveReq categoryQueryReq);
}
