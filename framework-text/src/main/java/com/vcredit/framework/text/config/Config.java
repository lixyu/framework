package com.vcredit.framework.text.config;

import java.util.List;

public class Config {
	private String codeType;
	private List<Analyze> analyzes;
	private List<Package> packages;
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public List<Analyze> getAnalyzes() {
		return analyzes;
	}
	public void setAnalyzes(List<Analyze> analyzes) {
		this.analyzes = analyzes;
	}
	public List<Package> getPackages() {
		return packages;
	}
	public void setPackages(List<Package> packages) {
		this.packages = packages;
	}
}
