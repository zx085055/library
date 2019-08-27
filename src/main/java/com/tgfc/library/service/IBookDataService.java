package com.tgfc.library.service;




import com.tgfc.library.entity.BookData;
import com.tgfc.library.request.BookDataPageRequest;
import org.springframework.data.domain.Page;

import java.io.FileNotFoundException;
import java.util.List;

public interface IBookDataService {
    Page<BookData> getBooksList(BookDataPageRequest model) ;
    BookData getById(int storeId);
}

