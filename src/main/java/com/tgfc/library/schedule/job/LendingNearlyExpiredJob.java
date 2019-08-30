package com.tgfc.library.schedule.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * TODO 出借即將到期通知
 */
public class LendingNearlyExpiredJob implements Job {
    /**
     * 傳入值:scheduleMailListModel (收件人，收件人信箱，書名，到期日期)
     * 邏輯:通過model組成文本，呼叫ScheduleService的批量寄信通知
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
