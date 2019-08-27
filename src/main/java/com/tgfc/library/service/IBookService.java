package com.tgfc.library.service;




import com.tgfc.library.entity.Book;
import com.tgfc.library.request.BookDataPageRequest;
import org.springframework.data.domain.Page;

public interface IBookService {
    Page<Book> getBooksList(BookDataPageRequest model) ;
    Book getById(int storeId);
}

