package com.tgfc.library.schedule.trigger;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Date;

@Component
public class OneDayOneTimeTrigger {
    public CronTrigger getTrigger(Time time){
        Date startTime = DateBuilder.nextGivenSecondDate(null, 1);
        return  TriggerBuilder
                .newTrigger()
                .withIdentity("OneDayOneTime", "group")
                .startAt(startTime)
//                    .withSchedule(CronScheduleBuilder.cronSchedule("0 9 * * * ?"))
                .withSchedule(CronScheduleBuilder.cronSchedule(
                        Integer.toString(time.getMinutes())+" "+Integer.toString(time.getHours())+" * * * ?"))
                .build();
    }
}
