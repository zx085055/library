package com.tgfc.library.service;

import com.tgfc.library.request.SendMailRequest;
import com.tgfc.library.response.BaseResponse;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface IRecordsService {
    BaseResponse select(String name, Integer status, Pageable pageable);

    BaseResponse delete(Integer id);

    BaseResponse findAll(Pageable pageable);

    BaseResponse findByTimeInterval(Date startDate, Date endDate, Pageable pageable);

    BaseResponse returnNotify(SendMailRequest model);

    BaseResponse returnBook(Integer id);

    BaseResponse findByEmpId(Pageable pageable);

    BaseResponse findByTimeIntervalWithEmpId(Date startDate, Date endDate, Pageable pageable);

    BaseResponse renew(Integer bookId, Integer recordId);

}
