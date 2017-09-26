package com.vcredit.framework.text;

import java.util.Map;

@SuppressWarnings({ "rawtypes" })
public interface TextTransformationFilter {
	public AnalyzeModel analyze(String readStr, int index, Object obj, Map context);

	public AnalyzeModel pack(Object obj, Map context);
}
