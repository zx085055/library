package com.tgfc.library.service;

import com.tgfc.library.entity.Reservation;
import com.tgfc.library.response.BaseResponse;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface IReservationService {
    BaseResponse select(String keyword, Pageable pageable);

    BaseResponse insert(Reservation reservation);

    BaseResponse update(Reservation reservation);

    BaseResponse delete(Integer id);

    BaseResponse findByTimeInterval(Date startDate, Date endDate, Pageable pageable);

    BaseResponse findByEmpId(Pageable pageable);

    BaseResponse cancleReservation(Integer reservationId);

    BaseResponse findAll(Pageable pageable);

    BaseResponse getBook(Integer id);

    BaseResponse findByTimeIntervalWithEmpId(Date startDate, Date endDate, Pageable pageable);
}
