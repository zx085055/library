package com.tgfc.library.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(value = "/api")
    public String get(){

        return "Hello Security";
    }
}
