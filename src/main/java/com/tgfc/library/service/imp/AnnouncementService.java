package com.tgfc.library.service.imp;

import com.tgfc.library.util.ContextUtil;
import com.tgfc.library.entity.Announcement;
import com.tgfc.library.entity.Employee;
import com.tgfc.library.repository.IAnnouncementRepository;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Service
public class AnnouncementService implements IAnnouncementService {

    @Autowired
    IAnnouncementRepository announcementRepository;

    @Autowired
    IEmployeeRepository employeeRepository;

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
        String id = "TGFC061";

        Employee employee = employeeRepository.findById(id).get();
        announcement.setCreateTime(new Date());
        announcement.setEmployee(employee);
        announcementRepository.save(announcement);
        return true;
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
