package com.tgfc.library.service;

import com.tgfc.library.entity.Announcement;
import com.tgfc.library.response.BaseResponse;
import org.springframework.data.domain.Pageable;

public interface IAnnouncementService {
    BaseResponse select(String name, Pageable pageable);
    BaseResponse insert(Announcement announcement);
    BaseResponse update(Announcement announcement);
    BaseResponse delete(Integer id);
    BaseResponse statusChange(Announcement announcement);
}
