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
import java.util.Date;

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
        Assertions.assertEquals(1, dataMap.getInt("id"));
    }

    @Test
    public void chooseJob1Test() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("chooseJob", String.class);

        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "1")).thenReturn(true);

        Assertions.assertEquals(true, privateStringMethod.invoke(noticeJob, "1"));

        privateStringMethod.setAccessible(false);
    }

    @Test
    public void chooseJob2Test() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("chooseJob", String.class);

        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "2")).thenReturn(true);

        Assertions.assertEquals(true, privateStringMethod.invoke(noticeJob, "2"));

        privateStringMethod.setAccessible(false);
    }

    @Test
    public void chooseJob3Test() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("chooseJob", String.class);

        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "3")).thenReturn(true);

        Assertions.assertEquals(true, privateStringMethod.invoke(noticeJob, "3"));

        privateStringMethod.setAccessible(false);
    }

    @Test
    public void chooseJob4Test() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("chooseJob", String.class);

        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "4")).thenReturn(true);

        Assertions.assertEquals(true, privateStringMethod.invoke(noticeJob, "4"));

        privateStringMethod.setAccessible(false);
    }

    @Test
    public void chooseJobUndefinedTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("chooseJob", String.class);

        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "5")).thenReturn(false);

        Assertions.assertEquals(false, privateStringMethod.invoke(noticeJob, "5"));

        privateStringMethod.setAccessible(false);
    }

    @Test
    public void chooseJob1FalseTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("chooseJob", String.class);

        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "1")).thenReturn(false);

        Assertions.assertEquals(false, privateStringMethod.invoke(noticeJob, "1"));

        privateStringMethod.setAccessible(false);
    }

    @Test
    public void chooseJob2FalseTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("chooseJob", String.class);

        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "2")).thenReturn(false);

        Assertions.assertEquals(false, privateStringMethod.invoke(noticeJob, "2"));

        privateStringMethod.setAccessible(false);
    }

    @Test
    public void chooseJob3FalseTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("chooseJob", String.class);

        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "3")).thenReturn(false);

        Assertions.assertEquals(false, privateStringMethod.invoke(noticeJob, "3"));

        privateStringMethod.setAccessible(false);
    }

    @Test
    public void chooseJob4FalseTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("chooseJob", String.class);

        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "4")).thenReturn(false);

        Assertions.assertEquals(false, privateStringMethod.invoke(noticeJob, "4"));

        privateStringMethod.setAccessible(false);
    }

    @Test
    public void reservationNearlyExpiredTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("reservationNearlyExpired");
        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.getReservationNearlyExpiredList()).thenReturn(new ArrayList<>());
        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "1")).thenReturn(true);

        Assertions.assertEquals(true, privateStringMethod.invoke(noticeJob));
        privateStringMethod.setAccessible(false);
    }

    @Test
    public void reservationNearlyExpiredFalseTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("reservationNearlyExpired");
        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.getReservationNearlyExpiredList()).thenReturn(new ArrayList<>());
        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "1")).thenReturn(false);

        Assertions.assertEquals(false, privateStringMethod.invoke(noticeJob));
        privateStringMethod.setAccessible(false);
    }

    @Test
    public void reservationExpiredTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("reservationExpired");

        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.getReservationExpiredList()).thenReturn(new ArrayList<>());
        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "2")).thenReturn(true);
        Mockito.when(reservationRepository.reservationExpiredStatus(new Date())).thenReturn(1);

        Assertions.assertEquals(true, privateStringMethod.invoke(noticeJob));
        privateStringMethod.setAccessible(false);
    }

    @Test
    public void reservationExpiredFalseTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("reservationExpired");

        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.getReservationExpiredList()).thenReturn(new ArrayList<>());
        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "2")).thenReturn(false);
        Mockito.when(reservationRepository.reservationExpiredStatus(Mockito.any())).thenReturn(0);

        Assertions.assertEquals(false, privateStringMethod.invoke(noticeJob));
        privateStringMethod.setAccessible(false);
    }

    @Test
    public void lendingNearlyExpiredTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("lendingNearlyExpired");
        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.getLendingNearlyExpiredList()).thenReturn(new ArrayList<>());
        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "3")).thenReturn(true);

        Assertions.assertEquals(true, privateStringMethod.invoke(noticeJob));
        privateStringMethod.setAccessible(false);
    }

    @Test
    public void lendingNearlyExpiredFalseTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("lendingNearlyExpired");
        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.getLendingNearlyExpiredList()).thenReturn(new ArrayList<>());
        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "3")).thenReturn(false);

        Assertions.assertEquals(false, privateStringMethod.invoke(noticeJob));
        privateStringMethod.setAccessible(false);
    }

    @Test
    public void lendingExpiredTest() throws Exception {
        Date date = new Date(1573093382);
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("lendingExpired");
        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.getLendingExpiredJobList()).thenReturn(new ArrayList<>());
        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "4")).thenReturn(true);
        Mockito.when(recordsRepository.lendingExpiredStatus(Mockito.any())).thenReturn(1);

        Assertions.assertEquals(true, privateStringMethod.invoke(noticeJob));
        privateStringMethod.setAccessible(false);
    }

    @Test
    public void lendingExpiredFalseTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("lendingExpired");
        privateStringMethod.setAccessible(true);

        Mockito.when(mailService.getLendingExpiredJobList()).thenReturn(new ArrayList<>());
        Mockito.when(mailService.batchTemplateMailing(new ArrayList<>(), "4")).thenReturn(false);
        Mockito.when(recordsRepository.lendingExpiredStatus(new Date())).thenReturn(0);

        Assertions.assertEquals(false, privateStringMethod.invoke(noticeJob));
        privateStringMethod.setAccessible(false);
    }

    @Test
    public void setLastExecuteTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("setLastExecute", int.class, Boolean.class);
        privateStringMethod.setAccessible(true);

        Mockito.when(scheduleRepository.setLastExecute(1,"1")).thenReturn(1);

        Assertions.assertEquals(1, privateStringMethod.invoke(noticeJob,1,true));
        privateStringMethod.setAccessible(false);
    }

    @Test
    public void setFalseLastExecuteTest() throws Exception {
        Method privateStringMethod = NoticeJob.class.
                getDeclaredMethod("setLastExecute", int.class, Boolean.class);
        privateStringMethod.setAccessible(true);

        Mockito.when(scheduleRepository.setLastExecute(1,"2")).thenReturn(1);

        Assertions.assertEquals(1, privateStringMethod.invoke(noticeJob,1,false));
        privateStringMethod.setAccessible(false);
    }

}
