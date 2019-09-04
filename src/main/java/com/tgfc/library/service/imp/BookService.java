package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Book;
import com.tgfc.library.enums.BookStatusEnum;
import com.tgfc.library.repository.IBookRepository;
import com.tgfc.library.request.BookAddRequest;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.response.BooksResponse;
import com.tgfc.library.service.IBookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class BookService implements IBookService {

    @Autowired
    IBookRepository bookDataRepository;

    @Autowired
    PhotoService photoService;

    @Override
    public BaseResponse getBookList(BookDataPageRequest model) throws IOException{
        BaseResponse response = new BaseResponse();
        Pageable pageable = PageRequest.of(model.getPageNumber(), model.getPageSize());
        Page<Book> pageBook = bookDataRepository.findAllByKeyword(model.getKeyword(), pageable);
        List<BooksResponse> list = new ArrayList<>();
        for (Book book : pageBook) {
            if (book.getOriginalName() != null && book.getOriginalName().length() != 0) {
                book.setOriginalName(photoService.getPhotoUrl(book.getOriginalName()));
            }
            book.setStatus(book.getStatus());
            BooksResponse bookResponse = new BooksResponse();
            BeanUtils.copyProperties(book, bookResponse);
            list.add(bookResponse);
            response.setData(list);
            response.setStatus(true);
        }
        return response;
    }

    @Override
    public BaseResponse getById(int id) throws IOException{
        BaseResponse response = new BaseResponse();
        Book book = bookDataRepository.getById(id);
        if (book.getOriginalName() != null && book.getOriginalName().length() != 0)
            book.setOriginalName(photoService.getPhotoUrl(book.getOriginalName()));
        response.setMessage("");
        response.setStatus(true);
        response.setData(book);
        return response;
    }

    @Override
    public BaseResponse upData(MultipartFile files, BookAddRequest bookAddRequest) {
        BaseResponse response = new BaseResponse();
        //判斷status是否正確
        if (BookStatusEnum.getStatus(bookAddRequest.getStatus()) != null) {
            //用Id去資料庫找出舊資料
            Book book = bookDataRepository.getById(bookAddRequest.getId());

            if (book == null) {
                book = new Book();
            }

            //是否有傳入檔案
            if (files != null) {
                //設定PhotoName
                bookAddRequest.setPhotoName(files.getOriginalFilename());
                //存檔案
                photoService.uploadPhoto(files, book.getId().toString());
                //將Id設定成OriginalName
                bookAddRequest.setOriginalName(book.getId() + ".jpg");

            } else if (bookAddRequest.getPhotoName() == null || bookAddRequest.getPhotoName().length() == 0) {
                photoService.deletePhoto( book.getOriginalName());
                bookAddRequest.setOriginalName(null);
            }
            try {
                BeanUtils.copyProperties(bookAddRequest, book);
            } catch (BeansException e) {
                e.printStackTrace();
            }
            if (bookDataRepository.save(book) != null)
                response.setStatus(true);
        } else {
            response.setMessage("Status錯誤");
        }
        return response;
    }

    @Override
    public BaseResponse findAll(Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        Page<Book> books = bookDataRepository.findAll(pageable);
        baseResponse.setData(books.getContent());
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢完成");
        return baseResponse;
    }

    @Override
    public BaseResponse findByKeyword(String keyword, Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        Page<Book> books = bookDataRepository.findBookByKeyWord(keyword,pageable);
        baseResponse.setData(books.getContent());
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }
}
