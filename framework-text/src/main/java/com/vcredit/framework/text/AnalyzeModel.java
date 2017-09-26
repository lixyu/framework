package com.vcredit.framework.text;

public class AnalyzeModel {
	private Object result;
	private boolean isAdd;
	private boolean isContinue;
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public boolean isAdd() {
		return isAdd;
	}
	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
	public AnalyzeModel(Object result, boolean isAdd,boolean isContinue) {
		super();
		this.result = result;
		this.isAdd = isAdd;
		this.isContinue = isContinue;
	}
	public AnalyzeModel(Object result) {
		super();
		this.result = result;
		this.isAdd = true;
		this.isContinue = true;
	}
	public boolean isContinue() {
		return isContinue;
	}
	public void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}
	
}
