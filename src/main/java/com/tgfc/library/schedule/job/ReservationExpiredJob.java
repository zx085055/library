package com.tgfc.library.schedule.job;

import com.tgfc.library.enums.JobTypeEnum;
import com.tgfc.library.repository.IReservationRepository;
import com.tgfc.library.response.MailResponse;
import com.tgfc.library.service.imp.MailService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReservationExpiredJob extends AbstractJob {

    @Autowired
    MailService mailService;

    @Autowired
    IReservationRepository reservationRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        List<MailResponse> list = mailService.getReservationExpiredList();
        Boolean success = mailService.batchTemplateMailing(list, JobTypeEnum.RESERVATION_EXPIRED.getCode());
        int count = reservationRepository.reservationExpiredStatus();
        success = (success && count >= 0);
        setLastExecute(dataMap.getInt("id"), success);
    }
}
