package com.vcredit.framework.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import com.vcredit.framework.util.JacksonUtil;

public abstract class AbstractAdvice implements MethodInterceptor {
	
	public String paramToString(Object args[]) {
		StringBuffer sb = new StringBuffer();
		for (Object arg : args) {
			try {
				sb.append(JacksonUtil.Obj2Json(arg)).append(";");
			} catch (Exception e) {
				sb.append(arg);
			}
		}
		return sb.toString();
	}
}
