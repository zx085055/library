package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Book;
import com.tgfc.library.repository.IBookRepository;
import com.tgfc.library.request.AddBook;
import com.tgfc.library.request.BookDataPageRequest;
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
    public List<BooksResponse> getBookList(BookDataPageRequest model) {
        String keyword = model.getKeyword() == null ? "%" : "%" + model.getKeyword() + "%";

        Pageable pageable = PageRequest.of(model.getPageNumber(), model.getPageSize());
        Page<Book> pageBook=bookDataRepository.findAllByKeyword(keyword, pageable);

//        pageBook.stream().forEach(book -> );
        List<BooksResponse> list=new ArrayList<>();
        for(Book book : pageBook){
            if(book.getOriginalName()!=null && book.getOriginalName().length()!=0){
                book.setOriginalName(photoService.getPhotoUrl(book.getOriginalName()));

            }
            BooksResponse bookResponse=new BooksResponse();
            BeanUtils.copyProperties(book, bookResponse);
            list.add(bookResponse);
        }
//        Page<BooksResponse> booksResponse=null;
//
//        BeanUtils.copyProperties(booksResponse, pageBook);
        return list;
    }

    @Override
    public Book getById(int id) {
        //bookDataRepository.getOne(storeId);
        Book book =bookDataRepository.getById(id);
        if(book.getOriginalName()!=null && book.getOriginalName().length()!=0)
        book.setOriginalName(photoService.getPhotoUrl(book.getOriginalName()));
        return book;
    }

    @Override
    public boolean upData(MultipartFile files, AddBook addBook) {

        //addBook.setPhotoUrl(files.getOriginalFilename());
        Book book = bookDataRepository.getById(addBook.getId());
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

            book.setPhotoName(files.getOriginalFilename());
            photoService.uploadPhoto(files, book.getIsbn());
            book.setOriginalName(book.getIsbn()+".jpg");

        }else{
            book.setPhotoName(files.getOriginalFilename());
            photoService.deletePhoto(files, book.getIsbn());
            book.setOriginalName(null);
        }
        //BeanUtils.copyProperties(addBook,book);
       // book.setPhotoUrl(book.getIsbn()+".jpg");
        if(bookDataRepository.save(book)!=null){
            return true;
        }
        return false;
    }
}
