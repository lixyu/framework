package com.vcredit.framework.interceptor;

import org.aopalliance.intercept.MethodInvocation;

import com.alibaba.dubbo.common.threadpool.RequestLocalThread;
import com.vcredit.framework.bean.MsgBean;
import com.vcredit.framework.util.JacksonUtil;

public class LogAdvice extends AbstractAdvice {
	private SendAdvice sendAdvice;
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		MsgBean msg = packageMsg(invocation);
		long dtDate = System.currentTimeMillis();
		try {
			Object obj = invocation.proceed();
			msg.put("result", JacksonUtil.Obj2Json(obj));
			msg.put("level", "INFO");
			return obj;
		} catch (Exception e) {
			msg.put("result", e.toString());
			msg.put("level", "ERROR");
			throw e;
		} finally {
			msg.put("runTime",System.currentTimeMillis() - dtDate);
			sendAdvice.addMessage(msg);
		}
	}
	public SendAdvice getSendAdvice() {
		return sendAdvice;
	}
	public void setSendAdvice(SendAdvice sendAdvice) {
		this.sendAdvice = sendAdvice;
	}
	
	private MsgBean packageMsg(MethodInvocation invocation){
		MsgBean msg = new MsgBean("log","log");
		msg.put("className", invocation.getThis().getClass().getName());
		msg.put("methodName",  invocation.getMethod().getName());
		msg.put("param",  paramToString(invocation.getArguments()));
		msg.put("requestId", RequestLocalThread.getRequestId());
		return msg;
	}
}
