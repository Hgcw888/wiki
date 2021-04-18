package com.hgcw.wiki.mapper;

import com.hgcw.wiki.domin.Test;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 13:41
 */
@Repository
public interface TestMapper {
    List<Test> list();

    Test queryId(@Param("id") String id);
}
