package com.tgfc.library.service;

import com.tgfc.library.entity.Reservation;
import com.tgfc.library.response.BaseResponse;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface IReservationService {
    BaseResponse select(Integer id);
    BaseResponse insert(Reservation reservation);
    BaseResponse update(Reservation reservation);
    BaseResponse delete(Integer id);
    BaseResponse findByStartDateBetween(Date startDate,Date endDate,Pageable pageable);
    BaseResponse findByBookId(Integer bookId);
}
