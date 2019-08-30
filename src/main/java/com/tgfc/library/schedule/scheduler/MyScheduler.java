package com.tgfc.library.schedule.scheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

@Component
public class MyScheduler {

    Scheduler scheduler;

    public MyScheduler() {
        InputStream propertyStream = ClassLoader.getSystemClassLoader().getResourceAsStream("quartz.properties");
        Properties properties = new Properties();

        try {
            properties.load(propertyStream);
            SchedulerFactory sf = new StdSchedulerFactory(properties);
            scheduler = sf.getScheduler();
            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean addJob(JobDetail job, Trigger trigger) {
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
