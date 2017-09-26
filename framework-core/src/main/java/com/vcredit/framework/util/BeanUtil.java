package com.vcredit.framework.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.ReflectionUtils;

import net.sf.cglib.beans.BeanCopier;

public class BeanUtil {
	public static <T, V> T copy(V source, T target) {
		BeanCopier bc = BeanCopier.create(source.getClass(), target.getClass(), false);
		bc.copy(source, target, null);
		return target;
	}

	public static <T, V> T copy(V source, T target, BeanUtilFilter<T, V> filter) {
		BeanCopier bc = BeanCopier.create(source.getClass(), target.getClass(), false);
		bc.copy(source, target, null);
		target = filter.filter(source, target);
		return target;
	}

	public static <T, V> List<T> copy(Collection<V> sources, Class<T> target) {
		if (sources == null)
			return null;
		List<T> results = new ArrayList<T>();
		Iterator<V> it = sources.iterator();
		while (it.hasNext()) {
			V obj = it.next();
			try {
				T t = target.newInstance();
				BeanCopier bc = BeanCopier.create(obj.getClass(), t.getClass(), false);
				bc.copy(obj, t, null);
				results.add(t);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

	public static <T, V> List<T> copy(Collection<V> sources, Class<T> target, BeanUtilFilter<T, V> filter) {
		if (sources == null)
			return null;
		List<T> results = new ArrayList<T>();
		Iterator<V> it = sources.iterator();
		while (it.hasNext()) {
			V obj = it.next();
			try {
				T t = target.newInstance();
				BeanCopier bc = BeanCopier.create(obj.getClass(), t.getClass(), false);
				bc.copy(obj, t, null);
				t = filter.filter(obj, t);
				results.add(t);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

	public static String toString(Object obj) {
		StringBuffer buf = new StringBuffer();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field f : fields) {
			ReflectionUtils.makeAccessible(f);
			if (f.getType().equals(Integer.class) || f.getType().equals(Long.class) || f.getType().equals(String.class)
					|| f.getType().equals(Short.class) || f.getType().equals(int.class)
					|| f.getType().equals(long.class) || f.getType().equals(byte.class)
					|| f.getType().equals(short.class)) {
				buf.append("/").append(f.getName()).append(":").append(ReflectionUtils.getField(f, obj));
			} else if (f.getType().equals(Date.class)) {
				buf.append("/").append(f.getName()).append(":")
						.append(DateUtil.formatDateTime((Date) ReflectionUtils.getField(f, obj)));
			}

		}
		return buf.toString();
	}
}
