package com.hgcw.wiki.controller;

import org.springframework.web.bind.annotation.*;

import javax.swing.*;

/**
 * @author hgcw
 * @date 2021/4/17 20:23
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }

    @PostMapping("/post")
    public String post( String name) {
        return "Hello World" + name;
    }
}
