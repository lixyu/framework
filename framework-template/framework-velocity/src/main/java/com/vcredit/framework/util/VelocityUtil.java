package com.vcredit.framework.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.util.Base64;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;

/**
 * 模版工具
 * @author maoyibiao
 *
 */
public class VelocityUtil {
	private static VelocityEngine ve;

	private static Properties properties;

	private static Template template;

	private static VelocityContext context;

	private static Writer wt;

	/**
	 * @param tempFile
	 *            模版路径
	 * @param tempFileName
	 * 			模版名称
	 * @param dowFileName
	 * 			下载名称
	 * @param ImageUrl
	 *            图片路径
	 * @param headList
	 * @param detailList
	 * @param TabHeaderList
	 * @param servletRequest
	 * @return
	 * @throws Exception
	 */
	public static void exportFile( String tempFile,String tempFileName,String dowFileName, String ImageUrl, @SuppressWarnings("rawtypes") Map resultMap,
			HttpServletResponse response) throws Exception {
		response.reset();
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String(dowFileName.getBytes("GBK"),"ISO8859-1"));
		response.setContentType("application/msword; charset=GBK");
		// ============利用velocity读取vm模版
		// 取得VelocityEngine/properties对象
		ve = new VelocityEngine();
		properties = new Properties();
		// 取得vm模版路径
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, tempFile);
		// 初始化properties
		ve.init(properties);
		// 取得vm模版/vm模版上下文
		template = ve.getTemplate(tempFileName, "UTF-8");
		context = new VelocityContext();
		// 替换模版内容
//		context.put("imgdata", CodeConve(ImageUrl));
		context.put("imgsrc", ImageUrl);
		context.put("data", resultMap);
		// ===========将更改后的vm另存为word文件
//		toWordFile(expFile);
		PrintWriter out = response.getWriter();
		template.merge(context, out);
		out.close();
//		wt.close();
	}
	@SuppressWarnings("rawtypes")
	public static String exportString(Map resultMap,String tempFile,String tempFileName) throws Exception{
		ve = new VelocityEngine();
		properties = new Properties();
		// 取得vm模版路径
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, tempFile);
		// 初始化properties
		ve.init(properties);
		// 取得vm模版/vm模版上下文
		template = ve.getTemplate(tempFileName, "UTF-8");
		context = new VelocityContext();
		context.put("dateTool", new DateTool());
		context.put("numberTool", new NumberTool());
		for(Object key:resultMap.keySet()){
			context.put(key.toString(), resultMap.get(key));
		}
		StringWriter w = new StringWriter();
		template.merge(context, w);
		return w.toString();
	}
	/**
	 * @param urlString
	 *            产生word文件路径
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static void toWordFile(String strUrl) throws Exception {
		// ==========生成文件
		File file = new File(strUrl);
		if (file.exists()) {
			FileWriter emptyWriter = new FileWriter(file);
			wt = new BufferedWriter(emptyWriter);
			wt.write("");
		} else {
			file.createNewFile();
		}
		FileOutputStream outfile = new FileOutputStream(file);
		wt = new PrintWriter(outfile);
		template.merge(context, wt);
		wt.flush();
	}

	/**
	 * 将汉字转换成10进制
	 * 
	 * @param ch
	 * 
	 * @return 10进制编码
	 */
	@SuppressWarnings("unused")
	private static String codeConve(char ch) throws Exception {
		if (ch > 255) {
			return "&#" + (ch & 0xffff) + ";";
		} else {
			return String.valueOf(ch);
		}
	}

	/**
	 * 根据图片路径将图片转换成base64位编码
	 * 
	 * @param imgUrl
	 *            图片路径
	 * @return base64位编码
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static String CodeConve(String imgUrl) throws Exception {
		byte[] data = null;
		InputStream in = new FileInputStream(imgUrl);
		data = new byte[in.available()];
		in.read(data);
		in.close();
		// 对字节数组Base64编码
		return Base64.encodeBase64String(data);// 返回Base64编码过的字节数组字符串
	}
	
	public static String templateToText(String tempPath, Object... objs)
			throws Exception {
		if (objs.length % 2 != 0)
			throw new Exception("args size exception");
		Properties p = new Properties();
		p.put("file.resource.loader.class",
		"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(p);
		context = new VelocityContext();
		for (int i = 0; i < objs.length; i = i + 2) {
			context.put((String) objs[i], objs[i + 1]);
		}
		template = Velocity.getTemplate(tempPath,"UTF-8");
		wt = new StringWriter();
		template.merge(context, wt);
		return wt.toString();
	}
}
