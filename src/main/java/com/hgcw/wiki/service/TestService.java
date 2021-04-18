package com.hgcw.wiki.service;

import com.hgcw.wiki.domin.Test;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 14:18
 */
public interface TestService {
    List<Test> selectList();

    Test selectId(String id);
}
