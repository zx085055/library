package com.tgfc.library.controller;

import com.tgfc.library.entity.Book;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookDataController {

    @Autowired
    IBookService bookDataService;

    @GetMapping(value = "/api/update")
    public Book get(@RequestParam("id") Integer id) {
        return bookDataService.getById(id);
    }

    @PostMapping(value = "/api/update")
    public Page<Book> getKeyWord(@RequestBody BookDataPageRequest model) {
        return bookDataService.getBooksList(model);
    }
}
