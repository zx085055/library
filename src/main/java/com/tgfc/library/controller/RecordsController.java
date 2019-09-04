package com.tgfc.library.controller;

import com.tgfc.library.request.PageableRequest;
import com.tgfc.library.request.RecordsPageRequest;
import com.tgfc.library.request.SendMailRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/records")
public class RecordsController {
    @Autowired
    IRecordsService recordsService;

    @PostMapping("/select")
    public BaseResponse select(@RequestBody RecordsPageRequest records) {
        return recordsService.select(records.getKeyword(), records.getStatus(), records.getPageable());
    }

    @DeleteMapping("/delete")
    public BaseResponse delete(@RequestParam int id) {
        return recordsService.delete(id);
    }

    @PostMapping("/returnNotify")
    public BaseResponse returnNotify(@RequestBody SendMailRequest model) {
        return recordsService.returnNotify(model);
    }

    @GetMapping("/returnBook")
    public BaseResponse returnBook(@RequestParam Integer id) {
        return recordsService.returnBook(id);
    }

    @PostMapping("/findAll")
    public BaseResponse findAll(@RequestBody PageableRequest pageableRequest) {
        return recordsService.findAll(pageableRequest.getPageable());
    }

    @PostMapping("/findByDate")
    public BaseResponse findByTimeIntervalBetween(@RequestBody RecordsPageRequest recordsPageRequest) {
        return recordsService.findByTimeInterval(recordsPageRequest.getBorrowDate(), recordsPageRequest.getReturnDate(), recordsPageRequest.getPageable());
    }

}
