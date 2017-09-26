package com.vcredit.framework.proxy.mybatis;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

import com.vcredit.framework.proxy.ProxyFactory;

@SuppressWarnings("serial")
public abstract class MybatisProxyFactory extends DefaultObjectFactory implements IMybatisProxyFactory {
	private static ProxyFactory proxyFactory;

	@Override
	public <T> T create(Class<T> type) {
		return create(type, null, null);
	}

	@SuppressWarnings("static-access")
	public MybatisProxyFactory() {
		this.proxyFactory = new ProxyFactory(init());
	}

	@Override
	public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
		Class<?> classToCreate = resolveInterface(type);
		@SuppressWarnings("unchecked")
		// we know types are assignable
		T created = (T) instantiateClass(classToCreate, constructorArgTypes, constructorArgs);
		return created;
	}

	@SuppressWarnings("static-access")
	private <T> T instantiateClass(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
		if (Collection.class.isAssignableFrom(type))
			return super.create(type, constructorArgTypes, constructorArgs);
		try {
			if (constructorArgTypes == null || constructorArgs == null) {
				return proxyFactory.newInstance(type);
			}
			return proxyFactory.newInstance(type, constructorArgTypes.toArray(new Class[constructorArgTypes.size()]),
					constructorArgs.toArray(new Object[constructorArgs.size()]));
		} catch (Exception e) {
			StringBuilder argTypes = new StringBuilder();
			if (constructorArgTypes != null) {
				for (Class<?> argType : constructorArgTypes) {
					argTypes.append(argType.getSimpleName());
					argTypes.append(",");
				}
			}
			StringBuilder argValues = new StringBuilder();
			if (constructorArgs != null) {
				for (Object argValue : constructorArgs) {
					argValues.append(String.valueOf(argValue));
					argValues.append(",");
				}
			}
			throw new ReflectionException("Error instantiating " + type + " with invalid types (" + argTypes
					+ ") or values (" + argValues + "). Cause: " + e, e);
		}
	}
}
