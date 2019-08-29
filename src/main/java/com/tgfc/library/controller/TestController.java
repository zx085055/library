package com.tgfc.library.controller;

import com.tgfc.library.util.MailUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {

    @GetMapping(value = "/{id}")
    public String get(){
//        MailUtil.sendMail("我是標題","你好","tgfc061@tgfc.tw");
        return "Hello Security";
    }
}
