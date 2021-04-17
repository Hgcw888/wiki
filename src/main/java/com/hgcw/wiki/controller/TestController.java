package com.hgcw.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
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
        return "Hello World1112222";
    }

    @PostMapping("/post")
    public String post( String name) {
        return "Hello World" + name;
    }
}
