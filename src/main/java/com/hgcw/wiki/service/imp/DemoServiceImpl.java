package com.hgcw.wiki.service.imp;

import com.hgcw.wiki.domin.Demo;
import com.hgcw.wiki.mapper.DemoMapper;
import com.hgcw.wiki.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 15:33
 */
@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    private DemoMapper demoMapper;

    @Override
    public List<Demo> select() {
        /**
         * 选择性查询，相当于where，如果没有条件就是查询所有
         */
        return demoMapper.selectByExample(null);

    }
}
