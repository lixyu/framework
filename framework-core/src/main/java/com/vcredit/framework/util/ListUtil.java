package com.vcredit.framework.util;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 集合处理工具
 * 
 * @author donghuawei
 * 
 */
public class ListUtil {

	/**
	 * 将某个集合的某个属性值拼装成字符串 (获取对象get方法某个属性值)
	 * 
	 * @param list
	 *            对象集合
	 * @param property
	 *            属性值
	 * @param separator
	 *            字符串分隔符
	 * @return
	 */
	public static <T> String getPropertyStrFromList(List<T> list,
			String property, String separator) {
		StringBuilder sb = new StringBuilder();
		try {
			if (separator == null)
				separator = ",";
			if (list != null && !list.isEmpty()) {
				if (property == null) {
					for (T t : list) {
						sb.append(separator).append(t);
					}
				} else {
					for (T t : list) {
						if (t != null) {
							Class<? extends Object> clazz = t.getClass();
							Method method = clazz.getDeclaredMethod("get"
									+ property.replaceFirst(property.substring(
											0, 1), property.substring(0, 1)
											.toUpperCase()));
							sb.append(separator);
							sb.append(method.invoke(t));
						}
					}
					sb.deleteCharAt(0);
				}
			}
		} catch (Exception e) {
			System.out.println("getPropertyStrFromList: " + e.getMessage());
		}
		return sb.toString();
	}
}