package com.tgfc.library.schedule.job;

import com.tgfc.library.response.MailResponse;
import com.tgfc.library.service.imp.MailService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 借閱過期通知Job
 * 邏輯:呼叫MailService的getLendingExpiredJobList取得出借過期列表 (MailResponse : 收件人，收件人信箱，書名，到期日期)
 * 通過model組成文本，呼叫MailService的batchMailing進行批量寄信通知
 */
public class LendingExpiredJob implements Job {

    @Autowired
    MailService mailService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<MailResponse> list = mailService.getLendingExpiredJobList();
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
