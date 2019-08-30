package com.tgfc.library.service;

import com.tgfc.library.entity.Records;
import com.tgfc.library.request.SendMailRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRecordsService {
//    Page<Records> select(String name, Pageable pageable);
    Boolean returnNotify(SendMailRequest model);
}
