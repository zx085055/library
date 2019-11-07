package com.tgfc.library.schedule;

import com.tgfc.library.enums.ScheduleEnum;
import com.tgfc.library.request.SchedulePageRequest;
import com.tgfc.library.schedule.trigger.OneDayOneTimeTrigger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.TriggerBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

@ExtendWith(SpringExtension.class)
public class OneDayOneTimeTriggerTest {

    @InjectMocks
    OneDayOneTimeTrigger oneDayOneTimeTrigger;

    @Test
    public void getTriggerTest() {

        SchedulePageRequest model = new SchedulePageRequest();
        model.setId(1);
        model.setStartTime(new Date());
        model.setEndTime(new Date());
        model.setNoticeTime(new Time(0));
        LocalTime noticeTime = model.getNoticeTime().toLocalTime();
        String seconds = Integer.toString(noticeTime.getSecond());
        String minutes = Integer.toString(noticeTime.getMinute());
        String hours = Integer.toString(noticeTime.getHour());
        Date startTime = DateBuilder.nextGivenSecondDate(model.getStartTime(), 1);
        Date endTime = DateBuilder.nextGivenSecondDate(model.getEndTime(), 1);

        CronTrigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("OneDayOneTime", ScheduleEnum.GROUP.getCode() + model.getId())
                .startAt(startTime)
                .withSchedule(CronScheduleBuilder.cronSchedule(seconds + " " + minutes + " " + hours + " * * ?"))
                .endAt(endTime)
                .build();

        Assertions.assertEquals(trigger, oneDayOneTimeTrigger.getTrigger(model));
    }


}
