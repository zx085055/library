package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Reservation;
import com.tgfc.library.repository.IReservationRepository;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IReservationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReservationService implements IReservationService {

    @Autowired
    IReservationRepository reservationRepository;

    @Override
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
        Reservation exist = reservationRepository.findByBookId(reservation.getBook().getBookId());
        if(exist!=null){
            baseResponse.setMessage("已有此推薦");
            baseResponse.setStatus(false);
        }else{
            reservationRepository.save(reservation);
            baseResponse.setMessage("成功新增一筆推薦");
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
    public BaseResponse findByTimeInterval(Date startDate, Date endDate, Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        Page<Reservation> reservations = reservationRepository.findByTimeInterval(startDate, endDate,pageable);
        baseResponse.setData(reservations.getContent());
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }

    @Override
    public BaseResponse findByBookId(Integer bookId) {
        BaseResponse baseResponse = new BaseResponse();
        Boolean exist = reservationRepository.existsById(bookId);
        if (exist){
            Reservation reservation = reservationRepository.findByBookId(bookId);
            baseResponse.setMessage("成功查詢一筆");
            baseResponse.setStatus(true);
            baseResponse.setData(reservation);
        }else {
            baseResponse.setStatus(false);
            baseResponse.setMessage("無此預約");
        }
        return baseResponse;
    }
}
