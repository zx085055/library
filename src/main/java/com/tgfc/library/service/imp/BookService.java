package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Book;
import com.tgfc.library.entity.Recommend;
import com.tgfc.library.entity.Records;
import com.tgfc.library.entity.Reservation;
import com.tgfc.library.enums.BookStatusEnum;
import com.tgfc.library.repository.IBookRepository;
import com.tgfc.library.repository.IRecommendRepository;
import com.tgfc.library.repository.IRecordsRepository;
import com.tgfc.library.repository.IReservationRepository;
import com.tgfc.library.request.BookAddRequest;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.response.BookCountResponse;
import com.tgfc.library.response.BooksResponse;
import com.tgfc.library.service.IBookService;
import com.tgfc.library.service.IPhotoService;
import com.tgfc.library.util.MessageUtil;
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

    private final
    IReservationRepository iReservationRepository;

    private final
    IRecordsRepository iRecordsRepository;

    private BaseResponse.Builder builder;

    public BookService(IBookRepository bookDataRepository, IPhotoService photoService, IRecommendRepository iRecommendRepository, IReservationRepository iReservationRepository, IRecordsRepository iRecordsRepository) {
        this.bookDataRepository = bookDataRepository;
        this.photoService = photoService;
        this.iRecommendRepository = iRecommendRepository;
        this.iReservationRepository = iReservationRepository;
        this.iRecordsRepository = iRecordsRepository;
    }


    @Override
    public BaseResponse getBookList(BookDataPageRequest model) {
        Page<Book> pageBook = null;
        builder = new BaseResponse.Builder();
        Pageable pageable = model.getPageable();
        String keyWord = model.getKeyword();
        if (model.getCheckPermission()) {
            if (keyWord.isEmpty()) {
                pageBook = bookDataRepository.findAll(pageable);
            } else {
                pageBook = bookDataRepository.findAllByKeyword(keyWord, pageable);
            }
        } else {
            if (keyWord.isEmpty()) {
                pageBook = bookDataRepository.findAllNotScrapByKeyword(pageable);
            } else {
                pageBook = bookDataRepository.findNotScrapByKeyword(keyWord, BookStatusEnum.BOOK_STATUS_SCRAP.getCode(), pageable);
            }
        }

        List<BooksResponse> list = new ArrayList<>();
        for (Book book : pageBook) {
            BooksResponse bookResponse = new BooksResponse();
            BeanUtils.copyProperties(book, bookResponse);
            list.add(bookResponse);
            if (book.getPhotoName() != null && book.getPhotoName().length() != 0) {
                bookResponse.setPhotoName(photoService.getPhotoUrl(book.getPhotoName()));
            }

        }

        BookCountResponse bookCountResponse = new BookCountResponse();
        if (pageBook.isEmpty()) {
            return builder.content(list).message(MessageUtil.getMessage("book.findNoData")).status(false).build();
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
            return builder.message(MessageUtil.getMessage("book.ISBNCodeCannotDuplicate")).status(false).build();

        } else {
            return builder.message(MessageUtil.getMessage("book.ISBNCodeNoDuplicate")).status(true).build();

        }
    }

    @Override
    public BaseResponse checkPropertyCode(BookAddRequest model) {
        builder = new BaseResponse.Builder();
        List<Book> exist;
        if (model.getId() != null && !"".equals(model.getId())) {
            exist = bookDataRepository.findByPropertyCodeAndId(model.getId(), model.getPropertyCode());
        } else {
            exist = bookDataRepository.findByPropertyCode(model.getPropertyCode());
        }
        if (exist.size() > 0) {
            return builder.message(MessageUtil.getMessage("book.produceCodeCannotDuplicate")).status(false).build();

        } else {
            return builder.message(MessageUtil.getMessage("book.produceCodeNoDuplicate")).status(true).build();

        }
    }

    @Override
    public BaseResponse upData(MultipartFile files, BookAddRequest addBook) {


        builder = new BaseResponse.Builder();
        //判斷status是否正確

        if (BookStatusEnum.getStatus(addBook.getStatus()) == null) {
            return builder.message(MessageUtil.getMessage("book.statusWrong")).build();

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
            return builder.message(MessageUtil.getMessage("book.saveFail")).build();

        }
        Book save;
        try {
            save = bookDataRepository.save(book);
        } catch (Exception e) {
            return builder.message(MessageUtil.getMessage("book.updateFail")).build();

        }

        if (save != null) {
            return builder.message(MessageUtil.getMessage("book.updateSuccess")).status(true).build();

        }

        return builder.message(MessageUtil.getMessage("book.abnormalCondition")).build();

    }

    @Override
    public BaseResponse deleteBook(int id) {
        builder = new BaseResponse.Builder();
        Book book = bookDataRepository.getById(id);
        if (book == null) {
            return builder.message(MessageUtil.getMessage("book.findNoID")).build();
        }
        try {
            for (Reservation reservation : iReservationRepository.getReservationByBookId(id)) {
                iReservationRepository.deleteById(reservation.getId());
            }
            for (Records records : iRecordsRepository.getRecordsByBookId(id)) {
                iRecordsRepository.deleteById(records.getId());
            }
            bookDataRepository.deleteById(id);
        } catch (Exception e) {
            return builder.message(MessageUtil.getMessage("book.deleteFail")).build();
        }


        if (bookDataRepository.getById(id) == null) {
            if (book.getPhotoName() != null && !book.getPhotoName().equals("")) {
                photoService.deletePhoto(book.getPhotoName());
            }
            return builder.message(MessageUtil.getMessage("book.deleteSuccess")).status(true).build();

        }
        return builder.message(MessageUtil.getMessage("book.abnormalCondition")).build();

    }

    @Override
    public BaseResponse findAll(Pageable pageable) {
        builder = new BaseResponse.Builder();
        Page<Book> books = bookDataRepository.findAll(pageable);
        return builder.content(books.getContent()).message(MessageUtil.getMessage("book.searchSuccess")).status(true).build();

    }

    @Override
    public BaseResponse findByKeyword(String keyword, Pageable pageable) {
        builder = new BaseResponse.Builder();
        Page<Book> books = bookDataRepository.findBookByKeyWord(keyword, pageable);
        return builder.content(books.getContent()).message(MessageUtil.getMessage("book.searchSuccess")).status(true).build();

    }


}
