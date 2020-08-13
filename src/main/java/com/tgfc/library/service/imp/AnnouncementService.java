package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Announcement;
import com.tgfc.library.entity.EmployeeSafety;
import com.tgfc.library.repository.IAnnouncementRepository;
import com.tgfc.library.repository.IEmployeeRepositorySafety;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IAnnouncementService;
import com.tgfc.library.util.ContextUtil;
import com.tgfc.library.util.MessageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AnnouncementService implements IAnnouncementService {

    @Autowired
    IAnnouncementRepository announcementRepository;

    @Autowired
    IEmployeeRepositorySafety employeeRepository;

    private BaseResponse.Builder builder;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse select(String title, Date startTime, Date endTime, Boolean checkPermission, Pageable pageable) {
        builder = new BaseResponse.Builder();
        Page<Announcement> announcements;
        title = (title == null) ? "" : title.replace("/", "//").replace("%", "/%").replace("_", "/_");
        startTime = (startTime == null) ? new Date(Long.valueOf("-62135798400000")) : startTime;
        endTime = (endTime == null) ? new Date(Long.valueOf("253402271999000")) : endTime;
        checkPermission = (checkPermission == null) ? true : checkPermission;

        if (checkPermission) {
            announcements = announcementRepository.getAnnouncementsByNameLikeAndTimeInterval(title, startTime, endTime, pageable);
        } else {
            announcements = announcementRepository.getAnnouncementsByNameLikeAndTimeIntervalAndStatus(title, startTime, endTime, true, pageable);
        }
        return builder.content(announcements).status(true).message(MessageUtil.getMessage("announcement.selectSuccess")).build();
    }

    @Override
    public BaseResponse insert(Announcement announcement) {
        builder = new BaseResponse.Builder();
        String id = ContextUtil.getAccount();

        if (announcement.getEndTime().before(announcement.getStartTime())) {
            builder.status(false).message(MessageUtil.getMessage("announcement.insertDateError"));
            return builder.build();
        }

        EmployeeSafety employee = employeeRepository.findById(id).get();
        announcement.setEmployee(employee);
        announcementRepository.save(announcement);
        builder.status(true).message(MessageUtil.getMessage("announcement.insertSuccess"));
        return builder.build();
    }

    @Override
    public BaseResponse update(Announcement announcement) {
        builder = new BaseResponse.Builder();
        Announcement existAnnouncement = announcementRepository.findById(announcement.getId()).get();
        String id = ContextUtil.getAccount();
        EmployeeSafety employee = employeeRepository.findById(id).get();
        BeanUtils.copyProperties(announcement, existAnnouncement);
        existAnnouncement.setEmployee(employee);
        announcementRepository.save(existAnnouncement);
        builder.status(true).message(MessageUtil.getMessage("announcement.updateSuccess"));
        return builder.build();
    }

    @Override
    public BaseResponse delete(Integer id) {
        builder = new BaseResponse.Builder();
        if (!announcementRepository.existsById(id)) {
            builder.status(false).message(MessageUtil.getMessage("announcement.findNoData"));
            return builder.build();
        }
        announcementRepository.deleteById(id);
        builder.status(true).message(MessageUtil.getMessage("announcement.deleteSuccess"));
        return builder.build();
    }

    @Override
    public BaseResponse changeStatus(Announcement announcement) {
        builder = new BaseResponse.Builder();
        String id = ContextUtil.getAccount();

        EmployeeSafety employee = employeeRepository.findById(id).get();
        Announcement existAnnouncement = announcementRepository.findById(announcement.getId()).get();
        existAnnouncement.setEmployee(employee);
        existAnnouncement.setStatus(announcement.getStatus());
        announcementRepository.save(existAnnouncement);
        builder.status(true).message(MessageUtil.getMessage("announcement.switchStatusSuccess"));
        return builder.build();
    }

    @Override
    public BaseResponse getAnnouncementsByTimeInterval(Pageable pageable) {
        builder = new BaseResponse.Builder();
        Page<Announcement> announcements = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        try {
            announcements = announcementRepository.getAnnouncementsByTimeInterval(sdf.parse(sdf.format(new Date())), true, pageable);
        } catch (Exception e) {
        }
        return builder.content(announcements).status(true).message(MessageUtil.getMessage("announcement.selectNotExpiredSuccess")).build();
    }
}
