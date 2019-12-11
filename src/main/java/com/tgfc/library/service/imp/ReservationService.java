package com.tgfc.library.service.imp;

import com.tgfc.library.entity.*;
import com.tgfc.library.enums.BookStatusEnum;
import com.tgfc.library.enums.RecordsStatusEnum;
import com.tgfc.library.enums.ReservationEnum;
import com.tgfc.library.repository.*;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IReservationService;
import com.tgfc.library.util.ContextUtil;
import com.tgfc.library.util.MailUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ReservationService implements IReservationService {

    @Autowired
    IReservationRepository reservationRepository;

    @Autowired
    IEmployeeRepositorySafty employeeRepository;

    @Autowired
    IRecordsRepository recordsRepository;

    @Autowired
    IBookRepository bookRepository;

    BaseResponse.Builder builder;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse select(String keyword, Pageable pageable) {
        Integer aliveStatus = ReservationEnum.RESERVATION_ALIVE.getCode();
        Integer waitStatus = ReservationEnum.RESERVATION_WAIT.getCode();
        Page<Reservation> all;
        if (keyword == null) {
            all = reservationRepository.findAll(pageable);
        } else {
            all = reservationRepository.getReservationByKeywordLikeAndStatus(keyword, aliveStatus, waitStatus, pageable);
        }
        builder = new BaseResponse.Builder();
        return builder.content(all).message("預約查詢成功").status(true).build();
    }

    @Override
    public BaseResponse insert(Reservation reservation) {
        builder = new BaseResponse.Builder();
        Integer bookId = reservation.getBook().getBookId();
        String empId = ContextUtil.getAccount();
        List<Integer> statusList = new ArrayList<>();
        statusList.add(ReservationEnum.RESERVATION_ALIVE.getCode());
        statusList.add(ReservationEnum.RESERVATION_WAIT.getCode());
        Reservation exist = reservationRepository.getReservationByStatus(bookId, empId, statusList);

        List<Integer> bookStatusList = new ArrayList<>();
        bookStatusList.add(BookStatusEnum.BOOK_STATUS_LOST.getCode());
        bookStatusList.add(BookStatusEnum.BOOK_STATUS_BROKEN.getCode());
        bookStatusList.add(BookStatusEnum.BOOK_STATUS_NOT_RETURNED.getCode());
        bookStatusList.add(BookStatusEnum.BOOK_STATUS_SCRAP.getCode());

        Book bookAbnormal = bookRepository.checkBookAbnormal(bookId, bookStatusList);

        if (bookAbnormal != null) {
            builder.status(false).message("圖書狀況異常無法出借");
        } else if (exist != null) {
            builder.status(false).message("已有此預約");
        } else {
            int status;
            Date startDate = new Date();
            int count = reservationRepository.reservationCount(bookId);
            if (bookRepository.checkBookLended(bookId, BookStatusEnum.BOOK_STATUS_LEND.getCode()) != null) {
                if (recordsRepository.checkOwnBorrowed(bookId).getBorrowId().equals(ContextUtil.getAccount())) {
                    return builder.message("你正借閱本書中").build();
                }
                status = ReservationEnum.RESERVATION_WAIT.getCode();
                reservation.setStartDate(startDate);
                builder.content(count);
                builder.message("此書出借中，將您排入等候隊伍");
            }  else if (count > 0) {
                status = ReservationEnum.RESERVATION_WAIT.getCode();
                builder.content(count);
                builder.message("已有他人預約，將您排入等候隊伍");
                reservation.setStartDate(startDate);
            } else {
                status = ReservationEnum.RESERVATION_ALIVE.getCode();
                reservation.setStartDate(startDate);
                reservation.setEndDate(new Date(startDate.getTime() + 3 * 24 * 60 * 60 * 1000));
                builder.content(count);
                builder.message("可以直接取書囉！");
            }
            builder.status(true);
            reservation.setStatus(status);
            reservation.setEmployee(employeeRepository.findById(empId).get());
            reservationRepository.save(reservation);
        }
        return builder.build();
    }

    @Override
    public BaseResponse update(Reservation reservation) {
        boolean exist = reservationRepository.existsById(reservation.getId());
        builder = new BaseResponse.Builder();
        if (exist) {
            Reservation dataReserv = reservationRepository.getOne(reservation.getId());
            BeanUtils.copyProperties(reservation, dataReserv);
            reservationRepository.save(dataReserv);
            builder.message("成功新增一筆").status(true);
        } else {
            builder.message("無此預約").status(false);
        }
        return builder.build();
    }

    @Override
    public BaseResponse delete(Integer id) {
        builder = new BaseResponse.Builder();
        boolean exist = reservationRepository.existsById(id);
        if (exist) {
            reservationRepository.deleteById(id);
            builder.message("成功刪除一筆").status(true);
        } else {
            builder.message("無此資料").status(false);
        }
        return builder.build();
    }


    @Override
    @Transactional(readOnly = true)
    public BaseResponse findByTimeInterval(Date startDate, Date endDate, Pageable pageable) {
        builder = new BaseResponse.Builder();
        Page<Reservation> reservations = reservationRepository.findByTimeInterval(startDate, endDate, pageable);
        return builder.content(reservations).message("查詢成功").status(true).build();
    }


    @Override
    public BaseResponse cancelReservation(Integer reservationId) {
        BaseResponse baseResponse = new BaseResponse();
        Boolean exist = reservationRepository.existsById(reservationId);
        if (exist) {
            reservationRepository.changeReservationStatus(ReservationEnum.RESERVATION_CANCLE.getCode(), reservationId);
            Reservation reservation = reservationRepository.findById(reservationId).get();
            Integer bookId = reservation.getBook().getId();
            Integer bookStatus = reservation.getBook().getStatus();
            String id = reservation.getEmployee().getId();
            List<Integer> statusList = new ArrayList<>();
            statusList.add(ReservationEnum.RESERVATION_ALIVE.getCode());
            statusList.add(ReservationEnum.RESERVATION_WAIT.getCode());

            Reservation nextReservation = reservationRepository.getReservationByStatusAndBookId(statusList, bookId, id);//搜尋預約此本書狀態含ALIVE及WAIT等人且只取日期最早的
            if (BookStatusEnum.BOOK_STATUS_INSIDE.getCode().equals(bookStatus) && nextReservation != null && nextReservation.getStatus().equals(ReservationEnum.RESERVATION_WAIT.getCode())) {//判斷書籍出借中則不變更預約狀態
                MailUtil.sendMail("取書通知", "親愛的" + nextReservation.getEmployee().getName() + "先生/小姐，您可以來圖書館取書了。借閱的書名：（" + nextReservation.getBook().getName() + "）", nextReservation.getEmployee().getEmail());
                nextReservation.setStatus(ReservationEnum.RESERVATION_ALIVE.getCode());
                nextReservation.setEndDate(new Date());
                Date current = new Date();
                nextReservation.setEndDate(new Date(current.getTime() + 3 * 24 * 60 * 60 * 1000));
                reservationRepository.save(nextReservation);
            }
            Map<String, Object> data = new HashMap<>();
            List dataList = new ArrayList();
            dataList.add(reservation);
            data.put("results", dataList);
            baseResponse.setData(data);
            baseResponse.setStatus(true);
            baseResponse.setMessage("成功更新一筆");
        } else {
            baseResponse.setStatus(false);
            baseResponse.setMessage("無此預約");
        }
        return baseResponse;
    }

    @Override
    public BaseResponse findAll(Pageable pageable) {
        builder = new BaseResponse.Builder();
        Page<Reservation> all = reservationRepository.findAll(pageable);
        return builder.content(all).message("查詢成功").status(true).build();
    }

    @Override
    public BaseResponse getBook(Integer id) {
        BaseResponse baseResponse = new BaseResponse();
        String operatorId = ContextUtil.getAccount();

        Reservation reservation = reservationRepository.findById(id).get();
        if (!ReservationEnum.RESERVATION_ALIVE.getCode().equals(reservation.getStatus())) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("請檢查預約狀態");
            return baseResponse;
        }

        reservation.setStatus(ReservationEnum.RESERVATION_SUCCESS.getCode());

        Records records = new Records();
        EmployeeSafty employee = employeeRepository.findById(reservation.getEmployee().getId()).get();
        Book book = bookRepository.findById(reservation.getBook().getId()).get();
        Date current = new Date();
        Date endDate = new Date(current.getTime() + 14 * 24 * 60 * 60 * 1000);
        book.setStatus(BookStatusEnum.BOOK_STATUS_LEND.getCode());
        records.setStatus(RecordsStatusEnum.RECORDSSTATUS_BORROWING.getCode());
        records.setBorrowId(employee.getId());
        records.setBorrowUsername(employee.getName());
        records.setBorrowDate(current);
        records.setEndDate(endDate);
        records.setEmployee(employeeRepository.findById(operatorId).get());
        records.setBook(book);

        recordsRepository.save(records);
        reservationRepository.save(reservation);
        baseResponse.setStatus(true);
        baseResponse.setMessage("取書成功");
        return baseResponse;
    }

    @Override
    public BaseResponse findByEmpId(Pageable pageable) {
        builder = new BaseResponse.Builder();
        String empId = ContextUtil.getAccount();
        Page<Reservation> reservations = reservationRepository.findByEmpId(empId, pageable);
        return builder.content(reservations).message("查詢成功").status(true).build();
    }

    @Override
    public BaseResponse findByTimeIntervalWithEmpId(Date startDate, Date endDate, Pageable pageable) {
        builder = new BaseResponse.Builder();
        String empId = ContextUtil.getAccount();
        Page<Reservation> reservations = reservationRepository.findByTimeIntervalWithEmpId(empId, startDate, endDate, pageable);
        return builder.content(reservations).message("查詢成功").status(true).build();
    }
}
