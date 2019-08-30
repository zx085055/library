package com.tgfc.library.controller;

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

    @GetMapping("/findByBookId")
    public BaseResponse findByBookId(@RequestParam Integer bookId){
        return reservationService.findByBookId(bookId);
    }

    @PostMapping("/findByTimeInterval")
    public BaseResponse findByTimeIntervalBetween(@RequestBody ReservationPageRequest reservationPageRequest){
        return reservationService.findByStartDateBetween(reservationPageRequest.getStartDate(),reservationPageRequest.getEndDate(),reservationPageRequest.getPageable());
    }
}
