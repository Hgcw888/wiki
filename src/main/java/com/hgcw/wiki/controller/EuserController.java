package com.hgcw.wiki.controller;

import com.hgcw.wiki.domin.Euser;
import com.hgcw.wiki.req.EuserQueryReq;
import com.hgcw.wiki.req.EuserSaveReq;
import com.hgcw.wiki.resp.CommonResp;
import com.hgcw.wiki.resp.EuserQueryResp;
import com.hgcw.wiki.resp.PageResp;
import com.hgcw.wiki.service.EuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 21:37
 */
@RestController
@RequestMapping("/euser")
public class EuserController {
    @Autowired
    private EuserService euserService;

    @GetMapping("/selectList")
    public CommonResp selectList() {
        CommonResp<List<Euser>> resp = new CommonResp<>();
        List<Euser> eusers = euserService.selectList();
        resp.setContent(eusers);
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
    public CommonResp dimSelect(@Valid EuserQueryReq req) {
        PageResp<EuserQueryResp> eusers = euserService.dimSelect(req);
        CommonResp<PageResp<EuserQueryResp>> objectCommonResp = new CommonResp<>();
        objectCommonResp.setContent(eusers);
        return objectCommonResp;
    }

    /**
     * 删除电子书
     */
    @DeleteMapping("delectEuser/{id}")
    public CommonResp delectEuser(@PathVariable Long id) {
        CommonResp objectCommonResp = new CommonResp<>();
        euserService.delectEuser(id);
        return objectCommonResp;
    }
    /**
     * 编辑电子书
     * @Valid 实体类设定的字段是否为空
     */
    @PostMapping("/updateEuser")
    public CommonResp updateEuser(@Valid  @RequestBody EuserSaveReq euserSaveReq){
        CommonResp resp = new CommonResp<>();
        euserService.updateEuser(euserSaveReq);
        return resp;
    }
}
