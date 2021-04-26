package com.hgcw.wiki.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hgcw.wiki.domin.Doc;
import com.hgcw.wiki.domin.DocExample;
import com.hgcw.wiki.mapper.DocMapper;
import com.hgcw.wiki.req.DocQueryReq;
import com.hgcw.wiki.req.DocSaveReq;
import com.hgcw.wiki.resp.DocQueryResp;
import com.hgcw.wiki.resp.PageResp;
import com.hgcw.wiki.service.DocService;
import com.hgcw.wiki.util.CopyUtil;
import com.hgcw.wiki.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 21:35
 */
@Service
public class DocServiceImpl implements DocService {
    @Autowired
    private DocMapper docMapper;
    @Autowired
    private SnowFlake snowFlake;


    @Override
    public List<DocQueryResp> selectList() {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docsList = docMapper.selectByExample(docExample);

        /**
         * list拷贝
         */
        List<DocQueryResp> reqList1 = CopyUtil.copyList(docsList, DocQueryResp.class);

        return reqList1;
    }


    @Override
    public PageResp<DocQueryResp> dimSelect(DocQueryReq req) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
//        if (!ObjectUtils.isEmpty(req.getName())) {
//            criteria.andNameLike("%" + req.getName() + "%");
//
//        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docsList = docMapper.selectByExample(docExample);
        PageInfo<Doc> pageInfo = new PageInfo<>(docsList);

//        List<DocReq> reqList = new ArrayList<>();
//        for (Doc doc : docsList) {
////            DocReq docReq = new DocReq();
////            BeanUtils.copyProperties(doc, docReq);
//            /**
//             * 对象拷贝
//             */
//            DocReq copy = CopyUtil.copy(doc, DocReq.class);
//            reqList.add(copy);
//        }
        /**
         * list拷贝
         */
        List<DocQueryResp> reqList1 = CopyUtil.copyList(docsList, DocQueryResp.class);
        PageResp<DocQueryResp> pageResp = new PageResp(pageInfo.getTotal(), reqList1);

        return pageResp;
    }

    /**
     * 删除电子书
     *
     * @param id
     */
    @Override
    public void delectDoc(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新和添加电子书
     *
     * @param
     */
    @Override
    public void updateDoc(DocSaveReq docQueryReq) {
        //方法参数类型不同需要转换
        Doc doc = CopyUtil.copy(docQueryReq, Doc.class);
        if (ObjectUtils.isEmpty(docQueryReq.getId())) {
            //传入的id为空的为新增
            //新增对象id有三种：id自增，uuid，雪花算法新增
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        } else {
            //否则为更新
            docMapper.updateByPrimaryKey(doc);
        }

    }
}
