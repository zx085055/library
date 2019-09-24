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
        keyword = (keyword.isEmpty()) ? null : keyword;
        if (status != null) {
            baseResponse.setData(recordsRepository.getRecordsByNameLikeAndStatus(keyword, status, pageable).getContent());
            baseResponse.setMessage("查詢成功");
            baseResponse.setStatus(true);
        } else {
            baseResponse.setMessage("查詢失敗");
            baseResponse.setStatus(false);
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
        Reservation nextReservation = reservationRepository.getReservatonByStatusAndBookId(ReservationEnum.RESERVATION_WAIT.getCode(), records.getBook().getId());
        MailUtil.sendMail("取書通知", "親愛的" + nextReservation.getEmployee().getName() + "先生/小姐，您可以來圖書館取書了。", nextReservation.getEmployee().getEmail());
        nextReservation.setStatus(ReservationEnum.RESERVATION_ALIVE.getCode());
        records.setStatus(RecordsStatusEnum.RECORDSSTATUS_RETURNED.getCode());
        records.getBook().setStatus(BookStatusEnum.BOOK_STATUS_INSIDE.getCode());
        recordsRepository.save(records);
        reservationRepository.save(nextReservation);
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
        baseResponse.setData(reservations.getContent());
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }
}
