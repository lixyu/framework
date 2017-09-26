package com.vcredit.framework.quartz;

import java.util.Map;
import java.util.Set;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

public class QuartJobSchedulingListener implements ApplicationContextAware {

	@SuppressWarnings("unchecked")
	private void loadCronTriggers(ApplicationContext applicationContext, Scheduler scheduler) {
		Map<String, Object> quartzJobBeans = applicationContext.getBeansWithAnnotation(QuartzJob.class);
		Set<String> beanNames = quartzJobBeans.keySet();
		for (String beanName : beanNames) {
			Object object = quartzJobBeans.get(beanName);
			try {
				if (Job.class.isAssignableFrom(object.getClass())) {
					QuartzJob anno = AnnotationUtils.findAnnotation(object.getClass(), QuartzJob.class);
					String jobName = anno.name();
					String jobGroup = anno.group();
					JobKey jobKey = new JobKey(jobName, jobGroup);
					JobDetail job = JobBuilder.newJob((Class<? extends Job>) object.getClass()).withIdentity(jobKey)
							.build();
					CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName + "_trigger", jobGroup)
							.withSchedule(CronScheduleBuilder.cronSchedule(anno.cron())).build();
					if (scheduler.checkExists(jobKey)) {
						scheduler.deleteJob(jobKey);
					}
					scheduler.scheduleJob(job, cronTrigger);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Scheduler scheduler = applicationContext.getBean(Scheduler.class);
		this.loadCronTriggers(applicationContext, scheduler);
	}
}
