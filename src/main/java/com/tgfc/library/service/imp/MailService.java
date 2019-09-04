package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Records;
import com.tgfc.library.entity.Reservation;
import com.tgfc.library.repository.IRecordsRepository;
import com.tgfc.library.repository.IReservationRepository;
import com.tgfc.library.response.MailResponse;
import com.tgfc.library.service.IMailService;
import com.tgfc.library.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MailService implements IMailService {

    @Autowired
    IReservationRepository reservationRepository;

    @Autowired
    IRecordsRepository recordsRepository;


    @Override
    public List<MailResponse> getReservationExpiredList() {
        return reservation2Model(reservationRepository.getReservationExpiredList(new java.util.Date()));
    }

    @Override
    public List<MailResponse> getLendingNearlyExpiredList() {
        java.util.Date date = addThreeDays(new java.util.Date());
        return records2Model(recordsRepository.getLendingExpiredList(date));
    }

    @Override
    public List<MailResponse> getLendingExpiredJobList() {
        return records2Model(recordsRepository.getLendingExpiredList(new java.util.Date()));
    }

    /**
     * 批量寄信
     */
    @Override
    public Boolean batchMailing(List<Map<String, String>> list) {
        list.stream().forEach(map -> {
            String title = map.get("title");
            String context = map.get("context");
            String email = map.get("email");
//            MailUtil.sendMail(title, context, email);
            System.out.println(title+" "+context+" "+email);
        });
        return true;
    }

    private List<MailResponse> records2Model(List<Records> list) {
        return list.stream().map(records -> {
                    MailResponse mailResponse = new MailResponse();
                    mailResponse.setEmployee(records.getBorrowUsername());
                    mailResponse.setBookName(records.getBook().getName());
                    mailResponse.setEmail(records.getEmployee().getEmail());
                    mailResponse.setEndDate((Date) records.getEndDate());
                    return mailResponse;
                }
        ).collect(Collectors.toList());
    }

    private List<MailResponse> reservation2Model(List<Reservation> list) {
        return list.stream().map(reservation -> {
                    MailResponse mailResponse = new MailResponse();
                    mailResponse.setEmployee(reservation.getEmployee().getName());
                    mailResponse.setBookName(reservation.getBook().getName());
                    mailResponse.setEmail(reservation.getEmployee().getEmail());
                    mailResponse.setEndDate( new Date (reservation.getEndDate().getTime()));
                    return mailResponse;
                }
        ).collect(Collectors.toList());
    }

    /**
     * 計算出借即將到期的時間(3天)
     */
    private Date addThreeDays(java.util.Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 3);
        return (Date) calendar.getTime();
    }
}
