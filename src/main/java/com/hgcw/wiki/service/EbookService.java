package com.hgcw.wiki.service;

import com.hgcw.wiki.domin.Ebook;
import com.hgcw.wiki.req.EbookQueryReq;
import com.hgcw.wiki.req.EbookSaveReq;
import com.hgcw.wiki.resp.CategoryQueryResp;
import com.hgcw.wiki.resp.EbookQueryResp;
import com.hgcw.wiki.resp.PageResp;

import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 21:35
 */
public interface EbookService {
    List<Ebook> selectList();

    PageResp<EbookQueryResp> dimSelect(EbookQueryReq req);

    void delectEbook(Long id);

    void updateEbook(EbookSaveReq ebookQueryReq);
}
