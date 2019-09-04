package com.tgfc.library.schedule.job;

import com.tgfc.library.repository.IReservationRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

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
