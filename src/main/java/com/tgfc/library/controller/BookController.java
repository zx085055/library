package com.tgfc.library.controller;

import com.tgfc.library.entity.Book;
import com.tgfc.library.request.AddBook;
import com.tgfc.library.request.BookDataPageRequest;
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
import java.io.File;
import java.util.Date;

@RestController

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
    public Page<Book> getKeyWord(@RequestBody BookDataPageRequest model) {
        return bookDataService.getBookList(model);
    }

    @PostMapping(value = "/book2")
    public void getKeyWord(@RequestParam("files") MultipartFile files) {
       // photoService.uploadPhoto(files);
        return ;
    }


    @PostMapping(value = "/addBook")
    public void addBook(@RequestParam("files") MultipartFile files, String name, String author, String isbn, String pubHouse, String intro, Integer price, Date purchaseDate,Date publishDate,String type) {
        AddBook addBook=new AddBook();
        addBook.setName(name);
        addBook.setAuthor(author);
        addBook.setIsbn(isbn);
        addBook.setPubHouse(pubHouse);
        addBook.setIntro(intro);
        addBook.setPrice(price);
        addBook.setPurchaseDate(purchaseDate);
        addBook.setPublishDate(publishDate);
        addBook.setType(type);
           // photoService.uploadPhoto(files,addBook);
        bookDataService.upData(files,addBook);

    }
    @RequestMapping("/getPhoto")
    public ResponseEntity<byte[]> download(HttpServletRequest request, @RequestParam("fileName") String fileName, HttpServletResponse response)throws Exception {
        byte[] image = photoService.getPhoto(response,fileName);


        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);

    }
}