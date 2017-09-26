package com.vcredit.framework.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Map工具类
 *
 */
public class MapUtil {
	/**
	 * 创建一个新的Map并初始化键值对
	 * @param keyValues 键值对
	 * @return
	 */
	public static Map<String, Object> makeSimpleMap(Object ... keyValues) {
		int length = keyValues.length;
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (length % 2 == 0) {
			for (int i = 0; i < length; i+=2) {
				map.put(keyValues[i].toString(), keyValues[i + 1]);
			}
		}
		
		return map;		
	}
}
