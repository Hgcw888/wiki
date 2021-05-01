package com.hgcw.wiki.job;

import com.hgcw.wiki.mapper.DocMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 两种定时器
 */
@Component
@Slf4j
public class DocJob {
    @Autowired
    private DocMapper docMapper;


    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

//    /**
//     * 固定时间间隔，fixedRate单位毫秒
//     */
//    @Scheduled(fixedRate = 1000)
//    public void simple() throws InterruptedException {
//        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
//        String dateString = formatter.format(new Date());
//        Thread.sleep(2000);
//        LOG.info("每隔5秒钟执行一次： {}", dateString);
//    }

    /**
     * 自定义cron表达式跑批
     * 只有等上一次执行完成，下一次才会在下一个时间点执行，错过就错过
     */
    @Scheduled(cron = "5/30 * * * * ?")//没分钟的第五秒开始执行隔30秒执行一次：可到cron表达式网站
    public void cron() {
        LOG.info("更新电子书下的文档、点赞、阅读数的数据开始");
        //获取当前毫秒
        long l = System.currentTimeMillis();
        //需要定时执行的方法
        docMapper.updateEbookInfo();
        LOG.info("更新电子书下的文档、点赞、阅读数的数据的耗时:{}毫秒", System.currentTimeMillis() - l);
    }

}
