package com.vcredit.framework.bean;

import java.io.Serializable;
import java.util.HashMap;

public class MsgBean extends HashMap<String, Object> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 237739494453842307L;

	private String topic = "defualt";

	private String group = "default";

	public MsgBean(String group, String topic) {
		super();
		if (topic != null)
			this.topic = topic;
		if (group != null)
			this.group = group;
	}

	public MsgBean() {
		super();
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getGroup() {
		return group;
	}

}
