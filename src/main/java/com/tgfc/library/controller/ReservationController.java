package com.tgfc.library.controller;

import com.tgfc.library.entity.Reservation;
import com.tgfc.library.request.PageableRequest;
import com.tgfc.library.request.ReservationPageRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    IReservationService reservationService;

    @PostMapping("/findByDate")
    public BaseResponse findByTimeIntervalBetween(@RequestBody ReservationPageRequest reservationPageRequest){
        return reservationService.findByTimeInterval(reservationPageRequest.getStartDate(),reservationPageRequest.getEndDate(),reservationPageRequest.getPageable());
    }

    @PostMapping("/cancelReservation")
    public BaseResponse cancel(@RequestBody Reservation reservation){
        return  reservationService.cancleReservation(reservation.getId());
    }

    @PostMapping("/insert")
    public BaseResponse insert(@RequestBody Reservation reservation){
        return reservationService.insert(reservation);
    }

    @PostMapping("/findAll")
    public  BaseResponse findAll(@RequestBody PageableRequest pageableRequest){
        return reservationService.findAll(pageableRequest.getPageable());
    }

}
