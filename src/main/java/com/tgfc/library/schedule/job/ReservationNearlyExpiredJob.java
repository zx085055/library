package com.tgfc.library.schedule.job;

import com.tgfc.library.enums.JobTypeEnum;
import com.tgfc.library.response.MailResponse;
import com.tgfc.library.service.imp.MailService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReservationNearlyExpiredJob extends AbstractJob {
    @Autowired
    MailService mailService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        List<MailResponse> list = mailService.getReservationNearlyExpiredList();
        Boolean success = mailService.batchTemplateMailing(list, JobTypeEnum.RESERVATION_NEARLY_EXPIRED.getCode());
        setLastExecute(dataMap.getInt("id"), success);
    }
}
