package com.hgcw.wiki.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hgcw.wiki.domin.Content;
import com.hgcw.wiki.domin.Doc;
import com.hgcw.wiki.domin.DocExample;
import com.hgcw.wiki.mapper.ContentMapper;
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
    private ContentMapper contentMapper;
    @Autowired
    private SnowFlake snowFlake;


    @Override
    public List<DocQueryResp> selectList(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
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
     * 删除文档
     *
     * @param id
     */
    @Override
    public void delectDoc(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }


    /**
     * 删除文档,删除父节点的时候需要将其子类一起删掉
     *
     * @param
     */
    @Override
    public void delectDoc(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }


    /**
     * 更新和添加文档。因为文档内容在不同表中，所有同事也要插入content表中
     *
     * @param
     */
    @Override
    public void updateDoc(DocSaveReq docQueryReq) {
        //方法参数类型不同需要转换（添加文档）
        Doc doc = CopyUtil.copy(docQueryReq, Doc.class);
        //方法参数类型不同需要转换（添加文档内容）
        Content content = CopyUtil.copy(docQueryReq, Content.class);
        if (ObjectUtils.isEmpty(docQueryReq.getId())) {
            //传入的id为空的为新增
            //新增对象id有三种：id自增，uuid，雪花算法新增
            doc.setId(snowFlake.nextId());
            //insert方法字段没有数据时添加null进去，所有点赞，和阅读书需要相加所有初始要是0
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);

            //添加文档内容
            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            //否则为更新
            docMapper.updateByPrimaryKey(doc);
            //更新文档内容updateByPrimaryKeyWithBLOBs方法更新包含大文档
            int i = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            //如果更新是文档内容原先是空的就插入
            if (i == 0) {
                contentMapper.insert(content);
            }

        }

    }

    /**
     * 查询文档内容
     * @param id
     * @return
     */
    @Override
    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        return content.getContent();
    }


}
