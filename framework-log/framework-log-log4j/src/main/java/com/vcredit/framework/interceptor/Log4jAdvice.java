package com.vcredit.framework.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import com.alibaba.dubbo.common.threadpool.RequestLocalThread;
import com.vcredit.framework.util.JacksonUtil;

public class Log4jAdvice extends AbstractAdvice {
	protected static Logger logger = Logger.getLogger(Log4jAdvice.class);

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long dtDate = System.currentTimeMillis();
		try {
			Object obj = invocation.proceed();
			MDC.put("REQUEST_ID", RequestLocalThread.getRequestId());
			MDC.put("METHOD", invocation.getMethod().getName());
			MDC.put("CLASS_NAME", invocation.getThis().getClass());
			MDC.put("PARAM", paramToString(invocation.getArguments()));
			MDC.put("RUN_TIME", System.currentTimeMillis() - dtDate);
			logger.info(resultToString(obj));
			return obj;
		} catch (Exception e) {
			MDC.put("REQUEST_ID", RequestLocalThread.getRequestId());
			MDC.put("METHOD", invocation.getMethod().getName());
			MDC.put("CLASS_NAME",invocation.getThis().getClass());
			MDC.put("PARAM", paramToString(invocation.getArguments()));
			MDC.put("RUN_TIME", System.currentTimeMillis() - dtDate);
			logger.error(e.toString());
			throw e;
		}
	}

	public Object resultToString(Object obj) {
		try {
			return JacksonUtil.Obj2Json(obj);
		} catch (Exception e) {
			return obj;
		}
	}

}
