package com.tgfc.library.controller;

import com.tgfc.library.entity.Books;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.service.IBookDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookDataController {

    @Autowired
    IBookDataService bookDataService;

    @GetMapping(value = "/api/update")
    public Books get(@RequestParam("id") Integer id) {
        return bookDataService.getById(id);
    }

    @PostMapping(value = "/api/update")
    public Page<Books> getKeyWord(@RequestBody BookDataPageRequest model) {
        return bookDataService.getBooksList(model);
    }
}
