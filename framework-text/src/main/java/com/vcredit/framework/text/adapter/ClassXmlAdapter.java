package com.vcredit.framework.text.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

@SuppressWarnings("rawtypes")
public class ClassXmlAdapter extends XmlAdapter<String, Class> {

	@Override
	public Class unmarshal(String v) throws Exception {
		return Class.forName(v);
	}

	@Override
	public String marshal(Class v) throws Exception {
		return v.getName();
	}

}
