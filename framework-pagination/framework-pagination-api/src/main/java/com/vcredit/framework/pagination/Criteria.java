package com.vcredit.framework.pagination;

/**
 * 分页查询条件接口
 * 
 *
 */
public interface Criteria {
	/**
	 * 
	 * @return 第几页
	 */
	public Long getPage();
	
	/**
	 * 
	 * @return 每页记录数
	 */
	public Long getPageSize();
	
	public String getSort();
	
	public String getOrder();
}
