package com.tgfc.library.controller;

import com.tgfc.library.request.BookAddRequest;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.request.PageableRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IBookService;
import com.tgfc.library.service.IPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//import org.apache.tomcat.util.http.fileupload.FileUtils;

@RestController
@RequestMapping("/book")
public class BookController {


    @Autowired
    IBookService bookDataService;

    @Autowired
    IPhotoService photoService;

    @GetMapping(value = "/find")
    public BaseResponse get(@RequestParam("id") Integer id) throws IOException{
        return bookDataService.getById(id);
    }

    @PostMapping(value = "/books")
    public BaseResponse getKeyWord(@RequestBody BookDataPageRequest model) throws IOException {
        return bookDataService.getBookList(model);
    }

    @PostMapping(value = "/addBook")
    public BaseResponse addBook( MultipartFile files, BookAddRequest bookAddRequest){
        BaseResponse baseResponse=new BaseResponse();
        if(files!=null&&!files.getOriginalFilename().matches(".*.jpg")){
            baseResponse.setMessage("上傳格式錯誤");
            return baseResponse;
        }
        baseResponse=bookDataService.upData(files, bookAddRequest);
        return baseResponse;
    }
    @RequestMapping("/getPhoto")
    public ResponseEntity<byte[]> download( @RequestParam("fileName") String fileName)throws Exception {
        
            byte[] image = photoService.getPhoto(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);

    }

    @PostMapping(value = "/findAll")
    public BaseResponse findAll(@RequestBody PageableRequest pageableRequest){
        return bookDataService.findAll(pageableRequest.getPageable());
    }

    @PostMapping(value = "/findByKeyword")
    public BaseResponse findByKeyword(@RequestBody BookDataPageRequest bookDataPageRequest){
        return bookDataService.findByKeyword(bookDataPageRequest.getKeyword(),bookDataPageRequest.getPageable());
    }
}
