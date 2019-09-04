package com.tgfc.library.schedule.job;

import com.tgfc.library.repository.IReservationRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 檢查預約過期名單，並變更過期狀態Job
 */
public class ReservationExpiredStatusJob implements Job {
    @Autowired
    IReservationRepository reservationRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            reservationRepository.reservationExpiredStatus(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
