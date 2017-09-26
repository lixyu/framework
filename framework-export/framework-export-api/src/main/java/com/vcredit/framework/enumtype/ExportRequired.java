package com.vcredit.framework.enumtype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vcredit.framework.util.export.ExportDefaultFieldFormat;
/**
 * export excel注解
 * @author sk_mao
 *
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportRequired{
	public String name() default "";
	public String aliases() default "";
	public boolean isBean() default false;
	@SuppressWarnings("rawtypes")
	public Class<? extends ExportDefaultFieldFormat> format() default ExportDefaultFieldFormat.class ;
}
