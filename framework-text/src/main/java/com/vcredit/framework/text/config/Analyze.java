package com.vcredit.framework.text.config;

import java.util.List;

import com.vcredit.framework.text.TextTransformationFilter;

@SuppressWarnings({ "rawtypes" })
public class Analyze {
	private Class classType;
	private String split;
	private TextTransformationFilter filter;
	private List<Data> datas;

	public Class getClassType() {
		return classType;
	}

	public void setClassType(Class classType) {
		this.classType = classType;
	}

	public String getSplit() {
		return split;
	}

	public void setSplit(String split) {
		this.split = split;
	}

	public TextTransformationFilter getFilter() {
		return filter;
	}

	public void setFilter(TextTransformationFilter filter) {
		this.filter = filter;
	}

	public List<Data> getDatas() {
		return datas;
	}

	public void setDatas(List<Data> datas) {
		this.datas = datas;
	}

}
