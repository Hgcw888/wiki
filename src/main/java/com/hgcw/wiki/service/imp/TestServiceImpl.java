package com.hgcw.wiki.service.imp;

import com.hgcw.wiki.domin.Test;
import com.hgcw.wiki.mapper.TestMapper;
import com.hgcw.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 14:18
 */
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;

    @Override
    public List<Test> selectList() {
        List<Test> list = testMapper.list();
        return list;
    }

    @Override
    public Test selectId(String id) {
        Test test = testMapper.queryId(id);
        return test;
    }
}
