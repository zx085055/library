package com.tgfc.library.service;

import com.tgfc.library.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAnnouncementService {
    Page<Announcement> select(String name, Pageable pageable);
    Boolean insert(Announcement announcement);
    Boolean update(Announcement announcement);
    Boolean delete(Integer id);
    Boolean statusChange(Announcement announcement);
}
