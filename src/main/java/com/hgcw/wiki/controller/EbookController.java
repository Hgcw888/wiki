package com.hgcw.wiki.controller;

import com.hgcw.wiki.domin.Ebook;
import com.hgcw.wiki.req.EbookQueryReq;
import com.hgcw.wiki.req.EbookSaveReq;
import com.hgcw.wiki.resp.CommonResp;
import com.hgcw.wiki.resp.CategoryQueryResp;
import com.hgcw.wiki.resp.EbookQueryResp;
import com.hgcw.wiki.resp.PageResp;
import com.hgcw.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 21:37
 */
@RestController
@RequestMapping("/api")
public class EbookController {
    @Autowired
    private EbookService ebookService;

    @GetMapping("/selectList")
    public CommonResp selectList() {
        CommonResp<List<Ebook>> resp = new CommonResp<>();
        List<Ebook> ebooks = ebookService.selectList();
        resp.setContent(ebooks);
        return resp;
    }

    /**
     * 查询列表
     *
     * @param req
     * @return
     * @Valid开启参数校验规则
     */
    @GetMapping("/dimSelect")
    public CommonResp dimSelect(@Valid EbookQueryReq req) {
        PageResp<EbookQueryResp> ebooks = ebookService.dimSelect(req);
        CommonResp<PageResp<EbookQueryResp>> objectCommonResp = new CommonResp<>();
        objectCommonResp.setContent(ebooks);
        return objectCommonResp;
    }

    /**
     * 删除电子书
     */
    @DeleteMapping("delectEbook/{id}")
    public CommonResp delectEbook(@PathVariable Long id) {
        CommonResp objectCommonResp = new CommonResp<>();
        ebookService.delectEbook(id);
        return objectCommonResp;
    }
    /**
     * 编辑电子书
     * @Valid 实体类设定的字段是否为空
     */
    @PostMapping("/updateEbook")
    public CommonResp updateEbook(@Valid  @RequestBody EbookSaveReq ebookQueryReq){
        CommonResp resp = new CommonResp<>();
        ebookService.updateEbook(ebookQueryReq);
        return resp;
    }
}
