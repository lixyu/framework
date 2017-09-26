package com.vcredit.framework.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

import com.vcredit.framework.proxy.ProxyInterceptor;

public class Interceptor implements ProxyInterceptor {
	@Override
	public Object after(Object obj, Method method, Object[] args, MethodProxy proxy, Object runVal) throws Throwable {
		if (method.getReturnType().isAssignableFrom(String.class)) {
			if (runVal == null) {
				return "";
			}
			return ((String) runVal).trim();
		}
		if (Serializable.class.isAssignableFrom(method.getReturnType()) && runVal == null) {
			return ProxyFactory.newInstance(method.getReturnType(), null, null);
		}
		return runVal;
	}

	@Override
	public void before(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
	}
}
