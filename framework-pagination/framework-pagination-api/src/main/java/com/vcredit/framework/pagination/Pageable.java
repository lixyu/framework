package com.vcredit.framework.pagination;

/**
 * 可分页接口
 * 
 *
 */
public interface Pageable {
	/**
	 * 
	 * @return 总数
	 */
	public Long getCnt();
	public void setCnt(Long cnt);
}
