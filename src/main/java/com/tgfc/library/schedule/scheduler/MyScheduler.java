package com.tgfc.library.schedule.scheduler;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.List;

@Component
public class MyScheduler {

    @Autowired
    private Scheduler scheduler;

    /**
     *
     */
    public Boolean addJob(JobDetail job, Trigger trigger) {
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 啟動一個Scheduler
     */
    public void start() throws SchedulerException {
        scheduler.start();
    }

    /**
     * 关闭调度器
     */
    public void shutdown() throws SchedulerException {
        scheduler.shutdown();
    }

    /**
     * 檢查是否啟動
     */
    public boolean isStarted() throws SchedulerException {
        return scheduler.isStarted();
    }

    /**
     * 停止調度Job任務
     */
    public boolean unscheduleJob(TriggerKey triggerkey)
            throws SchedulerException {
        return scheduler.unscheduleJob(triggerkey);
    }

    /**
     * 重新恢复触发器相关的job任务
     */
    public Date rescheduleJob(TriggerKey triggerkey, Trigger trigger)
            throws SchedulerException {
        return scheduler.rescheduleJob(triggerkey, trigger);
    }

    /**
     * 删除相关的job任务
     *
     * @param jobkey
     * @return
     * @throws SchedulerException
     */
    public boolean deleteJob(JobKey jobkey) throws SchedulerException {
        return scheduler.deleteJob(jobkey);
    }

    /**
     * 暂停调度中所有的job任务
     *
     * @throws SchedulerException
     */
    public void pauseAll() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 恢复调度中所有的job的任务
     *
     * @throws SchedulerException
     */
    public void resumeAll() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 刪除全部job
     */
    public Boolean deleteAllJobs() {
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();
                    //get job's trigger
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    Date nextFireTime = triggers.get(0).getNextFireTime();
                    System.out.println("[jobName] : " + jobName + " [groupName] : "
                            + jobGroup + " - " + nextFireTime);
                    this.deleteJob(new JobKey(jobName, jobGroup));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 暫停指定Job
     */
    public void pauseJob(JobKey jobKey) {
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢復指定Job
     */
    public void resumeJob(JobKey jobKey) {
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void preDestroy() throws SchedulerException {
        scheduler.shutdown();
    }
}
