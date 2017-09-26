package com.vcredit.framework.interceptor;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

/**
 * 控制层拦截器 对错误提示信息封装
 * 
 * @author maoyibiao
 *
 */
public class ExceptionHandler extends ExceptionHandlerExceptionResolver {
	@Override
	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
			HandlerMethod handlerMethod, Exception exception) {
		ModelAndView mav = super.doResolveHandlerMethodException(request, response, handlerMethod, exception);
		if (exception instanceof Throwable) {
			Throwable throwable = (Throwable) exception;
			if (throwable.getCause() != null)
				exception = (Exception) throwable.getCause();
		}
		if (mav == null) {
			mav = new ModelAndView();
		}
		if (handlerMethod == null) {
			return null;
		}
		Method method = handlerMethod.getMethod();
		if (method == null) {
			return null;
		}
		if (exception != null) {
			if (method.getAnnotation(ResponseBody.class) != null) {
				Writer out = null;
				try {
					out = response.getWriter();
					out.write("{\"result\":1,\"message\":\"" + exception.toString() + "\"}");
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				mav.setViewName("error");
			}
		}
		return mav;
	}
}
