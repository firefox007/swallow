package com.dianping.swallow.producer.impl;

import com.dianping.swallow.common.message.TextMessage;

public interface ProducerServer {
	public String getStr(TextMessage strMsg);
}