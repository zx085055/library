package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Book;
import com.tgfc.library.repository.IBookRepository;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class BookService implements IBookService {


    @Autowired
    IBookRepository bookDataRepository;





    @Override
    public Page<Book> getBooksList(BookDataPageRequest model)  {
        String keyword = model.getKeyword() == null ? "%" : "%" + model.getKeyword() + "%";

        Pageable pageable = PageRequest.of(model.getPageNumber(), model.getPageSize());

        return bookDataRepository.findAllByKeyword( keyword,pageable);
    }

    @Override
    public Book getById(int storeId) {
        //bookDataRepository.getOne(storeId);
        return bookDataRepository.getOne(storeId);
    }
}
