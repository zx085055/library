package com.tgfc.library.controller;

import com.tgfc.library.entity.Announcement;
import com.tgfc.library.request.AnnouncementPageRequest;
import com.tgfc.library.service.IAnnouncementService;
import com.tgfc.library.service.imp.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
    @Autowired
    IAnnouncementService announcementService;

    @PostMapping("/select")
    public Page<Announcement> select(@RequestBody AnnouncementPageRequest announcement){
        return announcementService.select(announcement.getTitle(),announcement.getPageable());
    }
}