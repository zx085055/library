package com.tgfc.library.controller;

import com.tgfc.library.entity.Announcement;
import com.tgfc.library.enums.PermissionEnum;
import com.tgfc.library.request.AnnouncementPageRequest;
import com.tgfc.library.request.PageableRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
    @Autowired
    IAnnouncementService announcementService;

    @RolesAllowed({PermissionEnum.Role.ADMIN, PermissionEnum.Role.USER})
    @PostMapping("/select")
    public BaseResponse select(@RequestBody AnnouncementPageRequest announcement) {
        return announcementService.select(announcement.getTitle(), announcement.getStartTime(), announcement.getEndTime(), announcement.getPageable());
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PostMapping("/insert")
    public BaseResponse insert(@RequestBody Announcement announcement) {
        return announcementService.insert(announcement);
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PutMapping("/update")
    public BaseResponse update(@RequestBody Announcement announcement) {
        return announcementService.update(announcement);
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @DeleteMapping("/delete")
    public BaseResponse delete(@RequestParam int id) {
        return announcementService.delete(id);
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PostMapping("/changeStatus")
    public BaseResponse changeStatus(@RequestBody Announcement announcement) {
        return announcementService.changeStatus(announcement);
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PostMapping("/findByNotExpired")
    public BaseResponse findByNotExpired(@RequestBody PageableRequest pageableRequest) {
        return announcementService.getAnnouncementsByTimeInterval(pageableRequest.getPageable());
    }
}
