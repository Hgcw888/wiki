package com.hgcw.wiki.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hgcw.wiki.domin.Euser;
import com.hgcw.wiki.domin.EuserExample;
import com.hgcw.wiki.exception.BusinessException;
import com.hgcw.wiki.exception.BusinessExceptionCode;
import com.hgcw.wiki.mapper.EuserMapper;
import com.hgcw.wiki.req.EuserQueryReq;
import com.hgcw.wiki.req.EuserSaveReq;
import com.hgcw.wiki.resp.EuserQueryResp;
import com.hgcw.wiki.resp.PageResp;
import com.hgcw.wiki.service.EuserService;
import com.hgcw.wiki.util.CopyUtil;
import com.hgcw.wiki.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 21:35
 */
@Service
public class EuserServiceImpl implements EuserService {
    @Autowired
    private EuserMapper euserMapper;
    @Autowired
    private SnowFlake snowFlake;

    @Override
    public List<Euser> selectList() {
        List<Euser> eusers = euserMapper.selectByExample(null);
        return eusers;

    }

    /**
     * 查询
     *
     * @param req
     * @return
     */
    @Override
    public PageResp<EuserQueryResp> dimSelect(EuserQueryReq req) {
        EuserExample euserExample = new EuserExample();
        EuserExample.Criteria criteria = euserExample.createCriteria();
        //如果有值判断是否是Category2Id如果是就是调用了点击二级分类获取电子书
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Euser> eusersList = euserMapper.selectByExample(euserExample);
        PageInfo<Euser> pageInfo = new PageInfo<>(eusersList);
//        List<EuserReq> reqList = new ArrayList<>();
//        for (Euser euser : eusersList) {
////            EuserReq euserReq = new EuserReq();
////            BeanUtils.copyProperties(euser, euserReq);
//            /**
//             * 对象拷贝
//             */
//            EuserReq copy = CopyUtil.copy(euser, EuserReq.class);
//            reqList.add(copy);
//        }
        /**
         * list拷贝
         */
        List<EuserQueryResp> reqList1 = CopyUtil.copyList(eusersList, EuserQueryResp.class);
        PageResp<EuserQueryResp> pageResp = new PageResp(pageInfo.getTotal(), reqList1);

        return pageResp;
    }

    /**
     * 删除电子书
     *
     * @param id
     */
    @Override
    public void delectEuser(Long id) {
        euserMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新和添加电子书
     *
     * @param
     */
    @Override
    public void updateEuser(EuserSaveReq euserSaveReq) {
        //方法参数类型不同需要转换
        Euser euser = CopyUtil.copy(euserSaveReq, Euser.class);
        if (ObjectUtils.isEmpty(euserSaveReq.getId())) {
         if (ObjectUtils.isEmpty( selectLogName(euserSaveReq.getLoginName()))){
             //传入的id为空的为新增
             //新增对象id有三种：id自增，uuid，雪花算法新增
             euser.setId(snowFlake.nextId());
             euserMapper.insert(euser);
         }
         //用户名已存在
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);

        } else {
            //否则为更新,不能更改loginname
            euser.setLoginName(null);
            euserMapper.updateByPrimaryKeySelective(euser);
        }

    }

    /**
     * 查询用户名是否存在
     * @param loginName
     * @return
     */
    @Override
    public Euser selectLogName(String loginName) {
        EuserExample euserExample = new EuserExample();
        EuserExample.Criteria criteria = euserExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<Euser> eusers = euserMapper.selectByExample(euserExample);
        if (CollectionUtils.isEmpty(eusers)) {
            return null;
        } else {
            return eusers.get(0);
        }
    }

}
