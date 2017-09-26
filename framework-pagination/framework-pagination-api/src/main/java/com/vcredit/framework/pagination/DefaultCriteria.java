package com.vcredit.framework.pagination;

import java.util.HashMap;
import java.util.Set;

/**
 * 分页查询条件缺省实现
 * 
 *
 */
public class DefaultCriteria extends HashMap<String, Object> implements Criteria {
	private static final long serialVersionUID = 7873183891764993033L;
	
	public DefaultCriteria(Long page, Long rows) {
		super();
		this.put("page", page);
		this.put("rows", rows);
	}
	
	public Long getLongValue(String key) {
		Object value = this.get(key);
		return value == null ? null : Long.parseLong(value.toString());
	}
	
	public Long getPage() {
		return this.getLongValue("page");
	}

	public Long getPageSize() {
		return this.getLongValue("rows");
	}
	
	public String getSort() {
		return (String)this.get("sort");
	}
	
	@Override
	public String getOrder() {
		return (String)this.get("order");
	}
	
	@Override
	public String toString() {
		Set<String> keySet = this.keySet();
		StringBuilder sb = new StringBuilder();
		
		for (String key : keySet) {
			sb.append(key).append("=").append(this.get(key)).append(";");
		}
		
		return sb.toString();
	}
}
