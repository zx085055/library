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

        String minutes = Integer.toString(model.getNoticeTime().getMinutes());
        String hours = Integer.toString(model.getNoticeTime().getHours());

        Date startTime = DateBuilder.nextGivenSecondDate(model.getStartTime(), 1);
        Date endTime = DateBuilder.nextGivenSecondDate(model.getEndTime(), 1);

        CronTrigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("OneDayOneTime", model.getName() + model.getId())
                .startAt(startTime)
                .withSchedule(CronScheduleBuilder.cronSchedule("0 "+minutes+" "+hours+" * * ?"))
                .endAt(endTime)
                .build();

        return trigger;
    }
}
