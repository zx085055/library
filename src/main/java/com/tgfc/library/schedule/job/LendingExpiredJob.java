package com.tgfc.library.schedule.job;

import com.tgfc.library.response.MailResponse;
import com.tgfc.library.service.imp.MailService;
import com.tgfc.library.service.imp.ScheduleService;
import com.tgfc.library.util.MailUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO 出借到期通知
 */
public class LendingExpiredJob implements Job {

    @Autowired
    MailService mailService;
    /**
     * 傳入值:scheduleMailListModel (收件人，收件人信箱，書名，到期日期)
     * 邏輯:通過model組成文本，呼叫ScheduleService的批量寄信通知
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        List<MailResponse> list = mailService.getLendingExpiredJobList((Date)dataMap.get("startTime"), (Date)dataMap.get("endTime"));
        List<Map<String, String>> collect = list.stream().map(mailResponse -> {
            Map<String, String> map = new HashMap<>();
            map.put("title", "借書過期通知");
            map.put("context", mailResponse.getEmployee()+"您好，您借閱的書"+mailResponse.getBookName()
                    +" 借閱在"+mailResponse.getEndDate().toString()+"過期，請盡快歸還，謝謝");
            map.put("email", mailResponse.getEmail());
            return map;
        }).collect(Collectors.toList());
        mailService.batchMailing(collect);
    }
}
