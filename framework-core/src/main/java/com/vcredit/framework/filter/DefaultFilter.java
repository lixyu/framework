package com.vcredit.framework.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.dubbo.common.threadpool.RequestLocalThread;


/**
 * 缺省过滤器
 * 
 *
 */
public class DefaultFilter implements Filter {
	private String encoding;
	
	public void init(FilterConfig config) throws ServletException {
		this.encoding = config.getInitParameter("encoding");
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		RequestLocalThread.create();
		request.setCharacterEncoding(encoding);
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		httpServletResponse.setContentType("text/html;charset=" + encoding);
		httpServletResponse.setHeader("Pragma","No-cache"); 
		httpServletResponse.setHeader("Cache-Control","no-cache"); 
		httpServletResponse.setDateHeader("Expires", 0);
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		chain.doFilter(request, httpServletResponse);
		RequestLocalThread.remove();
	}

	public void destroy() {}
	
}
