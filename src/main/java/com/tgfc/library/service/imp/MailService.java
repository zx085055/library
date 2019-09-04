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
    public List<Reservation> getReservationExpiredList(Date startTime, Date endTime) {
        return reservationRepository.getReservationExpiredList(startTime, endTime);
    }

    @Override
    public List<Records> getLendingNearlyExpiredList(Date startTime, Date endTime) {
        startTime = addThreeDays(startTime);
        endTime = addThreeDays(endTime);
        return recordsRepository.getLendingExpiredList(startTime, endTime);
    }

    @Override
    public List<Records> getLendingExpiredJobList(Date startTime, Date endTime) {
        return recordsRepository.getLendingExpiredList(startTime, endTime);
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
            MailUtil.sendMail(title, context, email);
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
                    mailResponse.setEndDate((Date) reservation.getEndDate());
                    return mailResponse;
                }
        ).collect(Collectors.toList());
    }

    /**
     * 計算出借即將到期的時間(3天)
     */
    private Date addThreeDays(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 3);
        return (Date) calendar.getTime();
    }
}
