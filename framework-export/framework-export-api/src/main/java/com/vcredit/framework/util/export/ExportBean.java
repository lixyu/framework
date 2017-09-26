package com.vcredit.framework.util.export;

public class ExportBean {
	private Object val;
	private String title;
	@SuppressWarnings("rawtypes")
	private Class<? extends ExportDefaultFieldFormat> format;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ExportBean(Object val, String title, Class format) {
		super();
		this.val = val;
		this.title = title;
		this.format = format;
	}

	public Object getVal() {
		return val;
	}

	public void setVal(Object val) {
		this.val = val;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends ExportDefaultFieldFormat> getFormat() {
		return format;
	}

	@SuppressWarnings("rawtypes")
	public void setFormat(Class<? extends ExportDefaultFieldFormat> format) {
		this.format = format;
	}

}
