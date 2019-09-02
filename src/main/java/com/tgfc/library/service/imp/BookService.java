package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Book;
import com.tgfc.library.enums.BookStatus;
import com.tgfc.library.repository.IBookRepository;
import com.tgfc.library.request.AddBook;
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

import java.util.ArrayList;
import java.util.List;


@Service
public class BookService implements IBookService {

    @Autowired
    IBookRepository bookDataRepository;

    @Autowired
    PhotoService photoService;

    @Override
    public BaseResponse getBookList(BookDataPageRequest model) {
        BaseResponse response = new BaseResponse();
        String keyword = model.getKeyword() == null ? "%" : "%" + model.getKeyword() + "%";
        Pageable pageable = PageRequest.of(model.getPageNumber(), model.getPageSize());
        Page<Book> pageBook=bookDataRepository.findAllByKeyword(keyword, pageable);
        List<BooksResponse> list=new ArrayList<>();
        for(Book book : pageBook){
            if(book.getOriginalName()!=null && book.getOriginalName().length()!=0){
                book.setOriginalName(photoService.getPhotoUrl(book.getOriginalName()));
            }

            book.setStatus(book.getStatus());
            BooksResponse bookResponse=new BooksResponse();
            BeanUtils.copyProperties(book, bookResponse);
            list.add(bookResponse);
            response.setData(list);
            response.setStatus(true);
        }
        return response;
    }

    @Override
    public BaseResponse getById(int id) {
        BaseResponse response = new BaseResponse();
        Book book =bookDataRepository.getById(id);
        if(book.getOriginalName()!=null && book.getOriginalName().length()!=0)
        book.setOriginalName(photoService.getPhotoUrl(book.getOriginalName()));
        response.setMessage("");
        response.setStatus(true);
        response.setData(book);
        return response;
    }

    @Override
    public BaseResponse upData(MultipartFile files, AddBook addBook) {
        BaseResponse response = new BaseResponse();
            if( BookStatus.getStatus(addBook.getStatus())!=null){
        Book book = bookDataRepository.getById(addBook.getId());
        if (book == null) {
            book = new Book();
        }
        try {
            BeanUtils.copyProperties(addBook, book);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        if (files != null) {
            book.setPhotoName(files.getOriginalFilename());
            photoService.uploadPhoto(files, book.getIsbn());
            book.setOriginalName(book.getIsbn()+".jpg");
        }else{
            book.setPhotoName(files.getOriginalFilename());
            photoService.deletePhoto(files, book.getIsbn());
            book.setOriginalName(null);
        }
        if(bookDataRepository.save(book)!=null)
            response.setStatus(true);
            return response;
        }else{
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
}
