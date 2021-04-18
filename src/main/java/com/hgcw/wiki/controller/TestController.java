package com.hgcw.wiki.controller;

import com.hgcw.wiki.domin.Test;
import com.hgcw.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

/**
 * @author hgcw
 * @date 2021/4/17 20:23
 */
@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public String test() {
        return "Hello World1112222";
    }

    @PostMapping("/post")
    public String post(String name) {
        return "Hello World" + name;
    }

    @GetMapping("/list")
    public List<Test> list() {
        List<Test> tests = testService.selectList();
        return tests;
    }

    @PostMapping("/selectId")
    public Test selectId(String id) {
        Test test = testService.selectId(id);
        return test;
    }
}
