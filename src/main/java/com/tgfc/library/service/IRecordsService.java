package com.tgfc.library.service;

import com.tgfc.library.request.SendMailRequest;
import com.tgfc.library.response.BaseResponse;
import org.springframework.data.domain.Pageable;

public interface IRecordsService {
    BaseResponse select(String name,Integer status, Pageable pageable);
    BaseResponse returnNotify(SendMailRequest model);
    BaseResponse returnBook(Integer id);
}
