package com.vcredit.framework.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyFactory {
	private static ProxyPool proxyPool;
	public ProxyFactory(ProxyInterceptor...interceptors){
		proxyPool = new ProxyPool(false,interceptors);
	}
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<T> clazz,InvocationHandler invocationHandler){
		return (T)Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), invocationHandler);
	}
	
	public static <T> T newInstance(Class<T> clazz) throws Exception{
		return newInstance(clazz, null, null);
	}
	
	public static <T> T newInstance(Class<T> clazz,Class<?>[] constructorArgTypes, Object[] constructorArgs) throws Exception{
		if(proxyPool==null){
			throw new Exception("not find proxy pool");
		}
		return proxyPool.getProxy(clazz,constructorArgTypes,constructorArgs);
	}
	
}
