package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Announcement;
import com.tgfc.library.entity.Employee;
import com.tgfc.library.repository.IAnnouncementRepository;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IAnnouncementService;
import com.tgfc.library.util.ContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class AnnouncementService implements IAnnouncementService {

    @Autowired
    IAnnouncementRepository announcementRepository;

    @Autowired
    IEmployeeRepository employeeRepository;

    @Override
    public BaseResponse select(String title, Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();

        if (title == null) {
            baseResponse.setData(announcementRepository.findAll(pageable));
            baseResponse.setStatus(true);
            baseResponse.setMessage("查詢成功");
            return baseResponse;
        } else {
            baseResponse.setData(announcementRepository.getAnnouncementsByNameLike(title, pageable));
            baseResponse.setStatus(true);
            baseResponse.setMessage("查詢成功");
            return baseResponse;
        }
    }

    @Override
    public BaseResponse insert(Announcement announcement) {
        BaseResponse baseResponse = new BaseResponse();
        String id = ContextUtil.getPrincipal().toString();

        Employee employee = employeeRepository.findById(id).get();
        Date current = new Date();
        announcement.setCreateTime(current);
        announcement.setEmployee(employee);
        baseResponse.setData(announcementRepository.save(announcement));
        baseResponse.setStatus(true);
        baseResponse.setMessage("新增成功");
        return baseResponse;
    }

    @Override
    public BaseResponse update(Announcement announcement) {
        BaseResponse baseResponse = new BaseResponse();
        String id = ContextUtil.getPrincipal().toString();

        Announcement existAnnouncement = announcementRepository.findById(announcement.getId()).get();
        existAnnouncement.setStatus(announcement.getStatus());
        existAnnouncement.setTitle(announcement.getTitle());
        existAnnouncement.setContext(announcement.getContext());
        existAnnouncement.setUpdateUsername(id);
        baseResponse.setData(announcementRepository.save(existAnnouncement));
        baseResponse.setStatus(true);
        baseResponse.setMessage("編輯成功");
        return baseResponse;
    }

    @Override
    public BaseResponse delete(Integer id) {
        BaseResponse baseResponse = new BaseResponse();
        if (!announcementRepository.existsById(id)) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("刪除失敗");
            return baseResponse;
        }
        announcementRepository.deleteById(id);
        baseResponse.setStatus(true);
        baseResponse.setMessage("刪除成功");
        return baseResponse;
    }

    @Override
    public BaseResponse statusChange(Announcement announcement) {
        BaseResponse baseResponse = new BaseResponse();
        Announcement existAnnouncement = announcementRepository.findById(announcement.getId()).get();

        existAnnouncement.setStatus(announcement.getStatus());
        baseResponse.setData(announcementRepository.save(existAnnouncement));
        baseResponse.setStatus(true);
        baseResponse.setMessage("切換成功");
        return baseResponse;
    }
}
