package com.tgfc.library.schedule;

import com.tgfc.library.schedule.scheduler.MyScheduler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;

@ExtendWith(SpringExtension.class)
public class MySchedulerTest {

    @InjectMocks
    MyScheduler myScheduler;

    @Mock
    Scheduler scheduler;


    @Test
    public void deleteJobTest() throws SchedulerException {
        JobKey jobkey = new JobKey("","");
        Mockito.when(scheduler.deleteJob(jobkey)).thenReturn(true);
        Assertions.assertEquals(true, myScheduler.deleteJob(jobkey));
    }

    @Test
    public void deleteJobFalseTest() throws SchedulerException {
        JobKey jobkey = new JobKey("","");
        Mockito.when(scheduler.deleteJob(jobkey)).thenReturn(false);
        Assertions.assertEquals(false, myScheduler.deleteJob(jobkey));
    }

    @Test
    public void deleteAllJobsTest() throws SchedulerException {
        Mockito.when(scheduler.getJobGroupNames()).thenReturn(new ArrayList<String>());
        Mockito.when(scheduler.getJobKeys(Mockito.any())).thenReturn(new HashSet<>());
        Assertions.assertEquals(true, myScheduler.deleteAllJobs());
    }



}
