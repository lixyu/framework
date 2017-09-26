package com.vcredit.framework;

import com.vcredit.framework.bean.MsgBean;
import com.vcredit.framework.interceptor.SendAdvice;

public class MQService {

	private SendAdvice sendAdvice;

	public void pushMsg(String name, MsgBean msg) {
		msg.setTopic(name);
		sendAdvice.addMessage(msg);
	}
}
