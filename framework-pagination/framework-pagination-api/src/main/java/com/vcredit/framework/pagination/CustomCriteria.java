package com.vcredit.framework.pagination;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;

/**
 * 分页查询条件自定义实现
 *
 */
public class CustomCriteria extends HashMap<String, Object> implements Criteria {
	private static final long serialVersionUID = 7873183891764993033L;

	private Long rows = null;
	private Long page = null;

	@SuppressWarnings("unused")
	private CustomCriteria() {
		super();
	}

	public CustomCriteria(HttpServletRequest request, String... keyValues) {
		setHttpRequestParam(request);
		if (keyValues.length % 2 == 0) {
			for (int i = 0; i < keyValues.length; i += 2) {
				if (keyValues[i + 1] != null)
					this.put(keyValues[i].toString(), keyValues[i + 1]);
			}
		}
		setRowPage();
	}

	private void setHttpRequestParam(HttpServletRequest request) {
		String paramName = null;
		String paramValue = null;
		if (request != null) {
			Enumeration<?> paramNames = request.getParameterNames();

			while (paramNames.hasMoreElements()) {
				paramName = paramNames.nextElement().toString();
				paramValue = request.getParameter(paramName);
				if (StringUtils.isNotEmpty(paramValue))
					this.put(paramName, paramValue);
			}
		}
	}

	private void setRowPage() {
		Object pobj = this.get("page");
		if (pobj == null) {
			pobj = 1;
		}
		Object robj = this.get("rows");
		if (robj == null) {
			robj = 10;
		}
		this.rows = Long.valueOf(robj.toString());
		this.page = Long.valueOf(pobj.toString());
		this.put("start", (page - 1) * rows);
		this.put("end", rows);
	}

	public CustomCriteria(HttpServletRequest request, Object param) {
		setHttpRequestParam(request);
		if (param != null) {
			for (Field field : param.getClass().getDeclaredFields()) {
				ReflectionUtils.makeAccessible(field);
				this.put(field.getName(), ReflectionUtils.getField(field, param));
			}
		}
		setRowPage();
	}

	public String toString() {
		Set<String> keySet = this.keySet();
		StringBuilder sb = new StringBuilder();

		for (String key : keySet) {
			sb.append(key).append("=").append(this.get(key)).append(";");
		}

		return sb.toString();
	}

	public Long getRows() {
		return rows;
	}

	public void setRows(Long rows) {
		this.rows = rows;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	@Override
	public Long getPage() {
		return this.page;
	}

	@Override
	public Long getPageSize() {
		return this.rows;
	}

	@Override
	public String getSort() {
		return null;
	}

	@Override
	public String getOrder() {
		return null;
	}

}
