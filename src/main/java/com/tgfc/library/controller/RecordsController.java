package com.tgfc.library.controller;

import com.tgfc.library.request.SendMailRequest;
import com.tgfc.library.service.IRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records")
public class RecordsController {
    @Autowired
    IRecordsService recordsService;

    @PostMapping("/returnNotify")
    public Boolean returnNotify(@RequestBody SendMailRequest model) {
        return recordsService.returnNotify(model);
    }

}
