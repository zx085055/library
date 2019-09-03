package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Book;
import com.tgfc.library.entity.Employee;
import com.tgfc.library.entity.Records;
import com.tgfc.library.entity.Reservation;
import com.tgfc.library.enums.BookStatus;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
        baseResponse.setData(model);
        baseResponse.setStatus(true);
        baseResponse.setMessage("通知成功");
        return baseResponse;
    }

    @Override
    public BaseResponse returnBook(Integer id) {
        BaseResponse baseResponse = new BaseResponse();
        Records records = recordsRepository.findById(id).get();
        Reservation nextReservation = reservationRepository.getReservatonByStatusAndBookId(ReservationEnum.RESERVATION_WAIT.getCode(), records.getBook().getId());
        MailUtil.sendMail("取書通知","親愛的" + nextReservation.getEmployee().getName() + "先生/小姐，您可以來圖書館取書了。",nextReservation.getEmployee().getEmail());
        nextReservation.setStatus(ReservationEnum.RESERVATION_ALIVE.getCode());
        Date current = new Date();
        records.setReturnDate(current);
        records.setStatus(RecordsStatusEnum.RECORDSSTATUS_RETURNED.getCode());
        records.getBook().setStatus(BookStatus.BOOK_STATUS_INSIDE.getCode());
        List<Object> result = Arrays.asList(recordsRepository.save(records),reservationRepository.save(nextReservation));
        baseResponse.setData(result);
        baseResponse.setStatus(true);
        baseResponse.setMessage("歸還成功");
        return baseResponse;
    }

    @Override
    public BaseResponse findAll(Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        Page<Records> records = recordsRepository.findAll(pageable);
        baseResponse.setData(records.getContent());
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }

    @Override
    public BaseResponse findByTimeInterval(Date startDate, Date endDate, Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        Page<Reservation> reservations = recordsRepository.findByTimeInterval(startDate, endDate,pageable);
        baseResponse.setData(reservations.getContent());
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }
}
