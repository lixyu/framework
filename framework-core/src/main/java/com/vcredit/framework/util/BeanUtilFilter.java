package com.vcredit.framework.util;

public interface BeanUtilFilter <T,V> {
	public T filter(V source,T target);
}