package com.tgfc.library.service.imp;

import com.tgfc.library.LibraryApplication;
import com.tgfc.library.entity.Announcement;
import com.tgfc.library.entity.EmployeeSafty;
import com.tgfc.library.repository.IAnnouncementRepository;
import com.tgfc.library.repository.IEmployeeRepositorySafty;
import com.tgfc.library.response.BaseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = LibraryApplication.class)
@Transactional
class AnnouncementServiceTest {

    @Autowired
    IAnnouncementRepository announcementRepository;

    @Autowired
    IEmployeeRepositorySafty employeeRepository;

    @Test
    void changeStatus() {
        BaseResponse baseResponse = new BaseResponse();

        EmployeeSafty employee = employeeRepository.findById("TGFC061").get();
        Announcement existAnnouncement = announcementRepository.findById(7).get();
        existAnnouncement.setEmployee(employee);
        existAnnouncement.setStatus(true);
        announcementRepository.save(existAnnouncement);
        baseResponse.setStatus(true);
        baseResponse.setMessage("切換成功");
        System.out.println(baseResponse);
    }
}