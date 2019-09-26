package com.tgfc.library.controller;

import com.tgfc.library.enums.PermissionEnum;
import com.tgfc.library.request.PageableRequest;
import com.tgfc.library.request.RecordsPageRequest;
import com.tgfc.library.request.RecordsSearchPageRequest;
import com.tgfc.library.request.SendMailRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/records")
public class RecordsController {
    @Autowired
    IRecordsService recordsService;

    @RolesAllowed({ PermissionEnum.Role.ADMIN})
    @PostMapping("/select")
    public BaseResponse select(@RequestBody RecordsPageRequest records) {
        return recordsService.select(records.getKeyword(), records.getStatus(), records.getPageable());
    }

    @RolesAllowed({ PermissionEnum.Role.ADMIN})
    @DeleteMapping("/delete")
    public BaseResponse delete(@RequestParam int id) {
        return recordsService.delete(id);
    }

    @RolesAllowed({ PermissionEnum.Role.ADMIN})
    @PostMapping("/returnNotify")
    public BaseResponse returnNotify(@RequestBody SendMailRequest model) {
        return recordsService.returnNotify(model);
    }

    @RolesAllowed({ PermissionEnum.Role.ADMIN})
    @GetMapping("/returnBook")
    public BaseResponse returnBook(@RequestParam Integer id) {
        return recordsService.returnBook(id);
    }

    @RolesAllowed({PermissionEnum.Role.USER})
    @PostMapping("/findAll")
    public BaseResponse findAll(@RequestBody PageableRequest pageableRequest) {
        return recordsService.findAll(pageableRequest.getPageable());
    }

    @RolesAllowed({PermissionEnum.Role.USER})
    @PostMapping("/findByDate")
    public BaseResponse findByTimeIntervalBetween(@RequestBody RecordsSearchPageRequest recordsPageRequest) {
        return recordsService.findByTimeInterval(recordsPageRequest.getStartDate(), recordsPageRequest.getEndDate(), recordsPageRequest.getPageable());
    }

    @RolesAllowed({PermissionEnum.Role.USER})
    @PostMapping("/findByEmpId")
    public BaseResponse findByEmpId(@RequestBody PageableRequest pageableRequest) {
        return recordsService.findByEmpId(pageableRequest.getPageable());
    }

    @RolesAllowed({PermissionEnum.Role.USER})
    @PostMapping("/findByDateWithEmpId")
    public BaseResponse findByTimeWithEmpId(@RequestBody RecordsSearchPageRequest recordsPageRequest) {
        return recordsService.findByTimeIntervalWithEmpId(recordsPageRequest.getStartDate(), recordsPageRequest.getEndDate(), recordsPageRequest.getPageable());
    }

}
