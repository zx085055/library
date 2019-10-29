package com.tgfc.library.schedule;

import com.tgfc.library.repository.IRecordsRepository;
import com.tgfc.library.repository.IReservationRepository;
import com.tgfc.library.repository.IScheduleRepository;
import com.tgfc.library.schedule.job.NoticeJob;
import com.tgfc.library.service.imp.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.quartz.JobDataMap;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.JobExecutionContextImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
public class NoticeJobTest {

    @InjectMocks
    NoticeJob noticeJob;

    @Mock
    JobExecutionContextImpl jobExecutionContext;

    @Mock
    JobDetailImpl jobDetailImpl;

    @Mock
    MailService mailService;

    @Mock
    IScheduleRepository scheduleRepository;

    @Mock
    IRecordsRepository recordsRepository;

    @Mock
    IReservationRepository reservationRepository;

    @Test
    public void executeTest() {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("jobType", "1");
        dataMap.put("id", 1);
        Mockito.when(jobExecutionContext.getJobDetail()).thenReturn(jobDetailImpl);
        Mockito.when(jobDetailImpl.getJobDataMap()).thenReturn(dataMap);

        noticeJob.execute(jobExecutionContext);
        Assertions.assertEquals("1", dataMap.getString("jobType"));
    }

    @Test
    public void chooseJobTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("chooseJob", String.class);

        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(),"1")).thenReturn(true);

        Assertions.assertEquals(true ,privateStringMethod.invoke(noticeJob, "1"));

        privateStringMethod.setAccessible(false);
    }
}
