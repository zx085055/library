package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Book;
import com.tgfc.library.repository.IBookRepository;
import com.tgfc.library.request.AddBook;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.service.IBookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class BookService implements IBookService {


    @Autowired
    IBookRepository bookDataRepository;


    @Override
    public Page<Book> getBookList(BookDataPageRequest model) {
        String keyword = model.getKeyword() == null ? "%" : "%" + model.getKeyword() + "%";

        Pageable pageable = PageRequest.of(model.getPageNumber(), model.getPageSize());

        return bookDataRepository.findAllByKeyword(keyword, pageable);
    }

    @Override
    public Book getById(int id) {
        //bookDataRepository.getOne(storeId);
        return bookDataRepository.getById(id);
    }

    @Override
    public String upData(MultipartFile files, AddBook addBook) {

        //addBook.setPhotoUrl(files.getOriginalFilename());
        Book book = bookDataRepository.findByIsbn(addBook.getIsbn());
        //book =bookDataRepository.findByIsbn(addBook.getIsbn());
        if (book == null) {
            book = new Book();
        }
        try {
            BeanUtils.copyProperties(addBook, book);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        if (files != null) {
            PhotoService photoService = new PhotoService();
            photoService.uploadPhoto(files, book.getIsbn());
            book.setPhotoUrl(book.getIsbn()+".jpg");
        }
        //BeanUtils.copyProperties(addBook,book);

        bookDataRepository.save(book);

        return null;
    }
}
