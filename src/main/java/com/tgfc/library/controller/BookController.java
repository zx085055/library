package com.tgfc.library.controller;

import com.tgfc.library.enums.PermissionEnum;
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
import javax.annotation.security.RolesAllowed;
import java.io.IOException;

//import org.apache.tomcat.util.http.fileupload.FileUtils;

@RestController
@RequestMapping("/book")
public class BookController {




    private final IBookService bookDataService;

    private final IPhotoService photoService;

    @Autowired
    public BookController(IBookService bookDataService,IPhotoService photoService) {
        this.bookDataService = bookDataService;
        this.photoService = photoService;
    }


    @RolesAllowed({PermissionEnum.Role.ADMIN,PermissionEnum.Role.USER})
    @GetMapping(value = "/find")
    public BaseResponse get(@RequestParam("id") Integer id) throws IOException{
        return bookDataService.getById(id);
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN,PermissionEnum.Role.USER})
    @PostMapping(value = "/books")
    public BaseResponse getKeyWord(@RequestBody BookDataPageRequest model) throws IOException {
        return bookDataService.getBookList(model);
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PostMapping(value = "/addBook")
    public BaseResponse addBook( MultipartFile files, BookAddRequest bookAddRequest){
        BaseResponse baseResponse=new BaseResponse();
        if(files.getOriginalFilename()!=null&&!files.getOriginalFilename().matches("(.+)(\\.jpg|\\.gif|\\.jpeg|\\.png){1}$")){
            baseResponse.setMessage("上傳格式錯誤");
            return baseResponse;
        }
        baseResponse=bookDataService.upData(files, bookAddRequest);
        return baseResponse;
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @DeleteMapping(value = "/deleteBook")
    public BaseResponse deleteBook(@RequestParam("id") Integer id) {
        return bookDataService.deleteBook(id);
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN,PermissionEnum.Role.USER})
    @RequestMapping("/getPhoto")
    public ResponseEntity<byte[]> showPhoto( @RequestParam("fileName") String fileName)throws Exception {
            byte[] image = photoService.getPhoto(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN,PermissionEnum.Role.USER})
    @PostMapping(value = "/findAll")
    public BaseResponse findAll(@RequestBody PageableRequest pageableRequest){
        return bookDataService.findAll(pageableRequest.getPageable());
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN,PermissionEnum.Role.USER})
    @PostMapping(value = "/findByKeyword")
    public BaseResponse findByKeyword(@RequestBody BookDataPageRequest bookDataPageRequest){
        return bookDataService.findByKeyword(bookDataPageRequest.getKeyword(),bookDataPageRequest.getPageable());
    }

}
