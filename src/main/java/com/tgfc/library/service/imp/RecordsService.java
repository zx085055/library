package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Records;
import com.tgfc.library.entity.Reservation;
import com.tgfc.library.enums.BookStatusEnum;
import com.tgfc.library.enums.RecordsStatusEnum;
import com.tgfc.library.enums.ReservationEnum;
import com.tgfc.library.repository.IBookRepository;
import com.tgfc.library.repository.IEmployeeRepositorySafty;
import com.tgfc.library.repository.IRecordsRepository;
import com.tgfc.library.repository.IReservationRepository;
import com.tgfc.library.request.SendMailRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IRecordsService;
import com.tgfc.library.util.ContextUtil;
import com.tgfc.library.util.MailUtil;
import com.tgfc.library.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class RecordsService implements IRecordsService {
    @Autowired
    IRecordsRepository recordsRepository;

    @Autowired
    IEmployeeRepositorySafty employeeRepository;

    @Autowired
    IBookRepository bookRepository;

    @Autowired
    IReservationRepository reservationRepository;

    private BaseResponse.Builder builder;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse select(String keyword, Integer status, Pageable pageable) {
        builder = new BaseResponse.Builder();
        if (status.equals(RecordsStatusEnum.RECORDSSTATUS_ALL.getCode())) {
            Page<Records> records = recordsRepository.getRecordsByNameLike(keyword, pageable);
            builder.content(records).message(MessageUtil.getMessage("records.findAllSuccess")).status(true);
        } else {
            Page<Records> records = recordsRepository.getRecordsByNameLikeAndStatus(keyword, status, pageable);
            builder.content(records).message(MessageUtil.getMessage("records.selectSuccess")).status(true);
        }

        return builder.build();
    }

    @Override
    public BaseResponse delete(Integer id) {
        builder = new BaseResponse.Builder();
        if (!recordsRepository.existsById(id)) {
            return builder.message(MessageUtil.getMessage("records.findNoData")).status(false).build();
        }
        recordsRepository.deleteById(id);
        return builder.message(MessageUtil.getMessage("records.deleteSuccess")).status(true).build();
    }

    @Override
    public BaseResponse returnNotify(SendMailRequest model) {
        builder = new BaseResponse.Builder();
        Records records = recordsRepository.findById(model.getId()).get();
        model.setEmail(employeeRepository.findById(records.getBorrowId()).get().getEmail());
        MailUtil.sendMail(model.getTitle(), model.getContext(), model.getEmail());
        return builder.message(MessageUtil.getMessage("records.returnNoticeSuccess")).status(true).build();
    }

    @Override
    public BaseResponse returnBook(Integer id) {
        builder = new BaseResponse.Builder();
        Records records = recordsRepository.findById(id).get();
        Reservation nextReservation = reservationRepository.notifyNextByStatusAndBookId(ReservationEnum.RESERVATION_WAIT.getCode(), records.getBook().getId());
        if (nextReservation != null) {
            MailUtil.sendMail("取書通知", "親愛的" + nextReservation.getEmployee().getName() + "先生/小姐，您可以來圖書館取書了。借閱的書名：（" + nextReservation.getBook().getName() + "）", nextReservation.getEmployee().getEmail());
            nextReservation.setEndDate(new Date());
            nextReservation.setStatus(ReservationEnum.RESERVATION_ALIVE.getCode());
            nextReservation.setEndDate(new Date(new Date().getTime() + 3 * 24 * 60 * 60 * 1000));
            reservationRepository.save(nextReservation);
        }
        records.setStatus(RecordsStatusEnum.RECORDSSTATUS_RETURNED.getCode());
        records.getBook().setStatus(BookStatusEnum.BOOK_STATUS_INSIDE.getCode());
        recordsRepository.save(records);
        return builder.message(MessageUtil.getMessage("records.returnSuccess")).status(true).build();
    }

    @Override
    public BaseResponse findAll(Pageable pageable) {
        builder = new BaseResponse.Builder();
        Page<Records> records = recordsRepository.findAll(pageable);
        return builder.content(records).message(MessageUtil.getMessage("records.searchSuccess")).status(true).build();
    }

    @Override
    public BaseResponse findByTimeInterval(Date startDate, Date endDate, Pageable pageable) {
        builder = new BaseResponse.Builder();
        Page<Records> reservations = recordsRepository.findByTimeInterval(startDate, endDate, pageable);
        return builder.content(reservations).message(MessageUtil.getMessage("records.searchSuccess")).status(true).build();
    }

    @Override
    public BaseResponse findByEmpId(Pageable pageable) {
        builder = new BaseResponse.Builder();
        String empId = ContextUtil.getAccount();
        Page<Records> reservations = recordsRepository.getRecordsByEmpId(empId, pageable);
        return builder.content(reservations).message(MessageUtil.getMessage("records.searchSuccess")).status(true).build();
    }

    @Override
    public BaseResponse findByTimeIntervalWithEmpId(Date startDate, Date endDate, Pageable pageable) {
        builder = new BaseResponse.Builder();
        Page<Records> reservations = recordsRepository.findByTimeIntervalWithEmpId(ContextUtil.getAccount(), startDate, endDate, pageable);
        return builder.content(reservations).message(MessageUtil.getMessage("records.searchSuccess")).status(true).build();
    }
}
