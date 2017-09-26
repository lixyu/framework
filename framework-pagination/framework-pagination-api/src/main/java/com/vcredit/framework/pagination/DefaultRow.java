package com.vcredit.framework.pagination;

/**
 * 可分页的记录行
 * 
 *
 */
public class DefaultRow implements Pageable {
	private Long cnt;
	
	public Long getCnt() {
		return cnt;
	}
	
	public void setCnt(Long cnt) {
		this.cnt = cnt;
	}
}
