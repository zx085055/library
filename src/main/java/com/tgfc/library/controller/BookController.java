package com.tgfc.library.controller;

import com.tgfc.library.entity.Book;
import com.tgfc.library.request.AddBook;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.response.BooksResponse;
import com.tgfc.library.service.IBookService;
import com.tgfc.library.service.IPhotoService;
//import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    IBookService bookDataService;

    @Autowired
    IPhotoService photoService;

    @GetMapping(value = "/find")
    public Book get(@RequestParam("id") Integer id) {
        return bookDataService.getById(id);
    }

    @PostMapping(value = "/book")
    public List<BooksResponse> getKeyWord(@RequestBody BookDataPageRequest model) {
        return bookDataService.getBookList(model);
    }

    @PostMapping(value = "/addBook")
    public boolean addBook(@RequestParam("files") MultipartFile files, AddBook addBook){
        if(!files.getOriginalFilename().matches(".*.jpg")){
            return false;
        }

        if(bookDataService.upData(files,addBook))
        return true;
        return false;
    }
//    @RequestMapping("/getPhoto")
//    public String download(HttpServletRequest request, @RequestParam("fileName") String fileName, HttpServletResponse response)throws Exception {
//        String image = photoService.getPhotoUrl(fileName);
//
//
//        return image;
//    }
}
