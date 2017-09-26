package com.vcredit.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vcredit.framework.enumtype.InterfaceRequestTypeEnum;
@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceRequest {
	public String value() default "";
	public InterfaceRequestTypeEnum type() default InterfaceRequestTypeEnum.JSON;
}
