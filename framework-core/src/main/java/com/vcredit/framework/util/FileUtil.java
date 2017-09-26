package com.vcredit.framework.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class FileUtil {
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
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
		return sb.toString();

	}
	public static InputStream convertStringToStream(String str){
		try {
			return new ByteArrayInputStream(str.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
		return null;
	}
	
	public static String readInputStrem(InputStream input){
		StringBuilder sb = new StringBuilder();
		BufferedReader reader  = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
        String line = "";
        try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        return sb.toString();
	}
	
	/**
	 * 根据文件路径在web-root下创建文件夹和文件
	 * @param fileName
	 * @return
	 */
	public static File getRealFile(String filePath) {
		File dir = new File(getRealPath(filePath.substring(0,filePath.lastIndexOf("/"))));
		if(!dir.exists()){
			dir.mkdirs();
		}
		return new File(getRealPath(filePath));
	}
	
	private static String getRootPath() {
		return FileUtil.class.getResource("/").getPath() + "../..";
	}
	
	private static String getRealPath(String fileName) {
		return getRootPath() + fileName;
	}
}
