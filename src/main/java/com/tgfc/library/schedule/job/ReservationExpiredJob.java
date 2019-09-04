package com.tgfc.library.schedule.job;

import com.tgfc.library.entity.Reservation;
import com.tgfc.library.repository.IReservationRepository;
import com.tgfc.library.response.MailResponse;
import com.tgfc.library.service.imp.MailService;
import com.tgfc.library.service.imp.ScheduleService;
import com.tgfc.library.util.MailUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO 預約到期通知 - type 1
 */
@Service
public class ReservationExpiredJob implements Job {
    /**
     * 傳入值:scheduleMailListModel (收件人，收件人信箱，書名，到期日期)
     * 邏輯:通過model組成文本，呼叫ScheduleService的批量寄信通知
     */
    @Autowired
    MailService mailService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        List<MailResponse> list = mailService.getReservationExpiredList((Date)dataMap.get("startTime"), (Date)dataMap.get("endTime"));
        List<Map<String, String>> collect = list.stream().map(mailResponse -> {
            Map<String, String> map = new HashMap<>();
            map.put("title", "預約過期通知");
            map.put("context", mailResponse.getEmployee()+"您好，您預約的書"+mailResponse.getBookName()
                    +" 預約將在"+mailResponse.getEndDate().toString()+"過期，如有需要請再次預約，謝謝");
            map.put("email", mailResponse.getEmail());
            return map;
        }).collect(Collectors.toList());
        mailService.batchMailing(collect);
    }

}
