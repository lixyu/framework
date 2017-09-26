package com.vcredit.framework.enumtype;

/****
 * 
 * @author maoyibiao
 * 
 */
public enum InterfaceRequestTypeEnum {

	JSON(1L, "JSON"), FORM(2L,"FORM");

	private Long code;
	private String name;

	private InterfaceRequestTypeEnum(Long code, String name) {
		this.code = code;
		this.name = name;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
