package com.vcredit.framework.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProcessMethodInterceptor implements MethodInterceptor {
	private ProxyInterceptor[] interceptors;
	public ProcessMethodInterceptor(ProxyInterceptor ...interceptors ){
		this.interceptors = interceptors;
	}
	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		this.beforeInvoke(obj, method, args, proxy);
		Object returnVal = proxy.invokeSuper(obj, args);
		return this.afterInvoke(obj, method, args, proxy, returnVal);
		 
	}
	
	private void beforeInvoke(Object obj, Method method, Object[] args,MethodProxy proxy) throws Throwable{
		for(ProxyInterceptor incep:interceptors){
			incep.before(obj, method, args, proxy);
		}
	}
	
	private Object afterInvoke(Object obj, Method method, Object[] args,MethodProxy proxy,Object returnVal) throws Throwable{
		for(ProxyInterceptor incep:interceptors){
			returnVal = incep.after(obj, method, args, proxy, returnVal);
		}
		return returnVal;
	}
}
