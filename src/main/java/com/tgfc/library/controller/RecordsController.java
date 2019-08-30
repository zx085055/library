package com.tgfc.library.controller;

import com.tgfc.library.entity.Announcement;
import com.tgfc.library.request.AnnouncementPageRequest;
import com.tgfc.library.request.RecordsPageRequest;
import com.tgfc.library.request.SendMailRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/records")
public class RecordsController {
    @Autowired
    IRecordsService recordsService;

//    @PostMapping("/select")
//    public BaseResponse select(@RequestBody RecordsPageRequest records) {
//        return recordsService.select(records., records.getPageable());
//    }

    @PostMapping("/returnNotify")
    public Boolean returnNotify(@RequestBody SendMailRequest model) {
        return recordsService.returnNotify(model);
    }

}
