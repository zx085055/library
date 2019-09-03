package com.tgfc.library.schedule.trigger;

import com.tgfc.library.entity.Schedule;
import com.tgfc.library.request.SchedulePageRequset;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Date;

@Component
public class OneDayOneTimeTrigger {
    public CronTrigger getTrigger(SchedulePageRequset model) {
        Date startTime = DateBuilder.nextGivenSecondDate(null, 1);
        return TriggerBuilder
                .newTrigger()
                .withIdentity("OneDayOneTime", model.getName()+model.getId())
                .startAt(startTime)
                .withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ?"))
//                .withSchedule(CronScheduleBuilder.cronSchedule(
//                        Integer.toString(model.getNoticeTime().getMinutes()) + " "
//                                + Integer.toString(model.getNoticeTime().getHours()) + " * * * ?"))
                .build();
    }
}
