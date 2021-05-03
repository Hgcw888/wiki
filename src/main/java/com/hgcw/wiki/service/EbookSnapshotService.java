package com.hgcw.wiki.service;
import com.hgcw.wiki.resp.StatisticResp;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EbookSnapshotService {

    /**
     * 为所有的电子书生成一条今天的记录，如果还没有
     * 更新总阅读数、总点赞数
     * 更新今日阅读数、今日点赞数
     */
    void genSnapshot();

    /**
     * 获取首页数值数据：总阅读数、总点赞数、今日阅读数、今日点赞数、今日预计阅读数、今日预计阅读增长
     */
    List<StatisticResp> getStatistic();

    /**
     * 30天数值统计
     */
    List<StatisticResp> get30Statistic();

}
