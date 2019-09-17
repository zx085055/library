package com.tgfc.library.schedule.job;

import com.tgfc.library.enums.JobLastExecuteEnum;
import com.tgfc.library.repository.IRecordsRepository;
import com.tgfc.library.repository.IReservationRepository;
import com.tgfc.library.repository.IScheduleRepository;
import com.tgfc.library.response.MailResponse;
import com.tgfc.library.service.imp.MailService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NoticeJob implements Job {

    @Autowired
    MailService mailService;

    @Autowired
    IScheduleRepository scheduleRepository;

    @Autowired
    IRecordsRepository recordsRepository;

    @Autowired
    IReservationRepository reservationRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Boolean success = false;
        switch ((String) dataMap.get("jobType")) {
            case "1":
                success = reservationNearlyExpired();
                break;
            case "2":
                success = reservationExpired();
                break;
            case "3":
                success = lendingNearlyExpired();
                break;
            case "4":
                success = lendingExpired();
                break;
            default:
                break;
        }
        if (success) {
            scheduleRepository.setLastExecute(dataMap.getInt("id"), JobLastExecuteEnum.DONE.getCode());
        } else {
            scheduleRepository.setLastExecute(dataMap.getInt("id"), JobLastExecuteEnum.FAIL.getCode());
        }
    }

    private Boolean reservationNearlyExpired() {
        Boolean success = false;
        List<MailResponse> list = mailService.getReservationNearlyExpiredList();
        List<Map<String, String>> collect = list.stream().map(mailResponse -> {
            Map<String, String> map = new HashMap<>();
            map.put("title", "預約即將到期通知");
            map.put("context", mailResponse.getEmployee() + "您好，您預約的書" + mailResponse.getBookName()
                    + " 將在" + mailResponse.getEndDate().toString() + "過期，，謝謝請在期限內取書");
            map.put("email", mailResponse.getEmail());
            return map;
        }).collect(Collectors.toList());
        success = mailService.batchMailing(collect);
        return success;
    }

    private Boolean reservationExpired() {
        Boolean success = false;
        int count = -1;

        List<MailResponse> list = mailService.getReservationExpiredList();
        List<Map<String, String>> collect = list.stream().map(mailResponse -> {
            Map<String, String> map = new HashMap<>();
            map.put("title", "預約過期通知");
            map.put("context", mailResponse.getEmployee() + "您好，您預約的書" + mailResponse.getBookName()
                    + " 預約已在" + mailResponse.getEndDate().toString() + "過期，如有需要請再次預約，謝謝");
            map.put("email", mailResponse.getEmail());
            return map;
        }).collect(Collectors.toList());
        success = mailService.batchMailing(collect);
        count = reservationRepository.reservationExpiredStatus(new Date());
        success = (success && count >= 0);
        return success;
    }

    private Boolean lendingNearlyExpired() {
        Boolean success = false;
        List<MailResponse> list = mailService.getLendingNearlyExpiredList();
        List<Map<String, String>> collect = list.stream().map(mailResponse -> {
            Map<String, String> map = new HashMap<>();
            map.put("title", "借閱即將到期通知");
            map.put("context", mailResponse.getEmployee() + "您好，您借閱的書" + mailResponse.getBookName()
                    + " 將在" + ((java.sql.Date)mailResponse.getEndDate()).toString() + "過期，請在期限內歸還，謝謝");
            map.put("email", mailResponse.getEmail());
            return map;
        }).collect(Collectors.toList());
        success = mailService.batchMailing(collect);
        return success;
    }

    private Boolean lendingExpired() {
        Boolean success = false;
        int count = -1;
        List<MailResponse> list = mailService.getLendingExpiredJobList();
        List<Map<String, String>> collect = list.stream().map(mailResponse -> {
            Map<String, String> map = new HashMap<>();
            map.put("title", "借書過期通知");
            map.put("context", mailResponse.getEmployee() + "您好，您借閱的書" + mailResponse.getBookName()
                    + " 借閱在" + mailResponse.getEndDate().toString() + "過期，請盡快歸還，謝謝");
            map.put("email", mailResponse.getEmail());
            return map;
        }).collect(Collectors.toList());
        success = mailService.batchMailing(collect);
        count = recordsRepository.lendingExpiredStatus(new Date());
        success = (success && count >= 0);
        return success;
    }
}
