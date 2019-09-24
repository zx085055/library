package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Announcement;
import com.tgfc.library.entity.Employee;
import com.tgfc.library.repository.IAnnouncementRepository;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IAnnouncementService;
import com.tgfc.library.util.ContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnnouncementService implements IAnnouncementService {

    @Autowired
    IAnnouncementRepository announcementRepository;

    @Autowired
    IEmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse select(String title, Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        title = (title == null) ? "" : title;
        baseResponse.setData(announcementRepository.getAnnouncementsByNameLike(title, pageable).getContent());
        baseResponse.setStatus(true);
        baseResponse.setMessage("查詢成功");
        return baseResponse;
    }

    @Override
    public BaseResponse insert(Announcement announcement) {
        BaseResponse baseResponse = new BaseResponse();
        String id = ContextUtil.getAccount();

        Employee employee = employeeRepository.findById(id).get();
        announcement.setEmployee(employee);
        announcementRepository.save(announcement);
        baseResponse.setStatus(true);
        baseResponse.setMessage("新增成功");
        return baseResponse;
    }

    @Override
    public BaseResponse update(Announcement announcement) {
        BaseResponse baseResponse = new BaseResponse();
        String id = ContextUtil.getAccount();

        Employee employee = employeeRepository.findById(id).get();
        Announcement existAnnouncement = announcementRepository.findById(announcement.getId()).get();
        BeanUtils.copyProperties(announcement, existAnnouncement);
        existAnnouncement.setEmployee(employee);
        announcementRepository.save(existAnnouncement);
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
    public BaseResponse changeStatus(Announcement announcement) {
        BaseResponse baseResponse = new BaseResponse();
        String id = ContextUtil.getAccount();

        Employee employee = employeeRepository.findById(id).get();
        Announcement existAnnouncement = announcementRepository.findById(announcement.getId()).get();
        existAnnouncement.setEmployee(employee);
        existAnnouncement.setStatus(announcement.getStatus());
        announcementRepository.save(existAnnouncement);
        baseResponse.setStatus(true);
        baseResponse.setMessage("切換成功");
        return baseResponse;
    }
}
