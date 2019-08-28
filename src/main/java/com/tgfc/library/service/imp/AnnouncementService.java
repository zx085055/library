package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Announcement;
import com.tgfc.library.repository.IAnnouncementRepository;
import com.tgfc.library.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementService implements IAnnouncementService {

    @Autowired
    IAnnouncementRepository announcementRepository;

    @Override
    public Page<Announcement> select(String title, Pageable pageable) {
        if (title == null){
            return announcementRepository.findAll(pageable);
        }else {
            return announcementRepository.getAnnouncementsByNameLike(title,pageable);
        }
    }

    @Override
    public Boolean insert(Announcement announcement) {
        return null;
    }

    @Override
    public Boolean update(Announcement announcement) {
        return null;
    }

    @Override
    public Boolean delete(Integer id) {
        return null;
    }
}
