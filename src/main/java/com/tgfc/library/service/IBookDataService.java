package com.tgfc.library.service;




import com.tgfc.library.entity.Books;
import com.tgfc.library.request.BookDataPageRequest;
import org.springframework.data.domain.Page;

public interface IBookDataService {
    Page<Books> getBooksList(BookDataPageRequest model) ;
    Books getById(int storeId);
}

