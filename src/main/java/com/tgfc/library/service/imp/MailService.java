package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Records;
import com.tgfc.library.entity.Reservation;
import com.tgfc.library.enums.JobTypeEnum;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.repository.IRecordsRepository;
import com.tgfc.library.repository.IReservationRepository;
import com.tgfc.library.response.MailResponse;
import com.tgfc.library.service.IMailService;
import com.tgfc.library.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MailService implements IMailService {

    @Autowired
    IReservationRepository reservationRepository;

    @Autowired
    IRecordsRepository recordsRepository;

    @Autowired
    IEmployeeRepository employeeRepository;


    public MailService(IReservationRepository reservationRepository, IRecordsRepository recordsRepository) {
        this.reservationRepository = reservationRepository;
        this.recordsRepository = recordsRepository;
    }

    @Override
    public List<MailResponse> getReservationNearlyExpiredList() {
        return reservationToModel(reservationRepository.getReservationNearlyExpiredList(), JobTypeEnum.RESERVATION_NEARLY_EXPIRED.getTrans());
    }

    @Override
    public List<MailResponse> getReservationExpiredList() {
        return reservationToModel(reservationRepository.getReservationExpiredList(), JobTypeEnum.RESERVATION_EXPIRED.getTrans());
    }

    @Override
    public List<MailResponse> getLendingNearlyExpiredList() {
        return recordsToModel(recordsRepository.getLendingNearlyExpiredList(), JobTypeEnum.LENDING_NEARLY_EXPIRED.getTrans());
    }

    @Override
    public List<MailResponse> getLendingExpiredJobList() {
        return recordsToModel(recordsRepository.getLendingExpiredList(), JobTypeEnum.LENDING_EXPIRED.getTrans());
    }


    /**
     * 批量寄信
     *
     * @param list
     * @return Boolean
     */
    @Override
    public Boolean batchMailing(List<Map<String, String>> list) {
        list.stream().forEach(map -> {
            String title = map.get("title");
            String context = map.get("context");
            String email = map.get("email");
            MailUtil.sendMail(title, context, email);
        });
        return true;
    }

    @Override
    public Boolean batchTemplateMailing(List<MailResponse> list, String type) {
        for (MailResponse mailResponse : list) {
            MailUtil.sendTemplateMail(mailResponse, type);
        }
        return true;
    }

    @Override
    public List<Map<String, String>> getMailDetail(List<MailResponse> list, String jobType) {
        return list.stream().map(mailResponse -> {
            Map<String, String> map = new HashMap<>(16);
            map.put("title", "預約即將到期通知");
            map.put("context", mailResponse.getEmployee() + "您好，您預約的書" + mailResponse.getBookName()
                    + " 將在" + mailResponse.getEndDate().toString() + "過期，，謝謝請在期限內取書");
            map.put("email", mailResponse.getEmail());
            return map;
        }).collect(Collectors.toList());
    }


    private List<MailResponse> recordsToModel(List<Records> list, String title) {
        return list.stream().map(records -> {
                    MailResponse mailResponse = new MailResponse();
                    mailResponse.setEmployee(records.getBorrowUsername());
                    mailResponse.setBookName(records.getBook().getName());
                    mailResponse.setEmail(employeeRepository.getReservationByStatus(records.getBorrowId()));
                    mailResponse.setEndDate(new java.sql.Date((records.getEndDate()).getTime()));
                    mailResponse.setTitle(title);
                    return mailResponse;
                }
        ).collect(Collectors.toList());
    }

    private List<MailResponse> reservationToModel(List<Reservation> list, String title) {
        return list.stream().map(reservation -> {
                    MailResponse mailResponse = new MailResponse();
                    mailResponse.setEmployee(reservation.getEmployee().getName());
                    mailResponse.setBookName(reservation.getBook().getName());
                    mailResponse.setEmail(reservation.getEmployee().getEmail());
                    mailResponse.setEndDate(new java.sql.Date((reservation.getEndDate()).getTime()));
                    mailResponse.setTitle(title);
                    return mailResponse;
                }
        ).collect(Collectors.toList());
    }
}
