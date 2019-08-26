package com.tgfc.library.controller;

import com.tgfc.library.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private SendMailService sendMailService;

    @GetMapping(value = "/api")
    public String get(){
        sendMailService.sendMail("我是標題","你好","tgfc061@tgfc.tw");
        return "Hello Security";
    }
}
