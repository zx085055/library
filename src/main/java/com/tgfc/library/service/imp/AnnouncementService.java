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
import java.util.HashMap;
import java.util.Map;

@Service
public class AnnouncementService implements IAnnouncementService {

    @Autowired
    IAnnouncementRepository announcementRepository;

    @Autowired
    IEmployeeRepositorySafty employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse select(String title, Date startTime, Date endTime, Boolean checkPermission, Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        Page<Announcement> announcements;
        title = (title == null) ? "" : title;
        startTime = (startTime == null) ? new Date(Long.valueOf("-62135798400000")) : startTime;
        endTime = (endTime == null) ? new Date(Long.valueOf("253402271999000")) : endTime;
        checkPermission = (checkPermission == null) ? true : checkPermission;

        if(checkPermission) {
            announcements = announcementRepository.getAnnouncementsByNameLikeAndTimeInterval(title, startTime, endTime, pageable);
        }else {
            announcements = announcementRepository.getAnnouncementsByNameLikeAndTimeIntervalAndStatus(title, startTime, endTime, true, pageable);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("totalCount", announcements.getTotalElements());
        data.put("results", announcements.getContent());
        baseResponse.setData(data);
        baseResponse.setStatus(true);
        baseResponse.setMessage("公告查詢成功");
        return baseResponse;
    }

    @Override
    public BaseResponse insert(Announcement announcement) {
        BaseResponse baseResponse = new BaseResponse();
        String id = ContextUtil.getAccount();

        if (announcement.getEndTime().before(announcement.getStartTime())) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("新增公告日期有誤");
            return baseResponse;
        }

        EmployeeSafty employee = employeeRepository.findById(id).get();
        announcement.setEmployee(employee);
        announcementRepository.save(announcement);
        baseResponse.setStatus(true);
        baseResponse.setMessage("新增公告成功");
        return baseResponse;
    }

    @Override
    public BaseResponse update(Announcement announcement) {
        BaseResponse baseResponse = new BaseResponse();
        String id = ContextUtil.getAccount();

        EmployeeSafty employee = employeeRepository.findById(id).get();
        Announcement existAnnouncement = announcementRepository.findById(announcement.getId()).get();
        BeanUtils.copyProperties(announcement, existAnnouncement);
        existAnnouncement.setEmployee(employee);
        announcementRepository.save(existAnnouncement);
        baseResponse.setStatus(true);
        baseResponse.setMessage("公告編輯成功");
        return baseResponse;
    }

    @Override
    public BaseResponse delete(Integer id) {
        BaseResponse baseResponse = new BaseResponse();
        if (!announcementRepository.existsById(id)) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("公告無此資料");
            return baseResponse;
        }
        announcementRepository.deleteById(id);
        baseResponse.setStatus(true);
        baseResponse.setMessage("公告刪除成功");
        return baseResponse;
    }

    @Override
    public BaseResponse changeStatus(Announcement announcement) {
        BaseResponse baseResponse = new BaseResponse();
        String id = ContextUtil.getAccount();

        EmployeeSafty employee = employeeRepository.findById(id).get();
        Announcement existAnnouncement = announcementRepository.findById(announcement.getId()).get();
        existAnnouncement.setEmployee(employee);
        existAnnouncement.setStatus(announcement.getStatus());
        announcementRepository.save(existAnnouncement);
        baseResponse.setStatus(true);
        baseResponse.setMessage("公告狀態切換成功");
        return baseResponse;
    }

    @Override
    public BaseResponse getAnnouncementsByTimeInterval(Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        Page<Announcement> announcements = announcementRepository.getAnnouncementsByTimeInterval(new Date(),true , pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("totalCount", announcements.getTotalElements());
        data.put("results", announcements.getContent());
        baseResponse.setData(data);
        baseResponse.setStatus(true);
        baseResponse.setMessage("公告未過期查詢成功");
        return baseResponse;
    }
}
