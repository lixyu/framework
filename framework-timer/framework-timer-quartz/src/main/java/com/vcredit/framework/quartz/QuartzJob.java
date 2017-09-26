package com.vcredit.framework.quartz;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

@Documented
@Inherited
@Component
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface QuartzJob {
	String name();

	String group() default "DEFAULT_GROUP";

	String cron();
}
