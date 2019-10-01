package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Records;
import com.tgfc.library.entity.Reservation;
import com.tgfc.library.enums.BookStatusEnum;
import com.tgfc.library.enums.RecordsStatusEnum;
import com.tgfc.library.enums.ReservationEnum;
import com.tgfc.library.repository.IBookRepository;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.repository.IRecordsRepository;
import com.tgfc.library.repository.IReservationRepository;
import com.tgfc.library.request.SendMailRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IRecordsService;
import com.tgfc.library.util.ContextUtil;
import com.tgfc.library.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class RecordsService implements IRecordsService {
    @Autowired
    IRecordsRepository recordsRepository;

    @Autowired
    IEmployeeRepository employeeRepository;

    @Autowired
    IBookRepository bookRepository;

    @Autowired
    IReservationRepository reservationRepository;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse select(String keyword, Integer status, Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        if (status.equals(RecordsStatusEnum.RECORDSSTATUS_ALL.getCode())) {
            Page<Records> records = recordsRepository.getRecordsByNameLike(keyword, pageable);
            Map<String,Object> data = new HashMap<>();
            data.put("totalCount",records.getTotalElements());
            data.put("results",records.getContent());
            baseResponse.setData(data);
            baseResponse.setMessage("查詢成功");
            baseResponse.setStatus(true);
        } else {
            Page<Records> records = recordsRepository.getRecordsByNameLikeAndStatus(keyword, status, pageable);
            Map<String,Object> data = new HashMap<>();
            data.put("totalCount",records.getTotalElements());
            data.put("results",records.getContent());
            baseResponse.setData(data);
            baseResponse.setMessage("查詢成功");
            baseResponse.setStatus(true);
        }

        return baseResponse;
    }

    @Override
    public BaseResponse delete(Integer id) {
        BaseResponse baseResponse = new BaseResponse();
        if (!recordsRepository.existsById(id)) {
            baseResponse.setMessage("無此資料");
            baseResponse.setStatus(false);
        }
        recordsRepository.deleteById(id);
        baseResponse.setMessage("刪除成功");
        baseResponse.setStatus(true);
        return baseResponse;
    }

    @Override
    public BaseResponse returnNotify(SendMailRequest model) {
        BaseResponse baseResponse = new BaseResponse();
        Records records = recordsRepository.findById(model.getId()).get();
        model.setEmail(employeeRepository.findById(records.getBorrowId()).get().getEmail());
        MailUtil.sendMail(model.getTitle(), model.getContext(), model.getEmail());
        baseResponse.setMessage("通知成功");
        baseResponse.setStatus(true);
        return baseResponse;
    }

    @Override
    public BaseResponse returnBook(Integer id) {
        BaseResponse baseResponse = new BaseResponse();
        Records records = recordsRepository.findById(id).get();
        Reservation nextReservation = reservationRepository.getReservationByStatusAndBookId(ReservationEnum.RESERVATION_WAIT.getCode(), records.getBook().getId());
        if(nextReservation!=null) {
            MailUtil.sendMail("取書通知", "親愛的" + nextReservation.getEmployee().getName() + "先生/小姐，您可以來圖書館取書了。", nextReservation.getEmployee().getEmail());
            nextReservation.setStatus(ReservationEnum.RESERVATION_ALIVE.getCode());
            reservationRepository.save(nextReservation);
        }
        records.setStatus(RecordsStatusEnum.RECORDSSTATUS_RETURNED.getCode());
        records.getBook().setStatus(BookStatusEnum.BOOK_STATUS_INSIDE.getCode());
        recordsRepository.save(records);
        baseResponse.setStatus(true);
        baseResponse.setMessage("歸還成功");
        return baseResponse;
    }

    @Override
    public BaseResponse findAll(Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        Page<Records> records = recordsRepository.findAll(pageable);
        Map<String ,Object> data = new HashMap<>();
        data.put("totalCount",records.getTotalElements());
        data.put("results",records.getContent());
        baseResponse.setData(data);
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }

    @Override
    public BaseResponse findByTimeInterval(Date startDate, Date endDate, Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        Page<Records> reservations = recordsRepository.findByTimeInterval(startDate, endDate, pageable);
        Map<String,Object> data = new HashMap<>();
        data.put("totalCount",reservations.getTotalElements());
        data.put("results",reservations.getContent());
        baseResponse.setData(data);
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }

    @Override
    public BaseResponse findByEmpId(Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        String empId = ContextUtil.getAccount();
        Page<Records> reservations = recordsRepository.getRecordsByEmpId(empId,pageable);
        Map<String,Object> data = new HashMap<>();
        data.put("totalCount",reservations.getTotalElements());
        data.put("results",reservations.getContent());
        baseResponse.setData(data);
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }

    @Override
    public BaseResponse findByTimeIntervalWithEmpId(Date startDate, Date endDate, Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        String empId = ContextUtil.getAccount();
        Page<Records> reservations = recordsRepository.findByTimeIntervalWithEmpId(empId,startDate, endDate, pageable);
        Map<String,Object> data = new HashMap<>();
        data.put("totalCount",reservations.getTotalElements());
        data.put("results",reservations.getContent());
        baseResponse.setData(data);
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }
}
