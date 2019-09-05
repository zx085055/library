package com.tgfc.library.schedule.job;

import com.tgfc.library.enums.JobLastExecuteEnum;
import com.tgfc.library.repository.IScheduleRepository;
import com.tgfc.library.response.MailResponse;
import com.tgfc.library.service.imp.MailService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationNearlyExpiredJob implements Job {

    @Autowired
    MailService mailService;

    @Autowired
    IScheduleRepository scheduleRepository;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Boolean success = false;

        try {
            List<MailResponse> list = mailService.getReservationNearlyExpiredList();
            List<Map<String, String>> collect = list.stream().map(mailResponse -> {
                Map<String, String> map = new HashMap<>();
                map.put("title", "預約即將到期通知");
                map.put("context", mailResponse.getEmployee() + "您好，您借閱的書" + mailResponse.getBookName()
                        + " 將在" + mailResponse.getEndDate().toString() + "過期，請在期限內取書，謝謝");
                map.put("email", mailResponse.getEmail());
                return map;
            }).collect(Collectors.toList());
            success = mailService.batchMailing(collect);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (success) {
            scheduleRepository.setLastExecute(dataMap.getInt("id"), JobLastExecuteEnum.DONE.getCode());
        } else {
            scheduleRepository.setLastExecute(dataMap.getInt("id"), JobLastExecuteEnum.FAIL.getCode());
        }
    }
}
