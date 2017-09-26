package com.vcredit.framework.text;

import java.util.Map;

@SuppressWarnings("rawtypes")
public interface DataFormatter<T> {
	public T transformation(String str, Map context);

	public String transformation(T str, Map context);
}
