package com.tgfc.library.schedule.job;

import com.tgfc.library.service.imp.ScheduleService;
import com.tgfc.library.util.MailUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO 預約到期通知 - type 1
 */
public class ReservationExpiredJob implements Job {
    /**
     * 傳入值:scheduleMailListModel (收件人，收件人信箱，書名，到期日期)
     * 邏輯:通過model組成文本，呼叫ScheduleService的批量寄信通知
     */
    @Autowired
    ScheduleService scheduleService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        scheduleService.print();
    }
}
