package com.hgcw.wiki.controller;

import com.hgcw.wiki.resp.CommonResp;
import com.hgcw.wiki.resp.StatisticResp;
import com.hgcw.wiki.service.EbookSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author hgcw
 * @date 2021/5/3 17:19
 *   <!-- 获取首页数值数据：总阅读数、总点赞数、今日阅读数、今日点赞数、今日预计阅读数、今日预计阅读增长 -->
 */

@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {
 @Autowired
 private EbookSnapshotService ebookSnapshotService;

    /**
     * 获取首页数值数据：总阅读数、总点赞数、今日阅读数、今日点赞数、今日预计阅读数、今日预计阅读增长
     */
    @GetMapping("/get-statistic")
    public CommonResp getStatistic(){
        List<StatisticResp> statistic = ebookSnapshotService.getStatistic();
        CommonResp< List<StatisticResp>> objectCommonResp = new CommonResp<>();
        objectCommonResp.setContent(statistic);
        return objectCommonResp;
    }

    /**
     * 30天数值统计,放入前端echarts
     */
    @GetMapping("/get-30-statistic")
    public CommonResp get30Statistic(){
        List<StatisticResp> statistic = ebookSnapshotService.get30Statistic();
        CommonResp< List<StatisticResp>> objectCommonResp = new CommonResp<>();
        objectCommonResp.setContent(statistic);
        return objectCommonResp;
    }
}
