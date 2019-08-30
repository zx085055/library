package com.tgfc.library.controller;

import com.tgfc.library.request.AddBook;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IBookService;
import com.tgfc.library.service.IPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//import org.apache.tomcat.util.http.fileupload.FileUtils;

@RestController
@RequestMapping("/book")
public class BookController {


    @Autowired
    IBookService bookDataService;

    @Autowired
    IPhotoService photoService;

    @GetMapping(value = "/find")
    public BaseResponse get(@RequestParam("id") Integer id) {
        return bookDataService.getById(id);
    }

    @PostMapping(value = "/books")
    public BaseResponse getKeyWord(@RequestBody BookDataPageRequest model) {
        return bookDataService.getBookList(model);
    }

    @PostMapping(value = "/addBook")
    public BaseResponse addBook(@RequestParam("files") MultipartFile files, AddBook addBook){
        BaseResponse baseResponse=new BaseResponse();
        baseResponse.setStatus(false);
        if(!files.getOriginalFilename().matches(".*.jpg")){
            baseResponse.setMessage("上傳格式錯誤");
            return baseResponse;
        }
        baseResponse=bookDataService.upData(files,addBook);
        return baseResponse;
    }
//    @RequestMapping("/getPhoto")
//    public String download(HttpServletRequest request, @RequestParam("fileName") String fileName, HttpServletResponse response)throws Exception {
//        String image = photoService.getPhotoUrl(fileName);
//
//
//        return image;
//    }
}
