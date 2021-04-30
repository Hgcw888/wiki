package com.hgcw.wiki.controller;

import com.hgcw.wiki.req.DocQueryReq;
import com.hgcw.wiki.req.DocSaveReq;
import com.hgcw.wiki.resp.DocQueryResp;
import com.hgcw.wiki.resp.CommonResp;
import com.hgcw.wiki.resp.PageResp;
import com.hgcw.wiki.service.DocService;
import com.hgcw.wiki.util.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 21:37
 */
@RestController
@RequestMapping("/doc")
public class DocController {
    @Autowired
    private DocService docService;

    /**
     * 不使用分页查询列表
     * @param
     * @return
     */
    @GetMapping("/selectList/{ebookId}")
    public CommonResp selectList(@PathVariable Long ebookId) {
        List<DocQueryResp> docs = docService.selectList(ebookId);
        CommonResp<List<DocQueryResp>> objectCommonResp = new CommonResp<>();
        objectCommonResp.setContent(docs);
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
    public CommonResp dimSelect(@Valid DocQueryReq req) {
        PageResp<DocQueryResp> docs = docService.dimSelect(req);
        CommonResp<PageResp<DocQueryResp>> objectCommonResp = new CommonResp<>();
        objectCommonResp.setContent(docs);
        return objectCommonResp;
    }

    /**
     * 删除文档，因为删除父类节点时也要讲其下的子类删除
     */
    @DeleteMapping("delectDoc/{ids}")
    public CommonResp delectDoc(@PathVariable String ids) {
        CommonResp objectCommonResp = new CommonResp<>();
        List<String> strings = Arrays.asList(ids.split(","));
        docService.delectDoc(strings);
        return objectCommonResp;
    }
    /**
     * 编辑文档
     * @Valid 实体类设定的字段是否为空
     */
    @PostMapping("/updateDoc")
    public CommonResp updateDoc(@Valid  @RequestBody DocSaveReq docQueryReq){
        CommonResp resp = new CommonResp<>();
        docService.updateDoc(docQueryReq);
        return resp;
    }


    /**
     * 根据ID查询
     *
     * @param
     * @return
     *
     */
    @GetMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable Long id) {
        String content = docService.findContent(id);
        CommonResp<String> objectCommonResp = new CommonResp<>();
        objectCommonResp.setContent(content);
        return objectCommonResp;
    }


    /**
     * 点赞
     *
     * @param
     * @return
     *
     */
    @GetMapping("/vote/{id}")
    public CommonResp Innervote(@PathVariable Long id) {
        docService.Innervote(id);
        CommonResp objectCommonResp1 = new CommonResp<>();
        return objectCommonResp1;
    }
}
