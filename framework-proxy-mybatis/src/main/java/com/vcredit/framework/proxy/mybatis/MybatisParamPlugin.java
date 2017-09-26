package com.vcredit.framework.proxy.mybatis;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.BeanUtils;

@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class MybatisParamPlugin implements Interceptor {
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		for (Object obj : invocation.getArgs()) {
			if (obj.getClass().getName().contains("EnhancerByCGLIB")) {
				Object source = obj.getClass().getSuperclass().newInstance();
				BeanUtils.copyProperties(obj, source);
				obj = source;
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

}
