package com.vcredit.framework.util.export;

public class ExportDefaultFieldFormat<T> {
	public String fieldFormat(T obj) throws Exception{
		return obj==null?"":String.valueOf(obj);
	}
}
