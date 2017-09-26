package com.vcredit.framework.text.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.vcredit.framework.text.DataFormatter;

@SuppressWarnings("rawtypes")
public class DataFormatterAdapter extends XmlAdapter<String, DataFormatter> {

	@Override
	public DataFormatter unmarshal(String v) throws Exception {
		return (DataFormatter) Class.forName(v).newInstance();
	}

	@Override
	public String marshal(DataFormatter v) throws Exception {
		return v.getClass().getName();
	}

}
