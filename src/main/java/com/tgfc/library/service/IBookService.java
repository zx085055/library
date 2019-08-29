package com.tgfc.library.service;




import com.tgfc.library.entity.Book;
import com.tgfc.library.request.AddBook;
import com.tgfc.library.request.BookDataPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface IBookService {
    Page<Book> getBookList(BookDataPageRequest model) ;
    Book getById(int storeId);
    String upData(MultipartFile files,  AddBook addBook);
}

