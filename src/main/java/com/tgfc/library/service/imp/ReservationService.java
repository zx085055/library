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

    @Override
    @Transactional(readOnly = true)
    public BaseResponse select(String keyword, Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        Integer status = ReservationEnum.RESERVATION_ALIVE.getCode();
        Map<String,Object> data = new HashMap<>();
        Page<Reservation> all = null;
        if (keyword == null) {
            all = reservationRepository.findAll(pageable);
        } else {
            all = reservationRepository.getReservationByKeywordLikeAndStatus(keyword, status, pageable);
        }
        data.put("totalCount",all.getTotalElements());
        data.put("results",all.getContent());
        baseResponse.setData(data);
        baseResponse.setMessage("預約查詢成功");
        baseResponse.setStatus(true);
        return baseResponse;
    }

    @Override
    public BaseResponse insert(Reservation reservation) {
        BaseResponse baseResponse = new BaseResponse();
        Integer bookId = reservation.getBook().getBookId();
        String empId = ContextUtil.getAccount();
        List<Integer> statusList = new ArrayList<>();
        statusList.add(ReservationEnum.RESERVATION_ALIVE.getCode());
        statusList.add(ReservationEnum.RESERVATION_WAIT.getCode());
        Reservation exist = reservationRepository.getReservationByStatus(bookId, empId,statusList);
        Book bookBorrowed = bookRepository.checkBookLended(bookId,BookStatusEnum.BOOK_STATUS_LEND.getCode());

        List<Integer> bookStatusList = new ArrayList<>();
        bookStatusList.add(BookStatusEnum.BOOK_STATUS_LOST.getCode());
        bookStatusList.add(BookStatusEnum.BOOK_STATUS_BROKEN.getCode());
        bookStatusList.add(BookStatusEnum.BOOK_STATUS_NOT_RETURNED.getCode());

        Book bookAbnormal = bookRepository.checkBookAbnormal(bookId,bookStatusList);

        if (bookAbnormal != null){
            baseResponse.setMessage("圖書狀況異常無法出借");
            baseResponse.setStatus(false);
        }else if (exist != null) {
            baseResponse.setMessage("已有此預約");
            baseResponse.setStatus(false);
        } else {
            Integer count = reservationRepository.reservationStatusCount(bookId, ReservationEnum.RESERVATION_ALIVE.getCode());
            Date startDate = new Date();
            Date endDate = new Date(startDate.getTime() + 3 * 24 * 60 * 60 * 1000);
            Integer status;
            if (bookBorrowed != null){
                status = ReservationEnum.RESERVATION_WAIT.getCode();
                baseResponse.setMessage("此本書出借中,已加入排隊");
                reservation.setStartDate(startDate);
            } else if (count >= 1) {
                status = ReservationEnum.RESERVATION_WAIT.getCode();
                baseResponse.setMessage("此本書有人預約,已加入排隊");
                reservation.setStartDate(startDate);
            } else {
                status = ReservationEnum.RESERVATION_ALIVE.getCode();
                reservation.setStartDate(startDate);
                reservation.setEndDate(endDate);
                baseResponse.setMessage("成功新增一筆預約");
            }
            reservation.setStatus(status);
            reservation.setEmployee(employeeRepository.findById(empId).get());
            reservationRepository.save(reservation);
            baseResponse.setStatus(true);
        }
        return baseResponse;
    }

    @Override
    public BaseResponse update(Reservation reservation) {
        BaseResponse baseResponse = new BaseResponse();
        boolean exist = reservationRepository.existsById(reservation.getId());
        if (exist) {
            Reservation dataReserv = reservationRepository.getOne(reservation.getId());
            BeanUtils.copyProperties(reservation, dataReserv);
            reservationRepository.save(dataReserv);
            baseResponse.setStatus(true);
            baseResponse.setMessage("成功新增一筆");
        } else {
            baseResponse.setStatus(false);
            baseResponse.setMessage("無此預約");
        }
        return baseResponse;
    }

    @Override
    public BaseResponse delete(Integer id) {
        BaseResponse baseResponse = new BaseResponse();
        boolean exist = reservationRepository.existsById(id);
        if (exist) {
            reservationRepository.deleteById(id);
            baseResponse.setMessage("成功刪除一筆");
            baseResponse.setStatus(true);
        } else {
            baseResponse.setMessage("無此資料");
            baseResponse.setStatus(false);
        }
        return baseResponse;
    }


    @Override
    @Transactional(readOnly = true)
    public BaseResponse findByTimeInterval(Date startDate, Date endDate, Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        Page<Reservation> reservations = reservationRepository.findByTimeInterval(startDate, endDate, pageable);
        Map<String,Object> data = new HashMap<>();
        data.put("totalCount",reservations.getTotalElements());
        data.put("results",reservations.getContent());
        baseResponse.setData(data);
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }


    @Override
    public BaseResponse cancelReservation(Integer reservationId) {
        BaseResponse baseResponse = new BaseResponse();
        Boolean exist = reservationRepository.existsById(reservationId);
        if (exist) {
            reservationRepository.changeReservationStatus(ReservationEnum.RESERVATION_CANCLE.getCode(), reservationId);
            Reservation reservation = reservationRepository.findById(reservationId).get();
            Integer bookId = reservation.getBook().getId();
            Reservation nextReservation = reservationRepository.getReservationByStatusAndBookId(ReservationEnum.RESERVATION_WAIT.getCode(),bookId);
            if(nextReservation!=null) {
                MailUtil.sendMail("取書通知", "親愛的" + nextReservation.getEmployee().getName() + "先生/小姐，您可以來圖書館取書了。", nextReservation.getEmployee().getEmail());
                nextReservation.setStatus(ReservationEnum.RESERVATION_ALIVE.getCode());
                nextReservation.setEndDate(new Date());
                Date current = new Date();
                nextReservation.setEndDate(new Date(current.getTime() + 3 * 24 * 60 * 60 * 1000));
                reservationRepository.save(nextReservation);
            }
            Map<String,Object> data = new HashMap<>();
            List dataList = new ArrayList();
            dataList.add(reservation);
            data.put("results",dataList);
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
        BaseResponse baseResponse = new BaseResponse();
        Page<Reservation> all = reservationRepository.findAll(pageable);
        Map<String,Object> data = new HashMap<>();
        data.put("totalCount",all.getTotalElements());
        data.put("results",all.getContent());
        baseResponse.setData(data);
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }

    @Override
    public BaseResponse getBook(Integer id) {
        BaseResponse baseResponse = new BaseResponse();
        String operatorId = ContextUtil.getAccount();

        Reservation reservation = reservationRepository.findById(id).get();
        if(!ReservationEnum.RESERVATION_ALIVE.getCode().equals(reservation.getStatus())){
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
        BaseResponse baseResponse = new BaseResponse();
        String empId = ContextUtil.getAccount();
        Page<Reservation> reservations = reservationRepository.findByEmpId(empId,pageable);
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
        Page<Reservation> reservations = reservationRepository.findByTimeIntervalWithEmpId(empId,startDate, endDate, pageable);
        Map<String,Object> data = new HashMap<>();
        data.put("totalCount",reservations.getTotalElements());
        data.put("results",reservations.getContent());
        baseResponse.setData(data);
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }
}
