package com.vcredit.framework.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

import com.vcredit.framework.text.config.Analyze;
import com.vcredit.framework.text.config.Config;
import com.vcredit.framework.text.config.Data;
import com.vcredit.framework.text.config.Package;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TextProcess {
	protected static Config config;
	private static Map context = new HashMap();

	private static Analyze getAnalyze(Class c) throws Exception {
		for (Analyze analyze : config.getAnalyzes()) {
			if (analyze.getClassType().equals(c))
				return analyze;
		}
		throw new Exception("not find analyze");
	}

	private static Package getPackage(Class c) throws Exception {
		for (Package p : config.getPackages()) {
			if (p.getClassType().equals(c))
				return p;
		}
		throw new Exception("not find package");
	}

	public static synchronized <T> List<T> read(InputStream is, Class<T> c) throws Exception {
		List resultList = new ArrayList();
		Analyze analyze = getAnalyze(c);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, config.getCodeType()));
		String line = null;
		int index = 0;
		try {
			while ((line = reader.readLine()) != null) {
				AnalyzeModel model = transformationText(line, analyze, index);
				if (model.isAdd())
					resultList.add(model.getResult());
				index++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	public static synchronized String write(List lists, Class clazz) throws Exception {
		StringBuffer sb = new StringBuffer();
		Package pack = getPackage(clazz);
		Collections.sort(pack.getDatas());
		for (Object obj : lists) {
			AnalyzeModel am = null;
			if (pack.getFilter() != null) {
				am = pack.getFilter().pack(obj, context);
				if (am.isAdd()) {
					sb.append(am.getResult().toString());
				}
			} else {
				am = new AnalyzeModel(null);
			}
			if (am.isContinue()) {
				for (int i = 0; i < pack.getDatas().size(); i++) {
					Data d = pack.getDatas().get(i);
					Field field = obj.getClass().getDeclaredField(d.getField());
					ReflectionUtils.makeAccessible(field);
					Object result = ReflectionUtils.getField(field, obj);
					if (d.getFormatter() != null) {
						sb.append(d.getFormatter().transformation(result, context));
					} else {
						sb.append(result.toString());
					}
					if (i != pack.getDatas().size() - 1)
						sb.append(pack.getSplit());
				}
				sb.append("\r\n");
			}

		}
		return sb.toString();
	}

	private static AnalyzeModel transformationText(String line, Analyze analyze, int index)
			throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException,
			ClassNotFoundException {
		Object obj = analyze.getClassType().newInstance();
		AnalyzeModel model = null;
		if (analyze.getFilter() != null) {
			TextTransformationFilter filter = analyze.getFilter();
			model = filter.analyze(line, index, obj, context);
			if (!model.isContinue()) {
				return model;
			}
		} else {
			model = new AnalyzeModel(null);
		}
		String[] valTexts = line.split(analyze.getSplit());
		for (Data data : analyze.getDatas()) {
			if (valTexts.length > data.getIndex()) {
				Field field = obj.getClass().getDeclaredField(data.getField());
				ReflectionUtils.makeAccessible(field);
				if (data.getFormatter() != null) {
					ReflectionUtils.setField(field, obj,
							data.getFormatter().transformation(valTexts[data.getIndex()], context));
				}
				ReflectionUtils.setField(field, obj, getVal(valTexts[data.getIndex()], data.getType()));
			}
		}
		model.setResult(obj);
		return model;
	}

	private static Object getVal(String strVal, Class type) throws ClassNotFoundException {
		if (Long.class.equals(type)) {
			return Long.parseLong(strVal);
		}
		if (Integer.class.equals(type)) {
			return Integer.parseInt(strVal);
		}
		if (Double.class.equals(type)) {
			return Double.parseDouble(strVal);
		}
		return strVal;
	}

}
