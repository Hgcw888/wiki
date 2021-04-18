package com.hgcw.wiki.controller;

import com.hgcw.wiki.domin.Category;
import com.hgcw.wiki.req.CategoryQueryReq;
import com.hgcw.wiki.req.CategorySaveReq;
import com.hgcw.wiki.resp.CommonResp;
import com.hgcw.wiki.resp.CategoryQueryResp;
import com.hgcw.wiki.resp.PageResp;
import com.hgcw.wiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 21:37
 */
@RestController
@RequestMapping("/categoty")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 不使用分页查询列表
     * @param
     * @return
     */
    @GetMapping("/selectList")
    public CommonResp selectList() {
        List<CategoryQueryResp> categorys = categoryService.selectList();
        CommonResp<List<CategoryQueryResp>> objectCommonResp = new CommonResp<>();
        objectCommonResp.setContent(categorys);
        return objectCommonResp;
    }

    /**
     * 查询列表
     *
     * @param req
     * @return
     * @Valid开启参数校验规则
     */
    @GetMapping("/dimSelect")
    public CommonResp dimSelect(@Valid CategoryQueryReq req) {
        PageResp<CategoryQueryResp> categorys = categoryService.dimSelect(req);
        CommonResp<PageResp<CategoryQueryResp>> objectCommonResp = new CommonResp<>();
        objectCommonResp.setContent(categorys);
        return objectCommonResp;
    }

    /**
     * 删除电子书
     */
    @DeleteMapping("delectCategory/{id}")
    public CommonResp delectCategory(@PathVariable Long id) {
        CommonResp objectCommonResp = new CommonResp<>();
        categoryService.delectCategory(id);
        return objectCommonResp;
    }
    /**
     * 编辑电子书
     * @Valid 实体类设定的字段是否为空
     */
    @PostMapping("/updateCategory")
    public CommonResp updateCategory(@Valid  @RequestBody CategorySaveReq categoryQueryReq){
        CommonResp resp = new CommonResp<>();
        categoryService.updateCategory(categoryQueryReq);
        return resp;
    }
}
