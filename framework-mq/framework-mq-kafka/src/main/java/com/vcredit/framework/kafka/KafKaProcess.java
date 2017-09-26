package com.vcredit.framework.kafka;

import com.vcredit.framework.bean.MsgBean;

public interface KafKaProcess {
	public void process(MsgBean msgBean);
}
