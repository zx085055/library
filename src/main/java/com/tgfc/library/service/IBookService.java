package com.tgfc.library.service;




import com.tgfc.library.entity.Book;
import com.tgfc.library.request.AddBook;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.response.BooksResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBookService {
    BaseResponse getBookList(BookDataPageRequest model) ;
    BaseResponse getById(int storeId);
    BaseResponse upData(MultipartFile files,  AddBook addBook);
}

