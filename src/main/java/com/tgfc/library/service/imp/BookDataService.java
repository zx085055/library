package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Books;
import com.tgfc.library.repository.IBookDataRepository;
import com.tgfc.library.request.BookDataPageRequest;
import com.tgfc.library.service.IBookDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class BookDataService implements IBookDataService {


    @Autowired
    IBookDataRepository bookDataRepository;





    @Override
    public Page<Books> getBooksList(BookDataPageRequest model)  {
        String keyword = model.getKeyword() == null ? "%" : "%" + model.getKeyword() + "%";

        Pageable pageable = PageRequest.of(model.getPageNumber(), model.getPageSize());

        return bookDataRepository.findAllByKeyword( keyword,pageable);
    }

    @Override
    public Books getById(int storeId) {
        //bookDataRepository.getOne(storeId);
        return bookDataRepository.getOne(storeId);
    }
}
