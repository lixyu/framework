package com.vcredit.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;

import com.vcredit.framework.enumtype.ExportRequired;
import com.vcredit.framework.util.export.ExportBean;
import com.vcredit.framework.util.export.ExportDefaultFieldFormat;

/**
 * 报表导出公用类
 * 
 * @author sk_mao
 *
 */
public class ExportUtil {
	/**
	 * 导出excel
	 * 
	 * @param tempFilePath
	 *            模版路径
	 * @param filename
	 *            文件名
	 * @param sheetname
	 *            表空间
	 * @param list
	 *            bean集合
	 * @param response
	 *            HttpServletResponse对象
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void exportExcel(String tempFilePath, String sheetname, String[] fields, List<Object> list,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String filename = new Date().getTime() + ".xls";
			Workbook wb = Workbook.getWorkbook(new FileInputStream(tempFilePath));
			// 创建新的Excel 工作簿
			File tempFile = FileUtil.getRealFile("temp/" + filename);
			WritableWorkbook workbook = Workbook.createWorkbook(tempFile, wb);
			// 在Excel工作簿中建一工作表，其名为:第一页
			WritableSheet wsheet = workbook.createSheet(sheetname, 0); // sheet();
			WritableFont font = new WritableFont(WritableFont.ARIAL, 11, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			WritableCellFormat format = new WritableCellFormat(font);
			format.setBorder(Border.ALL, BorderLineStyle.THIN);
			format.setWrap(false);
			format.setAlignment(Alignment.LEFT);
			format.setVerticalAlignment(VerticalAlignment.TOP);
			for (int i = 1; i < list.size() + 1; i++) {
				Object obj = list.get(i - 1);
				List<ExportBean> beans = getExportBean(obj, obj.getClass(), fields);
				for (int j = 0; j < beans.size(); j++) {
					ExportBean bean = beans.get(j);
					ExportDefaultFieldFormat fieldFormat = bean.getFormat().newInstance();
					Label wlabel = new Label(j, i, fieldFormat.fieldFormat(bean.getVal()), format);
					wsheet.addCell(wlabel);
					if (i == 1) {
						// 设置字体
						WritableFont fontTitle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,
								false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
						WritableCellFormat topFormat = new WritableCellFormat(fontTitle);
						// 设置背景颜色
						workbook.setColourRGB(Colour.LIGHT_BLUE, 153, 204, 255);
						topFormat.setBackground(Colour.LIGHT_BLUE);
						// 设置边框
						topFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
						// 设置自动换行
						topFormat.setWrap(false);
						// 设置文字水平对齐方式
						topFormat.setAlignment(Alignment.LEFT);
						// 设置垂直对齐方式
						topFormat.setVerticalAlignment(VerticalAlignment.TOP);
						Label wlabel1 = new Label(j, 0, bean.getTitle(), topFormat);
						// 设置自动列宽
						// CellView cellView = new CellView();
						// cellView.setAutosize(false);
						// wsheet.setColumnView(j, cellView);
						wsheet.setColumnView(j, 20);
						// 锁定标题
						wsheet.getSettings().setVerticalFreeze(1);
						wsheet.addCell(wlabel1);
					}
				}
			}
			workbook.write();
			workbook.close();
			if (tempFile.exists()) {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("multipart/form-data");
				response.setHeader("Content-disposition", "attachment;filename=" + filename);// 将文件名转码
				byte[] b = new byte[1024];
				int length;
				InputStream inputStream = new FileInputStream(tempFile);
				OutputStream os = response.getOutputStream();
				while ((length = inputStream.read(b)) > 0) {
					os.write(b, 0, length);
				}
				inputStream.close();
				tempFile.delete();
			}
		} catch (WriteException ex1) {
			System.out.println("WriteException:" + ex1.getMessage());
		} catch (IOException ex2) {
			System.out.println("IOException:" + ex2.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	private static List<ExportBean> getExportBean(Object obj, Class clazz, String[] fields) throws Exception {
		List<ExportBean> list = new ArrayList<ExportBean>();
		List<AccessibleObject> assess = new ArrayList<AccessibleObject>();
		assess.addAll(Arrays.asList(clazz.getDeclaredFields()));
		assess.addAll(Arrays.asList(clazz.getDeclaredMethods()));
		for (AccessibleObject ass : assess) {
			ExportRequired exportRequired = ass.getAnnotation(ExportRequired.class);
			Object detail = null;
			Class type = null;
			String fieldName = null;
			if (exportRequired != null) {
				if (ass instanceof Field) {
					Field f = (Field) ass;
					type = f.getType();
					ReflectionUtils.makeAccessible(f);
					detail = obj == null ? null : ReflectionUtils.getField(f, obj);
					fieldName = StringUtils.isEmpty(exportRequired.aliases()) ? f.getName() : exportRequired.aliases();
				}
				if (ass instanceof Method) {
					Method m = (Method) ass;
					type = m.getReturnType();
					detail = obj == null ? null : m.invoke(obj, null);
					fieldName = StringUtils.isEmpty(exportRequired.aliases()) ? m.getName().replace("get", "")
							: exportRequired.aliases();
				}
				if (exportRequired.name().equals("")) {
					list.addAll(getExportBean(detail, type, fields));
				} else {
					if (exportRequired.isBean()) {
						list.add(new ExportBean(detail, exportRequired.name(), exportRequired.format()));
					} else {
						for (String field : fields) {
							if (StringUtils.isEmpty(field))
								continue;
							String aliases = null;
							String title = null;
							String[] f = field.split(":");
							if (f.length > 0) {
								aliases = f[0];
							}
							if (f.length > 1) {
								title = f[1];
							}
							boolean only = true;
							if (!"".equals(aliases) && aliases.toLowerCase().equals(fieldName.toLowerCase())) {
								if (only) {
									list.add(new ExportBean(detail,
											StringUtils.isEmpty(title) ? exportRequired.name() : title,
											exportRequired.format()));
									only = !only;
								} else {
									throw new Exception("not Exist :" + field);
								}
							}
						}
					}
				}
			}
		}
		return list;
	}
}
