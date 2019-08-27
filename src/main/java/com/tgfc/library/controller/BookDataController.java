package com.tgfc.library.controller;

import com.tgfc.library.entity.BookData;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.service.IBookDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookDataController {

    @Autowired
    IBookDataService bookDataService;

    @GetMapping(value = "/api/update")
    public BookData get(@RequestParam("id") Integer id) {
        return bookDataService.getById(id);
    }

    @PostMapping(value = "/api/update")
    public Page<BookData> getKeyWord(@RequestBody BookDataPageRequest model) {
        return bookDataService.getBooksList(model);
    }
}
