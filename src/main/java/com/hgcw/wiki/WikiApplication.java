package com.hgcw.wiki;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author WEI
 */
@SpringBootApplication
//配置orm文件
@MapperScan("com.hgcw.wiki.mapper")
//启用定时任务
@EnableScheduling
public class WikiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiApplication.class, args);
    }

}
