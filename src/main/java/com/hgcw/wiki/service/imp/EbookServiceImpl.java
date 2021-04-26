package com.hgcw.wiki.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hgcw.wiki.domin.Ebook;
import com.hgcw.wiki.domin.EbookExample;
import com.hgcw.wiki.mapper.EbookMapper;
import com.hgcw.wiki.req.EbookQueryReq;
import com.hgcw.wiki.req.EbookSaveReq;
import com.hgcw.wiki.resp.CategoryQueryResp;
import com.hgcw.wiki.resp.EbookQueryResp;
import com.hgcw.wiki.resp.PageResp;
import com.hgcw.wiki.service.EbookService;
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
public class EbookServiceImpl implements EbookService {
    @Autowired
    private EbookMapper ebookMapper;
    @Autowired
    private SnowFlake snowFlake;

    @Override
    public List<Ebook> selectList() {
        List<Ebook> ebooks = ebookMapper.selectByExample(null);
        return ebooks;

    }

    @Override
    public PageResp<EbookQueryResp> dimSelect(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        //如果有值判断是否是name如果是就是调用了通过name查询的电子书
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");

        }
        //如果有值判断是否是Category2Id如果是就是调用了点击二级分类获取电子书
        if (!ObjectUtils.isEmpty(req.getCategory2Id())) {
            criteria.andCategory2IdEqualTo(req.getCategory2Id());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebooksList = ebookMapper.selectByExample(ebookExample);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebooksList);

//        List<EbookReq> reqList = new ArrayList<>();
//        for (Ebook ebook : ebooksList) {
////            EbookReq ebookReq = new EbookReq();
////            BeanUtils.copyProperties(ebook, ebookReq);
//            /**
//             * 对象拷贝
//             */
//            EbookReq copy = CopyUtil.copy(ebook, EbookReq.class);
//            reqList.add(copy);
//        }
        /**
         * list拷贝
         */
        List<EbookQueryResp> reqList1 = CopyUtil.copyList(ebooksList, EbookQueryResp.class);
        PageResp<EbookQueryResp> pageResp = new PageResp(pageInfo.getTotal(), reqList1);

        return pageResp;
    }

    /**
     * 删除电子书
     *
     * @param id
     */
    @Override
    public void delectEbook(Long id) {
        ebookMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新和添加电子书
     *
     * @param
     */
    @Override
    public void updateEbook(EbookSaveReq ebookQueryReq) {
        //方法参数类型不同需要转换
        Ebook ebook = CopyUtil.copy(ebookQueryReq, Ebook.class);
        if (ObjectUtils.isEmpty(ebookQueryReq.getId())) {
            //传入的id为空的为新增
            //新增对象id有三种：id自增，uuid，雪花算法新增
          ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        } else {
            //否则为更新
            ebookMapper.updateByPrimaryKey(ebook);
        }

    }
}
