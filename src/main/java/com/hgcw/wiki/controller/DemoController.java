package com.hgcw.wiki.controller;

import com.hgcw.wiki.domin.Demo;
import com.hgcw.wiki.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/18 15:36
 */
@RestController
@RequestMapping("/api")
public class DemoController {
    @Autowired
    private DemoService demoService;

    @GetMapping("/select")
    public List<Demo> select() {
        List<Demo> list = demoService.select();
        return list;
    }
}
