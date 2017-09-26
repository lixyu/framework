package com.vcredit.framework.spring;

public class AopBeanDefinition {
	private String id;
	private  String expression;
	private String type;
	private String adviceRef;
	private String pointcutRef;
	
	
	public AopBeanDefinition(String id, String expression, String type, String adviceRef, String pointcutRef) {
		super();
		this.id = id;
		this.expression = expression;
		this.type = type;
		this.adviceRef = adviceRef;
		this.pointcutRef = pointcutRef;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAdviceRef() {
		return adviceRef;
	}
	public void setAdviceRef(String adviceRef) {
		this.adviceRef = adviceRef;
	}
	public String getPointcutRef() {
		return pointcutRef;
	}
	public void setPointcutRef(String pointcutRef) {
		this.pointcutRef = pointcutRef;
	}
}
