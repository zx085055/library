package com.tgfc.library.schedule.job;

import com.tgfc.library.repository.IRecordsRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class LendingExpiredStatusJob implements Job {
    @Autowired
    IRecordsRepository recordsRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        recordsRepository.lendingExpiredStatus(new Date());
    }
}
