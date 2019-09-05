package com.tgfc.library.schedule.job;

import com.tgfc.library.enums.JobLastExecuteEnum;
import com.tgfc.library.repository.IRecordsRepository;
import com.tgfc.library.repository.IScheduleRepository;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 檢查出借過期名單，並變更過期狀態Job
 */
public class LendingExpiredStatusJob implements Job {

    @Autowired
    IRecordsRepository recordsRepository;

    @Autowired
    IScheduleRepository scheduleRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int count = -1;
        try {
            count = recordsRepository.lendingExpiredStatus(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (count >= 0) {
            scheduleRepository.setLastExecute(dataMap.getInt("id"), JobLastExecuteEnum.DONE.getCode());
        } else {
            scheduleRepository.setLastExecute(dataMap.getInt("id"), JobLastExecuteEnum.FAIL.getCode());
        }
    }
}
