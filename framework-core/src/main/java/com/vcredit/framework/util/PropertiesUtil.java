package com.vcredit.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 配置文件读取
 * @author sk_mao
 *
 */
public class PropertiesUtil {
	private static Properties pro = new Properties();
	static{
		InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream("/config/application.properties");
		try {
			pro.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getProperty(String key){
		return pro.getProperty(key);
	}
}
