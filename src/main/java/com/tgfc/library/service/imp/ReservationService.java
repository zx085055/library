package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Reservation;
import com.tgfc.library.enums.ReservationEnum;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.repository.IReservationRepository;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IReservationService;
import com.tgfc.library.util.ContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class ReservationService implements IReservationService {

    @Autowired
    IReservationRepository reservationRepository;

    @Autowired
    IEmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse select(Integer id) {
        BaseResponse baseResponse = new BaseResponse();
        Reservation reserv = reservationRepository.getOne(id);
        if (reserv!=null){
            baseResponse.setData(reserv);
            baseResponse.setStatus(true);
            baseResponse.setMessage("查詢成功");
        }else{
            baseResponse.setStatus(false);
            baseResponse.setMessage("無此資料");
        }
        return baseResponse;
    }

    @Override
    public BaseResponse insert(Reservation reservation) {
        BaseResponse baseResponse = new BaseResponse();
        Integer bookId = reservation.getBook().getBookId();
        String empId = ContextUtil.getPrincipal().toString();
        Reservation exist = reservationRepository.findByBookId(bookId,empId);
        if(exist!=null){
            baseResponse.setMessage("已有此預約");
            baseResponse.setStatus(false);
        }else{
            reservation.setStatus(ReservationEnum.RESERVATION_ALIVE.getCode());
            String id = ContextUtil.getPrincipal().toString();
            Date startDate = new Date();
            Date endDate = new Date(startDate.getTime()+3*24*60*60*1000);
            reservation.setStartDate(startDate);
            reservation.setEndDate(endDate);
            reservation.setEmployee(employeeRepository.findById(empId).get());
            reservationRepository.save(reservation);
            baseResponse.setMessage("成功新增一筆預約");
            baseResponse.setStatus(true);
        }
        return baseResponse;
    }

    @Override
    public BaseResponse update(Reservation reservation) {
        BaseResponse baseResponse = new BaseResponse();
        boolean exist = reservationRepository.existsById(reservation.getId());
        if (exist){
            Reservation dataReserv = reservationRepository.getOne(reservation.getId());
            BeanUtils.copyProperties(reservation,dataReserv);
            reservationRepository.save(dataReserv);
            baseResponse.setStatus(true);
            baseResponse.setMessage("成功新增一筆");
        }else {
            baseResponse.setStatus(false);
            baseResponse.setMessage("無此預約");
        }
        return baseResponse;
    }

    @Override
    public BaseResponse delete(Integer id) {
        BaseResponse baseResponse = new BaseResponse();
        boolean exist = reservationRepository.existsById(id);
        if (exist){
            reservationRepository.deleteById(id);
            baseResponse.setMessage("成功刪除一筆");
            baseResponse.setStatus(true);
        }
        else {
            baseResponse.setMessage("無此資料");
            baseResponse.setStatus(false);
        }
        return baseResponse;
    }



    @Override
    @Transactional(readOnly = true)
    public BaseResponse findByTimeInterval(Date startDate, Date endDate, Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        Page<Reservation> reservations = reservationRepository.findByTimeInterval(startDate, endDate,pageable);
        baseResponse.setData(reservations.getContent());
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }


    @Override
    public BaseResponse cancleReservation(Integer reservationId) {
        BaseResponse baseResponse = new BaseResponse();
        Boolean exist = reservationRepository.existsById(reservationId);
        if (exist){
            reservationRepository.cancleReservation(reservationId);
            baseResponse.setStatus(true);
            baseResponse.setMessage("成功更新一筆");
        }else{
            baseResponse.setStatus(false);
            baseResponse.setMessage("無此預約");
        }
        return baseResponse;
    }
}
