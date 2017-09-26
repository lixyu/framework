package com.vcredit.framework.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

/**
 * Base64编码解码工具类
 * 
 * @author yangyanxia
 *
 */
public class Base64Util {

	public static byte[] decode(String content) {
		byte[] editorDeBase64 = null;
		try {
			editorDeBase64 = content.getBytes("UTF-8");
			Base64 base64 = new Base64();
			editorDeBase64 = base64.decode(editorDeBase64);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return editorDeBase64;
	}

	/**
	 * 字符串BASE64解密
	 * 
	 * @param 需要解密的String字符串
	 * @return 解密后的String字符串
	 */
	public static String base64Decode(String content) {
		return new String(decode(content));
	}

	/**
	 * 字符串BASE64加密
	 * 
	 * @param 需要加密的String字符串
	 * @return 加密后的String字符串
	 */
	public static String base64Encode(String content) {
		byte[] editorEnBase64;
		try {
			editorEnBase64 = content.getBytes("UTF-8");
			return base64Encode(editorEnBase64);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @Title: base64Encode @Description: content @param: @return @return:
	 *         String @throws
	 */
	public static String base64Encode(byte[] content) {
		Base64 base64 = new Base64();
		content = base64.encode(content);
		String editors = new String(content);
		return editors;
	}

}
