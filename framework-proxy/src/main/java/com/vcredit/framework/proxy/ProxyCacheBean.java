package com.vcredit.framework.proxy;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

@SuppressWarnings("rawtypes")
public class ProxyCacheBean {
	private Class sourceClass;
	private Enhancer enhancer;

	private ProxyCacheBean(Class sourceClass) {
		// System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,
		// "d:\\");
		this.sourceClass = sourceClass;
		this.enhancer = new Enhancer();
		enhancer.setSuperclass(sourceClass);
		enhancer.setClassLoader(sourceClass.getClassLoader());
	}

	public static ProxyCacheBean newInstatnce(Class clazz) {
		ProxyCacheBean bean = new ProxyCacheBean(clazz);
		return bean;
	}

	@SuppressWarnings({ "unchecked" })
	public <T> T getProxy(Callback callbacks, Class<?>[] constructorArgTypes, Object[] constructorArgs) {
		this.enhancer.setCallback(callbacks);
		if (constructorArgTypes == null || constructorArgs == null)
			return (T) enhancer.create();
		return (T) enhancer.create(constructorArgTypes, constructorArgs);
	}

	public boolean equals(Class sourceClass) {
		if (this.sourceClass.equals(sourceClass)) {
			return true;
		}
		return false;
	}
}
