package com.tgfc.library.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {


    @GetMapping(value = "/api/{id}")
    public String get(){
//        sendMailService.sendMail("我是標題","你好","tgfc061@tgfc.tw");
        return "Hello Security";
    }
}
