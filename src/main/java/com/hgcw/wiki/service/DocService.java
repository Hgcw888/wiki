package com.hgcw.wiki.service;

import com.hgcw.wiki.req.DocQueryReq;
import com.hgcw.wiki.req.DocSaveReq;
import com.hgcw.wiki.resp.DocQueryResp;
import com.hgcw.wiki.resp.PageResp;

import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 21:35
 */
public interface DocService {

    List<DocQueryResp> selectList();

    PageResp<DocQueryResp> dimSelect(DocQueryReq req);

    void delectDoc(Long id);

    void delectDoc(List<String> ids);

    void updateDoc(DocSaveReq docQueryReq);
}
