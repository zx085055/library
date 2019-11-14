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
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    private BaseResponse.Builder builder;

    public BookService(IBookRepository bookDataRepository, IPhotoService photoService, IRecommendRepository iRecommendRepository) {
        this.bookDataRepository = bookDataRepository;
        this.photoService = photoService;
        this.iRecommendRepository = iRecommendRepository;
    }


    @Override
    public BaseResponse getBookList(BookDataPageRequest model) {
        builder = new BaseResponse.Builder();
        Pageable pageable = model.getPageable();
        Page<Book> pageBook = bookDataRepository.findAllByKeyword("%" + model.getKeyword() + "%", model.getKeyword(), pageable);

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
            return builder.content(list).message("找不到資料").status(false).build();
        }
        bookCountResponse.setList(list);
        bookCountResponse.setCount(pageBook.getTotalElements());
        return builder.content(bookCountResponse).status(true).build();
    }

    @Override
    public BaseResponse checkISBN(BookAddRequest model) {
        builder = new BaseResponse.Builder();
        List<Book> exist;
        if (model.getId() != null && !"".equals(model.getId())) {
            exist = bookDataRepository.findByIsbnAndId(model.getId(), model.getIsbn());
        } else {
            exist = bookDataRepository.findByIsbn(model.getIsbn());
        }
        if (exist.size() > 0) {
            return builder.message("ISBN碼不可以重複").status(false).build();

        } else {
            return builder.message("ISBN碼沒有重複").status(true).build();

        }
    }

    @Override
    public BaseResponse upData(MultipartFile files, BookAddRequest addBook) {


        builder = new BaseResponse.Builder();
        //判斷status是否正確

        if (BookStatusEnum.getStatus(addBook.getStatus()) == null) {
            return builder.message("狀態錯誤").build();

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
            String photoName = date.getTime() + "." + FilenameUtils.getExtension(files.getOriginalFilename());
            photoService.uploadPhoto(files, photoName);

            book.setPhotoName(photoName);
        } else if (addBook.getPhotoOriginalName() == null || addBook.getPhotoOriginalName().length() == 0) {
            photoService.deletePhoto(book.getPhotoName());
            book.setPhotoName(null);
        }
        try {
            BeanUtils.copyProperties(addBook, book);
        } catch (BeansException e) {
            return builder.message("儲存失敗").build();

        }
        Book save;
        try {
            save = bookDataRepository.save(book);
        } catch (Exception e) {
            return builder.message("修改失敗").build();

        }

        if (save != null) {
            return builder.message("編輯成功").status(true).build();

        }

        return builder.message("異常狀況").build();

    }

    @Override
    public BaseResponse deleteBook(int id) {
        builder = new BaseResponse.Builder();
        Book book = bookDataRepository.getById(id);
        if (book == null) {
            return builder.message("無此ID").build();
        }
        try {
            bookDataRepository.deleteById(id);
        } catch (Exception e) {
            return builder.message("無法刪除").build();
        }


        if (bookDataRepository.getById(id) == null) {
            if (book.getPhotoName() != null && !book.getPhotoName().equals("")) {
                photoService.deletePhoto(book.getPhotoName());
            }
            return builder.message("刪除成功").status(true).build();

        }
        return builder.message("異常狀況").build();

    }

    @Override
    public BaseResponse findAll(Pageable pageable) {
        builder = new BaseResponse.Builder();
        Page<Book> books = bookDataRepository.findAll(pageable);
        return builder.content(books.getContent()).message("查詢完成").status(true).build();

    }

    @Override
    public BaseResponse findByKeyword(String keyword, Pageable pageable) {
        builder = new BaseResponse.Builder();
        Page<Book> books = bookDataRepository.findBookByKeyWord(keyword, pageable);
        return builder.content(books.getContent()).message("查詢成功").status(true).build();

    }


}
