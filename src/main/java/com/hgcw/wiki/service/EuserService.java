package com.hgcw.wiki.service;

import com.hgcw.wiki.domin.Euser;
import com.hgcw.wiki.req.EuserQueryReq;
import com.hgcw.wiki.req.EuserSaveReq;
import com.hgcw.wiki.resp.EuserQueryResp;
import com.hgcw.wiki.resp.PageResp;

import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 21:35
 */
public interface EuserService {
    List<Euser> selectList();

    PageResp<EuserQueryResp> dimSelect(EuserQueryReq req);

    void delectEuser(Long id);

    void updateEuser(EuserSaveReq euserSaveReq);

    Euser selectLogName(String loginName);
}
