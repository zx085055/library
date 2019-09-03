package com.tgfc.library.service;




import com.tgfc.library.entity.Book;
import com.tgfc.library.request.AddBook;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.response.BooksResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IBookService {
    BaseResponse getBookList(BookDataPageRequest model) throws IOException;
    BaseResponse getById(int storeId)throws IOException;
    BaseResponse upData(MultipartFile files,  AddBook addBook);
    BaseResponse findAll(Pageable pageable);
    BaseResponse findByKeyword(String keyword,Pageable pageable);
}

