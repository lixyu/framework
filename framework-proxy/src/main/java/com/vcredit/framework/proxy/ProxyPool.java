package com.vcredit.framework.proxy;

import java.util.ArrayList;
import java.util.List;



public class ProxyPool {
	private static List<ProxyCacheBean> availPool = new ArrayList<ProxyCacheBean>();
	private ProcessMethodInterceptor process;
	public ProxyPool(boolean noNull,ProxyInterceptor...interceptors){
		this.process = new ProcessMethodInterceptor(interceptors);
	}
	public synchronized <T> T getProxy(Class<T> clazz,Class<?>[] constructorArgTypes, Object[] constructorArgs){
		if(availPool.size()!=0){
			for(int i=0;i<availPool.size();i++){
				ProxyCacheBean bean = availPool.get(i);
				if(bean.equals(clazz)){
					return bean.getProxy(process,constructorArgTypes,constructorArgs);
				}
			}
		}
		ProxyCacheBean bean = ProxyCacheBean.newInstatnce(clazz);
		availPool.add(bean);
		return bean.getProxy(process,constructorArgTypes,constructorArgs);
	}
	
}
