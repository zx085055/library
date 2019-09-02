package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Records;
import com.tgfc.library.enums.BookStatus;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.repository.IRecordsRepository;
import com.tgfc.library.request.SendMailRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IRecordsService;
import com.tgfc.library.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class RecordsService implements IRecordsService {
    @Autowired
    IRecordsRepository recordsRepository;

    @Autowired
    IEmployeeRepository employeeRepository;

    @Override
    public BaseResponse select(String keyword, Integer status, Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        if (keyword == null || status == null) {
            baseResponse.setData(recordsRepository.findAll(pageable));
            baseResponse.setMessage("查詢成功");
            baseResponse.setStatus(true);
            return baseResponse;
        } else {
            baseResponse.setData(recordsRepository.getRecordsByNameLikeAndStatus(keyword, status, pageable));
            baseResponse.setMessage("查詢成功");
            baseResponse.setStatus(true);
            return baseResponse;
        }
    }

    @Override
    public BaseResponse returnNotify(SendMailRequest model) {
        BaseResponse baseResponse = new BaseResponse();
        Records records = recordsRepository.findById(model.getId()).get();
        model.setEmail(employeeRepository.getOne(records.getBorrowId()).getEmail());
        MailUtil.sendMail(model.getTitle(), model.getContext(), model.getEmail());
        baseResponse.setData(model);
        baseResponse.setStatus(true);
        baseResponse.setMessage("通知成功");
        return baseResponse;
    }

    @Override
    public BaseResponse returnBook(Integer id) {
        BaseResponse baseResponse = new BaseResponse();
        Records records = recordsRepository.findById(id).get();
        records.setStatus(BookStatus.BOOK_STATUS_INSIDE.getCode());
        return null;
    }
}
