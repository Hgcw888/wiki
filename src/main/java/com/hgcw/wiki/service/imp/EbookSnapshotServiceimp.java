package com.hgcw.wiki.service.imp;

import com.hgcw.wiki.mapper.EbookSnapshotMapperCust;
import com.hgcw.wiki.resp.StatisticResp;
import com.hgcw.wiki.service.EbookSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EbookSnapshotServiceimp implements EbookSnapshotService {

    @Autowired
    private EbookSnapshotMapperCust ebookSnapshotMapperCust;

    @Override
    public void genSnapshot() {
        ebookSnapshotMapperCust.genSnapshot();
    }

    /**
     * 获取首页数值数据：总阅读数、总点赞数、今日阅读数、今日点赞数、今日预计阅读数、今日预计阅读增长
     */
    @Override
    public List<StatisticResp> getStatistic() {
        return ebookSnapshotMapperCust.getStatistic();
    }

    /**
     * 30天数值统计
     */
    @Override
    public List<StatisticResp> get30Statistic() {
        return ebookSnapshotMapperCust.get30Statistic();
    }

}
