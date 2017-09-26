package com.vcredit.framework.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.vcredit.framework.exception.ParamException;

public class RequestParamValidAdvice implements MethodInterceptor{
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		for(Object o : args){
			if(o instanceof BindingResult){
				BindingResult bindingResult = (BindingResult)o; 
				//异常处理
				if(bindingResult.hasErrors()){
					StringBuffer errorsb = new StringBuffer();
					for(ObjectError error:bindingResult.getAllErrors()){
						errorsb.append(error.toString());
					}
					throw new ParamException(errorsb.toString());
				}
				
				
			}
		}
		return invocation.proceed();
	}
}
