package com.tgfc.library.controller;

import com.tgfc.library.enums.PermissionEnum;
import com.tgfc.library.request.BookAddRequest;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.request.PageableRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IBookService;
import com.tgfc.library.service.IPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;


@RestController
@RequestMapping("/book")
public class BookController {

    private final IBookService bookDataService;

    private final IPhotoService photoService;

    @Autowired
    public BookController(IBookService bookDataService, IPhotoService photoService) {
        this.bookDataService = bookDataService;
        this.photoService = photoService;
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN, PermissionEnum.Role.USER})
    @PostMapping(value = "/books")
    public BaseResponse getKeyWord(@RequestBody BookDataPageRequest model) throws IOException {
        return bookDataService.getBookList(model);
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PostMapping(value = "/addBook")
    public BaseResponse addBook(MultipartFile files, BookAddRequest bookAddRequest) {
        BaseResponse baseResponse = new BaseResponse();
        if (files != null && files.getOriginalFilename() != null && !files.getOriginalFilename().matches("(.+)(\\.jpg|\\.gif|\\.jpeg|\\.png){1}$")) {
            baseResponse.setMessage("上傳格式錯誤");
            return baseResponse;
        }
        baseResponse = bookDataService.update(files, bookAddRequest);
        return baseResponse;
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PostMapping(value = "/UpdateBook")
    public BaseResponse UpdateBook(MultipartFile files) {
        BaseResponse baseResponse = new BaseResponse();

        photoService.uploadPhoto(files, files.getOriginalFilename());
        return baseResponse;
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @DeleteMapping(value = "/deleteBook")
    public BaseResponse deleteBook(@RequestParam("id") int id) {

        return bookDataService.deleteBook(id);
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN, PermissionEnum.Role.USER})
    @PostMapping(value = "/findAll")
    public BaseResponse findAll(@RequestBody PageableRequest pageableRequest) {
        return bookDataService.findAll(pageableRequest.getPageable());
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN, PermissionEnum.Role.USER})
    @PostMapping(value = "/findByKeyword")
    public BaseResponse findByKeyword(@RequestBody BookDataPageRequest bookDataPageRequest) {
        return bookDataService.findByKeyword(bookDataPageRequest.getKeyword(), bookDataPageRequest.getPageable());
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @GetMapping(value = "/checkISBN")
    public BaseResponse checkISBN(@RequestParam("isbn") String isbn, @RequestParam("id") Integer id) {
        BookAddRequest model = new BookAddRequest();
        model.setIsbn(isbn);
        model.setId(id);
        return bookDataService.checkISBN(model);
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @GetMapping(value = "/checkPropertyCode")
    public BaseResponse checkPropertyCode(@RequestParam("propertyCode") String propertyCode, @RequestParam("id") Integer id) {
        BookAddRequest model = new BookAddRequest();
        model.setPropertyCode(propertyCode);
        model.setId(id);
        return bookDataService.checkPropertyCode(model);
    }
}
