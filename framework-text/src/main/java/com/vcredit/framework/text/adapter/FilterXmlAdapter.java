package com.vcredit.framework.text.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.vcredit.framework.text.TextTransformationFilter;

public class FilterXmlAdapter extends XmlAdapter<String, TextTransformationFilter> {

	@Override
	public TextTransformationFilter unmarshal(String v) throws Exception {
		return (TextTransformationFilter) Class.forName(v).newInstance();
	}

	@Override
	public String marshal(TextTransformationFilter v) throws Exception {
		return v.getClass().getName();
	}

}
