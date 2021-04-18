package com.hgcw.wiki.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hgcw.wiki.domin.Category;
import com.hgcw.wiki.domin.CategoryExample;
import com.hgcw.wiki.mapper.CategoryMapper;
import com.hgcw.wiki.req.CategoryQueryReq;
import com.hgcw.wiki.req.CategorySaveReq;
import com.hgcw.wiki.resp.CategoryQueryResp;
import com.hgcw.wiki.resp.PageResp;
import com.hgcw.wiki.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SnowFlake snowFlake;


    @Override
    public List<CategoryQueryResp> selectList() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categorysList = categoryMapper.selectByExample(categoryExample);

        /**
         * list拷贝
         */
        List<CategoryQueryResp> reqList1 = CopyUtil.copyList(categorysList, CategoryQueryResp.class);

        return reqList1;
    }


    @Override
    public PageResp<CategoryQueryResp> dimSelect(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
//        if (!ObjectUtils.isEmpty(req.getName())) {
//            criteria.andNameLike("%" + req.getName() + "%");
//
//        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categorysList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categorysList);

//        List<CategoryReq> reqList = new ArrayList<>();
//        for (Category category : categorysList) {
////            CategoryReq categoryReq = new CategoryReq();
////            BeanUtils.copyProperties(category, categoryReq);
//            /**
//             * 对象拷贝
//             */
//            CategoryReq copy = CopyUtil.copy(category, CategoryReq.class);
//            reqList.add(copy);
//        }
        /**
         * list拷贝
         */
        List<CategoryQueryResp> reqList1 = CopyUtil.copyList(categorysList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp(pageInfo.getTotal(), reqList1);

        return pageResp;
    }

    /**
     * 删除电子书
     *
     * @param id
     */
    @Override
    public void delectCategory(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新和添加电子书
     *
     * @param
     */
    @Override
    public void updateCategory(CategorySaveReq categoryQueryReq) {
        //方法参数类型不同需要转换
        Category category = CopyUtil.copy(categoryQueryReq, Category.class);
        if (ObjectUtils.isEmpty(categoryQueryReq.getId())) {
            //传入的id为空的为新增
            //新增对象id有三种：id自增，uuid，雪花算法新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
            //否则为更新
            categoryMapper.updateByPrimaryKey(category);
        }

    }
}
