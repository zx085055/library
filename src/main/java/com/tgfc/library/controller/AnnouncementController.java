package com.tgfc.library.controller;

import com.tgfc.library.entity.Announcement;
import com.tgfc.library.request.AnnouncementPageRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
    @Autowired
    IAnnouncementService announcementService;

    @PostMapping("/select")
    public BaseResponse select(@RequestBody AnnouncementPageRequest announcement) {
        return announcementService.select(announcement.getTitle(), announcement.getPageable());
    }

    @PostMapping("/insert")
    public BaseResponse insert(@RequestBody Announcement announcement) {
        return announcementService.insert(announcement);
    }

    @PutMapping("/update")
    public BaseResponse update(@RequestBody Announcement announcement) {
        return announcementService.update(announcement);
    }

    @DeleteMapping("/delete")
    public BaseResponse delete(@RequestParam int id) {
        return announcementService.delete(id);
    }

    @PostMapping("/statusChange")
    public BaseResponse statusChange(@RequestBody Announcement announcement) {
        return announcementService.statusChange(announcement);
    }
}
