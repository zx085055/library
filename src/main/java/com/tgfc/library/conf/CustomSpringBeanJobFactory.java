package com.tgfc.library.conf;

import org.quartz.Job;
import org.quartz.SchedulerContext;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public final class CustomSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
    //spring app context
    private ApplicationContext context;
    //scheduler context
    private SchedulerContext schedulerContext;

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        this.context = context;
    }

    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle) {
        Job job = context.getBean(bundle.getJobDetail().getJobClass());
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(job);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValues(bundle.getJobDetail().getJobDataMap());
        propertyValues.addPropertyValues(bundle.getTrigger().getJobDataMap());
        if (this.schedulerContext != null) {
            propertyValues.addPropertyValues(this.schedulerContext);
        }
        bw.setPropertyValues(propertyValues, true);
        return job;
    }

    public void setSchedulerContext(SchedulerContext schedulerContext)
    {
        this.schedulerContext = schedulerContext;
        super.setSchedulerContext(schedulerContext);
    }
}
