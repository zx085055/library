package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Book;
import com.tgfc.library.entity.Recommend;
import com.tgfc.library.enums.BookStatusEnum;
import com.tgfc.library.repository.IBookRepository;
import com.tgfc.library.repository.IRecommendRepository;
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
    @Autowired
    IRecommendRepository iRecommendRepository;


    @Override
    public BaseResponse getBookList(BookDataPageRequest model) throws IOException{
        BaseResponse response = new BaseResponse();
        Pageable pageable = PageRequest.of(model.getPageNumber(), model.getPageSize());
        Page<Book> pageBook = bookDataRepository.findAllByKeyword(model.getKeyword(), pageable);
        if(pageBook.isEmpty()){
            response.setMessage("找不到資料");
            return response;
        }
        List<BooksResponse> list = new ArrayList<>();
        for (Book book : pageBook) {
            if (book.getPhotoOriginalName() != null && book.getPhotoOriginalName().length() != 0) {
                book.setPhotoOriginalName(photoService.getApiPhotoUrl(book.getPhotoOriginalName()));
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
        if (book.getPhotoOriginalName() != null && book.getPhotoOriginalName().length() != 0)
            book.setPhotoOriginalName(photoService.getApiPhotoUrl(book.getPhotoOriginalName()));
        response.setMessage("");
        response.setStatus(true);
        response.setData(book);
        return response;
    }

    @Override
    public BaseResponse upData(MultipartFile files, BookAddRequest addBook) {
        BaseResponse response = new BaseResponse();
        //判斷status是否正確
        if (BookStatusEnum.getStatus(addBook.getStatus()) != null) {
            //用Id去資料庫找出舊資料
            Book book = bookDataRepository.getById(addBook.getId());
            if (book == null) {
                book=new Book();
                try {
                    BeanUtils.copyProperties(addBook, book);
                } catch (BeansException e) {}
                book=bookDataRepository.save(book);
                Recommend recommend=iRecommendRepository.findRecommendByIsbn(book.getIsbn());
                if(recommend!=null)
                recommend.setStatus(2);
            }
            //是否有傳入檔案
            if (files != null) {
                //addBook.setPhotoName(files.getOriginalFilename());
                //存檔案
                photoService.uploadPhoto(files, book.getId().toString());
                //將Id設定成OriginalName
                book.setPhotoOriginalName(book.getId() + ".jpg");
            } else if (addBook.getPhotoName() == null || addBook.getPhotoName().length() == 0) {
                photoService.deletePhoto( book.getPhotoOriginalName());
                book.setPhotoOriginalName(null);
            }
            try {
                addBook.setId(book.getId());
                BeanUtils.copyProperties(addBook, book);
            } catch (BeansException e) {
            }
            if (bookDataRepository.save(book) != null)
                response.setStatus(true);
        } else {
            response.setMessage("Status錯誤");
        }
        return response;
    }

    @Override
    public BaseResponse deleteBook(Integer id) {
        BaseResponse baseResponse = new BaseResponse();
        Book book=bookDataRepository.getById(id);
        bookDataRepository.deleteById(id);
        if(bookDataRepository.getById(id)==null){
            baseResponse.setStatus(true);
            photoService.deletePhoto(book.getPhotoOriginalName());
        }
        return baseResponse;
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
