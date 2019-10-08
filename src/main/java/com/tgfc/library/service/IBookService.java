package com.tgfc.library.service;




import com.tgfc.library.request.BookAddRequest;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.response.BaseResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IBookService {
    BaseResponse getBookList(BookDataPageRequest model) throws IOException;

    BaseResponse getById(int storeId)throws IOException;

    BaseResponse upData(MultipartFile files,  BookAddRequest bookAddRequest);

    BaseResponse findAll(Pageable pageable);

    BaseResponse findByKeyword(String keyword,Pageable pageable);

    BaseResponse deleteBook(int id);

}

