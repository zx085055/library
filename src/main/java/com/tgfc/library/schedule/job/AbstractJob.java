package com.tgfc.library.schedule.job;

import com.tgfc.library.enums.JobLastExecuteEnum;
import com.tgfc.library.repository.IScheduleRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractJob implements Job {

    @Autowired
    IScheduleRepository scheduleRepository;

    protected int setLastExecute(int id, Boolean success) {
        if (success) {
            return scheduleRepository.setLastExecute(id, JobLastExecuteEnum.DONE.getCode());
        } else {
            return scheduleRepository.setLastExecute(id, JobLastExecuteEnum.FAIL.getCode());
        }
    }
}
