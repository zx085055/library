package com.tgfc.library.controller;

import com.tgfc.library.entity.Reservation;
import com.tgfc.library.enums.PermissionEnum;
import com.tgfc.library.request.PageableRequest;
import com.tgfc.library.request.ReservationPageRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    IReservationService reservationService;

    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PostMapping("/select")
    public BaseResponse select(@RequestBody ReservationPageRequest reservationPageRequest) {
        return reservationService.select(reservationPageRequest.getKeyword(), reservationPageRequest.getPageable());
    }

    @RolesAllowed({PermissionEnum.Role.USER})
    @PostMapping("/findByDate")
    public BaseResponse findByTimeIntervalBetween(@RequestBody ReservationPageRequest reservationPageRequest) {
        return reservationService.findByTimeInterval(reservationPageRequest.getStartDate(), reservationPageRequest.getEndDate(), reservationPageRequest.getPageable());
    }

    @RolesAllowed({PermissionEnum.Role.USER})
    @PostMapping("/findByEmp")
    public BaseResponse findByEmpName(@RequestBody PageableRequest pageableRequest) {
        return reservationService.findByEmpId(pageableRequest.getPageable());
    }

    @RolesAllowed({PermissionEnum.Role.USER})
    @PostMapping("/cancelReservation")
    public BaseResponse cancel(@RequestBody Reservation reservation) {
        return reservationService.cancelReservation(reservation.getId());
    }

    @RolesAllowed({PermissionEnum.Role.USER})
    @PostMapping("/insert")
    public BaseResponse insert(@RequestBody Reservation reservation) {
        return reservationService.insert(reservation);
    }

    @RolesAllowed({PermissionEnum.Role.USER})
    @PostMapping("/findAll")
    public BaseResponse findAll(@RequestBody PageableRequest pageableRequest) {
        return reservationService.findAll(pageableRequest.getPageable());
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN, PermissionEnum.Role.USER})
    @GetMapping("/getBook")
    public BaseResponse getBook(@RequestParam Integer id) {
        return reservationService.getBook(id);
    }

    @RolesAllowed({PermissionEnum.Role.USER})
    @PostMapping("/findByDateWithEmpId")
    public BaseResponse findByDateWithEmpId(@RequestBody ReservationPageRequest reservationPageRequest) {
        return reservationService.findByTimeIntervalWithEmpId(reservationPageRequest.getStartDate(), reservationPageRequest.getEndDate(), reservationPageRequest.getPageable());
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN, PermissionEnum.Role.USER})
    @GetMapping("/checkReservationQue")
    public BaseResponse checkReservationQue(@RequestParam Integer bookId) {
        return reservationService.checkReservationQue(bookId);
    }

}
