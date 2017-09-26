package com.vcredit.framework.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/***
 * 
 * @author shikaiyuan
 *常用工具包
 */
public class CommonToolsUtil {
	public CommonToolsUtil() {
	}

	@SuppressWarnings("unused")
	private final static String SALTKEY = "INTELLIGENTPARKING", UNKNOW = "unknown",
			IPV6LOCAL = "0:0:0:0:0:0:0:1";

	/***
	 * list 非空验证
	 * 
	 * @param list
	 * @return
	 */
	public static boolean listIsEmpty(List<?> list) {
		boolean flag = false;
		if (null != list && !list.isEmpty() && list.size() > 0) {
			flag = true;
		}
		return flag;
	}

	/***
	 * 手机号码验证
	 * 
	 * @return
	 */
	public static boolean verfyPhone(String phoneNumber) {
		boolean flag = false;
		String temp = phoneNumber;
		if (StringUtils.isNotBlank(phoneNumber)) {
			if (StringUtils.contains(phoneNumber, "+86")) {
				temp = StringUtils.remove(phoneNumber, "+86");
			}
			String verfy = "^[1](([3][0-9]|(5[^4,//D])|(8[0,5-9])))\\d{8}$";
			Pattern pattern = Pattern.compile(verfy);
			Matcher matcher = pattern.matcher(temp);
			flag = matcher.matches();
		}
		return flag;
	}

	/***
	 * 特殊字符过滤
	 * 
	 * @param str
	 * @return
	 */
	public static String filterChar(String str) {
		String result = "";
		if (StringUtils.isNotBlank(str)) {
			result = StringUtils.trim(str);
			result = StringUtils.replace(result, "'", "");
			result = StringUtils.replace(result, "@", "");
			result = StringUtils.replace(result, "<", "");
			result = StringUtils.replace(result, ">", "");
			result = StringUtils.replace(result, "*", "");
			result = StringUtils.replace(result, " ", "");
		}
		return result;
	}

	/***
	 * 特殊字符过滤
	 * 
	 * @param str
	 * @return
	 */
	public static String filterCharInc(String str) {
		String result = "";
		if (StringUtils.isNotBlank(str)) {
			result = StringUtils.trim(str);
			result = StringUtils.replace(result, "'", "");
			result = StringUtils.replace(result, "@", "");
			result = StringUtils.replace(result, "<", "");
			result = StringUtils.replace(result, ">", "");
			result = StringUtils.replace(result, "*", "");
		}
		return result;
	}

	public static String processFileName(HttpServletRequest request,
			HttpServletResponse response, String fileName) {
		String temp = "";
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.ms-excel");
		String client = request.getHeader("user-agent");
		try {
			if (StringUtils.contains(client, "MSIE")) {
				temp = URLEncoder.encode(fileName, "utf-8");
			} else {
				temp = new String(fileName.getBytes("utf-8"), "iso-8859-1");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return temp;
	}

	// /***
	// * 加密
	// *
	// * @param pwd
	// * @return
	// */
	// public static String entryptByKey(String pwd) {
	// StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
	// encryptor.setPassword(SALTKEY);
	// return encryptor.encrypt(pwd);
	// }

	// /**
	// * 解密
	// *
	// * @param enPwd
	// * @return
	// */
	// public static String decrypt(String enPwd) {
	// StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
	// encryptor.setPassword(SALTKEY);
	// return encryptor.decrypt(enPwd);
	// }

	/***
	 * 获取 ip 地址
	 * 
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ipAddr = request.getHeader("x-forwarded-for");
		if (StringUtils.isBlank(ipAddr)
				|| StringUtils.equalsIgnoreCase(UNKNOW, ipAddr)) {
			ipAddr = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ipAddr)
				|| StringUtils.equalsIgnoreCase(UNKNOW, ipAddr)) {
			ipAddr = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ipAddr)
				|| StringUtils.equalsIgnoreCase(UNKNOW, ipAddr)) {
			ipAddr = request.getRemoteAddr();
		}
		if (StringUtils.equals(ipAddr, IPV6LOCAL)) {
			try {
				ipAddr = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return ipAddr;
	}

	public static File checkFileDir(String cache) {
		File saveDirFile = new File(cache);
		if (!saveDirFile.canWrite()) {
			saveDirFile.setWritable(true);
			saveDirFile = new File(cache);
		}
		if (!saveDirFile.exists() || !saveDirFile.isDirectory()) {
			saveDirFile.mkdirs();
		}
		return saveDirFile;
	}
}
