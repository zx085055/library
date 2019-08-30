package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Announcement;
import com.tgfc.library.entity.Employee;
import com.tgfc.library.repository.IAnnouncementRepository;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.service.IAnnouncementService;
import com.tgfc.library.util.ContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        String id = ContextUtil.getAuthentication().getName();

        Employee employee = employeeRepository.findById(id).get();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date current = new Date();
        announcement.setCreateTime(simpleDateFormat.format(current));
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

    @Override
    public Boolean statusChange(Announcement announcement) {
        Announcement ExistAnnouncemen = announcementRepository.findById(announcement.getId()).get();

        ExistAnnouncemen.setStatus(announcement.getStatus());
        announcementRepository.save(ExistAnnouncemen);
        return true;
    }
}
