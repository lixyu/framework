package com.vcredit.framework.text.config;

import com.vcredit.framework.text.DataFormatter;

@SuppressWarnings({ "rawtypes" })
public class Data implements Comparable<Data> {
	private String field;
	private int index;
	private Class type;
	private DataFormatter formatter;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public DataFormatter getFormatter() {
		return formatter;
	}

	public void setFormatter(DataFormatter formatter) {
		this.formatter = formatter;
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	@Override
	public int compareTo(Data o) {
		if (this.getIndex() < o.getIndex())
			return -1;
		return 1;
	}
}
