package com.vcredit.framework.quartz;

import java.text.ParseException;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

public class PersistableCronTriggerFactoryBean extends CronTriggerFactoryBean {
	@Override
	public void afterPropertiesSet() throws ParseException {
		super.afterPropertiesSet();
		System.out.println("PersistableCronTriggerFactoryBean-------------------");

		// Remove the JobDetail element
		getJobDataMap().remove(super.getObject().getKey().getName());
	}
}