package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Announcement;
import com.tgfc.library.entity.EmployeeSafty;
import com.tgfc.library.repository.IAnnouncementRepository;
import com.tgfc.library.repository.IEmployeeRepositorySafty;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IAnnouncementService;
import com.tgfc.library.util.ContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AnnouncementService implements IAnnouncementService {

    @Autowired
    IAnnouncementRepository announcementRepository;

    @Autowired
    IEmployeeRepositorySafty employeeRepository;

    private BaseResponse.Builder builder;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse select(String title, Date startTime, Date endTime, Boolean checkPermission, Pageable pageable) {
        builder = new BaseResponse.Builder();
        Page<Announcement> announcements;
        title = (title == null) ? "" : title;
        startTime = (startTime == null) ? new Date(Long.valueOf("-62135798400000")) : startTime;
        endTime = (endTime == null) ? new Date(Long.valueOf("253402271999000")) : endTime;
        checkPermission = (checkPermission == null) ? true : checkPermission;

        if (checkPermission) {
            announcements = announcementRepository.getAnnouncementsByNameLikeAndTimeInterval(title, startTime, endTime, pageable);
        } else {
            announcements = announcementRepository.getAnnouncementsByNameLikeAndTimeIntervalAndStatus(title, startTime, endTime, true, pageable);
        }
        return builder.content(announcements).message("公告查詢成功").build();
    }

    @Override
    public BaseResponse insert(Announcement announcement) {
        builder = new BaseResponse.Builder();
        String id = ContextUtil.getAccount();

        if (announcement.getEndTime().before(announcement.getStartTime())) {
            builder.status(false).message("新增公告日期有誤");
            return builder.build();
        }

        EmployeeSafty employee = employeeRepository.findById(id).get();
        announcement.setEmployee(employee);
        announcementRepository.save(announcement);
        builder.message("新增公告成功");
        return builder.build();
    }

    @Override
    public BaseResponse update(Announcement announcement) {
        builder = new BaseResponse.Builder();
        Announcement existAnnouncement = announcementRepository.findById(announcement.getId()).get();
        String id = ContextUtil.getAccount();
        EmployeeSafty employee = employeeRepository.findById(id).get();
        BeanUtils.copyProperties(announcement, existAnnouncement);
        existAnnouncement.setEmployee(employee);
        announcementRepository.save(existAnnouncement);
        builder.message("公告編輯成功");
        return builder.build();
    }

    @Override
    public BaseResponse delete(Integer id) {
        builder = new BaseResponse.Builder();
        if (!announcementRepository.existsById(id)) {
            builder.status(false).message("公告無此資料");
            return builder.build();
        }
        announcementRepository.deleteById(id);
        builder.message("公告刪除成功");
        return builder.build();
    }

    @Override
    public BaseResponse changeStatus(Announcement announcement) {
        builder = new BaseResponse.Builder();
        String id = ContextUtil.getAccount();

        EmployeeSafty employee = employeeRepository.findById(id).get();
        Announcement existAnnouncement = announcementRepository.findById(announcement.getId()).get();
        existAnnouncement.setEmployee(employee);
        existAnnouncement.setStatus(announcement.getStatus());
        announcementRepository.save(existAnnouncement);
        builder.message("公告狀態切換成功");
        return builder.build();
    }

    @Override
    public BaseResponse getAnnouncementsByTimeInterval(Pageable pageable) {
        builder = new BaseResponse.Builder();
        Page<Announcement> announcements = announcementRepository.getAnnouncementsByTimeInterval(new Date(), true, pageable);
        return builder.content(announcements).message("公告未過期查詢成功").build();
    }
}
