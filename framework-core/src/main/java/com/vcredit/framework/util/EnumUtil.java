package com.vcredit.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

/**
 * 枚举工具
 * 
 * @author maoyibiao
 *
 */
public class EnumUtil {
	@SuppressWarnings("rawtypes")
	public static Map<String, String> getEnumValues(String className, String key) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Class clazz = Class.forName(className);
		for (Object obj : clazz.getEnumConstants()) {
			Method m = obj.getClass().getDeclaredMethod("values", null);
			Object[] results = (Object[]) m.invoke(obj, null);
			for (Object result : results) {
				Field nameField = result.getClass().getDeclaredField(key);
				ReflectionUtils.makeAccessible(nameField);
				Field codeField = result.getClass().getDeclaredField("code");
				ReflectionUtils.makeAccessible(codeField);
				map.put(String.valueOf(ReflectionUtils.getField(codeField, result)),
						(String) ReflectionUtils.getField(nameField, result));
			}
			return map;
		}
		return null;
	}

	/**
	 * 根据code获取name
	 * 
	 * @param <T>
	 * @param clazz
	 * @param ordinal
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String valueOf(Class clazz, String code, String key) {
		try {
			return getEnumValues(clazz.getName(), key).get(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 根据name获取
	 * 
	 * @param <T>
	 * @param enumType
	 * @param name
	 * @return
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
		return (T) Enum.valueOf(enumType, name);
	}

}
