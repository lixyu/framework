package com.vcredit.framework.pagination;

import java.util.List;

public class Page {
	private Long cnt;
	private Long totalPage;
	private List<? extends Object> results;
	public Long getCnt() {
		return cnt;
	}
	
	
	public Long getTotalPage() {
		return (cnt + totalPage -1) / totalPage;
	}


	public void setTotalPage(Long totalPage) {
		this.totalPage = totalPage;
	}



	public Page(Long cnt, List<? extends Object> results) {
		super();
		this.cnt = cnt;
		this.results = results;
	}
	
	public Page(Long cnt, Long totalPage, List<? extends Object> results) {
		super();
		this.cnt = cnt;
		this.totalPage = totalPage;
		this.results = results;
	}

	public void setCnt(Long cnt) {
		this.cnt = cnt;
	}

	public List<? extends Object> getResults() {
		return results;
	}

	public void setResults(List<? extends Object> results) {
		this.results = results;
	}
}
