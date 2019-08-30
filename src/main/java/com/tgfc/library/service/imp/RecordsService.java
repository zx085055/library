package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Records;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.repository.IRecordsRepository;
import com.tgfc.library.request.SendMailRequest;
import com.tgfc.library.service.IRecordsService;
import com.tgfc.library.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RecordsService implements IRecordsService {
    @Autowired
    IRecordsRepository recordsRepository;

    @Autowired
    IEmployeeRepository employeeRepository;

//    @Override
//    public Page<Records> select(String name, Pageable pageable) {
//        if (name==null){
//            return recordsRepository.findAll(pageable);
//        }
//        else {
//            return recordsRepository.getRecordsByNameLike(name,pageable);
//        }
//    }

    @Override
    public Boolean returnNotify(SendMailRequest model) {
        Records records = recordsRepository.findById(model.getId()).get();
        model.setEmail(employeeRepository.getOne(records.getBorrowId()).getEmail());
        MailUtil.sendMail(model.getTitle(), model.getContext(), model.getEmail());
        return true;
    }
}
