package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Book;
import com.tgfc.library.entity.Recommend;
import com.tgfc.library.enums.BookStatusEnum;
import com.tgfc.library.repository.IBookRepository;
import com.tgfc.library.repository.IRecommendRepository;
import com.tgfc.library.request.BookAddRequest;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.response.BookCountResponse;
import com.tgfc.library.response.BooksResponse;
import com.tgfc.library.service.IBookService;
import com.tgfc.library.service.IPhotoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class BookService implements IBookService {


    private final
    IBookRepository bookDataRepository;

    private final
    IPhotoService photoService;
    private final
    IRecommendRepository iRecommendRepository;


    public BookService(IBookRepository bookDataRepository, IPhotoService photoService, IRecommendRepository iRecommendRepository) {
        this.bookDataRepository = bookDataRepository;
        this.photoService = photoService;
        this.iRecommendRepository = iRecommendRepository;
    }


    @Override
    public BaseResponse getBookList(BookDataPageRequest model) throws FileNotFoundException {
        BaseResponse response = new BaseResponse();
        Pageable pageable = model.getPageable();
        Page<Book> pageBook = bookDataRepository.findAllByKeyword("%" + model.getKeyword() + "%", pageable);

        List<BooksResponse> list = new ArrayList<>();
        for (Book book : pageBook) {

            book.setStatus(book.getStatus());
            BooksResponse bookResponse = new BooksResponse();
            BeanUtils.copyProperties(book, bookResponse);
            list.add(bookResponse);
            if (book.getPhotoName() != null && book.getPhotoName().length() != 0) {

                if (book.getPhotoName() != null && book.getPhotoName().length() != 0) {
                    bookResponse.setPhotoName(photoService.getPhotoUrl(book.getPhotoName()));
                }
            }
        }

        BookCountResponse bookCountResponse = new BookCountResponse();
        if (pageBook.isEmpty()) {
            response.setMessage("找不到資料");
            response.setStatus(false);
            bookCountResponse.setList(list);
            return response;
        }
        bookCountResponse.setList(list);
        bookCountResponse.setCount(pageBook.getTotalElements());
        response.setData(bookCountResponse);
        response.setStatus(true);
        return response;
    }

    @Override
    public BaseResponse getById(int id) {
        BaseResponse response = new BaseResponse();
        Book book = bookDataRepository.getById(id);
        if (book.getPhotoName() != null && book.getPhotoName().length() != 0)
            book.setPhotoName(photoService.getApiPhotoUrl(book.getPhotoName()));
        response.setMessage("");
        response.setStatus(true);
        response.setData(book);
        return response;
    }

    @Override
    public BaseResponse upData(MultipartFile files, BookAddRequest addBook) {


        BaseResponse response = new BaseResponse();
        //判斷status是否正確

        if (BookStatusEnum.getStatus(addBook.getStatus()) == null) {
            response.setMessage("狀態錯誤");
            return response;
        }
        //用Id去資料庫找出舊資料
        Book book = bookDataRepository.getById(addBook.getId());
        if (book == null) {
            book = new Book();
            Recommend recommend = iRecommendRepository.findRecommendByIsbn(addBook.getIsbn());
            if (recommend != null)
                recommend.setStatus(2);
        }
        //是否有傳入檔案
        if (files != null) {
            addBook.setPhotoOriginalName(files.getOriginalFilename());
            //存檔案
            Date date = new Date();
            String photoName = date.getTime() + ".jpg";
            photoService.uploadPhoto(files, photoName);

            book.setPhotoName(photoName);
        } else if (addBook.getPhotoOriginalName() == null || addBook.getPhotoOriginalName().length() == 0) {
            photoService.deletePhoto(book.getPhotoName());
            book.setPhotoName(null);
        }
        try {
//                addBook.setId(book.getId());
            BeanUtils.copyProperties(addBook, book);
        } catch (BeansException e) {
            response.setMessage("儲存失敗");
        }
//            bookDataRepository.save(book);
        if (bookDataRepository.save(book) != null){
//            Book book1 =bookDataRepository.getById(book.getId());
            response.setStatus(true);
            response.setMessage("編輯成功");
        }


        return response;
    }

    @Override
    public BaseResponse deleteBook(int id) {
        BaseResponse baseResponse = new BaseResponse();
        Book book = bookDataRepository.getById(id);
        if (book == null) {
            baseResponse.setMessage("無此ID");
            return baseResponse;
        }
        try {
            bookDataRepository.deleteById(id);
        } catch (Exception e) {
            baseResponse.setMessage("無法刪除");
            e.printStackTrace();
        }


        if (bookDataRepository.getById(id) == null) {
            if(book.getPhotoName()!=""&&book.getPhotoName()!=null){
                photoService.deletePhoto(book.getPhotoName());}
            baseResponse.setStatus(true);
            baseResponse.setMessage("刪除成功");
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
        Page<Book> books = bookDataRepository.findBookByKeyWord(keyword, pageable);
        baseResponse.setData(books.getContent());
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }


}
