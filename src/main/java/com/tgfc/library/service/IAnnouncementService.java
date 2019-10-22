package com.tgfc.library.service;

import com.tgfc.library.entity.Announcement;
import com.tgfc.library.response.BaseResponse;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface IAnnouncementService {
    BaseResponse select(String name, Date startTime, Date endTime, Boolean checkPermission, Pageable pageable);

    BaseResponse insert(Announcement announcement);

    BaseResponse update(Announcement announcement);

    BaseResponse delete(Integer id);

    BaseResponse changeStatus(Announcement announcement);

    BaseResponse getAnnouncementsByTimeInterval(Pageable pageable);
}
