package com.tgfc.library.schedule.job;

import com.tgfc.library.enums.JobLastExecuteEnum;
import com.tgfc.library.enums.JobTypeEnum;
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
import java.util.List;

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
        String jobType = (String) dataMap.get("jobType");
        Boolean success = chooseJob(jobType);
        setLastExecute(dataMap.getInt("id"), success);
    }

    private Boolean chooseJob(String jobType) {
        switch (jobType) {
            case "1":
                return reservationNearlyExpired();
            case "2":
                return reservationExpired();
            case "3":
                return lendingNearlyExpired();
            case "4":
                return lendingExpired();
            default:
                return false;
        }
    }

    private Boolean reservationNearlyExpired() {
        Boolean success = false;
        List<MailResponse> list = mailService.getReservationNearlyExpiredList();
        success = mailService.batchTemplateMailing(list, JobTypeEnum.RESERVATION_NEARLY_EXPIRED.getCode());
        return success;
    }

    private Boolean reservationExpired() {
        Boolean success = false;
        int count = -1;
        List<MailResponse> list = mailService.getReservationExpiredList();
        success = mailService.batchTemplateMailing(list, JobTypeEnum.RESERVATION_EXPIRED.getCode());
        count = reservationRepository.reservationExpiredStatus(new Date());
        success = (success && count >= 0);
        return success;
    }

    private Boolean lendingNearlyExpired() {
        Boolean success = false;
        List<MailResponse> list = mailService.getLendingNearlyExpiredList();
        success = mailService.batchTemplateMailing(list, JobTypeEnum.LENDING_NEARLY_EXPIRED.getCode());
        return success;
    }

    private Boolean lendingExpired() {
        Boolean success = false;
        int count = -1;
        List<MailResponse> list = mailService.getLendingExpiredJobList();
        success = mailService.batchTemplateMailing(list, JobTypeEnum.LENDING_EXPIRED.getCode());
        count = recordsRepository.lendingExpiredStatus(new Date());
        success = (success && count >= 0);
        return success;
    }

    private void setLastExecute(int id, Boolean success) {
        if (success) {
            scheduleRepository.setLastExecute(id, JobLastExecuteEnum.DONE.getCode());
        } else {
            scheduleRepository.setLastExecute(id, JobLastExecuteEnum.FAIL.getCode());
        }
    }


}
