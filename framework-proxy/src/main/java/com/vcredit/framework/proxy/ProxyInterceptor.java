package com.vcredit.framework.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;
public interface ProxyInterceptor {
	public void before(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable; 
	
	public Object after(Object obj, Method method, Object[] args,
			MethodProxy proxy,Object runVal) throws Throwable; 
}
