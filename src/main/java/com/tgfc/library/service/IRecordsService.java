package com.tgfc.library.service;

import com.tgfc.library.entity.Records;
import com.tgfc.library.request.SendMailRequest;
import com.tgfc.library.response.BaseResponse;
import org.springframework.data.domain.Pageable;

public interface IRecordsService {
    BaseResponse select(String name,Integer status, Pageable pageable);
    BaseResponse insert(String accountId);
    BaseResponse update(Records records);
    BaseResponse delete(Integer id);
    BaseResponse returnNotify(SendMailRequest model);
    BaseResponse returnBook(Integer id);
}
