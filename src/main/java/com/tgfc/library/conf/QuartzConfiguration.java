package com.tgfc.library.conf;


import com.tgfc.library.entity.Schedule;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        CustomSpringBeanJobFactory jobFactory = new CustomSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Autowired JobFactory jobFactory) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory);
        factory.setAutoStartup(true);
        factory.setQuartzProperties(quartzProperties());
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        InputStream propertyStream = ClassLoader.getSystemClassLoader().getResourceAsStream("quartz.properties");
        Properties properties = new Properties();
        properties.load(propertyStream);
        return properties;
    }

    @Bean
    public Scheduler scheduler(@Autowired SchedulerFactoryBean schedulerFactoryBean){
        return schedulerFactoryBean.getScheduler();
    }
}
